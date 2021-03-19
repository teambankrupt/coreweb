package com.example.coreweb.domains.contacts.models.mappers

import com.example.auth.entities.User
import com.example.auth.repositories.UserRepo
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.contacts.models.dtos.ContactDto
import com.example.coreweb.domains.contacts.models.entities.Contact
import com.example.coreweb.domains.base.models.mappers.BaseMapper
import com.example.coreweb.domains.globaladdresss.services.GlobalAddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ContactMapper @Autowired constructor(
    private val globalAddressService: GlobalAddressService,
    private val userRepo: UserRepo
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
            address = entity.address?.map { it.id }?.toMutableList()
            userId = entity.user.id
        }
        return dto
    }

    override fun map(dto: ContactDto, exEntity: Contact?): Contact {
        val entity = exEntity ?: Contact()
        entity.apply {
            name = dto.name
            phone = dto.phone
            email = dto.email
            address = dto.address?.map {
                globalAddressService.find(it).orElseThrow { ExceptionUtil.notFound("Global Address", it) }
            }?.toMutableList()
            this.user = userRepo.findById(dto.userId).orElseThrow { ExceptionUtil.notFound(User::class.java,dto.userId) }
        }
        return entity
    }
}
