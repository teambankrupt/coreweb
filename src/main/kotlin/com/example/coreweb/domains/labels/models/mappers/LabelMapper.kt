package com.example.coreweb.domains.labels.models.mappers

import com.example.common.exceptions.orDuckIt
import com.example.coreweb.domains.base.models.mappers.BaseMapperV2
import com.example.coreweb.domains.labels.models.dtos.LabelDto
import com.example.coreweb.domains.labels.models.entities.Label
import com.example.coreweb.domains.labels.repositories.LabelRepository
import org.springframework.stereotype.Component

@Component
class LabelMapper(
    private val labelRepository: LabelRepository
) : BaseMapperV2<Label, LabelDto> {

    override fun map(entity: Label): LabelDto {
        val dto = LabelDto()

        dto.apply {
            this.id = entity.id
            this.createdAt = entity.createdAt
            this.updatedAt = entity.updatedAt

            dto.name = entity.name
            dto.description = entity.description
            dto.parentId = entity.parent.map { it.id }.orElse(null)
            dto.icon = entity.icon
            dto.image = entity.image
            dto.path = entity.path
            dto.absolutePath = entity.absolutePath
            dto.rootId = entity.rootId
            dto.color = entity.color
            dto.uiHeight = entity.uiHeight
            dto.backgroundColor = entity.backgroundColor
        }

        return dto
    }

    override fun map(dto: LabelDto, exEntity: Label?): Label {
        val entity = exEntity ?: Label()

        entity.apply {
            this.name = dto.name
            this.description = dto.description
            this.setParent(dto.parentId?.let {
                labelRepository.find(it).orDuckIt(it)
            })
            this.icon = dto.icon
            this.image = dto.image
            this.color = dto.color ?: "#000000"
            this.backgroundColor = dto.backgroundColor ?: "#FFFFFF"
            this.uiHeight = dto.uiHeight ?: 100
        }

        return entity
    }
}