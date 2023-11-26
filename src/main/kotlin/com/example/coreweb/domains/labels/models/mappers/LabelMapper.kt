package com.example.coreweb.domains.labels.models.mappers

import arrow.core.toOption
import com.example.common.exceptions.orDuckIt
import com.example.common.utils.SessionIdentifierGenerator
import com.example.coreweb.domains.base.models.mappers.BaseMapperV2
import com.example.coreweb.domains.labels.models.dtos.LabelDto
import com.example.coreweb.domains.labels.models.entities.Label
import com.example.coreweb.domains.labels.repositories.LabelRepository
import org.springframework.stereotype.Component
import java.util.*

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

            this.name = entity.name
            this.code = entity.code
            this.description = entity.description
            this.parentId = entity.parent.map { it.id }.orElse(null)
            this.icon = entity.icon
            this.image = entity.image
            this.flagship = entity.flagship
            this.serial = entity.serial

            this.path = entity.path
            this.absolutePath = entity.absolutePath
            this.rootId = entity.rootId
            this.color = entity.color
            this.uiHeight = entity.uiHeight
            this.backgroundColor = entity.backgroundColor
        }

        return dto
    }

    override fun map(dto: LabelDto, exEntity: Label?): Label {
        val entity = exEntity ?: Label()

        entity.apply {
            this.name = dto.name
            this.code = (dto.code ?: "${dto.name}${SessionIdentifierGenerator.alphanumeric(6)}")
                .replace(" ", "_")
                .uppercase(Locale.getDefault())
                .take(100)
            this.description = dto.description
            this.setParent(dto.parentId?.let {
                labelRepository.find(it).orDuckIt(it)
            })
            this.serial = dto.serial
            this.flagship = dto.flagship
            this.icon = dto.icon
            this.image = dto.image
            this.color = dto.color ?: "#000000"
            this.backgroundColor = dto.backgroundColor ?: "#FFFFFF"
            this.uiHeight = dto.uiHeight ?: 100
        }

        return entity
    }
}