package com.example.coreweb.domains.labels.services

import com.example.coreweb.domains.base.services.CrudServiceV4
import com.example.coreweb.domains.labels.models.entities.Label
import com.example.coreweb.utils.PageableParams
import org.springframework.data.domain.Page

interface LabelService : CrudServiceV4<Label> {
    fun search(
        parentId: Long?,
        parentCode: String?,
        params: PageableParams
    ): Page<Label>
}