package com.example.app.domains.contact.controllers.web

import com.example.app.domains.contact.models.dtos.ContactDto
import com.example.app.domains.contact.models.mappers.ContactMapper
import com.example.app.domains.contact.services.ContactService
import com.example.app.routing.Route
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.controllers.CrudWebControllerV2
import com.example.coreweb.domains.base.models.enums.SortByFields
import org.springframework.data.domain.Sort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
class ContactWebController @Autowired constructor(
        private val contactService: ContactService,
        private val contactMapper: ContactMapper
) : CrudWebControllerV2<ContactDto> {

    /*
        COPY THESE URLS TO ROUTE FILE AND ADJUST
        ------------------------------------------------------
        // Contacts (Admin)
        const val ADMIN_SEARCH_CONTACTS = "$ADMIN/contacts"
        const val ADMIN_CREATE_CONTACT_PAGE = "$ADMIN/contacts/create"
        const val ADMIN_CREATE_CONTACT = "$ADMIN/contacts"
        const val ADMIN_FIND_CONTACT = "$ADMIN/contacts/{id}"
        const val ADMIN_UPDATE_CONTACT_PAGE = "$ADMIN/contacts/{id}/update"
        const val ADMIN_UPDATE_CONTACT = "$ADMIN/contacts/{id}"
        const val ADMIN_DELETE_CONTACT = "$ADMIN/contacts/{id}/delete"
        ------------------------------------------------------
    */

    @GetMapping(Route.V1.ADMIN_SEARCH_CONTACTS)
    override fun search(@RequestParam("q", defaultValue = "") query: String,
                        @RequestParam("page", defaultValue = "0") page: Int,
                        @RequestParam("size", defaultValue = "10") size: Int,
                        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
                        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction,
                        model: Model): String {
        val entities = this.contactService.search(query, page, size, sortBy, direction)
        model.addAttribute("contacts", entities)
        return "contacts/fragments/all"
    }

    @GetMapping(Route.V1.ADMIN_FIND_CONTACT)
    override fun find(@PathVariable("id") id: Long,
                      model: Model): String {
        val entity = this.contactService.find(id).orElseThrow { ExceptionUtil.notFound("Contact", id) }
        model.addAttribute("contact", entity)
        return "contacts/fragments/details"
    }

    @GetMapping(Route.V1.ADMIN_CREATE_CONTACT_PAGE)
    override fun createPage(model: Model): String {
        return "contacts/fragments/create"
    }

    @PostMapping(Route.V1.ADMIN_CREATE_CONTACT)
    override fun create(@Valid @ModelAttribute dto: ContactDto,
                        redirectAttributes: RedirectAttributes): String {
        val entity = this.contactService.save(this.contactMapper.map(dto, null))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.ADMIN_FIND_CONTACT.replace("{id}", entity.id.toString())}"
    }

    @GetMapping(Route.V1.ADMIN_UPDATE_CONTACT_PAGE)
    override fun updatePage(@PathVariable("id") id: Long, model: Model): String {
        val entity = this.contactService.find(id).orElseThrow { ExceptionUtil.notFound("Contact", id) }
        model.addAttribute("contact", entity)
        return "contacts/fragments/create"
    }

    @PostMapping(Route.V1.ADMIN_UPDATE_CONTACT)
    override fun update(@PathVariable("id") id: Long,
                        @Valid @ModelAttribute dto: ContactDto,
                        redirectAttributes: RedirectAttributes): String {
        var entity = this.contactService.find(id).orElseThrow { ExceptionUtil.notFound("Contact", id) }
        entity = this.contactService.save(this.contactMapper.map(dto, entity))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.ADMIN_FIND_CONTACT.replace("{id}", entity.id.toString())}"
    }

    @PostMapping(Route.V1.ADMIN_DELETE_CONTACT)
    override fun delete(@PathVariable("id") id: Long,
                        redirectAttributes: RedirectAttributes): String {
        this.contactService.delete(id, true)
        redirectAttributes.addFlashAttribute("message", "Deleted!!")
        return "redirect:${Route.V1.ADMIN_SEARCH_CONTACTS}";
    }

}