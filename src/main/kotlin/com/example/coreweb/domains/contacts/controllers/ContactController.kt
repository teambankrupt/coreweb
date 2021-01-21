package com.example.coreweb.domains.contacts.controllers

import com.example.coreweb.domains.contacts.models.dtos.ContactDto
import com.example.coreweb.domains.contacts.models.mappers.ContactMapper
import com.example.coreweb.domains.contacts.services.ContactService
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.commons.Constants
import com.example.coreweb.domains.base.controllers.CrudControllerV2
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.routing.Route
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import org.springframework.data.domain.Sort

@RestController
@Api(tags = [Constants.Swagger.CONTACT], description = Constants.Swagger.REST_API)
class ContactController @Autowired constructor(
        private val contactService: ContactService,
        private val contactMapper: ContactMapper
) : CrudControllerV2<ContactDto> {

    @GetMapping(Route.V1.SEARCH_CONTACTS)
    @ApiOperation(value = Constants.Swagger.SEARCH_ALL_MSG + Constants.Swagger.CONTACT)
    override fun search(@RequestParam("q", defaultValue = "") query: String,
                        @RequestParam("page", defaultValue = "0") page: Int,
                        @RequestParam("size", defaultValue = "10") size: Int,
                        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
                        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction): ResponseEntity<Page<ContactDto>> {
        val entities = this.contactService.search(query, page, size, sortBy, direction)
        return ResponseEntity.ok(entities.map { this.contactMapper.map(it) })
    }

    @GetMapping(Route.V1.FIND_CONTACT)
    @ApiOperation(value = Constants.Swagger.GET_MSG + Constants.Swagger.BY_ID_MSG + Constants.Swagger.CONTACT)
    override fun find(@PathVariable("id") id: Long): ResponseEntity<ContactDto> {
        val entity = this.contactService.find(id).orElseThrow { ExceptionUtil.notFound(Constants.Swagger.CONTACT, id) }
        return ResponseEntity.ok(this.contactMapper.map(entity))
    }

    @PostMapping(Route.V1.CREATE_CONTACT)
    @ApiOperation(value = Constants.Swagger.POST_MSG + Constants.Swagger.CONTACT)
    override fun create(@Valid @RequestBody dto: ContactDto): ResponseEntity<ContactDto> {
        val entity = this.contactService.save(this.contactMapper.map(dto, null))
        return ResponseEntity.ok(this.contactMapper.map(entity))
    }

    @PatchMapping(Route.V1.UPDATE_CONTACT)
    @ApiOperation(value = Constants.Swagger.PATCH_MSG + Constants.Swagger.CONTACT)
    override fun update(@PathVariable("id") id: Long,
                        @Valid @RequestBody dto: ContactDto): ResponseEntity<ContactDto> {
        var entity = this.contactService.find(id).orElseThrow { ExceptionUtil.notFound(Constants.Swagger.CONTACT, id) }
        entity = this.contactService.save(this.contactMapper.map(dto, entity))
        return ResponseEntity.ok(this.contactMapper.map(entity))
    }

    @DeleteMapping(Route.V1.DELETE_CONTACT)
    @ApiOperation(value = Constants.Swagger.DELETE_MSG + Constants.Swagger.CONTACT)
    override fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        this.contactService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}
