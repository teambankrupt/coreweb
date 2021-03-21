package com.example.coreweb.domains.globaladdresss.models.entities

import com.example.coreweb.domains.base.entities.BaseEntity
import com.example.coreweb.domains.locations.models.entities.Coordinate
import com.example.coreweb.domains.locations.models.entities.Location
import java.lang.StringBuilder
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "global_addresss", schema = "core_web")
class GlobalAddress : BaseEntity() {

    @Column(name = "address_line_one", nullable = false)
    lateinit var addressLineOne: String

    @Column(name = "address_line_two")
    var addressLineTwo: String? = null

    @Column(name = "zip_code")
    var zipCode: String? = null

    lateinit var title: String

    @Embedded
    lateinit var coordinate: Coordinate

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    lateinit var location: Location

    fun buildAddress(): String {
        val builder = StringBuilder()
        builder.append(this.addressLineOne)
            .append(", ")
            .append(this.addressLineTwo)
        val locStack = this.formatAddressFromLocation(location, Stack())
        locStack.reverse()
        while (!locStack.empty())
            builder.append(locStack.pop())
        return builder.toString()
    }

    private fun formatAddressFromLocation(location: Location, stack: Stack<String>): Stack<String> {
        if (location.parent == null) {
            stack.push(", " + location.label)
            return stack
        }
        return formatAddressFromLocation(location.parent!!, stack)
    }

}
