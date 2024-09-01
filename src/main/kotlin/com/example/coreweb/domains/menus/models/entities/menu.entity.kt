package com.example.coreweb.domains.menus.models.entities

import com.example.coreweb.domains.base.entities.BaseTreeEntityV2
import com.example.coreweb.domains.menus.models.enums.MenuTypes
import javax.persistence.*

@Entity
@Table(name = "menus", schema = "core_web")
class Menu : BaseTreeEntityV2<Menu>() {

    @Column(name = "title", nullable = false)
    var title: String = ""

    @Column(name = "description", nullable = false)
    var description: String = ""

    @Column(name = "image")
    var image: String? = null

    var link: String? = null

    var type: MenuTypes = MenuTypes.BLOG

    override fun getImpl(): Menu = this

}