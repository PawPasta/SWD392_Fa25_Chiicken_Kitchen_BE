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

// class RecipeNotFoundException(message: String) : RuntimeException(message)
// class RecipeAlreadyExistsException(message: String) : RuntimeException(message)