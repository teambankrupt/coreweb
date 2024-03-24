package com.example.coreweb.domains.alloweddomains.models.entities

import com.example.coreweb.domains.base.entities.BaseEntityV2
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "allowed_domains", schema = "core_web")
class AllowedDomain : BaseEntityV2() {

    @Column(name = "title", nullable = false, unique = true)
    var domain: String = ""

    @Column(name = "active", nullable = false)
    var active: Boolean = true

    @Column(name = "description", nullable = false)
    var description: String = ""

}