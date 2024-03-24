package com.example.coreweb.domains.alloweddomains.services

import arrow.core.Option
import com.example.auth.entities.UserAuth
import com.example.common.exceptions.toArrow
import com.example.common.validation.ValidationV2
import com.example.coreweb.domains.alloweddomains.models.entities.AllowedDomain
import com.example.coreweb.domains.alloweddomains.repositories.AllowedDomainRepository
import com.example.coreweb.domains.base.services.CrudServiceV5
import com.example.coreweb.utils.PageAttr
import com.example.coreweb.utils.PageableParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.time.Instant

interface AllowedDomainService : CrudServiceV5<AllowedDomain> {
    fun search(
        username: String?,
        fromDate: Instant, toDate: Instant,
        params: PageableParams
    ): Page<AllowedDomain>

    fun allowedDomains(): List<String>
}

@Service
open class AllowedDomainServiceBean @Autowired constructor(
    private val allowedDomainRepository: AllowedDomainRepository
) : AllowedDomainService {
    override fun search(
        username: String?,
        fromDate: Instant,
        toDate: Instant,
        params: PageableParams
    ): Page<AllowedDomain> = this.allowedDomainRepository.search(
        query = params.query,
        username = username,
        fromDate = fromDate,
        toDate = toDate,
        pageable = PageAttr.getPageRequest(params)
    )

    override fun allowedDomains(): List<String> =
        this.allowedDomainRepository.allowedDomains()

    override fun validations(asUser: UserAuth): Set<ValidationV2<AllowedDomain>> = setOf(
        domainNameValidation, domainShouldBeUnique {
            this.allowedDomainRepository.findByDomain(it).toArrow()
        }
    )

    override fun find(id: Long, asUser: UserAuth): Option<AllowedDomain> =
        this.allowedDomainRepository.find(id).toArrow()

    override fun getRepository(): JpaRepository<AllowedDomain, Long> = this.allowedDomainRepository

}