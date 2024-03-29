package com.example.coreweb.exceptions

import com.example.common.exceptions.forbidden.ForbiddenException
import com.example.common.exceptions.invalid.InvalidException
import com.example.common.exceptions.notacceptable.NotAcceptableException
import com.example.common.exceptions.notfound.NotFoundException
import com.example.common.utils.toHeaderMultiValueMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
open class BaseExHandler @Autowired constructor(
    private val env: Environment
) {

    fun debug(): Boolean {
        val profiles = env.activeProfiles
        return !profiles.contains("prod")
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.NOT_FOUND, ex)
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(ex: ForbiddenException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.FORBIDDEN, ex)
    }

    @ExceptionHandler(NotAcceptableException::class)
    fun handleNotAcceptableException(ex: NotAcceptableException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.NOT_ACCEPTABLE, ex)
    }

    @ExceptionHandler(InvalidException::class)
    fun handleInvalidException(ex: InvalidException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.BAD_REQUEST, ex)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.EXPECTATION_FAILED, ex)
    }

    fun buildResponse(
        status: HttpStatus,
        ex: Throwable,
        customHeaders: Map<String, Set<String>> = emptyMap()
    ): ResponseEntity<ErrorResponse> {
        val response = if (debug()) ErrorResponse(
            status.value(),
            status.name,
            ex.message ?: "",
            ex
        ) else ErrorResponse(
            status.value(),
            status.name,
            ex.message ?: ""
        )
        val headers = HttpHeaders(
            customHeaders.toHeaderMultiValueMap()
        )

        return ResponseEntity
            .status(status)
            .headers(headers)
            .body(response)
    }

}