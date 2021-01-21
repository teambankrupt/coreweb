package com.example.coreweb.domains.locationtypes.repositories

import com.example.coreweb.domains.locationtypes.models.entities.LocationType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LocationTypeRepository : JpaRepository<LocationType, Long> {

    @Query("SELECT e FROM LocationType e WHERE (:q IS NULL OR LOWER(e.label) LIKE %:q%) AND e.deleted=FALSE")
    fun search(@Param("q") query: String, pageable: Pageable): Page<LocationType>

    @Query("SELECT e FROM LocationType e WHERE e.id=:id AND e.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<LocationType>

    @Query("SELECT e FROM LocationType e WHERE e.code=:code AND e.deleted=FALSE")
    fun findByCode(@Param("code") code: String): Optional<LocationType>

    @Query("SELECT e FROM LocationType e WHERE LOWER(e.code) =LOWER(:code) ")
    fun findByCodeIncludingDeleted(@Param("code") code: String): Optional<LocationType>

}