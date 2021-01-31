package com.example.coreweb.domains.locations.models.dtos

import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
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

}