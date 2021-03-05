package com.example.coreweb.domains.profiles.models.entities

import com.example.auth.entities.User
import com.example.coreweb.domains.base.entities.BaseEntity
import com.example.coreweb.domains.contacts.models.entities.Contact
import com.example.coreweb.domains.profiles.models.enums.BloodGroup
import com.example.coreweb.domains.profiles.models.enums.Gender
import com.example.coreweb.domains.profiles.models.enums.MaritalStatus
import com.example.coreweb.domains.profiles.models.enums.Religion
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "profiles", schema = "core_web")
class Profile : BaseEntity() {

    @Column(nullable = false)
    lateinit var birthday: Instant

    var photo: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    lateinit var gender: Gender

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    var bloodGroup: BloodGroup? = null

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    var maritalStatus: MaritalStatus? = null

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    var religion: Religion? = null

    @OneToOne
    lateinit var user: User

    @OneToOne
    var contact: Contact? = null
}
