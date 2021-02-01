package com.example.coreweb.domains.contacts.models.mappers

import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.contacts.models.dtos.ContactDto
import com.example.coreweb.domains.contacts.models.entities.Contact
import com.example.coreweb.domains.base.models.mappers.BaseMapper
import com.example.coreweb.domains.globaladdresss.services.GlobalAddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ContactMapper @Autowired constructor(
    private val globalAddressService: GlobalAddressService
) : BaseMapper<Contact, ContactDto> {

    override fun map(entity: Contact): ContactDto {
        val dto = ContactDto()
        dto.apply {
            id = entity.id
            createdAt = entity.createdAt
            updatedAt = entity.updatedAt

            name = entity.name
            phone = entity.phone
            email = entity.email
            address = entity.address?.mapValues { it.value.id }?.toMutableMap()
        }
        return dto
    }

    override fun map(dto: ContactDto, exEntity: Contact?): Contact {
        val entity = exEntity ?: Contact()
        entity.apply {
            name = dto.name
            phone = dto.phone
            email = dto.email
            address = dto.address?.mapValues {
                globalAddressService.find(it.value).orElseThrow { ExceptionUtil.notFound("Global Address", it.value) }
            }?.toMutableMap()
        }
        return entity
    }
}
