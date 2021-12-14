package com.example.coreweb.domains.base.models.mappers

import com.example.coreweb.domains.base.entities.BaseEntityV2
import com.example.coreweb.domains.base.models.dtos.BaseDto

interface BaseMapperV2<T : BaseEntityV2, S : BaseDto> {
    fun map(entity: T): S
    fun map(dto: S, exEntity: T?): T
}