package com.minierp.exception
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val details: Map<String, String>? = null,
)

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException) =
        ResponseEntity(
            ErrorResponse(status = 400, error = "Bad Request", message = ex.message ?: "Invalid argument"),
            HttpStatus.BAD_REQUEST,
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException) =
        ResponseEntity(
            ErrorResponse(
                status = 400,
                error = "Validation Failed",
                message = "Invalid fields",
                details = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid") },
            ),
            HttpStatus.BAD_REQUEST,
        )

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception) =
        ResponseEntity(
            ErrorResponse(status = 500, error = "Internal Server Error", message = "Unexpected error"),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
}
