package com.example.coreweb.domains.locationtypes.controllers.web

import com.example.coreweb.domains.locationtypes.models.dtos.LocationTypeDto
import com.example.coreweb.domains.locationtypes.models.mappers.LocationTypeMapper
import com.example.coreweb.domains.locationtypes.services.LocationTypeService
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.controllers.CrudWebControllerV2
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.routing.Route
import org.springframework.data.domain.Sort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
class LocationTypeWebController @Autowired constructor(
        private val locationTypeService: LocationTypeService,
        private val locationTypeMapper: LocationTypeMapper
) : CrudWebControllerV2<LocationTypeDto> {

    @GetMapping(Route.V1.ADMIN_SEARCH_LOCATIONTYPES)
    override fun search(@RequestParam("q", defaultValue = "") query: String,
                        @RequestParam("page", defaultValue = "0") page: Int,
                        @RequestParam("size", defaultValue = "10") size: Int,
                        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
                        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction,
                        model: Model): String {
        val entities = this.locationTypeService.search(query, page, size, sortBy, direction)
        model.addAttribute("locationtypes", entities)
        return "locationtypes/fragments/all"
    }

    @GetMapping(Route.V1.ADMIN_FIND_LOCATIONTYPE)
    override fun find(@PathVariable("id") id: Long,
                      model: Model): String {
        val entity = this.locationTypeService.find(id).orElseThrow { ExceptionUtil.notFound("LocationType", id) }
        model.addAttribute("locationtype", entity)
        return "locationtypes/fragments/details"
    }

    @GetMapping(Route.V1.ADMIN_CREATE_LOCATIONTYPE_PAGE)
    override fun createPage(model: Model): String {
        return "locationtypes/fragments/create"
    }

    @PostMapping(Route.V1.ADMIN_CREATE_LOCATIONTYPE)
    override fun create(@Valid @ModelAttribute dto: LocationTypeDto,
                        redirectAttributes: RedirectAttributes): String {
        val entity = this.locationTypeService.save(this.locationTypeMapper.map(dto, null))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.ADMIN_FIND_LOCATIONTYPE.replace("{id}", entity.id.toString())}"
    }

    @GetMapping(Route.V1.ADMIN_UPDATE_LOCATIONTYPE_PAGE)
    override fun updatePage(@PathVariable("id") id: Long, model: Model): String {
        val entity = this.locationTypeService.find(id).orElseThrow { ExceptionUtil.notFound("LocationType", id) }
        model.addAttribute("locationtype", entity)
        return "locationtypes/fragments/create"
    }

    @PostMapping(Route.V1.ADMIN_UPDATE_LOCATIONTYPE)
    override fun update(@PathVariable("id") id: Long,
                        @Valid @ModelAttribute dto: LocationTypeDto,
                        redirectAttributes: RedirectAttributes): String {
        var entity = this.locationTypeService.find(id).orElseThrow { ExceptionUtil.notFound("LocationType", id) }
        entity = this.locationTypeService.save(this.locationTypeMapper.map(dto, entity))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.ADMIN_FIND_LOCATIONTYPE.replace("{id}", entity.id.toString())}"
    }

    @PostMapping(Route.V1.ADMIN_DELETE_LOCATIONTYPE)
    override fun delete(@PathVariable("id") id: Long,
                        redirectAttributes: RedirectAttributes): String {
        this.locationTypeService.delete(id, true)
        redirectAttributes.addFlashAttribute("message", "Deleted!!")
        return "redirect:${Route.V1.ADMIN_SEARCH_LOCATIONTYPES}";
    }

}