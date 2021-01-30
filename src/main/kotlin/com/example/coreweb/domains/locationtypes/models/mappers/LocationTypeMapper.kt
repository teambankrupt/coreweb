package com.example.coreweb.domains.locationtypes.models.mappers

import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.locationtypes.models.dtos.LocationTypeDto
import com.example.coreweb.domains.locationtypes.models.entities.LocationType
import com.example.coreweb.domains.base.models.mappers.BaseMapper
import com.example.coreweb.domains.locationtypes.repositories.LocationTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LocationTypeMapper @Autowired constructor(
        private val locationTypeRepository: LocationTypeRepository
) : BaseMapper<LocationType, LocationTypeDto> {

    override fun map(entity: LocationType): LocationTypeDto {
        val dto = LocationTypeDto()

        dto.apply {
            this.id = entity.id
            this.createdAt = entity.createdAt
            this.updatedAt = entity.updatedAt

            this.label = entity.label
            this.code = entity.code
            this.level = entity.level
            this.description = entity.description
            this.parentId = entity.parent?.id

            this.path = entity.path
            this.absolutePath = entity.getAbsolutePath()
            this.rootId =entity.getRootId()
        }

        return dto
    }

    override fun map(dto: LocationTypeDto, exEntity: LocationType?): LocationType {
        val entity = exEntity ?: LocationType()

        entity.apply {
            this.label = dto.label
            this.code = dto.code
            this.level = dto.level
            this.description = dto.description
            this.parent = dto.parentId?.let { locationTypeRepository.find(it).orElseThrow { ExceptionUtil.notFound("Parent", it) } }
        }

        return entity
    }
}