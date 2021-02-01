package com.example.coreweb.domains.contacts.repositories

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

    @Query("SELECT c FROM Contact c WHERE (:q IS NULL OR c.name LIKE %:q% OR c.phone LIKE %:q% OR c.email LIKE %:q% OR c.createdBy LIKE %:q%) AND c.deleted=FALSE")
    fun search(@Param("q") query: String, pageable: Pageable): Page<Contact>

    @Query("SELECT c FROM Contact c WHERE c.id=:id AND c.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<Contact>
}
