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

    @ExceptionHandler(IngredientAlreadyExistsException::class)
    fun handleIngredientAlreadyExists(e: IngredientAlreadyExistsException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Ingredient already exists")

    @ExceptionHandler(DuplicateNutrientInIngredientException::class)
    fun handleDuplicateNutrientInIngredient(e: DuplicateNutrientInIngredientException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Duplicate nutrient in ingredient")

    // ========== Nutrient Exceptions ==========
    @ExceptionHandler(NutrientNotFoundException::class)
    fun handleNutrientNotFound(e: NutrientNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Nutrient not found")

    @ExceptionHandler(NutrientAlreadyExistsException::class)
    fun handleNutrientAlreadyExists(e: NutrientAlreadyExistsException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Nutrient already exists")

    // ========== Cooking Method Exceptions ==========
    @ExceptionHandler(CookingMethodNotFoundException::class)
    fun handleCookingMethodNotFound(e: CookingMethodNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Cooking method not found")
    
    @ExceptionHandler(CookingMethodAlreadyExistsException::class)
    fun handleCookingMethodAlreadyExists(e: CookingMethodAlreadyExistsException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Cooking method already exists")

    // ========== Cooking Effect Exceptions ==========
    @ExceptionHandler(CookingEffectNotFoundException::class)
    fun handleCookingEffectNotFound(e: CookingEffectNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Cooking effect not found")

    @ExceptionHandler(CookingEffectAlreadyExistsException::class)
    fun handleCookingEffectAlreadyExists(e: CookingEffectAlreadyExistsException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Cooking effect already exists")

    // ========== Common Exceptions ==========
    @ExceptionHandler(QuantityMustBeNonNegativeException::class)
    fun handleQuantityNonNegative(e: QuantityMustBeNonNegativeException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Quantity must be non-negative")

    @ExceptionHandler(PriceMustBeNonNegativeException::class)
    fun handlePriceNonNegative(e: PriceMustBeNonNegativeException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Price must be non-negative")

    @ExceptionHandler(CategoryMustNotBeNullException::class)
    fun handleCategoryNotNull(e: CategoryMustNotBeNullException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Category must not be null")

    @ExceptionHandler(UnitMustNotBeNullException::class)
    fun handleUnitNotNull(e: UnitMustNotBeNullException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Unit must not be null")

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
