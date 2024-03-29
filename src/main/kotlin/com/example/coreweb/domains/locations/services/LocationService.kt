package com.example.coreweb.domains.locations.services

import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.domains.locations.models.entities.Location
import com.example.coreweb.domains.base.services.CrudServiceV2
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import java.util.*

interface LocationService : CrudServiceV2<Location> {
    fun searchByZipCode(zipCode: String, query: String, page: Int, size: Int, sortBy: SortByFields, direction: Sort.Direction): Page<Location>
    fun searchForType(typeId: Long, query: String, page: Int, size: Int, sortBy: SortByFields, direction: Sort.Direction): Page<Location>
    fun searchByParent(parentId: Long?, query: String, page: Int, size: Int, sortBy: SortByFields, direction: Sort.Direction): Page<Location>
    fun searchByZipCode(zipCode: String): Optional<Location>
    fun findByIds(ids: Set<Long>): Set<Location>
}
