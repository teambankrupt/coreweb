package com.example.coreweb.domains.labels.controllers

import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.domains.labels.models.dtos.LabelDto
import com.example.coreweb.domains.labels.models.mappers.LabelMapper
import com.example.coreweb.domains.labels.services.LabelService
import com.example.coreweb.routing.Route
import com.example.coreweb.utils.PageableParams
import io.swagger.annotations.Api
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Labels"], description = "Description about Labels")
class LabelAdminController(
    private val labelService: LabelService,
    private val labelMapper: LabelMapper
) {

    @GetMapping(Route.V1.ADMIN_SEARCH_LABELS)
    fun searchForAdmin(
        @RequestParam("parent_id", required = false) parentId: Long?,
        @RequestParam("parent_code", required = false) parentCode: String?,
        @RequestParam("q", required = false) query: String?,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction
    ): ResponseEntity<Page<LabelDto>> {
        val entities = this.labelService.search(
            parentId, parentCode,
            PageableParams.of(query, page, size, sortBy, direction)
        )
        return ResponseEntity.ok(entities.map { this.labelMapper.map(it) })
    }

    @PatchMapping(Route.V1.ADMIN_LABEL_FIX_PATHS)
    fun fixPaths(@RequestParam("parent_id", required = false) parentId: Long?) {
        this.labelService.fixPaths(parentId)
    }
}