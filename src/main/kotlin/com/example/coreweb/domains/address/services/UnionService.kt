package com.example.coreweb.domains.address.services

import com.example.coreweb.domains.address.models.entities.Union
import com.example.coreweb.domains.address.models.entities.Upazila
import com.example.coreweb.domains.base.services.CrudService
import org.springframework.data.domain.Page

interface UnionService: CrudService<Union>{
    fun search(upazilaId: Long?, query: String, page: Int, size: Int): Page<Union>
}
