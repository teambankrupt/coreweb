package com.example.coreweb.configs

import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.alloweddomains.services.AllowedDomainService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.net.InetAddress
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Configuration
open class CustomCorsConfiguration {

    @Bean
    open fun domainFilterRegistration(domainFilter: DomainFilter): FilterRegistrationBean<*> {
        val registration = FilterRegistrationBean(domainFilter)
        registration.order = 1
        return registration
    }

}

@Component
class DomainFilter(
    private val allowedDomainService: AllowedDomainService,
) : Filter {
    private val logger: Logger = LoggerFactory.getLogger(DomainFilter::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val domain = httpRequest.serverName
        val hostAddress = InetAddress.getLocalHost().hostAddress
        val allowedDomains = allowedDomainService.allowedDomains()
            .plus(hostAddress)

        if (allowedDomains.contains(domain)) {
            chain.doFilter(request, response)
        } else {
            throw ExceptionUtil.forbidden("Domain $domain is not allowed and also differed from host address $hostAddress!")
        }
    }
}
