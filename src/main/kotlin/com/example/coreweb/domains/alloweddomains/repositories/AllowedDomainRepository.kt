package com.example.coreweb.domains.alloweddomains.repositories

import com.example.coreweb.domains.alloweddomains.models.entities.AllowedDomain
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface AllowedDomainRepository : JpaRepository<AllowedDomain, Long> {

    @Query(
        """
        SELECT e FROM AllowedDomain e WHERE (:q IS NULL OR LOWER(e.domain) LIKE %:q%)
        AND (:username IS NULL OR e.createdBy=:username)
        AND (e.createdAt BETWEEN :fromDate AND :toDate) AND e.deleted=FALSE
    """
    )
    fun search(
        @Param("q") query: String?,
        @Param("username") username: String?,
        @Param("fromDate") fromDate: Instant,
        @Param("toDate") toDate: Instant,
        pageable: Pageable
    ): Page<AllowedDomain>

    @Query("SELECT e FROM AllowedDomain e WHERE e.id=:id AND e.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<AllowedDomain>


    @Query("SELECT e FROM AllowedDomain e WHERE e.domain=:domain AND e.deleted=FALSE")
    fun findByDomain(@Param("domain") domain: String): Optional<AllowedDomain>

    @Query("SELECT e.domain FROM AllowedDomain e WHERE e.active=TRUE AND e.deleted=FALSE")
    fun allowedDomains(): List<String>
}