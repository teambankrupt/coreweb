package com.example.coreweb.domains.globaladdresss.controllers

import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.controllers.CrudControllerV2
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.domains.globaladdresss.models.dtos.GlobalAddressDto
import com.example.coreweb.domains.globaladdresss.models.dtos.GlobalAddressResponse
import com.example.coreweb.domains.globaladdresss.models.dtos.toResponse
import com.example.coreweb.domains.globaladdresss.models.mappers.GlobalAddressMapper
import com.example.coreweb.domains.globaladdresss.services.GlobalAddressService
import com.example.coreweb.routing.Route
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Api(tags = ["Global Addresss"], description = "Description about Global Addresss")
class GlobalAddressController @Autowired constructor(
    private val globalAddressService: GlobalAddressService,
    private val globalAddressMapper: GlobalAddressMapper
) : CrudControllerV2<GlobalAddressDto> {

    @GetMapping(Route.V1.SEARCH_GLOBALADDRESSS)
    override fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction
    ): ResponseEntity<Page<GlobalAddressDto>> {
        val entities = this.globalAddressService.search(query, page, size, sortBy, direction)
        return ResponseEntity.ok(entities.map { this.globalAddressMapper.map(it) })
    }

    @GetMapping(Route.V1.FIND_GLOBALADDRESS)
    override fun find(@PathVariable("id") id: Long): ResponseEntity<GlobalAddressDto> {
        val entity = this.globalAddressService.find(id).orElseThrow { ExceptionUtil.notFound("GlobalAddress", id) }
        return ResponseEntity.ok(this.globalAddressMapper.map(entity))
    }

    /*
    *   This iteration adds full location response object in location tree
    *   Sorted by level
     */
    @GetMapping(Route.V2.GlobalAddress.GET)
    fun findV2(@PathVariable("id") id: Long): ResponseEntity<GlobalAddressResponse> {
        val entity = this.globalAddressService.find(id)
            .orElseThrow { ExceptionUtil.notFound("GlobalAddress", id) }
        return ResponseEntity.ok(entity.toResponse())
    }

    @PostMapping(Route.V1.CREATE_GLOBALADDRESS)
    override fun create(@Valid @RequestBody dto: GlobalAddressDto): ResponseEntity<GlobalAddressDto> {
        val entity = this.globalAddressService.save(this.globalAddressMapper.map(dto, null))
        return ResponseEntity.ok(this.globalAddressMapper.map(entity))
    }

    @PatchMapping(Route.V1.UPDATE_GLOBALADDRESS)
    override fun update(
        @PathVariable("id") id: Long,
        @Valid @RequestBody dto: GlobalAddressDto
    ): ResponseEntity<GlobalAddressDto> {
        var entity = this.globalAddressService.find(id).orElseThrow { ExceptionUtil.notFound("GlobalAddress", id) }
        entity = this.globalAddressService.save(this.globalAddressMapper.map(dto, entity))
        return ResponseEntity.ok(this.globalAddressMapper.map(entity))
    }

    @DeleteMapping(Route.V1.DELETE_GLOBALADDRESS)
    override fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        this.globalAddressService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}