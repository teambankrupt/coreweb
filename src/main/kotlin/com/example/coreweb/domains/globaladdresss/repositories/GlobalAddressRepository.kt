package com.example.coreweb.domains.globaladdresss.repositories

import com.example.coreweb.domains.globaladdresss.models.entities.GlobalAddress
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GlobalAddressRepository : JpaRepository<GlobalAddress, Long> {

    @Query("SELECT e FROM GlobalAddress e WHERE (:q IS NULL OR LOWER(e.createdBy) LIKE %:q%) AND e.deleted=FALSE")
    fun search(@Param("q") query: String?, pageable: Pageable): Page<GlobalAddress>

    @Query("SELECT e FROM GlobalAddress e WHERE e.id=:id AND e.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<GlobalAddress>

}