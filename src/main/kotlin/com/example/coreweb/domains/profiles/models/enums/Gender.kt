package com.example.coreweb.domains.profiles.models.enums

import com.example.common.utils.ExceptionUtil
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 5/3/21
 */
enum class Gender(
    var id: Byte,
    @JsonProperty("label") var label: String
) {

    MALE(0, "Male"),
    FEMALE(1, "Female"),
    OTHER(2, "Other");

    companion object {

        @JvmStatic
        fun get(x: Byte): Gender {
            for (g: Gender in values()) {
                if (g.id == x)
                    return g
            }
            return OTHER
        }

        @JvmStatic
        fun get(x: String): Gender {
            for (g: Gender in values()) {
                if (g.name == x || g.label == x)
                    return g
            }
            return OTHER
        }

        @JvmStatic
        fun validate(x: Byte): Byte {
            if (values().any { it.id == x }) return x
            else throw ExceptionUtil.notFound(Gender::class.simpleName.toString(), x.toLong())
        }
    }
}
