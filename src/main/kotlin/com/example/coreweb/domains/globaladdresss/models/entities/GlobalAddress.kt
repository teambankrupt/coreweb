package com.example.coreweb.domains.globaladdresss.models.entities

import com.example.coreweb.domains.base.entities.BaseEntity
import com.example.coreweb.domains.locations.models.entities.Coordinate
import com.example.coreweb.domains.locations.models.entities.Location
import java.lang.StringBuilder
import java.util.*
import javax.persistence.*
import kotlin.collections.HashSet

@Entity
@Table(name = "global_addresss", schema = "core_web")
class GlobalAddress : BaseEntity() {

    lateinit var title: String

    @Column(name = "address_line_one", nullable = false)
    lateinit var addressLineOne: String

    @Column(name = "address_line_two")
    var addressLineTwo: String? = null

    @Column(name = "zip_code")
    var zipCode: String? = null

    @Embedded
    lateinit var coordinate: Coordinate

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    lateinit var location: Location

    fun buildAddress(): String {
        val locStack = this.flattenLocation(location, Stack())
        return buildAddress(locStack)
    }

    fun buildAddress(locStack: Stack<Location>): String {
        val builder = StringBuilder()
        builder.append(this.addressLineOne)
        if (this.addressLineTwo != null) {
            builder
                .append(", ")
                .append(this.addressLineTwo)
        }
        locStack.reverse()
        while (!locStack.empty())
            builder.append(", ").append(locStack.pop().label)
        return builder.toString()
    }

    fun flattenLocation(location: Location, stack: Stack<Location>): Stack<Location> {
        stack.push(location)
        if (location.parent.isPresent)
            flattenLocation(location.parent.get(), stack)
        return stack
    }

}
