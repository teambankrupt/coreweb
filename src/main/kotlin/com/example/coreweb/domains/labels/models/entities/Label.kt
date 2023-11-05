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

    @Column(nullable = false, length = 500)
    lateinit var description: String

    var image: String? = null

    var color: String = "0x000000"

    override fun getImpl(): Label = this
}