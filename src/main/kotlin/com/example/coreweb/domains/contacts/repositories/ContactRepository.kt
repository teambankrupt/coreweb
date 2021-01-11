package com.example.app.domains.contact.repositories

import com.example.coreweb.domains.contacts.models.entities.Contact
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ContactRepository : JpaRepository<Contact, Long> {

    @Query("SELECT e FROM Contact e WHERE (:q IS NULL OR e.createdBy LIKE %:q%) AND e.deleted=FALSE")
    fun search(@Param("q") query: String, pageable: Pageable): Page<Contact>

    @Query("SELECT e FROM Contact e WHERE e.id=:id AND e.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<Contact>

}
