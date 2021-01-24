package com.example.coreweb.domains.locations.services.beans

import com.example.coreweb.domains.locations.models.entities.Location
import com.example.coreweb.domains.locations.repositories.LocationRepository
import com.example.coreweb.domains.locations.services.LocationService
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.utils.PageAttr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*
import com.example.coreweb.domains.base.models.enums.SortByFields
import org.springframework.data.domain.Sort

@Service
class LocationServiceBean @Autowired constructor(
        private val locationRepository: LocationRepository
) : LocationService {

    override fun searchForType(typeId: Long, query: String, page: Int, size: Int, sortBy: SortByFields, direction: Sort.Direction): Page<Location> {
        return this.locationRepository.search(query.toLowerCase(), typeId, PageAttr.getPageRequest(page, size, sortBy.fieldName, direction))
    }

    override fun search(query: String, page: Int, size: Int, sortBy: SortByFields, direction: Sort.Direction): Page<Location> {
        return this.locationRepository.search(query, null, PageAttr.getPageRequest(page, size, sortBy.fieldName, direction))
    }

    override fun save(entity: Location): Location {
        this.validate(entity)
        return this.locationRepository.save(entity)
    }

    override fun find(id: Long): Optional<Location> {
        return this.locationRepository.find(id)
    }

    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val entity = this.find(id).orElseThrow { ExceptionUtil.notFound("Location", id) }
            entity.isDeleted = true
            this.locationRepository.save(entity)
        }
        this.locationRepository.deleteById(id)
    }

    override fun validate(entity: Location) {

        // UNIQUE CODE VALIDATION
        val location = this.locationRepository.findByCodeIncludingDeleted(entity.code)
        if (location.isPresent) {
            if (entity.isNew)
                throw ExceptionUtil.exists("Location Type already exists with code: ${entity.code}")
            else {
                /*
             check if updating entity is different from existing entity, if it's different, that means
             this entity code is changed, but the changed code belongs another existing entity, which is not allowed
             */
                if (entity.id != location.get().id) throw ExceptionUtil.exists("Another location has same code that you've entered.")
            }
        }

        // LOCATION TYPE VALIDATION

        /*
        If Location does not have a parent, then it must have a location type without a parent.
         */
        if (entity.parent == null && entity.type.parent != null)
            throw ExceptionUtil.invalid("If Location does not have a parent, then it must have a location type without a parent.")

        /*
        If Location has a parent, then parent LocationType must match child entity LocationType parent
         */
        val parent = entity.parent
        if (parent != null) {
            val parentsLocationTypeId = parent.type.id
            val locationTypesParentId = entity.type.parent?.id
            if (parentsLocationTypeId != locationTypesParentId)
                throw ExceptionUtil.invalid("If Location has a parent, then parent LocationType must match child entity LocationType parent")
        }

        // check if entity is a parent of it's own
        if (entity.isNew) {
            if (entity.parent?.id == entity.id)
                throw ExceptionUtil.forbidden("${entity.label} can not be it's own parent.")
        }
    }
}