package com.example.coreweb.domains.locations.models.entities

import com.example.coreweb.domains.base.entities.BaseEntity
import com.example.coreweb.domains.locationtypes.models.entities.LocationType
import javax.persistence.*

@Entity
@Table(name = "locations", schema = "core_web")
class Location : BaseEntity() {

    @Column(name = "label", nullable = false)
    lateinit var label: String

    @Column(name = "code", nullable = false, unique = true)
    lateinit var code: String

    @Column(name = "description")
    var description: String? = null

    @Embedded
    lateinit var coorodinate: Coordinate

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    lateinit var type: LocationType

    @ManyToOne(optional = true)
    @JoinColumn(name = "parent_id", nullable = true)
    var parent: Location? = null

}