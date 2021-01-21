package com.example.coreweb.domains.locations.controllers.web

import com.example.coreweb.domains.locations.models.dtos.LocationDto
import com.example.coreweb.domains.locations.models.mappers.LocationMapper
import com.example.coreweb.domains.locations.services.LocationService
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
class LocationWebController @Autowired constructor(
        private val locationService: LocationService,
        private val locationMapper: LocationMapper
) : CrudWebControllerV2<LocationDto> {

    @GetMapping(Route.V1.ADMIN_SEARCH_LOCATIONS)
    override fun search(@RequestParam("q", defaultValue = "") query: String,
                        @RequestParam("page", defaultValue = "0") page: Int,
                        @RequestParam("size", defaultValue = "10") size: Int,
                        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
                        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction,
                        model: Model): String {
        val entities = this.locationService.search(query, page, size, sortBy, direction)
        model.addAttribute("locations", entities)
        return "locations/fragments/all"
    }

    @GetMapping(Route.V1.ADMIN_FIND_LOCATION)
    override fun find(@PathVariable("id") id: Long,
                      model: Model): String {
        val entity = this.locationService.find(id).orElseThrow { ExceptionUtil.notFound("Location", id) }
        model.addAttribute("location", entity)
        return "locations/fragments/details"
    }

    @GetMapping(Route.V1.ADMIN_CREATE_LOCATION_PAGE)
    override fun createPage(model: Model): String {
        return "locations/fragments/create"
    }

    @PostMapping(Route.V1.ADMIN_CREATE_LOCATION)
    override fun create(@Valid @ModelAttribute dto: LocationDto,
                        redirectAttributes: RedirectAttributes): String {
        val entity = this.locationService.save(this.locationMapper.map(dto, null))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.ADMIN_FIND_LOCATION.replace("{id}", entity.id.toString())}"
    }

    @GetMapping(Route.V1.ADMIN_UPDATE_LOCATION_PAGE)
    override fun updatePage(@PathVariable("id") id: Long, model: Model): String {
        val entity = this.locationService.find(id).orElseThrow { ExceptionUtil.notFound("Location", id) }
        model.addAttribute("location", entity)
        return "locations/fragments/create"
    }

    @PostMapping(Route.V1.ADMIN_UPDATE_LOCATION)
    override fun update(@PathVariable("id") id: Long,
                        @Valid @ModelAttribute dto: LocationDto,
                        redirectAttributes: RedirectAttributes): String {
        var entity = this.locationService.find(id).orElseThrow { ExceptionUtil.notFound("Location", id) }
        entity = this.locationService.save(this.locationMapper.map(dto, entity))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.ADMIN_FIND_LOCATION.replace("{id}", entity.id.toString())}"
    }

    @PostMapping(Route.V1.ADMIN_DELETE_LOCATION)
    override fun delete(@PathVariable("id") id: Long,
                        redirectAttributes: RedirectAttributes): String {
        this.locationService.delete(id, true)
        redirectAttributes.addFlashAttribute("message", "Deleted!!")
        return "redirect:${Route.V1.ADMIN_SEARCH_LOCATIONS}";
    }

}