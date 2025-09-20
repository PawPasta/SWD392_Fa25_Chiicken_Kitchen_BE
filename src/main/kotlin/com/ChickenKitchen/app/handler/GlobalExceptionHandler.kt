package com.ChickenKitchen.app.handler

import com.ChickenKitchen.app.model.dto.response.ResponseModel
import com.ChickenKitchen.app.handler.UserAlreadyExistsException
import com.ChickenKitchen.app.handler.UserNotFoundException
import com.ChickenKitchen.app.handler.AuthenticationException
import com.ChickenKitchen.app.handler.TokenException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import jakarta.persistence.EntityNotFoundException
import java.sql.SQLIntegrityConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler {

    // ========== Authentication Exceptions ==========

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(e: UserAlreadyExistsException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "User already exists")

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuth(e: AuthenticationException) =
        buildError(HttpStatus.UNAUTHORIZED, e.message ?: "Authentication failed")

    @ExceptionHandler(AccessDeniedException::class)
    fun handleForbidden(e: AccessDeniedException) =
        buildError(HttpStatus.FORBIDDEN, e.message ?: "Forbidden")

    @ExceptionHandler(TokenException::class)
    fun handleToken(e: TokenException) =
        buildError(HttpStatus.UNAUTHORIZED, e.message ?: "Token invalid or expired")

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(e: UserNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "User not found")

    // ========== Wallet Exceptions ==========

    @ExceptionHandler(WalletNotFoundException::class)
    fun handleWalletNotFound(e: WalletNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Wallet not found")

    // ========== Ingredient Exceptions ==========

    @ExceptionHandler(IngredientNotFoundException::class)
    fun handleIngredientNotFound(e: IngredientNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Ingredient not found")

    // ========== Nutrient Exceptions ==========
    @ExceptionHandler(NutrientNotFoundException::class)
    fun handleNutrientNotFound(e: NutrientNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Nutrient not found")

    @ExceptionHandler(NutrientAlreadyExistsException::class)
    fun handleNutrientAlreadyExists(e: NutrientAlreadyExistsException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Nutrient already exists")

    // ========== Helper ==========
    private fun buildError(status: HttpStatus, message: String): ResponseEntity<ResponseModel> {
        return ResponseEntity(
            ResponseModel.error(
                statusCode = status.value(),
                message = message
            ),
            status
        )
    }
}
