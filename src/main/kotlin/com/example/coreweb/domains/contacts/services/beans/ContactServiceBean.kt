package com.example.coreweb.domains.contacts.services.beans

import com.example.auth.config.security.SecurityContext
import com.example.auth.entities.User
import com.example.auth.repositories.UserRepo
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
    private val contactRepository: ContactRepository,
    private val userRepository: UserRepo
) : ContactService {
    override fun search(
        userId: Long,
        query: String,
        page: Int,
        size: Int,
        sortBy: SortByFields,
        direction: Sort.Direction
    ): Page<Contact> {
        val contacts = this.contactRepository.search(
            userId,
            query,
            PageAttr.getPageRequest(page, size, sortBy.fieldName, direction)
        )
        if (contacts.isEmpty) {
            this.createDefaultContact(userId)
            return this.search(userId, query, page, size, sortBy, direction)
        }
        return contacts
    }

    private fun createDefaultContact(userId: Long): Contact {
        val user = userRepository.findById(userId).orElseThrow { ExceptionUtil.notFound(User::class.java, userId) }
        val contact = Contact()
        contact.name = user.name
        contact.phone = user.phone
        contact.email = user.email
        contact.user = user
        return this.save(contact)
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

    override fun findSelfContact(): Optional<Contact> {
        return this.contactRepository.findSelfContact(SecurityContext.getCurrentUser().id)
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

        // check if user is trying to change contact for another user
        val auth = SecurityContext.getCurrentUser()
        if (!auth.isAdmin && entity.user.id != auth.id)
            throw ExceptionUtil.forbidden("Can't change contact for another user!!")
    }
}
