package com.example.coreweb.domains.configurations.models.dtos

import arrow.core.getOrElse
import com.example.common.exceptions.invalid.InvalidException
import com.example.common.utils.HtmlSanitizer
import com.example.common.utils.SessionIdentifierGenerator
import com.example.coreweb.domains.configurations.models.entities.Configuration
import com.example.coreweb.domains.configurations.models.enums.ConfigurationType
import com.example.coreweb.domains.configurations.models.enums.ValueType
import com.fasterxml.jackson.annotation.JsonProperty
import java.lang.reflect.Field
import java.util.*
import java.util.stream.Collectors
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


data class ConfigurationReq(
    @field:JsonProperty("namespace")
    @field:Size(min = 1, max = 20, message = "Namespace must be between 1 and 20 characters long!")
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

fun Any.asConfiguration(
    namespace: String = SessionIdentifierGenerator.alphanumeric(15),
    type: ConfigurationType,
    sanitizeValue: Boolean = false
): Set<Configuration> = this.let { obj ->
    Arrays.stream(obj.javaClass.declaredFields)
        .map { f: Field ->
            val fieldType = f.type.simpleName.uppercase(Locale.getDefault())
            f.isAccessible = true
            Configuration().apply {
                this.namespace = namespace
                this.type = type
                this.key = f.name
                this.valueType = ValueType.fromString(fieldType).getOrElse {
                    throw InvalidException("Invalid value type for: $fieldType!")
                }
                val value = f.get(obj).toString()
                this.value = if (!sanitizeValue) value else HtmlSanitizer.sanitizeHTML(value)
                this.description = type.toString() + " config for: " + f.name
            }
        }.collect(Collectors.toSet())
}
