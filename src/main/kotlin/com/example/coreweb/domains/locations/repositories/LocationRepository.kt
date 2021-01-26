package com.example.coreweb.domains.locations.repositories

import com.example.coreweb.domains.locations.models.entities.Location
import com.example.coreweb.domains.locationtypes.models.entities.LocationType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LocationRepository : JpaRepository<Location, Long> {

    @Query("SELECT e FROM Location e WHERE (:q IS NULL OR LOWER(e.label) LIKE %:q%) AND (e.parent.id IS NULL) AND e.deleted=FALSE AND e.type.deleted=FALSE")
    fun searchRootLocations(@Param("q") query: String, pageable: Pageable): Page<Location>

    @Query("SELECT e FROM Location e WHERE (:q IS NULL OR LOWER(e.label) LIKE %:q%) AND (e.parent.id=:parentId) AND e.deleted=FALSE AND e.type.deleted=FALSE")
    fun searchByParent(@Param("parentId") parentId: Long, @Param("q") query: String, pageable: Pageable): Page<Location>

    @Query("SELECT e FROM Location e WHERE (:q IS NULL OR LOWER(e.label) LIKE %:q%) AND (:typeId IS NULL OR e.type.id=:typeId) AND e.deleted=FALSE AND e.type.deleted=FALSE")
    fun search(@Param("q") query: String, @Param("typeId") typeId: Long?, pageable: Pageable): Page<Location>

    @Query("SELECT e FROM Location e WHERE e.id=:id AND e.deleted=FALSE AND e.type.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<Location>

    @Query("SELECT e FROM Location e WHERE e.code=:code AND e.deleted=FALSE AND e.type.deleted=FALSE")
    fun findByCode(@Param("code") code: String): Optional<Location>

    @Query("SELECT e FROM Location e WHERE LOWER(e.code) =LOWER(:code)")
    fun findByCodeIncludingDeleted(@Param("code") code: String): Optional<Location>
}