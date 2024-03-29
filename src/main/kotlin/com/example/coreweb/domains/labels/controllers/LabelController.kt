package com.example.coreweb.domains.labels.controllers

import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.models.enums.LabelSortByFields
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.domains.labels.models.dtos.LabelDto
import com.example.coreweb.domains.labels.models.mappers.LabelMapper
import com.example.coreweb.domains.labels.services.LabelService
import com.example.coreweb.routing.Route
import com.example.coreweb.utils.PageableParams
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Api(tags = ["Labels"], description = "Description about Labels")
class LabelController @Autowired constructor(
    private val labelService: LabelService,
    private val labelMapper: LabelMapper
) {

    /*
    PUBLIC API'S
     */
    @GetMapping(Route.V1.SEARCH_LABELS_CHILDREN_PUBLIC)
    fun searchChildren(
        @PathVariable("code") parentCode: String,
        @RequestParam("q", required = false) query: String?,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "SERIAL") sortBy: LabelSortByFields,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction
    ): ResponseEntity<Page<LabelDto>> {
        val entities = this.labelService.search(
            null, parentCode, query,
            PageRequest.of(page, size, direction, sortBy.fieldName)
        )
        return ResponseEntity.ok(entities.map { this.labelMapper.map(it) })
    }

    /*
    END PUBLIC API'S
     */

    @GetMapping(Route.V1.FIND_LABEL)
    fun find(@PathVariable("id") id: Long): ResponseEntity<LabelDto> {
        val entity = this.labelService.find(id).orElseThrow { ExceptionUtil.notFound("Label", id) }
        return ResponseEntity.ok(this.labelMapper.map(entity))
    }

    @GetMapping(Route.V1.FIND_LABEL_MULTIPLE)
    fun findMultiple(
        @RequestParam ids: Set<Long>
    ): ResponseEntity<Set<LabelDto>> {
        if (ids.size > 100) throw ExceptionUtil.forbidden("Cannot fetch more than 100 categories at once!")
        val categories = this.labelService.findByIds(ids)
        return ResponseEntity.ok(categories.map { this.labelMapper.map(it) }.toSet())
    }


    @PostMapping(Route.V1.CREATE_LABEL)
    fun create(@Valid @RequestBody dto: LabelDto): ResponseEntity<LabelDto> {
        val entity = this.labelService.save(this.labelMapper.map(dto, null))
        return ResponseEntity.ok(this.labelMapper.map(entity))
    }

    @PatchMapping(Route.V1.UPDATE_LABEL)
    fun update(
        @PathVariable("id") id: Long,
        @Valid @RequestBody dto: LabelDto
    ): ResponseEntity<LabelDto> {
        var entity = this.labelService.find(id).orElseThrow { ExceptionUtil.notFound("Label", id) }
        entity = this.labelService.save(this.labelMapper.map(dto, entity))
        return ResponseEntity.ok(this.labelMapper.map(entity))
    }

    @DeleteMapping(Route.V1.DELETE_LABEL)
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        this.labelService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}
