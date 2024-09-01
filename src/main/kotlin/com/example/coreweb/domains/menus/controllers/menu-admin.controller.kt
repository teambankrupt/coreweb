package com.example.coreweb.domains.menus.controllers

import arrow.core.flatMap
import com.example.coreweb.domains.base.controllers.CrudControllerV6
import com.example.coreweb.domains.menus.models.dtos.*
import com.example.coreweb.domains.menus.services.MenuService
import com.example.coreweb.routing.Route
import com.example.coreweb.utils.PageableParamsV2
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
@Api(tags = ["Menus"], description = "Description about Menus")
class MenuAdminController @Autowired constructor(
    private val env: Environment,
    private val menuService: MenuService,
) : CrudControllerV6<MenuReq, MenuBriefResponse, MenuDetailResponse> {

    @GetMapping(Route.V2.Menus.AdminApis.SEARCH)
    override fun search(
        @RequestParam("username", required = false) username: String?,
        @RequestParam("from_date", required = false) fromDate: Instant?,
        @RequestParam("to_date", required = false) toDate: Instant?,
        @RequestParam("q", required = false) query: String?,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "ID") sortBy: String,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction,
        @RequestParam(required = false) extra: Map<String, String>,
    ): ResponseEntity<ResponseData<Page<MenuBriefResponse>>> =
        this.menuService.search(
            username = username,
            fromDate = fromDate ?: Instant.EPOCH,
            toDate = toDate ?: Instant.now(),
            params = PageableParamsV2(query, page, size, sortBy, direction)
        ).toResponse { it.toBriefResponse() }

    @GetMapping(Route.V2.Menus.AdminApis.FIND)
    override fun find(@PathVariable("id") id: Long): ResponseEntity<ResponseData<MenuDetailResponse>> =
        onSecuredContext { auth ->
            this.menuService.getAsEither(id, asUser = auth)
                .toResponse(debug = debug()) {
                    it.toDetailResponse()
                }
        }

    @PostMapping(Route.V2.Menus.AdminApis.CREATE)
    override fun create(
        @Valid @RequestBody req: MenuReq
    ): ResponseEntity<ResponseData<MenuDetailResponse>> =
        onSecuredContext { auth ->
            this.menuService.save(
                entity = req.asMenu(
                    getMenu = { this.menuService.find(id = it, asUser = auth) }
                ),
                asUser = auth
            ).toResponse(debug = debug()) {
                it.toDetailResponse()
            }
        }

    @PatchMapping(Route.V2.Menus.AdminApis.UPDATE)
    override fun update(
        @PathVariable("id") id: Long,
        @Valid @RequestBody req: MenuReq
    ): ResponseEntity<ResponseData<MenuDetailResponse>> =
        onSecuredContext { auth ->
            this.menuService.getAsEither(id, asUser = auth)
                .flatMap { menu ->
                    this.menuService.save(
                        entity = req.asMenu(
                            menu = menu,
                            getMenu = { this.menuService.find(id = it, asUser = auth) }
                        ),
                        asUser = auth
                    )
                }
                .toResponse(debug = debug()) {
                    it.toDetailResponse()
                }
        }

    @DeleteMapping(Route.V2.Menus.AdminApis.DELETE)
    override fun delete(
        @PathVariable("id") id: Long
    ): ResponseEntity<ResponseData<Boolean>> =
        onSecuredContext { auth ->
            this.menuService.delete(
                id = id, softDelete = true, asUser = auth
            ).toResponse(debug = debug()) { it }
        }


    override fun getEnv(): Environment = this.env

}
