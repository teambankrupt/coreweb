package com.example.coreweb.domains.locationtypes.services.beans

import com.example.coreweb.domains.locationtypes.models.entities.LocationType
import com.example.coreweb.domains.locationtypes.repositories.LocationTypeRepository
import com.example.coreweb.domains.locationtypes.services.LocationTypeService
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.utils.PageAttr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*
import com.example.coreweb.domains.base.models.enums.SortByFields
import org.springframework.data.domain.Sort

@Service
class LocationTypeServiceBean @Autowired constructor(
    private val locationTypeRepository: LocationTypeRepository
) : LocationTypeService {

    override fun search(
        query: String,
        page: Int,
        size: Int,
        sortBy: SortByFields,
        direction: Sort.Direction
    ): Page<LocationType> {
        return this.locationTypeRepository.search(
            query.toLowerCase(),
            PageAttr.getPageRequest(page, size, direction, "level", sortBy.fieldName)
        )
    }

    override fun save(entity: LocationType): LocationType {
        this.validate(entity)
        return this.locationTypeRepository.save(entity)
    }

    override fun find(id: Long): Optional<LocationType> {
        return this.locationTypeRepository.find(id)
    }

    override fun findByIds(ids: Set<Long>): Set<LocationType> =
        this.locationTypeRepository.findByIds(ids)

    override fun delete(id: Long, softDelete: Boolean) {
        if (this.locationTypeRepository.childCount(id) > 0)
            throw ExceptionUtil.forbidden("LocationType with children can't be deleted!")

        if (softDelete) {
            val entity = this.find(id).orElseThrow { ExceptionUtil.notFound("LocationType", id) }
            entity.isDeleted = true
            this.locationTypeRepository.save(entity)
            return
        }
        this.locationTypeRepository.deleteById(id)
    }

    override fun validate(entity: LocationType) {
        val locationType = this.locationTypeRepository.findByCodeIncludingDeleted(entity.code)
        if (locationType.isPresent) {
            if (entity.isNew)
                throw ExceptionUtil.exists("Location Type already exists with code: ${entity.code}")
            else {
                /*
             check if updating entity is different from existing entity, if it's different, that means
             this entity code is changed, but the changed code belongs another existing entity, which is not allowed
             */
                if (entity.id != locationType.get().id) throw ExceptionUtil.exists("Another location has same code that you've entered.")
            }
        }

        // check if entity is a parent of it's own
        if (!entity.isNew) {
            if (entity.parentId.orElse(null) == entity.id)
                throw ExceptionUtil.forbidden("${entity.label} can not be it's own parent.")
        }
    }
}