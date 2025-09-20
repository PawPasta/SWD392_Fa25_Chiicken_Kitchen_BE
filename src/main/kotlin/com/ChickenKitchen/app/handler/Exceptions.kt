package com.ChickenKitchen.app.handler

// ==================== Authentication Exceptions ====================
class UserAlreadyExistsException(message: String) : RuntimeException(message)

class UserNotFoundException(message: String) : RuntimeException(message)

class AuthenticationException(message: String) : RuntimeException(message)

class TokenException(message: String) : RuntimeException(message)

class AccessDeniedException(message: String) : RuntimeException(message)

class EmailSendException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

// ==================== User Exceptions ====================

