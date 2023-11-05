package com.example.coreweb.domains.labels.controllers.web

import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.controllers.CrudWebControllerV3
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.domains.labels.models.dtos.LabelDto
import com.example.coreweb.domains.labels.models.mappers.LabelMapper
import com.example.coreweb.domains.labels.services.LabelService
import com.example.coreweb.routing.Route
import com.example.coreweb.utils.PageableParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
class LabelWebController @Autowired constructor(
    private val labelService: LabelService,
    private val labelMapper: LabelMapper
) : CrudWebControllerV3<LabelDto> {

    @GetMapping(Route.V1.WEB_SEARCH_LABELS)
    override fun search(
        @RequestParam("q", required = false) query: String?,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction,
        model: Model
    ): String {
        val entities = this.labelService.search(PageableParams.of(query, page, size, sortBy, direction))
        model.addAttribute("labels", entities.map { this.labelMapper.map(it) })
        return "labels/fragments/all"
    }

    @GetMapping(Route.V1.WEB_FIND_LABEL)
    override fun find(
        @PathVariable("id") id: Long,
        model: Model
    ): String {
        val entity = this.labelService.find(id).orElseThrow { ExceptionUtil.notFound("Label", id) }
        model.addAttribute("label", this.labelMapper.map(entity))
        return "labels/fragments/details"
    }

    @GetMapping(Route.V1.WEB_CREATE_LABEL_PAGE)
    override fun createPage(model: Model): String {
        return "labels/fragments/create"
    }

    @PostMapping(Route.V1.WEB_CREATE_LABEL)
    override fun create(
        @Valid @ModelAttribute dto: LabelDto,
        redirectAttributes: RedirectAttributes
    ): String {
        val entity = this.labelService.save(this.labelMapper.map(dto, null))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.WEB_FIND_LABEL.replace("{id}", entity.id.toString())}"
    }

    @GetMapping(Route.V1.WEB_UPDATE_LABEL_PAGE)
    override fun updatePage(@PathVariable("id") id: Long, model: Model): String {
        val entity = this.labelService.find(id).orElseThrow { ExceptionUtil.notFound("Label", id) }
        model.addAttribute("label", this.labelMapper.map(entity))
        return "labels/fragments/create"
    }

    @PostMapping(Route.V1.WEB_UPDATE_LABEL)
    override fun update(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute dto: LabelDto,
        redirectAttributes: RedirectAttributes
    ): String {
        var entity = this.labelService.find(id).orElseThrow { ExceptionUtil.notFound("Label", id) }
        entity = this.labelService.save(this.labelMapper.map(dto, entity))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.WEB_FIND_LABEL.replace("{id}", entity.id.toString())}"
    }

    @PostMapping(Route.V1.WEB_DELETE_LABEL)
    override fun delete(
        @PathVariable("id") id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        this.labelService.delete(id, true)
        redirectAttributes.addFlashAttribute("message", "Deleted!!")
        return "redirect:${Route.V1.WEB_SEARCH_LABELS}";
    }

}
