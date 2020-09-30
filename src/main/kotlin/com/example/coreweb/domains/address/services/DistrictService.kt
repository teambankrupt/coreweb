package com.example.coreweb.domains.address.services

import com.example.coreweb.domains.address.models.entities.District
import com.example.coreweb.domains.base.services.CrudService
import org.springframework.data.domain.Page

interface DistrictService : CrudService<District> {
    fun search(divisionId: Long?, query: String, page: Int, size: Int): Page<District>
}
