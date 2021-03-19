package com.example.coreweb.domains.contacts.services.beans

import com.example.coreweb.domains.contacts.models.entities.Contact
import com.example.coreweb.domains.contacts.repositories.ContactRepository
import com.example.coreweb.domains.contacts.services.ContactService
import com.example.common.utils.ExceptionUtil
import com.example.common.utils.Validator
import com.example.coreweb.commons.Constants
import com.example.coreweb.commons.ResourceUtil
import com.example.coreweb.utils.PageAttr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*
import com.example.coreweb.domains.base.models.enums.SortByFields
import org.springframework.data.domain.Sort

@Service
class ContactServiceBean @Autowired constructor(
    private val contactRepository: ContactRepository
) : ContactService {
    override fun search(
        userId: Long,
        query: String,
        page: Int,
        size: Int,
        sortBy: SortByFields,
        direction: Sort.Direction
    ): Page<Contact> {
        return this.contactRepository.search(
            userId,
            query,
            PageAttr.getPageRequest(page, size, sortBy.fieldName, direction)
        )
    }

    override fun search(
        query: String,
        page: Int,
        size: Int,
        sortBy: SortByFields,
        direction: Sort.Direction
    ): Page<Contact> {
        return this.contactRepository.search(query, PageAttr.getPageRequest(page, size, sortBy.fieldName, direction))
    }

    override fun save(entity: Contact): Contact {
        this.validate(entity)
        return this.contactRepository.save(entity)
    }

    override fun find(id: Long): Optional<Contact> {
        return this.contactRepository.find(id)
    }

    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val entity = this.find(id).orElseThrow { ExceptionUtil.notFound(Constants.Swagger.CONTACT, id) }
            entity.isDeleted = true
            this.contactRepository.save(entity)
        }
        this.contactRepository.deleteById(id)
    }

    override fun validate(entity: Contact) {
        entity.email?.let { Validator.isValidEmail(it) }
    }
}
