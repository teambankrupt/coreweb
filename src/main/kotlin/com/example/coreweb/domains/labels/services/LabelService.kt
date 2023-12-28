package com.example.coreweb.domains.labels.services

import com.example.coreweb.domains.base.services.CrudServiceV4
import com.example.coreweb.domains.labels.models.entities.Label
import com.example.coreweb.utils.PageableParams
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface LabelService : CrudServiceV4<Label> {
    fun search(
        parentId: Long?,
        parentCode: String?,
        query: String?,
        pageRequest: PageRequest
    ): Page<Label>

    fun search(
        parentId: Long?,
        parentCode: String?,
        params: PageableParams
    ): Page<Label>

    fun findByIds(ids: Set<Long>): Set<Label>

    fun fixPaths(parentId: Long?)
}