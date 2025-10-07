package com.ChickenKitchen.app.handler

// ==================== Authentication Exceptions ====================

class UserNotFoundException(message: String) : RuntimeException(message)

class AuthenticationException(message: String) : RuntimeException(message)

class TokenException(message: String) : RuntimeException(message)

class AccessDeniedException(message: String) : RuntimeException(message)
