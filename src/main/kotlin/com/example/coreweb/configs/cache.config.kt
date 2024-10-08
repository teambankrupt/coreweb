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
	}

	@Bean
	open fun cacheManager(): CacheManager {
		return ConcurrentMapCacheManager(
			CacheNames.ALLOWED_DOMAINS,
			CacheNames.ORDERS,
			CacheNames.ARTICLES,
			CacheNames.WEBPAGES
		)
	}

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Dhaka")
	fun evictDailyCache() {
		cacheManager().getCache(CacheNames.ORDERS)?.clear()
		println("Evicting daily cache..")
	}

}

object CacheNames {
	const val ALLOWED_DOMAINS = "allowedDomains"
	const val ORDERS = "orders"
	const val WEBPAGES = "webpages"
	const val ARTICLES = "articles"
}
