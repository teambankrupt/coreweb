package com.example.coreweb.domains.base.services

import com.example.coreweb.domains.base.entities.BaseEntity
import com.example.coreweb.utils.PageableParams
import org.springframework.data.domain.Page
import java.util.*

interface CrudServiceV3<T : BaseEntity> {
    fun search(params: PageableParams): Page<T>
    fun save(entity: T): T
    fun find(id: Long): Optional<T>
    fun delete(id: Long, softDelete: Boolean)
    fun validate(entity: T)
}