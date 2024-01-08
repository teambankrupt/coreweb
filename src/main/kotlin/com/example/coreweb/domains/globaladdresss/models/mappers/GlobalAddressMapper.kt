package com.example.coreweb.domains.globaladdresss.models.mappers

import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.globaladdresss.models.dtos.GlobalAddressDto
import com.example.coreweb.domains.globaladdresss.models.entities.GlobalAddress
import com.example.coreweb.domains.base.models.mappers.BaseMapper
import com.example.coreweb.domains.locations.models.dtos.LocationDto
import com.example.coreweb.domains.locations.models.entities.Coordinate
import com.example.coreweb.domains.locations.models.entities.Location
import com.example.coreweb.domains.locations.models.mappers.LocationMapper
import com.example.coreweb.domains.locations.repositories.LocationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class GlobalAddressMapper @Autowired constructor(
    private val locationRepository: LocationRepository,
    private val locationMapper: LocationMapper
) : BaseMapper<GlobalAddress, GlobalAddressDto> {

    override fun map(entity: GlobalAddress): GlobalAddressDto {
        val dto = GlobalAddressDto()

        dto.apply {
            this.id = entity.id
            this.createdAt = entity.createdAt
            this.updatedAt = entity.updatedAt

            this.addressLineOne = entity.addressLineOne
            this.addressLineTwo = entity.addressLineTwo
            this.zipCode = entity.zipCode
            this.title = entity.title
            this.latitude = entity.coordinate.latitude
            this.longitude = entity.coordinate.longitude
            this.altitude = entity.coordinate.altitude

            this.locationId = entity.location.id

//            this.locations = addLocation(map, entity.location).toSortedMap(Comparator { o1, o2 -> o2 - o1 })
            val locStack = entity.flattenLocation(entity.location, Stack())
            val locStackForAddress = Stack<Location>()
            locStackForAddress.addAll(locStack)

            while (!locStack.isEmpty()){
                val location = locStack.pop()
                this.locationsTree[location.type.code] = location.id
            }
            this.fullAddress = entity.buildAddress(locStackForAddress)
        }

        return dto
    }


    private fun addLocation(map: HashMap<Int, LocationDto>, location: Location): HashMap<Int, LocationDto> {
        map[location.type.level] = this.locationMapper.map(location)

        val parent = location.parent
        if (parent.isPresent)
            addLocation(map, parent.get())

        return map
    }


    override fun map(dto: GlobalAddressDto, exEntity: GlobalAddress?): GlobalAddress {
        val entity = exEntity ?: GlobalAddress()

        entity.apply {
            this.addressLineOne = dto.addressLineOne
            this.addressLineTwo = dto.addressLineTwo
            this.zipCode = dto.zipCode
            this.title = dto.title ?: "Home"
            this.coordinate = Coordinate(dto.latitude, dto.longitude, dto.altitude)
            this.location = locationRepository.find(dto.locationId)
                .orElseThrow { ExceptionUtil.notFound("Location", dto.locationId) }
        }

        return entity
    }
}
