package com.example.coreweb.domains.locations.models.dtos

import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.example.coreweb.domains.locations.models.entities.Location
import com.example.coreweb.domains.locationtypes.models.dtos.LocationTypeDto
import com.example.coreweb.domains.locationtypes.models.dtos.LocationTypeResponse
import com.example.coreweb.domains.locationtypes.models.dtos.toResponse
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import java.time.Instant
import javax.persistence.Column
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class LocationDto : BaseDto() {

    @NotBlank
    @ApiModelProperty(required = true)
    @JsonProperty("label")
    lateinit var label: String

    @NotBlank
    @ApiModelProperty(required = true)
    @JsonProperty("code")
    lateinit var code: String

    @ApiModelProperty(required = false)
    @JsonProperty("description")
    var description: String? = null

    @JsonProperty("image")
    var image: String? = null

    @NotNull
    @ApiModelProperty(required = true)
    @JsonProperty("latitude")
    var latitude: Double = 0.0

    @NotNull
    @ApiModelProperty(required = true)
    @JsonProperty("longitude")
    var longitude: Double = 0.0

    @NotNull
    @ApiModelProperty(required = true)
    @JsonProperty("altitude")
    var altitude: Double = 0.0

    @NotNull
    @Min(1)
    @JsonProperty("type_id")
    var typeId: Long = 0

    @JsonProperty("parent_id")
    var parentId: Long? = null

    @JsonProperty("zip_code")
    var zipCode: String? = null

    /*
    READONLY PROPERTIES
     */

    @ApiModelProperty(readOnly = true)
    @JsonProperty("location_type")
    var typeDto: LocationTypeDto? = null

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

data class LocationResponse(
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

    @JsonProperty("description")
    val description: String? = null,

    @JsonProperty("image")
    val image: String? = null,

    @JsonProperty("latitude")
    val latitude: Double = 0.0,

    @JsonProperty("longitude")
    val longitude: Double = 0.0,

    @JsonProperty("altitude")
    val altitude: Double = 0.0,

    @JsonProperty("parent_id")
    val parentId: Long? = null,

    @JsonProperty("zip_code")
    val zipCode: String? = null,

    @JsonProperty("type_id")
    val typeId: Long? = null,

    @JsonProperty("path")
    val path: String? = null,

    @JsonProperty("absolute_path")
    val absolutePath: String? = null,

    @JsonProperty("root_id")
    val rootId: Long? = null
)

fun Location.toResponse() = LocationResponse(
    id = this.id!!,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    createdBy = this.createdBy,
    label = this.label,
    code = this.code,
    description = this.description,
    image = this.image,
    latitude = this.coorodinate.latitude,
    longitude = this.coorodinate.longitude,
    altitude = this.coorodinate.altitude,
    parentId = this.parent.map { it.id }.orElse(null),
    zipCode = this.zipCode,
    typeId = this.type.id!!,
    path = this.path,
    absolutePath = this.absolutePath,
    rootId = this.rootId
)


data class LocationDetailResponse(
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

    @JsonProperty("description")
    val description: String? = null,

    @JsonProperty("image")
    val image: String? = null,

    @JsonProperty("latitude")
    val latitude: Double = 0.0,

    @JsonProperty("longitude")
    val longitude: Double = 0.0,

    @JsonProperty("altitude")
    val altitude: Double = 0.0,

    @JsonProperty("parent_id")
    val parentId: Long? = null,

    @JsonProperty("zip_code")
    val zipCode: String? = null,

    @JsonProperty("type_id")
    val typeId: Long? = null,

    @JsonProperty("path")
    val path: String? = null,

    @JsonProperty("absolute_path")
    val absolutePath: String? = null,

    @JsonProperty("root_id")
    val rootId: Long? = null,

    /*
    Detail Fields
     */
    @JsonProperty("type")
    val type: LocationTypeResponse? = null,

    @JsonProperty("parent")
    val parent: LocationResponse? = null,
)

fun Location.toDetailResponse() = LocationDetailResponse(
    id = this.id!!,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    createdBy = this.createdBy,
    label = this.label,
    code = this.code,
    description = this.description,
    image = this.image,
    latitude = this.coorodinate.latitude,
    longitude = this.coorodinate.longitude,
    altitude = this.coorodinate.altitude,
    parentId = this.parent.map { it.id }.orElse(null),
    zipCode = this.zipCode,
    typeId = this.type.id!!,
    path = this.path,
    absolutePath = this.absolutePath,
    rootId = this.rootId,
    type = this.type.toResponse(),
    parent = this.parent.map { it.toResponse() }.orElse(null)
)