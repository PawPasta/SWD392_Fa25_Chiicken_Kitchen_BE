package com.ChickenKitchen.app.handler

import com.ChickenKitchen.app.model.dto.response.ResponseModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    // ========== Authentication Exceptions ==========

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuth(e: AuthenticationException) =
        buildError(HttpStatus.UNAUTHORIZED, e.message ?: "Authentication Failed")

    @ExceptionHandler(TokenException::class)
    fun handleToken(e: TokenException) =
        buildError(HttpStatus.UNAUTHORIZED, e.message ?: "Token Invalid Or Expired")

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(e: UserNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "User Not Found")

    //USERS
    @ExceptionHandler(UserEmailRequiredException::class)
    fun handleEmailRequired(e: UserEmailRequiredException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Email is required")

    @ExceptionHandler(UserCannotDeleteException::class)
    fun handleCannotDelete(e: UserCannotDeleteException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Cannot delete user")



    // ========== Business Exceptions ==========
    //CATEGORY
    @ExceptionHandler(CategoryNotFoundException::class)
    fun handleCategoryNotFound(e: CategoryNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Category Not Found")

    @ExceptionHandler(CategoryHasMenuItemsException::class)
    fun handleCategoryHasMenuItems(e: CategoryHasMenuItemsException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Category has menu items")

    @ExceptionHandler(CategoryNameExistException::class)
    fun handleCategoryNameExist(e: CategoryNameExistException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Category name already exists")

    //INGREDIENT
    @ExceptionHandler(IngredientNotFoundException::class)
    fun handleNotFound(e: IngredientNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Ingredient not found")

    @ExceptionHandler(IngredientNameExistException::class)
    fun handleNameExist(e: IngredientNameExistException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Ingredient name already exists")

    @ExceptionHandler(IngredientHasBatchesException::class)
    fun handleHasBatches(e: IngredientHasBatchesException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Ingredient has batches and cannot be deleted")

    //STORE
    @ExceptionHandler(StoreNotFoundException::class)
    fun handleNotFound(e: StoreNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Store not found")

    @ExceptionHandler(StoreHasOrdersException::class)
    fun handleHasOrders(e: StoreHasOrdersException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Store has orders, cannot delete")

    @ExceptionHandler(StoreUsedInMenuException::class)
    fun handleUsedInMenu(e: StoreUsedInMenuException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Store is used in menu, cannot delete")

    @ExceptionHandler(StoreHasIngredientsException::class)
    fun handleHasIngredients(e: StoreHasIngredientsException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Store has ingredient batches, cannot delete")

    @ExceptionHandler(StoreNameExistException::class)
    fun handleNameExist(e: StoreNameExistException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Store name already exists")

    @ExceptionHandler(StoreAddressExistException::class)
    fun handleNameExist(e: StoreAddressExistException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Store address already exists")

    //DAILY MENU
    @ExceptionHandler(DailyMenuNotFoundException::class)
    fun handleNotFound(e: DailyMenuNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Daily menu not found")

    @ExceptionHandler(DailyMenuHasStoresException::class)
    fun handleHasStores(e: DailyMenuHasStoresException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Daily menu is associated with stores, cannot delete")

    @ExceptionHandler(DailyMenuAlreadyExistsException::class)
    fun handleAlreadyExists(e: DailyMenuAlreadyExistsException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Daily menu already exists for this date")


    //MENU ITEMS
    @ExceptionHandler(MenuItemNotFoundException::class)
    fun handleNotFound(e: MenuItemNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "MenuItem not found")

    @ExceptionHandler(MenuItemHasOrdersException::class)
    fun handleHasOrders(e: MenuItemHasOrdersException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "MenuItem has orders, cannot delete")

    @ExceptionHandler(MenuItemUsedInDailyMenuException::class)
    fun handleUsedInDailyMenu(e: MenuItemUsedInDailyMenuException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "MenuItem is used in daily menu, cannot delete")

    @ExceptionHandler(MenuItemHasRecipesException::class)
    fun handleHasRecipes(e: MenuItemHasRecipesException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "MenuItem has recipes, cannot delete")

    //NUTRIENTS
    @ExceptionHandler(NutrientNotFoundException::class)
    fun handleNutrientNotFound(e: NutrientNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Nutrient not found")
    @ExceptionHandler(NutrientHasMenuItemsException::class)
    fun handleHasMenuItems(e: NutrientHasMenuItemsException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Nutrient has menu items, cannot delete")
    @ExceptionHandler(NutrientNameExistException::class)
    fun handleNameExist(e: NutrientNameExistException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Nutrient name already exists")

    //Promotions
    @ExceptionHandler(PromotionNotFoundException::class)
    fun handleNotFound(e: PromotionNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Promotion not found")

    @ExceptionHandler(PromotionHasOrdersException::class)
    fun handleHasOrders(e: PromotionHasOrdersException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Promotion has associated orders, cannot delete")

    //TRANSACTION
    @ExceptionHandler(PaymentMethodNotFoundException::class)
    fun handleNotFound(e: PaymentMethodNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Payment method not found")

    @ExceptionHandler(PaymentMethodHasTransactionsException::class)
    fun handleHasTransactions(e: PaymentMethodHasTransactionsException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Payment method has transactions, cannot delete")
    @ExceptionHandler(PaymentMethodNameExistException::class)
    fun handleNameExist(e: PaymentMethodNameExistException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Payment Method name already exists")

    @ExceptionHandler(TransactionNotFoundException::class)
    fun handleNotFound(e: TransactionNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Transaction not found")

    //STEP
    @ExceptionHandler(StepNotFoundException::class)
    fun handleStepNotFound(e: StepNotFoundException) =
        buildError(HttpStatus.NOT_FOUND, e.message ?: "Step not found")

    @ExceptionHandler(StepHasOrderStepsException::class)
    fun handleStepHasOrders(e: StepHasOrderStepsException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Step has associated order steps, cannot delete")

    @ExceptionHandler(StepCategoryInactiveException::class)
    fun handleCategoryInactive(e: StepCategoryInactiveException) =
        buildError(HttpStatus.BAD_REQUEST, e.message ?: "Step's category is inactive, operation not allowed")

    @ExceptionHandler(StepNumberConflictException::class)
    fun handleStepNumberConflict(e: StepNumberConflictException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Step number already exists in this category")

    @ExceptionHandler(StepNameExistInCategoryException::class)
    fun handleStepNameExist(e: StepNameExistInCategoryException) =
        buildError(HttpStatus.CONFLICT, e.message ?: "Step name already exists in this category")

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
