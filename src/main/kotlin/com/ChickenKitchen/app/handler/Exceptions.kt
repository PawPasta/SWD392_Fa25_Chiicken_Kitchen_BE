package com.ChickenKitchen.app.handler

// ==================== Authentication Exceptions ====================
class UserNotFoundException(message: String) : RuntimeException(message)
class AuthenticationException(message: String) : RuntimeException(message)
class TokenException(message: String) : RuntimeException(message)

//USERS
class UserEmailRequiredException(message: String) : RuntimeException(message)
class UserCannotDeleteException(message: String) : RuntimeException(message)


// ========== Business Exceptions ==========

//CATEGORY
class CategoryNotFoundException(message: String) : RuntimeException(message)
class CategoryHasMenuItemsException(message: String) : RuntimeException(message)
class CategoryNameExistException(message: String) : RuntimeException(message)

//INGREDIENT
class IngredientNotFoundException(message: String) : RuntimeException(message)
class IngredientNameExistException(message: String) : RuntimeException(message)
class IngredientHasBatchesException(message: String) : RuntimeException(message)

//STORE
class StoreNotFoundException(message: String) : RuntimeException(message)
class StoreHasOrdersException(message: String) : RuntimeException(message)
class StoreUsedInMenuException(message: String) : RuntimeException(message)
class StoreHasIngredientsException(message: String) : RuntimeException(message)
class StoreNameExistException(message: String) : RuntimeException(message)
class StoreAddressExistException(message: String) : RuntimeException(message)

//DAILY MENU
class DailyMenuNotFoundException(message: String) : RuntimeException(message)
class DailyMenuHasStoresException(message: String) : RuntimeException(message)
class DailyMenuAlreadyExistsException(message: String) : RuntimeException(message)


//MENU ITEMS
class MenuItemNotFoundException(message: String) : RuntimeException(message)
class MenuItemHasOrdersException(message: String) : RuntimeException(message)
class MenuItemUsedInDailyMenuException(message: String) : RuntimeException(message)
class MenuItemHasRecipesException(message: String) : RuntimeException(message)

//NUTRIENTS
class NutrientNotFoundException(message: String) : RuntimeException(message)
class NutrientHasMenuItemsException(message: String) : RuntimeException(message)
class NutrientNameExistException(message: String) : RuntimeException(message)


//PROMOTIONS
class PromotionNotFoundException(message: String) : RuntimeException(message)
class PromotionHasOrdersException(message: String) : RuntimeException(message)

//TRANSACTION
class PaymentMethodNotFoundException(message: String) : RuntimeException(message)
class PaymentMethodHasTransactionsException(message: String) : RuntimeException(message)
class PaymentMethodNameExistException(message: String) : RuntimeException(message)
class TransactionNotFoundException(message: String) : RuntimeException(message)

//STEP
class StepNotFoundException(message: String) : RuntimeException(message)
class StepHasOrderStepsException(message: String) : RuntimeException(message)
class StepCategoryInactiveException(message: String) : RuntimeException(message)
class StepNumberConflictException(message: String) : RuntimeException(message)
class StepNameExistInCategoryException(message: String) : RuntimeException(message)

// ORDER

class OrderNotFoundException(message: String) : RuntimeException(message)
class InvalidOrderStatusException(message: String) : RuntimeException(message)
class EmptyOrderException(message: String) : RuntimeException(message)
class OrderAlreadyCompletedException(message: String) : RuntimeException(message)
class OrderAlreadyCancelledException(message: String) : RuntimeException(message)
class AddDishFailedException(message: String) : RuntimeException(message)
class InvalidOrderStepException(message: String) : RuntimeException(message)
class DishNotFoundException(message: String) : RuntimeException(message)
class UpdateDishFailedException(message: String) : RuntimeException(message)
class DeleteDishFailedException(message: String) : RuntimeException(message)
class CurrentOrderNotFoundException(message: String) : RuntimeException(message)
class DailyMenuUnavailableException(message: String) : RuntimeException(message)