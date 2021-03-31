package com.example.coreweb.domains.contacts.services

import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.domains.contacts.models.entities.Contact
import com.example.coreweb.domains.base.services.CrudServiceV2
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import java.util.*

interface ContactService : CrudServiceV2<Contact> {
    fun search(
        userId: Long,
        query: String,
        page: Int,
        size: Int,
        sortBy: SortByFields,
        direction: Sort.Direction
    ): Page<Contact>

    fun findSelfContact(): Optional<Contact>
}
