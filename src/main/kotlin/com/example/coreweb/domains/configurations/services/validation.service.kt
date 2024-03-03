package com.example.coreweb.domains.configurations.services

import arrow.core.Option
import com.example.common.validation.ValidationScope
import com.example.common.validation.genericValidation
import com.example.coreweb.domains.configurations.models.entities.Configuration
import com.example.coreweb.domains.configurations.models.enums.ConfigurationType
import com.example.coreweb.domains.configurations.models.enums.ValueType
import java.util.Optional


val valueValidation = genericValidation<Configuration>(
    message = "Title must be at least 3 characters long!",
    instruction = "Please provide a title with at least 3 characters!",
    scopes = setOf(ValidationScope.Write, ValidationScope.Modify)
) {
    try {
        when (it.valueType) {
            ValueType.STRING -> it.value.length >= 3
            ValueType.BOOLEAN -> it.value.toBoolean()
            ValueType.BYTE -> it.value.toByte()
            ValueType.SHORT -> it.value.toShort()
            ValueType.INTEGER, ValueType.INT -> it.value.toInt()
            ValueType.LONG -> it.value.toLong()
            ValueType.FLOAT -> it.value.toFloat()
            ValueType.DOUBLE -> it.value.toDouble()
            ValueType.CHAR -> it.value.length == 1
        }
        true
    } catch (e: Exception) {
        false
    }
}

fun uniqueKeyValidation(config: (namespace: String, type: ConfigurationType, key: String) -> Option<Configuration>) =
    genericValidation<Configuration>(
        message = "Configuration type and key must be unique!",
        instruction = "Please provide a unique key!",
        scopes = setOf(ValidationScope.Write)
    ) { c ->
        config(c.namespace, c.type, c.key).fold(
            { true },
            {
                if (c.isNew) false
                else it.id == c.id
            }
        )
    }
