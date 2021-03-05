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
class LocationPublicController @Autowired constructor(
        private val locationService: LocationService,
        private val locationMapper: LocationMapper
) {


    @GetMapping(Route.V1.SEARCH_CHILD_LOCATIONS_PUBLIC)
    fun searchChildLocations(@RequestParam("parent_id", required = false) parentId: Long?,
                             @RequestParam("q", defaultValue = "") query: String,
                             @RequestParam("page", defaultValue = "0") page: Int,
                             @RequestParam("size", defaultValue = "10") size: Int,
                             @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
                             @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction): ResponseEntity<Page<LocationDto>> {
        val entities = this.locationService.searchByParent(parentId, query, page, size, sortBy, direction)
        return ResponseEntity.ok(entities.map { this.locationMapper.map(it) })
    }


}