package com.example.app.domains.contact.controllers

import com.example.app.domains.contact.models.dtos.ContactDto
import com.example.app.domains.contact.models.mappers.ContactMapper
import com.example.app.domains.contact.services.ContactService
import com.example.app.routing.Route
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.controllers.CrudControllerV2
import com.example.coreweb.domains.base.models.enums.SortByFields
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import org.springframework.data.domain.Sort

@RestController
@Api(tags = ["Contacts"], description = "Description about Contacts")
class ContactController @Autowired constructor(
        private val contactService: ContactService,
        private val contactMapper: ContactMapper
) : CrudControllerV2<ContactDto> {

    /*
        COPY THESE URLS TO ROUTE FILE AND ADJUST
        ------------------------------------------------------
        // Contacts
        const val SEARCH_CONTACTS = "$API$VERSION/contacts"
        const val CREATE_CONTACT = "$API$VERSION/contacts"
        const val FIND_CONTACT = "$API$VERSION/contacts/{id}"
        const val UPDATE_CONTACT = "$API$VERSION/contacts/{id}"
        const val DELETE_CONTACT = "$API$VERSION/contacts/{id}"
        ------------------------------------------------------
    */

    @GetMapping(Route.V1.SEARCH_CONTACTS)
    override fun search(@RequestParam("q", defaultValue = "") query: String,
                        @RequestParam("page", defaultValue = "0") page: Int,
                        @RequestParam("size", defaultValue = "10") size: Int,
                        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
                        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction): ResponseEntity<Page<ContactDto>> {
        val entities = this.contactService.search(query, page, size, sortBy, direction)
        return ResponseEntity.ok(entities.map { this.contactMapper.map(it) })
    }

    @GetMapping(Route.V1.FIND_CONTACT)
    override fun find(@PathVariable("id") id: Long): ResponseEntity<ContactDto> {
        val entity = this.contactService.find(id).orElseThrow { ExceptionUtil.notFound("Contact", id) }
        return ResponseEntity.ok(this.contactMapper.map(entity))
    }

    @PostMapping(Route.V1.CREATE_CONTACT)
    override fun create(@Valid @RequestBody dto: ContactDto): ResponseEntity<ContactDto> {
        val entity = this.contactService.save(this.contactMapper.map(dto, null))
        return ResponseEntity.ok(this.contactMapper.map(entity))
    }

    @PatchMapping(Route.V1.UPDATE_CONTACT)
    override fun update(@PathVariable("id") id: Long,
                        @Valid @RequestBody dto: ContactDto): ResponseEntity<ContactDto> {
        var entity = this.contactService.find(id).orElseThrow { ExceptionUtil.notFound("Contact", id) }
        entity = this.contactService.save(this.contactMapper.map(dto, entity))
        return ResponseEntity.ok(this.contactMapper.map(entity))
    }

    @DeleteMapping(Route.V1.DELETE_CONTACT)
    override fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        this.contactService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}