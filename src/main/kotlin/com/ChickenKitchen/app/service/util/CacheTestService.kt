package com.ChickenKitchen.app.service.util

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CacheTestService {

    @Cacheable(cacheNames = ["testEcho"], key = "#key")
    fun slowEcho(key: String): String {
        Thread.sleep(400)
        return "$key:" + System.currentTimeMillis()
    }
}

