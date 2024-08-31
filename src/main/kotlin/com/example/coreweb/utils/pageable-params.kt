package com.example.coreweb.utils

import com.example.coreweb.domains.base.entities.BaseEntityV2
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import kotlin.reflect.full.memberProperties

data class PageableParamsV2(
    private val query: String?,
    val page: Int,
    val size: Int,
    val sortBy: String,
    val sortDirection: Sort.Direction,
) {
    fun query() = query?.trim()?.lowercase()
    inline fun <reified T : BaseEntityV2> toPageRequest(): PageRequest {
        val sort = T::class.memberProperties.firstOrNull { it.name == sortBy }?.name ?: "id"
        return PageRequest.of(
            page, size, sortDirection, sort
        )
    }
}
