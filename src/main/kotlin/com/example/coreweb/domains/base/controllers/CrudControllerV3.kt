package com.example.coreweb.domains.base.controllers

import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.example.coreweb.domains.base.models.enums.SortByFields
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity

@Deprecated("Use CrudControllerV4 instead")
interface CrudControllerV3<T : BaseDto> {
    fun search(query: String?, page: Int, size: Int, sortBy: SortByFields, direction: Sort.Direction): ResponseEntity<Page<T>>
    fun find(id: Long): ResponseEntity<T>
    fun create(dto: T): ResponseEntity<T>
    fun update(id: Long, dto: T): ResponseEntity<T>
    fun delete(id: Long): ResponseEntity<Any>
}
