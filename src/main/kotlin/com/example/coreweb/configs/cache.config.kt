package com.example.coreweb.configs

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled


@Configuration
@EnableCaching
open class CacheConfig {
    companion object {
        const val CACHE_NAME = "bankrupt-cache"
        const val CACHE_NAME_DAILY = "bankrupt-cache-daily"
    }

    @Bean
    open fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager(CACHE_NAME, CACHE_NAME_DAILY)
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Dhaka")
    fun evictDailyCache() {
        cacheManager().getCache(CACHE_NAME_DAILY)?.clear()
        println("Evicting daily cache..")
    }
}
