package com.example.coreweb.domains.alloweddomains.models.dtos

import com.example.coreweb.domains.alloweddomains.models.entities.AllowedDomain

data class AllowedDomainReq(
    val domain: String,
    val description: String,
    val active: Boolean,
) {
    fun asAllowedDomain(allowedDomain: AllowedDomain = AllowedDomain()): AllowedDomain =
        this.let { req ->
            allowedDomain.apply {
                this.domain = req.domain
                this.description = req.description
                this.active = req.active
            }
        }
}