package com.ChickenKitchen.app.handler

// ==================== Authentication Exceptions ====================
class UserAlreadyExistsException(message: String) : RuntimeException(message)

class UserNotFoundException(message: String) : RuntimeException(message)

class AuthenticationException(message: String) : RuntimeException(message)

class TokenException(message: String) : RuntimeException(message)

class AccessDeniedException(message: String) : RuntimeException(message)

class EmailSendException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

// ==================== Wallet Exceptions ====================

class WalletNotFoundException(message: String) : RuntimeException(message)

// ==================== Ingredient Exceptions ====================

class IngredientNotFoundException(message: String) : RuntimeException(message)

// =================== Nutrient Exceptions ====================

class NutrientNotFoundException(message: String) : RuntimeException(message)

class NutrientAlreadyExistsException(message: String) : RuntimeException(message)

// =================== Recipe Exceptions ====================


// =================== Cooking Method Exceptions ====================
class CookingMethodNotFoundException(message: String) : RuntimeException(message)

class CookingMethodAlreadyExistsException(message: String) : RuntimeException(message)

// =================== Cooking Effect Exceptions ====================
class CookingEffectNotFoundException(message: String) : RuntimeException(message)

class CookingEffectAlreadyExistsException(message: String) : RuntimeException(message)

// class RecipeNotFoundException(message: String) : RuntimeException(message)
// class RecipeAlreadyExistsException(message: String) : RuntimeException(message)