package com.example.coreweb.domains.menus.models.dtos

import arrow.core.Option
import com.example.coreweb.domains.menus.models.entities.Menu
import com.example.coreweb.domains.menus.models.enums.MenuTypes
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


data class MenuReq(
    @NotBlank
    @Size(min = 3, max = 255)
    val title: String,

    @NotBlank
    @Size(min = 3, max = 255)
    val description: String,

    val image: String?,

    @Size(min = 3, max = 255)
    val link: String?,

    @NotNull
    val type: MenuTypes,

    @field:JsonProperty("parent_id")
    val parentId: Long?
) {
    fun asMenu(
        menu: Menu = Menu(),
        getMenu: (id: Long) -> Option<Menu>,
    ): Menu =
        this.let { req ->
            menu.apply {
                this.title = req.title
                this.description = req.description
                this.image = req.image
                this.link = req.link
                this.type = type
                this.setParent(
                    req.parentId?.let {
                        getMenu(it).getOrNull()
                    }
                )
            }
        }
}