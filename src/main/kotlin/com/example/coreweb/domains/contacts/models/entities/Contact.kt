package com.example.coreweb.domains.contacts.models.entities

import com.example.coreweb.domains.base.entities.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "contacts", schema = "core_web")
class Contact : BaseEntity() {

    @Column(nullable = false)
    lateinit var name: String

    @Column(nullable = false, unique = true)
    lateinit var phone: String

    var email: String? = null
}
