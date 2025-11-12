package com.ChickenKitchen.app.serviceImpl.AI

import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.repository.step.DishRepository
import com.ChickenKitchen.app.serviceImpl.step.DishNutritionCacheService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.io.File
import java.math.BigDecimal
import java.nio.file.Paths
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Component
class AiDishCatalogCache(
    private val dishRepository: DishRepository,
    private val dishNutritionCacheService: DishNutritionCacheService,
    @Value("\${ai.catalog.cache-path:build/ai-dish-catalog.json}")
    private val cachePath: String
) {

    private val mapper = jacksonObjectMapper()
    private val lock = ReentrantLock()

    private val keywordLexicon = mapOf(
        "gà" to "chicken",
        "ga" to "chicken",
        "chicken" to "chicken",
        "bò" to "beef",
        "bo" to "beef",
        "beef" to "beef",
        "tôm" to "shrimp",
        "tom" to "shrimp",
        "shrimp" to "shrimp",
        "cá hồi" to "salmon",
        "ca hoi" to "salmon",
        "salmon" to "salmon",
        "đậu hũ" to "tofu",
        "dau hu" to "tofu",
        "tofu" to "tofu",
        "trứng" to "egg",
        "trung" to "egg",
        "egg" to "egg",
        "cơm" to "rice",
        "com" to "rice",
        "rice" to "rice",
        "mì" to "noodle",
        "mi" to "noodle",
        "noodle" to "noodle",
        "pasta" to "pasta",
        "udon" to "udon",
        "quinoa" to "quinoa",
        "couscous" to "couscous",
        "cá" to "fish",
        "ca" to "fish",
        "fish" to "fish",
        "cá ngừ" to "tuna",
        "ca ngu" to "tuna",
        "tuna" to "tuna",
        "cá thu" to "mackerel",
        "ca thu" to "mackerel",
        "mackerel" to "mackerel",
        "cá basa" to "basa",
        "ca basa" to "basa",
        "basa" to "basa",

    )

    // 2) Hàm chuẩn hoá
    private fun normalizeKeywords(raw: List<String>): List<String> =
        raw.mapNotNull { it.trim().lowercase().takeIf { s -> s.isNotBlank() } }
        .map { keywordLexicon[it] ?: it }

    // 3) Trong searchCatalog(...):



    @PostConstruct
    fun preload() {
        runCatching { ensureCacheExists() }
    }

    fun getRepresentativeCatalogJson(): String = lock.withLock {
        val cacheFile = cacheFile()
        if (cacheFile.exists() && cacheFile.length() > 0) {
            return cacheFile.readText()
        }
        val json = buildCatalogJson()
        writeCache(cacheFile, json)
        return json
    }

    private fun ensureCacheExists() = lock.withLock {
        val cacheFile = cacheFile()
        if (!cacheFile.exists() || cacheFile.length() == 0L) {
            val json = buildCatalogJson()
            writeCache(cacheFile, json)
        }
    }

    private fun cacheFile(): File = Paths.get(cachePath).toFile()

    private fun writeCache(cacheFile: File, content: String) {
        cacheFile.parentFile?.mkdirs()
        cacheFile.writeText(content)
    }

    private fun buildCatalogJson(): String {
        val dishes = dishRepository.findAllByIsCustomFalse(Sort.by(Sort.Direction.DESC, "createdAt"))
        val snapshots = dishes.filter { it.id != null }.map { dish ->
            AiDishSnapshot(
                id = dish.id!!,
                name = dish.name,
                cal = dish.cal,
                price = dish.price,
                description = dish.note,
                nutrients = extractTrackedNutrients(dish)
            )
        }

        val payload = if (snapshots.isEmpty()) emptyCatalogPayload() else buildCatalogPayload(snapshots)

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload)
    }

    private fun buildCatalogPayload(snapshots: List<AiDishSnapshot>) = mapOf(
            "carb_high" to mapPayloads(pickByNutrient(snapshots, NutrientSlot.CARB, true)),
            "carb_low" to mapPayloads(pickByNutrient(snapshots, NutrientSlot.CARB, false)),
            "protein_high" to mapPayloads(pickByNutrient(snapshots, NutrientSlot.PROTEIN, true)),
            "protein_low" to mapPayloads(pickByNutrient(snapshots, NutrientSlot.PROTEIN, false)),
            "fat_high" to mapPayloads(pickByNutrient(snapshots, NutrientSlot.FAT, true)),
            "fat_low" to mapPayloads(pickByNutrient(snapshots, NutrientSlot.FAT, false)),
            "fiber_high" to mapPayloads(pickByNutrient(snapshots, NutrientSlot.FIBER, true)),
            "fiber_low" to mapPayloads(pickByNutrient(snapshots, NutrientSlot.FIBER, false)),
            "cal_high" to mapPayloads(pickByCalories(snapshots, true)),
            "cal_low" to mapPayloads(pickByCalories(snapshots, false))
        )

    private fun emptyCatalogPayload() = mapOf(
        "carb_high" to emptyList<Any>(),
        "carb_low" to emptyList<Any>(),
        "protein_high" to emptyList<Any>(),
        "protein_low" to emptyList<Any>(),
        "fat_high" to emptyList<Any>(),
        "fat_low" to emptyList<Any>(),
        "fiber_high" to emptyList<Any>(),
        "fiber_low" to emptyList<Any>(),
        "cal_high" to emptyList<Any>(),
        "cal_low" to emptyList<Any>()
    )

    // ========= Ad-hoc query from AI (Advice flow) =========
    // Accepts a JSON with structure described by the user and returns
    // a list of dish payloads (id, name, cal, price, description, nutrient{carb,protein,fat,fiber}).
    fun searchCatalogJson(queryJson: String): String {
        val results = searchCatalog(queryJson)
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(results)
    }

    fun searchCatalog(queryJson: String): List<Map<String, Any?>> {
        val root = runCatching { mapper.readTree(queryJson) }.getOrNull()
        val filters = root?.get("filters")
        val sortNode = root?.get("sort")
        val limit = root?.get("limit")?.asInt(20) ?: 20

        val keywords: List<String> = filters?.get("keyword")?.let { kwNode ->
            if (kwNode.isArray) kwNode.mapNotNull { it.asText(null) } else emptyList()
        } ?: emptyList()
    
        val normKeywords = normalizeKeywords(keywords)

        // val keywords: List<String> = filters?.get("keyword")?.let { kwNode ->
        //     if (kwNode.isArray) kwNode.mapNotNull { it.asText(null) }.map { it.trim().lowercase() }.filter { it.isNotBlank() } else emptyList()
        // } ?: emptyList()

        val calRange = parseRange(filters?.get("cal"))
        val priceRange = parseRange(filters?.get("price"))
        val proteinRange = parseRange(filters?.get("protein"))
        val fatRange = parseRange(filters?.get("fat"))
        val carbRange = parseRange(
            filters?.get("carbohydrates")
                ?: filters?.get("carb")
                ?: filters?.get("carbs")
                ?: filters?.get("carbohydrate")
        )
        val fiberRange = parseRange(filters?.get("fiber") ?: filters?.get("fibber"))

        // Load all non-custom dishes, then filter in-memory to honor mixed constraints.
        val dishes = dishRepository.findAllByIsCustomFalse(Sort.by(Sort.Direction.DESC, "createdAt"))
        if (dishes.isEmpty()) return emptyList()

        val snapshots = dishes.filter { it.id != null }.map { dish ->
            AiDishSnapshot(
                id = dish.id!!,
                name = dish.name,
                cal = dish.cal,
                price = dish.price,
                description = dish.note,
                nutrients = extractTrackedNutrients(dish)
            )
        }

        // Apply filters
        val filtered = snapshots.filter { snap ->
            // keyword matches (on dish name only to avoid heavy joins here)

            val haystack = (snap.name + " " + (snap.description ?: "")).lowercase()
            val keywordOk = if (normKeywords.isEmpty()) true else normKeywords.any { kw -> haystack.contains(kw) }

            val calOk = inRange(snap.cal.toBigDecimal(), calRange)
            val priceOk = inRange(snap.price.toBigDecimal(), priceRange)

            val proteinOk = inRange(snap.nutrients[NutrientSlot.PROTEIN] ?: BigDecimal.ZERO, proteinRange)
            val fatOk = inRange(snap.nutrients[NutrientSlot.FAT] ?: BigDecimal.ZERO, fatRange)
            val carbOk = inRange(snap.nutrients[NutrientSlot.CARB] ?: BigDecimal.ZERO, carbRange)
            val fiberOk = inRange(snap.nutrients[NutrientSlot.FIBER] ?: BigDecimal.ZERO, fiberRange)

            keywordOk && calOk && priceOk && proteinOk && fatOk && carbOk && fiberOk
        }

        // Sorting
        val by = sortNode?.get("by")?.asText(null)?.trim()?.lowercase()
        val orderDesc = sortNode?.get("order")?.asText("desc")?.equals("desc", ignoreCase = true) ?: true

        val comparator = when (by) {
            "protein" -> compareBy<AiDishSnapshot> { it.nutrients[NutrientSlot.PROTEIN] ?: BigDecimal.ZERO }
            "fat" -> compareBy { it.nutrients[NutrientSlot.FAT] ?: BigDecimal.ZERO }
            "carb", "carbs", "carbohydrate", "carbohydrates" -> compareBy { it.nutrients[NutrientSlot.CARB] ?: BigDecimal.ZERO }
            "fiber", "fibre", "fibers", "fibres" -> compareBy { it.nutrients[NutrientSlot.FIBER] ?: BigDecimal.ZERO }
            "cal", "calories" -> compareBy { it.cal }
            "price" -> compareBy { it.price }
            else -> null
        }

        val ordered = comparator?.let { cmp ->
            val sorted = filtered.sortedWith(cmp.thenBy { it.name.lowercase() })
            if (orderDesc) sorted.reversed() else sorted
        } ?: filtered

        val limited = if (limit > 0) ordered.take(limit) else ordered
        return mapPayloads(limited)
    }

    private data class Range(val min: BigDecimal?, val max: BigDecimal?)

    private fun parseRange(node: JsonNode?): Range {
        if (node == null || node.isNull) return Range(null, null)
        val min = node.get("min")?.asDouble()?.let { BigDecimal.valueOf(it) }
        val max = node.get("max")?.asDouble()?.let { BigDecimal.valueOf(it) }
        return Range(min, max)
    }

    private fun inRange(value: BigDecimal, range: Range): Boolean {
        val minOk = range.min?.let { value >= it } ?: true
        val maxOk = range.max?.let { value <= it } ?: true
        return minOk && maxOk
    }

    private fun extractTrackedNutrients(dish: Dish): Map<NutrientSlot, BigDecimal> {
        val tracked = mutableMapOf<NutrientSlot, BigDecimal>()
        dishNutritionCacheService.ensureSnapshot(dish).forEach { nutrient ->
            val slot = when (nutrient.name.trim().lowercase()) {
                "carb", "carbs", "carbohydrate", "carbohydrates" -> NutrientSlot.CARB
                "protein", "proteins" -> NutrientSlot.PROTEIN
                "fat", "fats" -> NutrientSlot.FAT
                "fiber", "fibers", "fibre", "fibres" -> NutrientSlot.FIBER
                else -> null
            }
            if (slot != null) {
                tracked[slot] = nutrient.quantity
            }
        }
        return tracked
    }

    private fun pickByNutrient(
        snapshots: List<AiDishSnapshot>,
        slot: NutrientSlot,
        descending: Boolean
    ): List<AiDishSnapshot> {
        val sorted = snapshots.sortedWith(
            compareBy<AiDishSnapshot>(
                { it.nutrients[slot] ?: BigDecimal.ZERO },
                { it.name.lowercase() }
            )
        )
        val ordered = if (descending) sorted.reversed() else sorted
        return ordered.take(3)
    }

    private fun pickByCalories(
        snapshots: List<AiDishSnapshot>,
        descending: Boolean
    ): List<AiDishSnapshot> {
        val sorted = snapshots.sortedWith(
            compareBy<AiDishSnapshot>(
                { it.cal },
                { it.name.lowercase() }
            )
        )
        val ordered = if (descending) sorted.reversed() else sorted
        return ordered.take(3)
    }

    private fun mapPayloads(snapshots: List<AiDishSnapshot>): List<Map<String, Any?>> =
        snapshots.map { snapshot ->
            mapOf(
                "id" to snapshot.id,
                "name" to snapshot.name,
                "cal" to snapshot.cal,
                "price" to snapshot.price,
                "description" to snapshot.description,
                "nutrient" to mapOf(
                    "carb" to snapshot.nutrients[NutrientSlot.CARB]?.toDouble(),
                    "protein" to snapshot.nutrients[NutrientSlot.PROTEIN]?.toDouble(),
                    "fat" to snapshot.nutrients[NutrientSlot.FAT]?.toDouble(),
                    "fiber" to snapshot.nutrients[NutrientSlot.FIBER]?.toDouble()
                )
            )
        }

    private data class AiDishSnapshot(
        val id: Long,
        val name: String,
        val cal: Int,
        val price: Int,
        val description: String?,
        val nutrients: Map<NutrientSlot, BigDecimal>
    )

    private enum class NutrientSlot {
        CARB, PROTEIN, FAT, FIBER
    }
}
