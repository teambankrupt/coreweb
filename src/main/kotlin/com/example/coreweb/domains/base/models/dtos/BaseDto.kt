package com.example.coreweb.domains.base.models.dtos

import com.example.auth.config.security.SecurityContext
import com.example.common.utils.TimeUtility
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable
import java.time.Instant

open class BaseDto : Serializable {
    @JsonProperty("id", access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(readOnly = true, example = "1")
    var id: Long? = null

    @JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(
        readOnly = true,
        example = "2020-09-13T03:48:36Z",
        notes = "Date when this entity was first created. ( Format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z' )"
    )
    lateinit var createdAt: Instant

    @JsonProperty("updated_at", access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(
        readOnly = true,
        example = "2020-09-13T03:48:36Z",
        notes = "Date when this entity was last updated. ( Format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z' )"
    )
    var updatedAt: Instant? = null

    @JsonIgnore
    open fun getLoggedInUsername(): String? {
        return SecurityContext.getLoggedInUsername()
    }

    @JsonIgnore
    open fun getCreatedAtReadable(): String? {
        return TimeUtility.readableDateTimeFromInstant(this.createdAt)
    }

    @JsonIgnore
    open fun getUpdatedAtReadable(): String? {
        return TimeUtility.readableDateTimeFromInstant(this.updatedAt)
    }
}
