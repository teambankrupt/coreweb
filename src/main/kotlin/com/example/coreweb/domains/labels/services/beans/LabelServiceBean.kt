package com.example.coreweb.domains.labels.services.beans

import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.labels.models.entities.Label
import com.example.coreweb.domains.labels.repositories.LabelRepository
import com.example.coreweb.domains.labels.services.LabelService
import com.example.coreweb.utils.PageAttr
import com.example.coreweb.utils.PageableParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*

@Service
class LabelServiceBean @Autowired constructor(
    private val labelRepository: LabelRepository
) : LabelService {

    override fun search(params: PageableParams): Page<Label> {
        return this.labelRepository.search(params.query, PageAttr.getPageRequest(params))
    }

    override fun save(entity: Label): Label {
        this.validate(entity)
        return this.labelRepository.save(entity)
    }

    override fun find(id: Long): Optional<Label> {
        return this.labelRepository.find(id)
    }

    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val entity = this.find(id).orElseThrow { ExceptionUtil.notFound("Label", id) }
            entity.isDeleted = true
            this.labelRepository.save(entity)
        }
        this.labelRepository.deleteById(id)
    }

    override fun validate(entity: Label) {
    }
}