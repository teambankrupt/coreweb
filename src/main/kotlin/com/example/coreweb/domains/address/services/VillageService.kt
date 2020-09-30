package com.example.coreweb.domains.address.services

import com.example.coreweb.domains.address.models.entities.Village
import com.example.coreweb.domains.base.services.CrudService
import org.springframework.data.domain.Page


interface VillageService : CrudService<Village> {
    fun search(unionId: Long?, query: String, page: Int, size: Int): Page<Village>
}