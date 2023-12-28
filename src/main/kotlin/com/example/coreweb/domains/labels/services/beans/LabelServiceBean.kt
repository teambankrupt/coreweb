package com.example.coreweb.domains.labels.services.beans

import arrow.core.toOption
import com.example.common.utils.ExceptionUtil
import com.example.common.utils.Validator
import com.example.coreweb.domains.labels.models.entities.Label
import com.example.coreweb.domains.labels.repositories.LabelRepository
import com.example.coreweb.domains.labels.services.LabelService
import com.example.coreweb.domains.mail.services.MailService
import com.example.coreweb.utils.PageAttr
import com.example.coreweb.utils.PageableParams
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PropertySource("classpath:mail.properties")
open class LabelServiceBean @Autowired constructor(
    private val labelRepository: LabelRepository,
    private val mailService: MailService,
    @Value("\${email.username}") private val appEmail: String
) : LabelService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun search(parentId: Long?, parentCode: String?, query: String?, pageRequest: PageRequest): Page<Label> =
        this.labelRepository.search(parentId, parentCode, query, pageRequest)

    override fun search(parentId: Long?, parentCode: String?, params: PageableParams): Page<Label> =
        this.labelRepository.search(parentId, parentCode, params.query, PageAttr.getPageRequest(params))

    override fun search(params: PageableParams): Page<Label> {
        return this.labelRepository.search(null, null, params.query, PageAttr.getPageRequest(params))
    }

    @Transactional
    override fun save(entity: Label): Label {
        this.validate(entity)
        if (!entity.isNew) {
            Thread {
                this.updatePathForChildren(entity)
                this.mailService.send(
                    appEmail,
                    "Label saved",
                    "Label ${entity.name} saved and fixed child paths successfully!",
                    true
                )
            }.start()
        }
        return this.labelRepository.save(entity)
    }

    @Transactional
    override fun fixPaths(parentId: Long?): Unit =
        Thread {
            parentId.toOption().fold(
                { this.labelRepository.findRootCategories() },
                { this.labelRepository.findChildren(it) }
            ).forEach {
                this.logger.info("Fixing path for label: ${it.name}")
                if (it.parent.isPresent) {
                    it.syncPath()
                    this.logger.info("New path for label: ${it.name} is ${it.path}")
                    this.save(it)
                } else {
                    logger.info("Label ${it.name} is root label, skipping..")
                }
                this.updatePathForChildren(it)
            }
        }.start()

    private fun updatePathForChildren(label: Label) {
        this.logger.info("Fixed path for label: ${label.name}, updating children..")
        val children = this.labelRepository.findChildren(label.id)
        this.logger.info("Found ${children.size} children for label: ${label.name}")
        if (children.isEmpty()) {
            this.logger.info("No children found for label: ${label.name}, operation finished.")
            return
        }

        children.forEach { c: Label ->
            this.logger.info("Updating path for label: ${c.name}")
            c.syncPath()
            this.logger.info("new path for label: ${c.name} is ${c.path}")
            this.labelRepository.save(c)
            this.logger.info("Updated path for label: ${c.name}, updating children..")
            this.updatePathForChildren(c)
        }
    }

    override fun find(id: Long): Optional<Label> {
        return this.labelRepository.find(id)
    }

    override fun findByIds(ids: Set<Long>): Set<Label> =
        this.labelRepository.findByIds(ids)

    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val entity = this.find(id).orElseThrow { ExceptionUtil.notFound("Label", id) }
            entity.isDeleted = true
            this.labelRepository.save(entity)
            return
        }
        this.labelRepository.deleteById(id)
    }

    override fun validate(entity: Label) {
        if (entity.isNew) {
            if (this.labelRepository.findByCodeIncludingDeleted(entity.code).isPresent)
                throw ExceptionUtil.alreadyExists("Label already exists with code: ${entity.code}")
        } else {
            this.labelRepository.findByCodeIncludingDeleted(entity.code).ifPresent {
                if (it.id != entity.id) throw ExceptionUtil.alreadyExists("Label already exists with code: ${entity.code}")
            }
        }
    }
}