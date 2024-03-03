package com.example.coreweb.domains.configurations.models.dtos

import com.example.coreweb.domains.configurations.models.entities.Configuration
import com.example.coreweb.domains.configurations.models.enums.ConfigurationType
import com.example.coreweb.domains.configurations.models.enums.ValueType
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


data class ConfigurationReq(
    @field:JsonProperty("namespace")
    val namespace: String,

    @field:JsonProperty("type")
    val type: ConfigurationType,

    @field:JsonProperty("value_type")
    val valueType: ValueType,

    @field:Size(max = 50)
    @field:NotBlank
    val key: String,

    @field:NotBlank
    val value: String,

    val description: String?,
) {
    fun asConfiguration(configuration: Configuration = Configuration()): Configuration =
        this.let { req ->
            configuration.apply {
                this.namespace = req.namespace
                this.type = req.type
                this.valueType = req.valueType
                this.key = req.key
                this.value = req.value
                this.description = req.description
            }
        }
}