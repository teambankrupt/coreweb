package com.example.coreweb.domains.labels.models.dtos

import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class LabelDto : BaseDto() {

    @NotBlank
    @JsonProperty(required = true)
    lateinit var name: String

    @Size(max = 100)
    @JsonProperty("code")
    var code: String? = null

    @NotNull
    @JsonProperty(required = true)
    lateinit var description: String

    @JsonProperty("parent_id")
    var parentId: Long? = null

    @JsonProperty("flagship")
    var flagship: Boolean = false

    @JsonProperty("serial")
    var serial: Int = 0

    @JsonProperty("icon")
    var icon: String? = null

    @JsonProperty("image")
    var image: String? = null

    @Size(max = 10)
    @JsonProperty("color")
    var color: String? = null

    @Size(max = 10)
    @JsonProperty("background_color")
    var backgroundColor: String? = null

    @JsonProperty("ui_height")
    var uiHeight: Int? = null

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