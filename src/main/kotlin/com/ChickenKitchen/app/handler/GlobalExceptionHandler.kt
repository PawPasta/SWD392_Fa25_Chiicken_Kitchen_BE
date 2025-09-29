package com.ChickenKitchen.app.handler

import com.ChickenKitchen.app.model.dto.response.ResponseModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    // ========== Authentication Exceptions ==========

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(e: UserAlreadyExistsException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "User already exists")

    @ExceptionHandler(UsernameNotCorrectException ::class)
    fun handleUsernameNotCorrect(e: UsernameNotCorrectException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Username required more than 5 characters")

    @ExceptionHandler(PasswordNotCorrectException ::class)
    fun handlePasswordNotCorrect(e: PasswordNotCorrectException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Password must have at least 8 characters, 1 uppercase letter, 1 number, and 1 special character")

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

    // ========== Address Exception ==========
    @ExceptionHandler(PhoneNumberInvalidException::class)
    fun handlePhoneNumberInvalid(e: PhoneNumberInvalidException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Phone number is invalid")


    // ========== Order Exception ==========
    @ExceptionHandler(OrderNotFoundException::class)
    fun handleOrderNotFound(e: OrderNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Order not found")
    @ExceptionHandler(OrderNotInStatusException::class)
    fun handleOrderNotInStatus(e: OrderNotInStatusException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Order not in the required status")


    //===========Transactions Exception ==========
    @ExceptionHandler(TransactionNotFoundException::class)
    fun handleTransactionNotFound(e: TransactionNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Transaction not found")


    //=========== Payment Method Exception ==========
    @ExceptionHandler(PaymentMethodNotActiveException::class)
    fun handlePaymentMethodNotActive(e: PaymentMethodNotActiveException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Payment method is under maintenance")
    @ExceptionHandler(PaymentMethodNotFoundException :: class)
    fun handlePaymentMethodNotFound(e: PaymentMethodNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Payment method not found")

    //=========== Payment Exception ==========
    @ExceptionHandler(VNPayPaymentNotVerify :: class)
    fun handlePaymentMethodNotFoundInPayment(e: VNPayPaymentNotVerify) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Payment method not found")
    @ExceptionHandler(PaymentException ::class)
    fun handlePaymentException(e: PaymentException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Payment processing error")

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
