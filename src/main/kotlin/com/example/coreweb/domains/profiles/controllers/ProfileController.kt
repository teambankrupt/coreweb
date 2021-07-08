package com.example.app.domains.profile.controllers

import com.example.coreweb.domains.profiles.models.dtos.ProfileDto
import com.example.coreweb.domains.profiles.models.mappers.ProfileMapper
import com.example.coreweb.domains.profiles.services.ProfileService
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
@Api(tags = ["Profiles"], description = "Description about Profiles")
class ProfileController @Autowired constructor(
    private val profileService: ProfileService,
    private val profileMapper: ProfileMapper
) : CrudControllerV2<ProfileDto> {

    /*
        COPY THESE URLS TO ROUTE FILE AND ADJUST
        ------------------------------------------------------
        // Profiles
        const val SEARCH_PROFILES = "$API$VERSION/profiles"
        const val CREATE_PROFILE = "$API$VERSION/profiles"
        const val FIND_PROFILE = "$API$VERSION/profiles/{id}"
        const val UPDATE_PROFILE = "$API$VERSION/profiles/{id}"
        const val DELETE_PROFILE = "$API$VERSION/profiles/{id}"
        ------------------------------------------------------
    */

    @GetMapping(Route.V1.SEARCH_PROFILES)
    override fun search(@RequestParam("q", defaultValue = "") query: String,
                        @RequestParam("page", defaultValue = "0") page: Int,
                        @RequestParam("size", defaultValue = "10") size: Int,
                        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
                        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction): ResponseEntity<Page<ProfileDto>> {
        val entities = this.profileService.search(query, page, size, sortBy, direction)
        return ResponseEntity.ok(entities.map { this.profileMapper.map(it) })
    }

    @GetMapping(Route.V1.FIND_PROFILE)
    override fun find(@PathVariable("id") id: Long): ResponseEntity<ProfileDto> {
        val entity = this.profileService.find(id).orElseThrow { ExceptionUtil.notFound("Profile", id) }
        return ResponseEntity.ok(this.profileMapper.map(entity))
    }

    @PostMapping(Route.V1.CREATE_PROFILE)
    override fun create(@Valid @RequestBody dto: ProfileDto): ResponseEntity<ProfileDto> {
        val entity = this.profileService.save(this.profileMapper.map(dto, null))
        return ResponseEntity.ok(this.profileMapper.map(entity))
    }

    @PatchMapping(Route.V1.UPDATE_PROFILE)
    override fun update(@PathVariable("id") id: Long,
                        @Valid @RequestBody dto: ProfileDto
    ): ResponseEntity<ProfileDto> {
        var entity = this.profileService.find(id).orElseThrow { ExceptionUtil.notFound("Profile", id) }
        entity = this.profileService.save(this.profileMapper.map(dto, entity))
        return ResponseEntity.ok(this.profileMapper.map(entity))
    }

    @DeleteMapping(Route.V1.DELETE_PROFILE)
    override fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        this.profileService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}
