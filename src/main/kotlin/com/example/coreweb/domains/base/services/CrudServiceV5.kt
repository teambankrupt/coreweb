package com.example.coreweb.domains.base.services

import arrow.core.*
import com.example.auth.config.security.SecurityContext
import com.example.common.exceptions.Err
import com.example.common.validation.ValidationV2
import com.example.coreweb.domains.base.entities.BaseEntityV2
import com.example.coreweb.utils.PageableParams
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import javax.validation.ConstraintViolationException

interface CrudServiceV5<ENTITY : BaseEntityV2> {

    fun search(
        username: String?,
        fromDate: Instant, toDate: Instant,
        params: PageableParams
    ): Page<ENTITY>

    fun save(entity: ENTITY): Either<Err.ValidationErr, ENTITY> = this.validations()
        .map { it.apply(entity) } // Apply all validations
        .firstOrNone { it.isLeft() } // Get the first error, if any of the validations failed
        .fold(
            { entity.right() }, // No error
            { it } // there's error, and of course left since we filtered it above
        )
        .map { this.getRepository().save(it) }


    fun validations(): Set<ValidationV2<ENTITY>>

    fun find(id: Long): Option<ENTITY>

    fun getAsEither(id: Long): Either<Err.OperationErr.NonExistentErr, ENTITY> = this.find(id)
        .toEither { Err.OperationErr.NonExistentErr(id) }

    fun delete(id: Long, softDelete: Boolean): Either<Err.OperationErr, Boolean> = this.getAsEither(id)
        .flatMap {
            if (canAccess(it).not())
                Err.OperationErr.ForbiddenErr.left()
            else it.right()
        }
        .map { entity ->
            if (softDelete) {
                entity.isDeleted = true
                this.save(entity)
            } else {
                try {
                    this.getRepository().deleteById(id)
                } catch (e: ConstraintViolationException) {
                    return Err.OperationErr.ConstraintViolationErr.left()
                }
            }
            true.right()
        }
        .getOrElse { Err.OperationErr.NonExistentErr(id).left() }

    fun getRepository(): JpaRepository<ENTITY, Long>

    fun canAccess(entity: ENTITY): Boolean =
        SecurityContext.getCurrentUser().let { auth ->
            auth.isAdmin || entity.createdBy == auth.username
        }
}