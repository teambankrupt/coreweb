package com.example.coreweb.exceptions

import com.example.common.exceptions.Err
import com.example.common.utils.toHeaderMultiValueMap
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ErrMessage(
    val type: String,
    val message: String,
    val status: HttpStatus,
    val description: String = "",
    val actions: Set<ErrActions> = setOf(),
    var exception: Throwable? = null
) {
    fun asResponse(headers: Map<String, Set<String>> = mapOf()): ResponseEntity<ErrMessage> =
        ResponseEntity.status(status)
            .headers(HttpHeaders(headers.toHeaderMultiValueMap()))
            .body(this)
}

enum class ErrActions(val value: String) {
    SHOW_CONNECTION_ERR_DIALOG("show-conn-err-dialog"),
    SHOW_UPCOMING_PAGE("show-upcoming-page"),
    TRIGGER_OTP_INPUT("trigger-otp-input");


    companion object {
        private const val headerName = "s-err-action"
        fun from(headers: Map<String, List<String>>): Set<ErrActions> {
            val header = headers[headerName] ?: emptyList()
            val actions = header.mapNotNull {
                fromValue(it)
            }.toSet()
            return actions
        }

        private fun fromValue(value: String) =
            entries.firstOrNull { it.value == value }
    }
}

fun Err.toMessage(debug: Boolean): ErrMessage = when (this) {
    Err.GenericError ->
        ErrMessage(
            actions = HashSet(),
            type = this::class.simpleName ?: "",
            message = this.throwable.message ?: "",
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            exception = if (debug) this.throwable else null
        )

    is Err.UserErr ->
        ErrMessage(
            actions = HashSet(),
            type = this::class.simpleName ?: "",
            message = this.throwable.message ?: "",
            status = HttpStatus.BAD_REQUEST,
            exception = if (debug) this.throwable else null
        )

    is Err.ValidationErr -> ErrMessage(
        type = this::class.simpleName ?: "",
        message = this.throwable.message ?: "",
        description = this.instructionMsg,
        status = HttpStatus.BAD_REQUEST,
        exception = if (debug) this.throwable else null
    )

    is Err.OperationErr -> when (this) {
        is Err.OperationErr.NonExistentErr -> ErrMessage(
            actions = HashSet(),
            type = this::class.simpleName ?: "",
            message = this.throwable.message ?: "",
            status = HttpStatus.NOT_FOUND,
            exception = if (debug) this.throwable else null
        )

        Err.OperationErr.ForbiddenErr -> ErrMessage(
            actions = HashSet(),
            type = this::class.simpleName ?: "",
            message = this.throwable.message ?: "",
            status = HttpStatus.FORBIDDEN,
            exception = if (debug) this.throwable else null
        )

        Err.OperationErr.ConstraintViolationErr -> ErrMessage(
            actions = HashSet(),
            type = this::class.simpleName ?: "",
            message = this.throwable.message ?: "",
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            exception = if (debug) this.throwable else null
        )

        Err.OperationErr.UnavailableErr -> ErrMessage(
            actions = HashSet(),
            type = this::class.simpleName ?: "",
            message = this.throwable.message ?: "",
            status = HttpStatus.NOT_FOUND,
            exception = if (debug) this.throwable else null
        )

    }
}