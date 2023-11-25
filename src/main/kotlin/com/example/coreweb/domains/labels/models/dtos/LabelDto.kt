package com.example.coreweb.domains.labels.models.dtos

import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class LabelDto : BaseDto() {

    @NotBlank
    @JsonProperty(required = true)
    lateinit var name: String

    @NotNull
    @JsonProperty(required = true)
    lateinit var description: String

    @JsonProperty("parent_id")
    var parentId: Long? = null

    @JsonProperty("icon")
    var icon: String? = null

    @JsonProperty("image")
    var image: String? = null

    @JsonProperty("color")
    var color: String? = null

    @JsonProperty("ui_height")
    var uiHeight: Int? = null

    @JsonProperty("background_color")
    var backgroundColor: String? = null

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