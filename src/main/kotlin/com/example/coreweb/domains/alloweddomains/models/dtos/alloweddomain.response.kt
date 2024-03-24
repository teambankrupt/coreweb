package com.example.app.domains.alloweddomains.models.dtos

import com.example.coreweb.domains.alloweddomains.models.entities.AllowedDomain
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class AllowedDomainBriefResponse(
    val id: Long,

    @field:JsonProperty("created_at")
    val createdAt: Instant,

    @field:JsonProperty("updated_at")
    val updatedAt: Instant? = null,

    val domain: String,

    val active: Boolean,

    val description: String
)

fun AllowedDomain.toBriefResponse(): AllowedDomainBriefResponse =
    AllowedDomainBriefResponse(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        domain = this.domain,
        active = this.active,
        description = this.description
    )

data class AllowedDomainDetailResponse(
    val id: Long,

    @field:JsonProperty("created_at")
    val createdAt: Instant,

    @field:JsonProperty("updated_at")
    val updatedAt: Instant? = null,

    val domain: String,

    val active: Boolean,

    val description: String
)

fun AllowedDomain.toDetailResponse(): AllowedDomainDetailResponse =
    AllowedDomainDetailResponse(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        domain = this.domain,
        active = this.active,
        description = this.description
    )