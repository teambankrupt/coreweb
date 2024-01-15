package com.example.coreweb.domains.base.services

import arrow.core.*
import com.example.auth.config.security.SecurityContext
import com.example.auth.entities.UserAuth
import com.example.common.exceptions.Err
import com.example.common.validation.ValidationScope
import com.example.common.validation.ValidationV2
import com.example.coreweb.domains.base.entities.BaseEntityV2
import com.example.coreweb.utils.PageableParams
import org.json.XMLTokener.entity
import org.springframework.data.domain.Page
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import javax.validation.ConstraintViolationException

interface CrudServiceV5<ENTITY : BaseEntityV2> {

    fun save(entity: ENTITY, asUser: UserAuth): Either<Err.ValidationErr, ENTITY> =
        this.validations(asUser)
            .map { it.apply(entity, ValidationScope.Write) } // Apply all validations
            .firstOrNone { it.isLeft() } // Get the first error, if any of the validations failed
            .fold(
                { entity.right() }, // No error
                { it } // there's error, and of course left since we filtered it above
            )
            .map { this.getRepository().save(it) }


    fun validations(asUser: UserAuth): Set<ValidationV2<ENTITY>>

    fun find(id: Long, asUser: UserAuth): Option<ENTITY>

    fun getAsEither(id: Long, asUser: UserAuth): Either<Err, ENTITY> =
        this.find(id = id, asUser = asUser)
            .map { entity ->
                this.validations(asUser)
                    .map { it.apply(entity, ValidationScope.Read) }
                    .firstOrNone { it.isLeft() }
                    .fold(
                        { entity.right() },
                        { it }
                    )
            }
            .toEither { Err.OperationErr.NonExistentErr(id) }
            .flatten()

    fun delete(id: Long, softDelete: Boolean, asUser: UserAuth): Either<Err.OperationErr, Boolean> =
        this.getAsEither(id = id, asUser = asUser)
            .flatMap {
                if (canAccess(it).not())
                    Err.OperationErr.ForbiddenErr.left()
                else it.right()
            }
            .map { entity ->
                if (softDelete) {
                    entity.isDeleted = true
                    this.save(entity, asUser)
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