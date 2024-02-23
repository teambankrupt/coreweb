package com.example.coreweb.configs

enum class Locales {
    EN_US, BN_BD
}

fun String.toLocale(): Locales {
    return when (this) {
        "en-US" -> Locales.EN_US
        "bn-BN" -> Locales.BN_BD
        else -> Locales.EN_US
    }
}