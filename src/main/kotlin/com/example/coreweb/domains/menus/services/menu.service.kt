package com.example.coreweb.domains.menus.services

import arrow.core.Option
import com.example.coreweb.domains.menus.repositories.MenuRepository
import com.example.auth.entities.UserAuth
import com.example.common.exceptions.toArrow
import com.example.common.validation.ValidationV2
import com.example.coreweb.domains.base.services.CrudServiceV5
import com.example.coreweb.domains.menus.models.entities.Menu
import com.example.coreweb.utils.PageableParamsV2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.time.Instant

interface MenuService : CrudServiceV5<Menu> {
    fun search(
        username: String?,
        fromDate: Instant, toDate: Instant,
        params: PageableParamsV2
    ): Page<Menu>
}

@Service
class MenuServiceBean @Autowired constructor(
    private val menuRepository: MenuRepository
) : MenuService {
    override fun search(
        username: String?,
        fromDate: Instant,
        toDate: Instant,
        params: PageableParamsV2
    ): Page<Menu> = this.menuRepository.search(
        query = params.query(),
        username = username,
        fromDate = fromDate,
        toDate = toDate,
        pageable = params.toPageRequest<Menu>()
    )

    override fun validations(asUser: UserAuth): Set<ValidationV2<Menu>> = setOf(
        titleValidation
    )

    override fun find(id: Long, asUser: UserAuth): Option<Menu> =
        this.menuRepository.find(id).toArrow()

    override fun getRepository(): JpaRepository<Menu, Long> = this.menuRepository

}