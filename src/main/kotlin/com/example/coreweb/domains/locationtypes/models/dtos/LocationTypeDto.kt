package com.example.coreweb.domains.locationtypes.models.dtos

import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.example.coreweb.domains.locationtypes.models.entities.LocationType
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import java.time.Instant
import javax.persistence.Column
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

class LocationTypeDto : BaseDto() {

    @NotBlank
    @JsonProperty("label")
    @ApiModelProperty(required = true)
    lateinit var label: String

    @NotBlank
    @JsonProperty("code")
    @ApiModelProperty(required = true)
    lateinit var code: String

    @JsonProperty("level")
    @Min(1)
    var level: Int = 0

    @NotBlank
    @JsonProperty("description")
    @ApiModelProperty(required = true)
    lateinit var description: String

    @JsonProperty("parent_id")
    @ApiModelProperty("parent_id", required = false)
    var parentId: Long? = null

    /*
    READONLY PROPERTIES
     */
    @ApiModelProperty(readOnly = true)
    @JsonProperty("path")
    var path: String? = null

    @ApiModelProperty(readOnly = true)
    @JsonProperty("absolute_path")
    var absolutePath: String? = null

    @ApiModelProperty(readOnly = true)
    @JsonProperty("root_id")
    var rootId: Long? = null
}

data class LocationTypeResponse(
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("created_at")
    val createdAt: Instant?,

    @JsonProperty("updated_at")
    val updatedAt: Instant? = null,

    @JsonProperty("created_by")
    val createdBy: String?,

    @JsonProperty("label")
    val label: String,

    @JsonProperty("code")
    val code: String,

    @JsonProperty("level")
    val level: Int,

    @JsonProperty("description")
    val description: String,

    @JsonProperty("parent_id")
    val parentId: Long?,

    @JsonProperty("path")
    val path: String?,

    @JsonProperty("absolute_path")
    val absolutePath: String?,

    @JsonProperty("root_id")
    val rootId: Long?
)

data class LocationTypeDetailResponse(
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("created_at")
    val createdAt: Instant?,

    @JsonProperty("updated_at")
    val updatedAt: Instant? = null,

    @JsonProperty("created_by")
    val createdBy: String?,

    @JsonProperty("label")
    val label: String,

    @JsonProperty("code")
    val code: String,

    @JsonProperty("level")
    val level: Int,

    @JsonProperty("description")
    val description: String,

    @JsonProperty("parent_id")
    val parentId: Long?,

    @JsonProperty("path")
    val path: String?,

    @JsonProperty("absolute_path")
    val absolutePath: String?,

    @JsonProperty("root_id")
    val rootId: Long?,

    /*
    Detail Fields
     */
    @JsonProperty("parent")
    val parent: LocationTypeResponse?,

    )

fun LocationType.toResponse() = LocationTypeResponse(
    id = this.id!!,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    createdBy = this.createdBy,
    label = this.label,
    code = this.code,
    level = this.level,
    description = this.description,
    parentId = this.parent.map { it.id }.orElse(null),
    path = this.path,
    absolutePath = this.absolutePath,
    rootId = this.rootId

)

fun LocationType.toDetailResponse() = LocationTypeDetailResponse(
    id = this.id!!,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    createdBy = this.createdBy,
    label = this.label,
    code = this.code,
    level = this.level,
    description = this.description,
    parentId = this.parent.map { it.id }.orElse(null),
    path = this.path,
    absolutePath = this.absolutePath,
    rootId = this.rootId,
    parent = this.parent.map { it.toResponse() }.orElse(null)
)