package com.example.coreweb.domains.address.services

import com.example.coreweb.domains.address.models.entities.Upazila
import com.example.coreweb.domains.base.services.CrudService
import org.springframework.data.domain.Page

interface UpazilaService : CrudService<Upazila> {
    fun search(districtId: Long?, query: String, page: Int, size: Int): Page<Upazila>
}