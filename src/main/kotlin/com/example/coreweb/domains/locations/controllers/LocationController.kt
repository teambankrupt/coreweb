package com.example.coreweb.domains.locations.controllers

import com.example.coreweb.domains.locations.models.dtos.LocationDto
import com.example.coreweb.domains.locations.models.mappers.LocationMapper
import com.example.coreweb.domains.locations.services.LocationService
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.controllers.CrudControllerV2
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.routing.Route
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import org.springframework.data.domain.Sort

@RestController
@Api(tags = ["Locations"], description = "Description about Locations")
class LocationController @Autowired constructor(
        private val locationService: LocationService,
        private val locationMapper: LocationMapper
) : CrudControllerV2<LocationDto> {

    //    @GetMapping(Route.V1.SEARCH_LOCATIONS)
    override fun search(@RequestParam("q", defaultValue = "") query: String,
                        @RequestParam("page", defaultValue = "0") page: Int,
                        @RequestParam("size", defaultValue = "10") size: Int,
                        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
                        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction): ResponseEntity<Page<LocationDto>> {
        val entities = this.locationService.search(query, page, size, sortBy, direction)
        return ResponseEntity.ok(entities.map { this.locationMapper.map(it) })
    }

    @GetMapping(Route.V1.SEARCH_LOCATIONS)
    fun searchForType(@PathVariable("type_id") typeId: Long,
                      @RequestParam("q", defaultValue = "") query: String,
                      @RequestParam("page", defaultValue = "0") page: Int,
                      @RequestParam("size", defaultValue = "10") size: Int,
                      @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
                      @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction): ResponseEntity<Page<LocationDto>> {
        val entities = this.locationService.searchForType(typeId, query, page, size, sortBy, direction)
        return ResponseEntity.ok(entities.map { this.locationMapper.map(it) })
    }

    @GetMapping(Route.V1.SEARCH_CHILD_LOCATIONS)
    fun searchChildLocations(@RequestParam("parent_id", required = false) parentId: Long?,
                             @RequestParam("q", defaultValue = "") query: String,
                             @RequestParam("page", defaultValue = "0") page: Int,
                             @RequestParam("size", defaultValue = "10") size: Int,
                             @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
                             @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction): ResponseEntity<Page<LocationDto>> {
        val entities = this.locationService.searchByParent(parentId, query, page, size, sortBy, direction)
        return ResponseEntity.ok(entities.map { this.locationMapper.map(it) })
    }

    @GetMapping(Route.V1.FIND_LOCATION)
    override fun find(@PathVariable("id") id: Long): ResponseEntity<LocationDto> {
        val entity = this.locationService.find(id).orElseThrow { ExceptionUtil.notFound("Location", id) }
        return ResponseEntity.ok(this.locationMapper.map(entity))
    }

    @PostMapping(Route.V1.CREATE_LOCATION)
    override fun create(@Valid @RequestBody dto: LocationDto): ResponseEntity<LocationDto> {
        val entity = this.locationService.save(this.locationMapper.map(dto, null))
        return ResponseEntity.ok(this.locationMapper.map(entity))
    }

    @PatchMapping(Route.V1.UPDATE_LOCATION)
    override fun update(@PathVariable("id") id: Long,
                        @Valid @RequestBody dto: LocationDto): ResponseEntity<LocationDto> {
        var entity = this.locationService.find(id).orElseThrow { ExceptionUtil.notFound("Location", id) }
        entity = this.locationService.save(this.locationMapper.map(dto, entity))
        return ResponseEntity.ok(this.locationMapper.map(entity))
    }

    @DeleteMapping(Route.V1.DELETE_LOCATION)
    override fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        this.locationService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}