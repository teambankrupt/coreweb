package com.example.coreweb.domains.menus.repositories

import com.example.coreweb.domains.menus.models.entities.Menu
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface MenuRepository : JpaRepository<Menu, Long> {

    @Query(
        """
        SELECT e FROM Menu e WHERE (:q IS NULL OR LOWER(e.title) LIKE %:q%)
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
    ): Page<Menu>

    @Query("SELECT e FROM Menu e WHERE e.id=:id AND e.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<Menu>

}