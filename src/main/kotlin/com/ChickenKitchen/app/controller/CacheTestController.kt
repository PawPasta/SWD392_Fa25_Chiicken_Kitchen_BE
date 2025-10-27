package com.ChickenKitchen.app.controller

import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.service.menu.DailyMenuService
import com.ChickenKitchen.app.service.util.CacheTestService
import org.springframework.cache.CacheManager
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.system.measureTimeMillis

@RestController
@RequestMapping("/api/cache-test")
class CacheTestController(
    private val cacheTestService: CacheTestService,
    private val dailyMenuService: DailyMenuService,
    private val cacheManager: CacheManager,
) {

    @GetMapping("/echo")
    fun echo(@RequestParam key: String): ResponseEntity<ResponseModel> {
        var value: String
        val time = measureTimeMillis {
            value = cacheTestService.slowEcho(key)
        }
        return ResponseEntity.ok(
            ResponseModel.success(
                mapOf(
                    "value" to value,
                    "timeMs" to time
                ),
                "Echo with cache"
            )
        )
    }

    @PostMapping("/evict")
    fun evict(
        @RequestParam(name = "cache", defaultValue = "testEcho") cacheName: String,
        @RequestParam(required = false) key: String?
    ): ResponseEntity<ResponseModel> {
        val cache = cacheManager.getCache(cacheName)
        return if (cache != null) {
            if (key == null) cache.clear() else cache.evict(key)
            ResponseEntity.ok(ResponseModel.success(message = "Evicted ${cacheName}${if (key != null) " key=$key" else " all"}"))
        } else {
            ResponseEntity.ok(ResponseModel.error(404, "Cache '$cacheName' not found"))
        }
    }

    @GetMapping("/daily")
    fun daily(
        @RequestParam storeId: Long,
        @RequestParam date: String
    ): ResponseEntity<ResponseModel> {
        lateinit var data: Any
        val time = measureTimeMillis {
            data = dailyMenuService.getByStoreAndDate(storeId, date)
        }
        return ResponseEntity.ok(
            ResponseModel.success(
                mapOf(
                    "data" to data,
                    "timeMs" to time
                ),
                "Daily menu measured"
            )
        )
    }
}

