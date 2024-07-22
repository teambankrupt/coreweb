package com.example.coreweb.domains.labels.models.dtos

import com.example.coreweb.domains.labels.models.entities.Label
import com.fasterxml.jackson.annotation.JsonProperty

data class LabelBriefResponse(
    val id: Long,

    val name: String,

    val code: String,

    val color: String?,

    @field:JsonProperty("background_color")
    val backgroundColor: String?
)

fun Label.toBriefResponse(): LabelBriefResponse =
    LabelBriefResponse(
        id = this.id,
        name = this.name,
        code = this.code,
        color = this.color,
        backgroundColor = this.backgroundColor
    )