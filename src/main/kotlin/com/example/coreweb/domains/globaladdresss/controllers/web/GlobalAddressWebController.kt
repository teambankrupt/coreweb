package com.example.coreweb.domains.globaladdresss.controllers.web

import com.example.coreweb.domains.globaladdresss.models.dtos.GlobalAddressDto
import com.example.coreweb.domains.globaladdresss.models.mappers.GlobalAddressMapper
import com.example.coreweb.domains.globaladdresss.services.GlobalAddressService
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
class GlobalAddressWebController @Autowired constructor(
        private val globalAddressService: GlobalAddressService,
        private val globalAddressMapper: GlobalAddressMapper
) : CrudWebControllerV2<GlobalAddressDto> {

    @GetMapping(Route.V1.ADMIN_SEARCH_GLOBALADDRESSS)
    override fun search(@RequestParam("q", defaultValue = "") query: String,
                        @RequestParam("page", defaultValue = "0") page: Int,
                        @RequestParam("size", defaultValue = "10") size: Int,
                        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
                        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction,
                        model: Model): String {
        val entities = this.globalAddressService.search(query, page, size, sortBy, direction)
        model.addAttribute("globaladdresss", entities)
        return "globaladdresss/fragments/all"
    }

    @GetMapping(Route.V1.ADMIN_FIND_GLOBALADDRESS)
    override fun find(@PathVariable("id") id: Long,
                      model: Model): String {
        val entity = this.globalAddressService.find(id).orElseThrow { ExceptionUtil.notFound("GlobalAddress", id) }
        model.addAttribute("globaladdress", entity)
        return "globaladdresss/fragments/details"
    }

    @GetMapping(Route.V1.ADMIN_CREATE_GLOBALADDRESS_PAGE)
    override fun createPage(model: Model): String {
        return "globaladdresss/fragments/create"
    }

    @PostMapping(Route.V1.ADMIN_CREATE_GLOBALADDRESS)
    override fun create(@Valid @ModelAttribute dto: GlobalAddressDto,
                        redirectAttributes: RedirectAttributes): String {
        val entity = this.globalAddressService.save(this.globalAddressMapper.map(dto, null))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.ADMIN_FIND_GLOBALADDRESS.replace("{id}", entity.id.toString())}"
    }

    @GetMapping(Route.V1.ADMIN_UPDATE_GLOBALADDRESS_PAGE)
    override fun updatePage(@PathVariable("id") id: Long, model: Model): String {
        val entity = this.globalAddressService.find(id).orElseThrow { ExceptionUtil.notFound("GlobalAddress", id) }
        model.addAttribute("globaladdress", entity)
        return "globaladdresss/fragments/create"
    }

    @PostMapping(Route.V1.ADMIN_UPDATE_GLOBALADDRESS)
    override fun update(@PathVariable("id") id: Long,
                        @Valid @ModelAttribute dto: GlobalAddressDto,
                        redirectAttributes: RedirectAttributes): String {
        var entity = this.globalAddressService.find(id).orElseThrow { ExceptionUtil.notFound("GlobalAddress", id) }
        entity = this.globalAddressService.save(this.globalAddressMapper.map(dto, entity))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.ADMIN_FIND_GLOBALADDRESS.replace("{id}", entity.id.toString())}"
    }

    @PostMapping(Route.V1.ADMIN_DELETE_GLOBALADDRESS)
    override fun delete(@PathVariable("id") id: Long,
                        redirectAttributes: RedirectAttributes): String {
        this.globalAddressService.delete(id, true)
        redirectAttributes.addFlashAttribute("message", "Deleted!!")
        return "redirect:${Route.V1.ADMIN_SEARCH_GLOBALADDRESSS}";
    }

}