package com.example.coreweb.domains.locations.models.entities

import com.example.common.utils.ExceptionUtil
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

    var path: String? = null

    @Embedded
    lateinit var coorodinate: Coordinate

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    lateinit var type: LocationType

    @ManyToOne(optional = true)
    @JoinColumn(name = "parent_id", nullable = true)
    var parent: Location? = null

    @PrePersist
    @PreUpdate
    fun onNodePersist() {
        this.parent?.let {
            path = if (it.path.isNullOrBlank()) it.id.toString() else "${it.path}:${it.id}"
        }
    }

    fun getRootId(): Long {
        return if (this.path.isNullOrBlank()) this.id
        else this.path!!.split(":")[0].toLong()
    }

    fun hasParent(parent: LocationType): Boolean {
        if (parent.isNew) throw ExceptionUtil.invalid("Entity that isn't persisted yet can't be a parent.")
        return parent.id == this.parent?.id
    }

    fun getAbsolutePath(): String {
        if (this.isNew) throw ExceptionUtil.invalid("Entity that isn't persisted yet can't have absolute path.")
        if (path == null) return "$id"
        return "$path:$id"
    }

}