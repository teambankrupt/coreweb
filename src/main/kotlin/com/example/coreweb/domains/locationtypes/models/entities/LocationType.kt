package com.example.coreweb.domains.locationtypes.models.entities

import com.example.coreweb.domains.base.entities.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "location_types", schema = "core_web")
class LocationType : BaseEntity() {

    @Column(name = "label", nullable = false)
    lateinit var label: String


    @Column(name = "code", nullable = false, unique = true)
    lateinit var code: String

    @Column(name = "level", nullable = false)
    var level: Int = 0

    @Column(name = "description", nullable = false)
    lateinit var description: String

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    var parent: LocationType? = null

}