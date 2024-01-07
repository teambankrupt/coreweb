package com.example.coreweb.domains.locationtypes.controllers

import com.example.coreweb.domains.locationtypes.models.dtos.LocationTypeDto
import com.example.coreweb.domains.locationtypes.models.mappers.LocationTypeMapper
import com.example.coreweb.domains.locationtypes.services.LocationTypeService
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.controllers.CrudControllerV2
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.domains.locationtypes.models.dtos.LocationTypeDetailResponse
import com.example.coreweb.domains.locationtypes.models.dtos.toDetailResponse
import com.example.coreweb.routing.Route
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import org.springframework.data.domain.Sort

@RestController
@Api(tags = ["LocationTypes"], description = "Description about LocationTypes")
class LocationTypeController @Autowired constructor(
    private val locationTypeService: LocationTypeService,
    private val locationTypeMapper: LocationTypeMapper
) {

    @GetMapping(Route.V1.SEARCH_LOCATIONTYPES)
    fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction
    ): ResponseEntity<Page<LocationTypeDto>> {
        val entities = this.locationTypeService.search(query, page, size, sortBy, direction)
        return ResponseEntity.ok(entities.map { this.locationTypeMapper.map(it) })
    }

    @GetMapping(Route.V1.FIND_LOCATIONTYPE)
    fun find(@PathVariable("id") id: Long): ResponseEntity<LocationTypeDetailResponse> {
        val entity = this.locationTypeService.find(id)
            .orElseThrow { ExceptionUtil.notFound("LocationType", id) }
        return ResponseEntity.ok(entity.toDetailResponse())
    }

    @PostMapping(Route.V1.CREATE_LOCATIONTYPE)
    fun create(@Valid @RequestBody dto: LocationTypeDto): ResponseEntity<LocationTypeDto> {
        val entity = this.locationTypeService.save(this.locationTypeMapper.map(dto, null))
        return ResponseEntity.ok(this.locationTypeMapper.map(entity))
    }

    @PatchMapping(Route.V1.UPDATE_LOCATIONTYPE)
    fun update(
        @PathVariable("id") id: Long,
        @Valid @RequestBody dto: LocationTypeDto
    ): ResponseEntity<LocationTypeDto> {
        var entity = this.locationTypeService.find(id).orElseThrow { ExceptionUtil.notFound("LocationType", id) }
        entity = this.locationTypeService.save(this.locationTypeMapper.map(dto, entity))
        return ResponseEntity.ok(this.locationTypeMapper.map(entity))
    }

    @DeleteMapping(Route.V1.DELETE_LOCATIONTYPE)
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        this.locationTypeService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}