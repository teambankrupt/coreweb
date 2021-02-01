package com.example.coreweb.domains.contacts.models.entities

import com.example.coreweb.domains.base.entities.BaseEntity
import com.example.coreweb.domains.globaladdresss.models.entities.GlobalAddress
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*


@Entity
@Table(name = "contacts", schema = "core_web")
class Contact : BaseEntity() {

    @Column(nullable = false)
    lateinit var name: String

    @Column(nullable = false, unique = true)
    lateinit var phone: String

    var email: String? = null

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "contact_addresses", schema = "core_web",
        joinColumns = [JoinColumn(name = "contact_address_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "global_address_id", referencedColumnName = "id")]
    )
    //@ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    var address: MutableMap<String, GlobalAddress>? = null
}
