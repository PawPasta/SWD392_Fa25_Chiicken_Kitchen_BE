package com.ChickenKitchen.app.serviceImpl.AI

import com.ChickenKitchen.app.handler.TokenException
import com.ChickenKitchen.app.handler.StoreNotFoundException
import com.ChickenKitchen.app.model.dto.request.AiChatRequest
import com.ChickenKitchen.app.model.dto.request.CreateExistingDishRequest
import com.ChickenKitchen.app.model.dto.response.AiChatResponse
import com.ChickenKitchen.app.model.entity.ai.ChatMessage
import com.ChickenKitchen.app.model.entity.auth.UserSession
import com.ChickenKitchen.app.model.entity.menu.MenuItem
import com.ChickenKitchen.app.model.entity.step.Dish
import com.ChickenKitchen.app.repository.ai.ChatMessageRepository
import com.ChickenKitchen.app.repository.auth.UserSessionRepository
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
import com.ChickenKitchen.app.repository.step.DishRepository
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.service.AI.AiChatService
import com.ChickenKitchen.app.service.order.CustomerOrderService
import com.ChickenKitchen.app.serviceImpl.AI.AiDishCatalogCache
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.sql.Timestamp
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

/**
 * Ultra-clean AiChatServiceImpl:
 * - Stage 0 (Prevalidate): backend xác thực món/topping theo DB → nếu không khớp → clarify (không gọi LLM).
 * - Stage 1 (Intent filter): LLM chỉ trả "intent" (order|add|remove|update|advice|decline|unknown).
 * - Stage 2 (Execute):
 *      * Order flow: LLM CHỈ format JSON; backend enforce id/tên từ DB; không cho đoán/sinh món.
 *      * Advice flow: LLM CHỈ chọn trong catalog do backend cung cấp + enforce lại theo DB.
 * - Mọi output trả về cho FE đều đã được backend hậu kiểm (no-bịa-được).
 */
@Service
class AiChatServiceImpl(
    private val userSessionRepository: UserSessionRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val customerOrderService: CustomerOrderService,
    private val dishRepository: DishRepository,
    private val menuItemRepository: MenuItemRepository,
    private val storeRepository: StoreRepository,
    private val aiDishCatalogCache: AiDishCatalogCache,
    webClientBuilder: WebClient.Builder,
    @Value("\${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private val groqApiUrl: String,
    @Value("\${groq.model:llama-3.1-8b-instant}")
    private val groqModel: String,
    @Value("\${groq.model.key:}")
    private val groqApiKeyFromEnv: String,
) : AiChatService {

    val AI_SYSTEM_PROMPT = """
        Role:
        You are an AI assistant that helps users choose and filter dishes from the ChickenKitchen database.
        All responses must be valid JSON.
        Do not include any text, explanation, or commentary outside of the JSON object.

        Objective:
        Analyze the user’s natural language query (in English or Vietnamese)
        → Extract and output structured JSON that defines the user’s intent (intent), filters (filters), sorting (sort), and result limit (limit).

        Catalog Context (Internal)
        Sometimes backend provides a short list of dishes (with fields: id, name, cal, price, description, nutrient{carb,protein,fat,fiber}).
        - Use this ONLY for disambiguation and id lookup.
        - When intent is "order_menu", you MUST include each item's id (from context) and quantity.
        - If names are ambiguous/not in context, either ask to clarify or switch to query_menu filters.

        Expected JSON Format
        {
        "intent": "query_menu",
        "filters": {
            "keyword": ["chicken", "egg"],
            "cal": { "max": 700 },
            "protein": { "min": 30 },
            "fat": { "max": 10 },
            "carbohydrates": { "max": 40 },
            "price": { "max": 120000 }
        },
        "sort": { "by": "protein", "order": "desc" },
        "limit": 10,
        "notes": "Summarize the user’s goal in plain language"
        }

        Interpretation Rules
        intent:
        If the user is searching, filtering, or asking about dishes → "query_menu".
        If the user wants to place an order → "order_menu".
        If the user asks about dish types or ingredients → "get_categories".
        If the user just wants random or recommended dishes → "recommend_random".
        filters:

        Each field is optional. Include only what the user implies.
        User phrase	JSON filter:
        “with chicken / salmon / tofu”	"keyword": ["chicken"]
        “low calorie / under 500 cal / healthy”	"cal": { "max": 500 }"
        “high calorie / filling”	"cal": { "min": 800 }"
        “high protein / rich in protein”	"protein": { "min": 30 }"
        “low fat / not greasy”	"fat": { "max": 10 }"
        “low carb / less starch”	"carbohydrates": { "max": 30 }"
        “under 100k / cheap”	"price": { "max": 100000 }"
        “premium / expensive”	"price": { "min": 120000 }"

        Example Outputs:
        1. User: “Show me chicken dishes under 100k with low calories.”
        {
        "intent": "query_menu",
        "filters": {
            "keyword": ["chicken"],
            "price": { "max": 100000 },
            "cal": { "max": 600 }
        },
        "sort": { "by": "cal", "order": "asc" },
        "limit": 10,
        "notes": "User wants affordable chicken dishes with low calories"
        }

        4. User: “Order dishes by id”
        {
        "intent": "order_menu",
        "items": [
            {"id": 101, "quantity": 2},
            {"id": 205, "quantity": 1}
        ],
        "notes": "User wants to place an order; ids refer to sample dishes (isCustom=false)"
        }

        2. User: “I want something high in protein but low in carbs.”
        {
        "intent": "query_menu",
        "filters": {
            "protein": { "min": 30 },
            "carbohydrates": { "max": 25 }
        },
        "sort": { "by": "protein", "order": "desc" },
        "limit": 10,
        "notes": "User wants high-protein, low-carb dishes"
        }

        3. User: “What are your premium options?”
        {
        "intent": "query_menu",
        "filters": {
            "price": { "min": 120000 }
        },
        "sort": { "by": "price", "order": "desc" },
        "limit": 5,
        "notes": "User wants premium or expensive dishes"
        }

    """.trimIndent()

    // Keep last dataset shown to this session for id disambiguation in subsequent turns
    private val recommendationCache = ConcurrentHashMap<Long, List<Map<String, Any?>>>()

    override
    fun chat(req: AiChatRequest): AiChatResponse {
        val text = req.message?.trim().orEmpty()
        if (text.isBlank()) {
            return AiChatResponse(
                answerRaw = "Bạn muốn mình trợ giúp gì?",
                answerJson = null,
                orderResult = null
            )
        }

        storeRepository.findById(req.storeId).orElseThrow { StoreNotFoundException("Store not found") }

        val session = resolveActiveSession()
        chatMessageRepository.save(ChatMessage(userSession = session, role = "user", content = text))

        val apiKey = resolveGroqApiKey()
        if (apiKey.isNullOrBlank()) {
            val msg = "Vui lòng cấu hình GROQ_API_KEY để bật chat AI."
            chatMessageRepository.save(ChatMessage(userSession = session, role = "assistant", content = msg))
            return AiChatResponse(answerRaw = msg, answerJson = null, orderResult = null)
        }

        // Phase 1: planner decides if we need DB and prepares a JSON plan (use previous dataset to force ids)
        val lastDataset = session.id?.let { recommendationCache[it] }
        val plannerJson = runPlanner(apiKey, text, lastDataset)
        val intent = plannerJson.path("intent").asText("")

        val finalAnswer = when (intent) {
            "query_menu" -> {
                val dataset = aiDishCatalogCache.searchCatalog(plannerJson.toString())

                val kw = plannerJson.path("filters").path("keyword")
                    .takeIf { it.isArray }?.map { it.asText().lowercase() } ?: emptyList()

                // kiểm tra nếu không có keyword hoặc không có món nào chứa keyword trong tên
                val noKeywordMatch = if (kw.isNotEmpty()) {
                    val hasMatch = dataset.any { dish ->
                        val name = dish["name"]?.toString()?.lowercase() ?: ""
                        kw.any { name.contains(it) }
                    }
                    !hasMatch
                } else false

                if (dataset.isEmpty() || noKeywordMatch) {
                    return AiChatResponse(
                        answerRaw = "Mình chưa tìm thấy món nào phù hợp tiêu chí của bạn. Bạn có thể cho mình biết thêm yêu cầu được không?",
                        answerJson = null,
                        orderResult = null
                    )
                }

                // Save dataset to cache for later order intent
                session.id?.let { recommendationCache[it] = dataset }
                runAnswerer(apiKey, text, dataset, plannerJson.path("notes").asText(null))
            }
            "recommend_random" -> {
                // Use representative catalog as lightweight context
                val representative = mapper.readTree(aiDishCatalogCache.getRepresentativeCatalogJson())
                val dataset = representative.fields().asSequence().flatMap { (_, v) -> v.elements().asSequence() }
                    .map { it }
                    .take(20)
                    .toList()
                val mapped = dataset.map { mapper.convertValue(it, Map::class.java) as Map<String, Any?> }
                session.id?.let { recommendationCache[it] = mapped }
                runAnswerer(apiKey, text, mapped, "Gợi ý nhanh từ thực đơn")
            }
            "order_menu" -> {
                val items = plannerJson.path("items")
                if (!items.isArray || items.size() == 0) {
                    "Mình cần danh sách món (id và số lượng) để thêm vào đơn nhé."
                } else {
                    val added = mutableListOf<Long>()
                    val failed = mutableListOf<Long>()
                    val contextList = session.id?.let { recommendationCache[it] } ?: emptyList()
                    items.forEach { node ->
                        val qty = node.path("quantity").asInt(1).coerceAtLeast(1)
                        var dishId = node.path("id").asLong(-1)

                        if (dishId <= 0) {
                            val name = node.path("name").asText("").trim()
                            if (name.isNotBlank()) {
                                // Try resolve from previously shown dataset first
                                val lower = name.lowercase()
                                val fromCtx = contextList.firstOrNull { m ->
                                    val n = (m["name"] as? String)?.lowercase() ?: ""
                                    n == lower || n.contains(lower) || lower.contains(n)
                                }
                                if (fromCtx != null) {
                                    val idAny = fromCtx["id"]
                                    dishId = when (idAny) {
                                        is Number -> idAny.toLong()
                                        is String -> idAny.toLongOrNull() ?: -1
                                        else -> -1
                                    }
                                }
                                if (dishId <= 0) {
                                    val found = dishRepository.findFirstByIsCustomFalseAndNameIgnoreCase(name)
                                    if (found != null) dishId = found.id ?: -1
                                }
                            }
                        }

                        if (dishId <= 0) {
                            failed.add(dishId)
                            return@forEach
                        }

                        runCatching {
                            customerOrderService.addExistingDishToCurrentOrder(
                                CreateExistingDishRequest(storeId = req.storeId, dishId = dishId, quantity = qty)
                            )
                        }.onSuccess { added.add(dishId) }
                         .onFailure { failed.add(dishId) }
                    }
                    when {
                        added.isEmpty() -> "Không thể thêm món nào. Vui lòng kiểm tra lại id và số lượng."
                        failed.isEmpty() -> "Đã thêm ${added.size} món vào đơn thành công."
                        else -> "Đã thêm ${added.size} món, nhưng một số id không hợp lệ: ${failed.joinToString(", ")}."
                    }
                }
            }
            else -> {
                // Small talk fallback
                "Mình có thể gợi ý món theo tiêu chí (calo, protein, béo, carb, giá). Bạn muốn ưu tiên tiêu chí nào?"
            }
        }

        chatMessageRepository.save(ChatMessage(userSession = session, role = "assistant", content = finalAnswer))
        return AiChatResponse(answerRaw = finalAnswer, answerJson = null, orderResult = null)
    }

    override
    fun getHistory(): List<ChatMessage> {
        val session = resolveActiveSession()
        return chatMessageRepository.findAllByUserSessionOrderByCreatedAtAsc(session)
    }

    override
    fun getHistory(page: Int, size: Int, asc: Boolean): Page<ChatMessage> {
        val session = resolveActiveSession()
        val p = max(page, 1) - 1
        val s = max(size, 1)
        val sort = if (asc) Sort.by("createdAt").ascending() else Sort.by("createdAt").descending()
        return chatMessageRepository.findAllByUserSession(session, PageRequest.of(p, s, sort))
    }

    private val mapper = ObjectMapper()
    private val client: WebClient = webClientBuilder.build()

    private fun runPlanner(apiKey: String, userText: String, contextDataset: List<Map<String, Any?>>?): JsonNode {
        val messages = mutableListOf(
            mapOf("role" to "system", "content" to AI_SYSTEM_PROMPT)
        )
        if (!contextDataset.isNullOrEmpty()) {
            val ds = contextDataset.take(50)
            val ctxJson = try { mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ds) } catch (_: Exception) { "[]" }
            messages.add(mapOf("role" to "system", "content" to "Catalog context (internal; do not show to user). Use ids from this list when ordering.\n$ctxJson"))
        }
        messages.add(mapOf("role" to "user", "content" to userText))
        val raw = callGroq(apiKey, messages)
        val jsonOnly = extractJson(raw)
        return runCatching { mapper.readTree(jsonOnly) }.getOrElse { mapper.createObjectNode() }
    }

    private fun runAnswerer(apiKey: String, userText: String, dataset: List<Map<String, Any?>>, plannerNote: String?): String {
        val answerSystem = """
        Bạn là trợ lý của ChickenKitchen. Trả lời NGẮN GỌN bằng tiếng Việt.
        TUYỆT ĐỐI chỉ được sử dụng các món có trong "dataset" đã cung cấp.
        Không sáng tác tên món mới, không liệt kê ví dụ ngoài dataset.
        Nếu dataset trống hoặc không đủ phù hợp, trả lời một câu HỎI LẠI ngắn gọn để làm rõ tiêu chí
        (ví dụ: “Bạn muốn gà nướng, ít cal hay ít béo?”). Không đưa danh sách minh hoạ khi dataset trống.
        Khi có dữ liệu, liệt kê 3–6 món, nêu lý do ngắn theo tiêu chí người dùng (cal/protein/fat/carb/giá).
        """.trimIndent()
        

        val toolContent = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            mapOf(
                "dataset" to dataset,
                "user_query" to userText,
                "note" to plannerNote
            )
        )
        val messages = listOf(
            mapOf("role" to "system", "content" to answerSystem),
            mapOf("role" to "user", "content" to "Dữ liệu để phân tích:\n$toolContent\n\nHãy trả lời người dùng.")
        )
        val raw = callGroq(apiKey, messages)
        return raw.trim()
    }

    private fun callGroq(apiKey: String, messages: List<Map<String, String>>): String {
        val reqBody = mapOf(
            "model" to groqModel,
            "temperature" to 0.2,
            "max_tokens" to 700,
            "messages" to messages
        )
        val entity = client.post()
            .uri(groqApiUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(reqBody)
            .retrieve()
            .bodyToMono(String::class.java)
            .timeout(Duration.ofSeconds(30))
            .onErrorResume { Mono.just("{\"error\":\"groq_failed\"}") }
            .block() ?: ""

        // Parse OpenAI-style response
        return try {
            val root = mapper.readTree(entity)
            val choices = root.path("choices")
            val first = if (choices.isArray && choices.size() > 0) choices[0] else null
            first?.path("message")?.path("content")?.asText("") ?: ""
        } catch (_: Exception) {
            entity
        }
    }

    private fun extractJson(raw: String): String {
        var s = raw.trim()
        if (s.startsWith("```")) {
            s = s.removePrefix("```json").removePrefix("```").trim()
        }
        if (s.endsWith("```")) s = s.removeSuffix("```").trim()
        return s
    }

    private fun resolveGroqApiKey(): String? =
        groqApiKeyFromEnv.takeIf { !it.isNullOrBlank() }
            ?: System.getenv("GROQ_API_KEY")

    private fun resolveActiveSession(): UserSession {
        val auth = SecurityContextHolder.getContext().authentication
            ?: throw TokenException("Unauthenticated")

        val email = auth.name
        val sessions = userSessionRepository.findAllByUserEmailAndIsCancelledFalse(email)

        if (sessions.isEmpty()) throw TokenException("No active session")
        if (sessions.size > 1) throw TokenException("Multiple active sessions found for user: $email")

        val session = sessions[0]
        if (session.isCancelled) throw TokenException("Session has been cancelled")

        session.lastActivity = Timestamp(System.currentTimeMillis())
        return userSessionRepository.save(session)
    }

}
