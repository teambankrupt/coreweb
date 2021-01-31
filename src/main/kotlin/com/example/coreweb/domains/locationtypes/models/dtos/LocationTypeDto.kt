package com.example.coreweb.domains.locationtypes.models.dtos

import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
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