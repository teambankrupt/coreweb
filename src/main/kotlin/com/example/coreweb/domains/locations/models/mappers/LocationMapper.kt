package com.example.coreweb.domains.locations.models.mappers

import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.locations.models.dtos.LocationDto
import com.example.coreweb.domains.locations.models.entities.Location
import com.example.coreweb.domains.base.models.mappers.BaseMapper
import com.example.coreweb.domains.locations.models.entities.Coordinate
import com.example.coreweb.domains.locations.repositories.LocationRepository
import com.example.coreweb.domains.locationtypes.models.mappers.LocationTypeMapper
import com.example.coreweb.domains.locationtypes.repositories.LocationTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LocationMapper @Autowired constructor(
    private val locationTypeRepository: LocationTypeRepository,
    private val locationRepository: LocationRepository,
    private val locationTypeMapper: LocationTypeMapper
) : BaseMapper<Location, LocationDto> {

    override fun map(entity: Location): LocationDto {
        val dto = LocationDto()
        dto.id = entity.id
        dto.createdAt = entity.createdAt
        dto.updatedAt = entity.updatedAt

        dto.label = entity.label
        dto.code = entity.code
        dto.zipCode = entity.zipCode
        dto.description = entity.description
        dto.image = entity.image

        dto.latitude = entity.coorodinate.latitude
        dto.longitude = entity.coorodinate.longitude
        dto.altitude = entity.coorodinate.altitude

        dto.typeId = entity.type.id
        dto.parentId = entity.parentId.orElse(null)

        dto.typeDto = this.locationTypeMapper.map(entity.type)
        dto.path = entity.path
        dto.absolutePath = entity.absolutePath
        dto.rootId = entity.rootId

        return dto
    }

    override fun map(dto: LocationDto, exEntity: Location?): Location {
        val entity = exEntity ?: Location()

        entity.label = dto.label
        entity.code = dto.code
        entity.description = dto.description
        entity.image = dto.image
        entity.zipCode = dto.zipCode

        entity.coorodinate = Coordinate(dto.latitude, dto.longitude, dto.altitude)

        entity.type = this.locationTypeRepository.find(dto.typeId)
            .orElseThrow { ExceptionUtil.notFound("LocationType", dto.typeId) }
        entity.setParent(
            dto.parentId?.let { this.locationRepository.find(it).orElseThrow { ExceptionUtil.notFound("Parent", it) } }
        )
        return entity
    }
}
