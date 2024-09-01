package com.example.coreweb.domains.menus.models.dtos

import com.example.coreweb.domains.menus.models.entities.Menu
import com.example.coreweb.domains.menus.models.enums.MenuTypes
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

data class MenuBriefResponse(
    val id: Long,

    @field:JsonProperty("created_at")
    val createdAt: Instant,

    @field:JsonProperty("updated_at")
    val updatedAt: Instant? = null,

    val title: String,

    val description: String,

    val image: String?,

    val link: String?,

    val type: MenuTypes,

    @field:JsonProperty("parent_id")
    val parentId: Long?
)

fun Menu.toBriefResponse(): MenuBriefResponse =
    MenuBriefResponse(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        title = this.title,
        description = this.description,
        image = this.image,
        link = this.link,
        type = this.type,
        parentId = this.parent?.map { it.id }?.getOrNull()
    )

data class MenuDetailResponse(
    val id: Long,

    @field:JsonProperty("created_at")
    val createdAt: Instant,

    @field:JsonProperty("updated_at")
    val updatedAt: Instant? = null,

    val title: String,

    val description: String,

    val image: String?,

    val link: String?,

    val type: MenuTypes,

    @field:JsonProperty("parent")
    val parent: MenuBriefResponse?
)

fun Menu.toDetailResponse(): MenuDetailResponse =
    MenuDetailResponse(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        title = this.title,
        description = this.description,
        image = this.image,
        link = this.link,
        type = this.type,
        parent = this.parent?.map { it.toBriefResponse() }?.getOrNull()
    )