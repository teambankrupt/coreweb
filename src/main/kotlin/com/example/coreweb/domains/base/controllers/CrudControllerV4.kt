package com.example.coreweb.domains.base.controllers

import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.utils.ResponseData
import org.springframework.core.env.Environment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import java.time.Instant

interface CrudControllerV4<REQ, BRIEF_RESPONSE, DETAIL_RESPONSE> {
    fun search(
        username: String?,
        fromDate: Instant?, toDate: Instant?,
        query: String?,
        page: Int, size: Int,
        sortBy: SortByFields,
        direction: Sort.Direction
    ): ResponseEntity<ResponseData<Page<BRIEF_RESPONSE>>>

    fun find(id: Long): ResponseEntity<ResponseData<DETAIL_RESPONSE>>
    fun create(req: REQ): ResponseEntity<ResponseData<DETAIL_RESPONSE>>
    fun update(id: Long, req: REQ): ResponseEntity<ResponseData<DETAIL_RESPONSE>>
    fun delete(id: Long): ResponseEntity<ResponseData<Boolean>>

    fun getEnv(): Environment

    fun debug(): Boolean = getEnv().activeProfiles.contains("prod").not()

}

fun Environment.isDebug(): Boolean = this.activeProfiles.contains("prod").not()
