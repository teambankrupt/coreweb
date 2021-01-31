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
        dto.id = entity.id
        dto.createdAt = entity.createdAt
        dto.updatedAt = entity.updatedAt

        dto.label = entity.label
        dto.code = entity.code
        dto.description = entity.description
        dto.parentId = entity.parent?.id

        return dto
    }

    override fun map(dto: LocationTypeDto, exEntity: LocationType?): LocationType {
        val entity = exEntity ?: LocationType()

        entity.label = dto.label
        entity.code = dto.code
        entity.description = dto.description
        entity.parent = dto.parentId?.let { this.locationTypeRepository.find(it).orElseThrow { ExceptionUtil.notFound("Parent",it) } }

        return entity
    }
}