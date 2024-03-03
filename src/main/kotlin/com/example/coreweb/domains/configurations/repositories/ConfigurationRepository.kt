package com.example.coreweb.domains.configurations.repositories

import com.example.coreweb.domains.configurations.models.entities.Configuration
import com.example.coreweb.domains.configurations.models.enums.ConfigurationType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface ConfigurationRepository : JpaRepository<Configuration, Long> {

    @Query(
        """
        SELECT e FROM Configuration e WHERE (:q IS NULL OR LOWER(e.key) LIKE %:q%)
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
    ): Page<Configuration>

    @Query("SELECT e FROM Configuration e WHERE e.id=:id AND e.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<Configuration>

    @Query("SELECT e FROM Configuration e WHERE e.namespace=:namespace AND e.type=:type AND e.key=:key AND e.deleted=FALSE")
    fun findByNamespaceAndTypeAndKey(
        @Param("namespace") namespace: String,
        @Param("type") type: ConfigurationType,
        @Param("key") key: String
    ): Optional<Configuration>
}