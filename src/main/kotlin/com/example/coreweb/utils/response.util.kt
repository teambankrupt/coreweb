package com.example.coreweb.utils

import arrow.core.Either
import com.example.auth.config.security.SecurityContext
import com.example.auth.entities.UserAuth
import com.example.common.exceptions.Err
import com.example.coreweb.exceptions.ErrMessage
import com.example.coreweb.exceptions.toMessage
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.Instant

enum class ResponseType {
    SUCCESS, ERROR, WARNING, INFO
}


data class ResponseData<DATA>(
    val type: ResponseType,
    val status: HttpStatus,
    val code: Int = status.value(),
    val time: Instant = Instant.now(),
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val body: DATA? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val error: ErrMessage? = null
) {
    fun asResponse(): ResponseEntity<ResponseData<DATA>> = ResponseEntity.status(status).body(this)
}

fun <E, R> Either<Err, E>.toResponse(debug: Boolean, map: (entity: E) -> R): ResponseEntity<ResponseData<R>> =
    this.fold(
        {
            val errMessage = it.toMessage(debug)
            ResponseData<R>(
                type = ResponseType.ERROR,
                status = errMessage.status,
                body = null,
                error = errMessage
            ).asResponse()
        },
        {
            ResponseData(
                type = ResponseType.SUCCESS,
                status = HttpStatus.OK,
                body = map(it),
                error = null
            ).asResponse()
        }
    )

fun <E, R> Page<E>.toResponse(map: (entity: E) -> R): ResponseEntity<ResponseData<Page<R>>> =
    ResponseData(
        type = ResponseType.SUCCESS,
        status = HttpStatus.OK,
        body = this.map { map(it) },
        error = null
    ).asResponse()

fun <E, R> Collection<E>.toResponse(map: (entity: E) -> R): ResponseEntity<ResponseData<List<R>>> =
    ResponseData(
        type = ResponseType.SUCCESS,
        status = HttpStatus.OK,
        body = this.map { map(it) },
        error = null
    ).asResponse()

fun <T>onSecuredContext(block: (auth: UserAuth) -> T) =
    block(SecurityContext.getCurrentUser())
