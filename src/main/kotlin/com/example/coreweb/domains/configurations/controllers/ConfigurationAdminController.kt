package com.example.coreweb.domains.configurations.controllers

import arrow.core.flatMap
import com.example.coreweb.domains.configurations.services.ConfigurationService
import com.example.auth.config.security.SecurityContext
import com.example.coreweb.domains.base.controllers.CrudControllerV5
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.domains.configurations.models.dtos.*
import com.example.coreweb.routing.Route
import com.example.coreweb.utils.PageableParams
import com.example.coreweb.utils.ResponseData
import com.example.coreweb.utils.toResponse
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import javax.validation.Valid


@RestController
@Api(tags = ["Configurations"], description = "Description about Configurations")
class ConfigurationAdminController @Autowired constructor(
    private val env: Environment,
    private val configurationService: ConfigurationService,
) : CrudControllerV5<ConfigurationReq, ConfigurationBriefResponse, ConfigurationDetailResponse> {


    @GetMapping(Route.V2.Configurations.AdminApis.SEARCH)
    override fun search(
        @RequestParam("username", required = false) username: String?,
        @RequestParam("from_date", required = false) fromDate: Instant?,
        @RequestParam("to_date", required = false) toDate: Instant?,
        @RequestParam("q", required = false) query: String?,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction,
        @RequestParam(required = false) extra: Map<String, String>,
    ): ResponseEntity<ResponseData<Page<ConfigurationBriefResponse>>> =
        this.configurationService.search(
            username = username,
            fromDate = fromDate ?: Instant.EPOCH,
            toDate = toDate ?: Instant.now(),
            params = PageableParams.of(query, page, size, sortBy, direction)
        ).toResponse { it.toBriefResponse() }

    @GetMapping(Route.V2.Configurations.AdminApis.FIND)
    override fun find(@PathVariable("id") id: Long): ResponseEntity<ResponseData<ConfigurationDetailResponse>> =
        this.configurationService.getAsEither(id, asUser = SecurityContext.getCurrentUser())
            .toResponse(debug = debug()) {
                it.toDetailResponse()
            }

    @PostMapping(Route.V2.Configurations.AdminApis.CREATE)
    override fun create(
        @Valid @RequestBody req: ConfigurationReq
    ): ResponseEntity<ResponseData<ConfigurationDetailResponse>> =
        this.configurationService.save(
            entity = req.asConfiguration(),
            asUser = SecurityContext.getCurrentUser()
        ).toResponse(debug = debug()) {
            it.toDetailResponse()
        }

    @PatchMapping(Route.V2.Configurations.AdminApis.UPDATE)
    override fun update(
        @PathVariable("id") id: Long,
        @Valid @RequestBody req: ConfigurationReq
    ): ResponseEntity<ResponseData<ConfigurationDetailResponse>> =
        this.configurationService.getAsEither(id, asUser = SecurityContext.getCurrentUser())
            .flatMap {
                this.configurationService.save(
                    entity = req.asConfiguration(it),
                    asUser = SecurityContext.getCurrentUser()
                )
            }
            .toResponse(debug = debug()) {
                it.toDetailResponse()
            }

    @DeleteMapping(Route.V2.Configurations.AdminApis.DELETE)
    override fun delete(
        @PathVariable("id") id: Long
    ): ResponseEntity<ResponseData<Boolean>> =
        this.configurationService.delete(
            id = id, softDelete = true, asUser = SecurityContext.getCurrentUser()
        ).toResponse(debug = debug()) { it }

    override fun getEnv(): Environment = this.env

}
