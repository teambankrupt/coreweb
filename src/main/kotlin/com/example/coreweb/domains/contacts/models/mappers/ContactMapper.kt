package com.example.app.domains.contact.models.mappers

import com.example.app.domains.contact.models.dtos.ContactDto
import com.example.coreweb.domains.contacts.models.entities.Contact
import com.example.coreweb.domains.base.models.mappers.BaseMapper
import org.springframework.stereotype.Component

@Component
class ContactMapper : BaseMapper<Contact, ContactDto> {

    override fun map(entity: Contact): ContactDto {
        val dto = ContactDto()
        dto.id = entity.id
        dto.createdAt = entity.createdAt
        dto.updatedAt = entity.updatedAt

        return dto
    }

    override fun map(dto: ContactDto, exEntity: Contact?): Contact {
        val entity = exEntity ?: Contact()

        return entity
    }
}
