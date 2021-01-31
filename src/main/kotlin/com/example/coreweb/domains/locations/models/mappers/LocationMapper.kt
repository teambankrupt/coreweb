package com.example.coreweb.domains.locations.models.mappers

import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.locations.models.dtos.LocationDto
import com.example.coreweb.domains.locations.models.entities.Location
import com.example.coreweb.domains.base.models.mappers.BaseMapper
import com.example.coreweb.domains.locations.repositories.LocationRepository
import com.example.coreweb.domains.locationtypes.repositories.LocationTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LocationMapper @Autowired constructor(
        private val locationTypeRepository: LocationTypeRepository,
        private val locationRepository: LocationRepository
) : BaseMapper<Location, LocationDto> {

    override fun map(entity: Location): LocationDto {
        val dto = LocationDto()
        dto.id = entity.id
        dto.createdAt = entity.createdAt
        dto.updatedAt = entity.updatedAt

        dto.label = entity.label
        dto.code = entity.code
        dto.description = entity.description
        dto.latitude = entity.latitude
        dto.longitude = entity.longitude
        dto.altitude = entity.altitude

        dto.typeId = entity.type.id
        dto.parentId = entity.parent?.id

        return dto
    }

    override fun map(dto: LocationDto, exEntity: Location?): Location {
        val entity = exEntity ?: Location()

        entity.label = dto.label
        entity.code = dto.code
        entity.description = dto.description
        entity.latitude = dto.latitude
        entity.longitude = dto.longitude
        entity.altitude = dto.altitude

        entity.type = this.locationTypeRepository.find(dto.typeId).orElseThrow { ExceptionUtil.notFound("LocationType", dto.typeId) }
        entity.parent = dto.parentId?.let { this.locationRepository.find(it).orElseThrow { ExceptionUtil.notFound("Parent", it) } }

        return entity
    }
}