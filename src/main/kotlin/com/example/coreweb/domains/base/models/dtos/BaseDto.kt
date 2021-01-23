package com.example.coreweb.domains.base.models.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable
import java.time.Instant

open class BaseDto : Serializable {
    @JsonProperty("id")
    @ApiModelProperty(readOnly = true, example = "1")
    var id: Long? = null

    @JsonProperty(value = "created_at")
    @ApiModelProperty(readOnly = true, example = "2020-09-13T03:48:36Z", notes = "Date when this entity was first created. ( Format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z' )")
    lateinit var createdAt: Instant

    @JsonProperty("updated_at")
    @ApiModelProperty(readOnly = true,example = "2020-09-13T03:48:36Z", notes = "Date when this entity was last updated. ( Format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z' )")
    lateinit var updatedAt: Instant
}
