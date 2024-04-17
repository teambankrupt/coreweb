package com.example.coreweb.domains.events.controllers

import arrow.core.flatMap
import com.example.coreweb.domains.events.models.dtos.EventBriefResponse
import com.example.coreweb.domains.events.models.dtos.EventDetailResponse
import com.example.coreweb.domains.events.models.dtos.toBriefResponse
import com.example.coreweb.domains.events.models.dtos.toDetailResponse
import com.example.auth.repositories.UserRepo
import com.example.common.exceptions.toArrow
import com.example.coreweb.domains.base.controllers.CrudControllerV5
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.domains.events.models.dtos.EventReq
import com.example.coreweb.domains.events.services.EventService
import com.example.coreweb.routing.Route
import com.example.coreweb.utils.PageableParams
import com.example.coreweb.utils.ResponseData
import com.example.coreweb.utils.onSecuredContext
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
@Api(tags = ["Events"], description = "Description about Events")
class EventAdminController @Autowired constructor(
    private val env: Environment,
    private val eventService: EventService,
    private val userRepo: UserRepo
) : CrudControllerV5<EventReq, EventBriefResponse, EventDetailResponse> {

    @GetMapping(Route.V2.Events.AdminApis.SEARCH)
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
    ): ResponseEntity<ResponseData<Page<EventBriefResponse>>> =
        this.eventService.search(
            username = username,
            fromDate = fromDate ?: Instant.EPOCH,
            toDate = toDate ?: Instant.now(),
            params = PageableParams.of(query, page, size, sortBy, direction)
        ).toResponse { it.toBriefResponse() }

    @GetMapping(Route.V2.Events.AdminApis.FIND)
    override fun find(@PathVariable("id") id: Long): ResponseEntity<ResponseData<EventDetailResponse>> =
        onSecuredContext { auth ->
            this.eventService.getAsEither(id, asUser = auth)
                .toResponse(debug = debug()) {
                    it.toDetailResponse()
                }
        }

    @PostMapping(Route.V2.Events.AdminApis.CREATE)
    override fun create(
        @Valid @RequestBody req: EventReq
    ): ResponseEntity<ResponseData<EventDetailResponse>> =
        onSecuredContext { auth ->
            this.eventService.save(
                entity = req.asEvent(
                    getUser = { this.userRepo.find(it).toArrow() }
                ),
                asUser = auth
            ).toResponse(debug = debug()) {
                it.toDetailResponse()
            }
        }

    @PatchMapping(Route.V2.Events.AdminApis.UPDATE)
    override fun update(
        @PathVariable("id") id: Long,
        @Valid @RequestBody req: EventReq
    ): ResponseEntity<ResponseData<EventDetailResponse>> =
        onSecuredContext { auth ->
            this.eventService.getAsEither(id, asUser = auth)
                .flatMap {
                    this.eventService.save(
                        entity = req.asEvent(
                            event = it,
                            getUser = { this.userRepo.find(it).toArrow() }
                        ),
                        asUser = auth
                    )
                }
                .toResponse(debug = debug()) {
                    it.toDetailResponse()
                }
        }

    @DeleteMapping(Route.V2.Events.AdminApis.DELETE)
    override fun delete(
        @PathVariable("id") id: Long
    ): ResponseEntity<ResponseData<Boolean>> =
        onSecuredContext { auth ->
            this.eventService.delete(
                id = id, softDelete = true, asUser = auth
            ).toResponse(debug = debug()) { it }
        }


    override fun getEnv(): Environment = this.env

}
