package com.example.coreweb.domains.configurations.models.enums

import arrow.core.Option
import arrow.core.firstOrNone


enum class ValueType {
    STRING,
    BOOLEAN,
    BYTE,
    SHORT,
    INTEGER,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    CHAR;

    companion object {
        fun fromString(str: String): Option<ValueType> =
            entries.filter { it.toString().equals(str, ignoreCase = true) }
                .firstOrNone()
    }
}