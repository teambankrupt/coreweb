package com.example.coreweb.domains.base.services

import com.example.coreweb.domains.base.entities.BaseEntity
import com.example.coreweb.domains.base.models.enums.SortByFields
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import java.util.*

interface CrudServiceV2<T : BaseEntity> {
    fun search(query: String, page: Int, size: Int, sortBy: SortByFields, direction: Sort.Direction): Page<T>
    fun save(entity: T): T
    fun find(id: Long): Optional<T>
    fun delete(id: Long, softDelete: Boolean)
    fun validate(entity: T)
}