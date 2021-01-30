package com.example.coreweb.domains.locationtypes.models.entities

import com.example.common.utils.ExceptionUtil
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

    var path: String? = null

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    var parent: LocationType? = null

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