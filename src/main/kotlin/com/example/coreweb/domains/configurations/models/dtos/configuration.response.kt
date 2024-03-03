package com.example.coreweb.domains.configurations.models.dtos

import com.example.coreweb.domains.configurations.models.entities.Configuration
import com.example.coreweb.domains.configurations.models.enums.ConfigurationType
import com.example.coreweb.domains.configurations.models.enums.ValueType
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant


data class ConfigurationBriefResponse(
    val id: Long,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val namespace: String,
    val type: ConfigurationType,
    @field:JsonProperty("value_type")
    val valueType: ValueType,
    val key: String,
    val value: String,
    val description: String?,
)

fun Configuration.toBriefResponse(): ConfigurationBriefResponse =
    ConfigurationBriefResponse(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        namespace = this.namespace,
        type = this.type,
        valueType = this.valueType,
        key = this.key,
        value = this.value,
        description = this.description
    )

data class ConfigurationDetailResponse(
    val id: Long,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val namespace: String,
    val type: ConfigurationType,
    @field:JsonProperty("value_type")
    val valueType: ValueType,
    val key: String,
    val value: String,
    val description: String?,
)

fun Configuration.toDetailResponse(): ConfigurationDetailResponse =
    ConfigurationDetailResponse(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        namespace = this.namespace,
        type = this.type,
        valueType = this.valueType,
        key = this.key,
        value = this.value,
        description = this.description
    )