package com.example.coreweb.domains.base.services

import com.example.coreweb.domains.base.entities.BaseEntityV2
import com.example.coreweb.utils.PageableParams
import org.springframework.data.domain.Page
import java.util.*

@Deprecated("Use CrudServiceV5 instead", ReplaceWith("CrudServiceV5<T>"))
interface CrudServiceV4<T : BaseEntityV2> {
    fun search(params: PageableParams): Page<T>
    fun save(entity: T): T
    fun find(id: Long): Optional<T>
    fun delete(id: Long, softDelete: Boolean)
    fun validate(entity: T)
}