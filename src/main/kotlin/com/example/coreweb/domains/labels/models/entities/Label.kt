package com.example.coreweb.domains.labels.models.entities

import com.example.coreweb.domains.base.entities.BaseTreeEntityV2
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "labels", schema = "core_web")
class Label : BaseTreeEntityV2<Label>() {
    @Column(nullable = false)
    lateinit var name: String

    @Column(name = "code", nullable = false)
    lateinit var code: String

    @Column(nullable = false, length = 500)
    lateinit var description: String

    @Column(nullable = false)
    var flagship: Boolean = false

    @Column(nullable = false)
    var serial: Int = 0

    @Column(name = "icon")
    var icon: String? = null

    @Column(name = "image")
    var image: String? = null

    @Column(name = "color", nullable = false)
    var color: String = "0xFFFFFF"

    @Column(name = "background_color", nullable = false)
    var backgroundColor: String = "0x000000"

    @Column(name = "ui_height", nullable = false)
    var uiHeight: Int = 100

    override fun getImpl(): Label = this
}