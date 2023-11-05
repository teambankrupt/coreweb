package com.example.coreweb.domains.labels.models.dtos

import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class LabelDto: BaseDto() {

    @NotBlank
    lateinit var name: String

    @NotNull
    lateinit var description: String

    @JsonProperty("parent_id")
    var parentId: Long? = null

    @JsonProperty("image")
    var image: String? = null

    @JsonProperty("color")
    var color: String = "0x000000"

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