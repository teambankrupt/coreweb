package com.example.app.domains.contact.models.mappers

import com.example.coreweb.domains.contacts.models.dtos.ContactDto
import com.example.coreweb.domains.contacts.models.entities.Contact
import com.example.coreweb.domains.base.models.mappers.BaseMapper
import org.springframework.stereotype.Component

@Component
class ContactMapper : BaseMapper<Contact, ContactDto> {

    override fun map(entity: Contact): ContactDto {
        val dto = ContactDto()
        dto.apply {
            id = entity.id
            createdAt = entity.createdAt
            updatedAt = entity.updatedAt

            name = entity.name
            phone = entity.phone
            email = entity.email
            address = entity.address
        }
        return dto
    }

    override fun map(dto: ContactDto, exEntity: Contact?): Contact {
        val entity = exEntity ?: Contact()
        entity.apply {
            name = dto.name
            phone = dto.phone
            email = dto.email
            address = dto.address
        }
        return entity
    }
}
