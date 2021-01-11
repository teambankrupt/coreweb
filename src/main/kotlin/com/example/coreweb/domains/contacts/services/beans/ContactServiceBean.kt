package com.example.app.domains.contact.services.beans

import com.example.coreweb.domains.contacts.models.entities.Contact
import com.example.app.domains.contact.repositories.ContactRepository
import com.example.app.domains.contact.services.ContactService
import com.example.common.utils.ExceptionUtil
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

    override fun search(query: String, page: Int, size: Int, sortBy: SortByFields, direction: Sort.Direction): Page<Contact> {
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
            val entity = this.find(id).orElseThrow { ExceptionUtil.notFound("Contact", id) }
            entity.isDeleted = true
            this.contactRepository.save(entity)
        }
        this.contactRepository.deleteById(id)
    }

    override fun validate(entity: Contact) {
        TODO("Not yet implemented")
    }
}
