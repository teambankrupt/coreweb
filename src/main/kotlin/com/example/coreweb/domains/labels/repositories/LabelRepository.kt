package com.example.coreweb.domains.labels.repositories

import com.example.coreweb.domains.labels.models.entities.Label
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LabelRepository : JpaRepository<Label, Long> {

    @Query(
        "SELECT l FROM Label l WHERE (:q IS NULL OR LOWER(l.name) LIKE %:q%) " +
                " AND (:parentId IS NULL OR l.parent.id=:parentId) AND l.deleted=FALSE"
    )
    fun search(
        @Param("parentId") parentId: Long?,
        @Param("q") query: String?,
        pageable: Pageable
    ): Page<Label>

    @Query("SELECT e FROM Label e WHERE e.id=:id AND e.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<Label>

}