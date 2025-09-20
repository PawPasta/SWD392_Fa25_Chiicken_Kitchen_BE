package com.ChickenKitchen.app.enum

enum class Role { USER, EMPLOYEE, ADMIN }

enum class DiscountType { PERCENT, AMOUNT }

enum class OrderStatus { PENDING, CONFIRMED, PREPARING, READY, DELIVERING, COMPLETED, CANCELED }

enum class TransactionType { CAPTURE, REFUND }

enum class TransactionStatus { PENDING, AUTHORIZED, CAPTURED, FAILED, REFUNDED }

enum class ItemType { RECIPE, CUSTOM }

enum class IngredientCategory { GRAIN, VEGETABLE, FRUIT, MEAT, SEAFOOD, SAUCE, SPICE, OTHER }

enum class UnitEnum { MG, G, ML, PIECE }            // đơn vị hiển thị chung

enum class EffectType { INCREASE, DECREASE }     // cooking_effects

enum class PaymentMethodType { CREDIT_CARD, DEBIT_CARD, PAYPAL, CASH_ON_DELIVERY, BANK_TRANSFER }

enum class RecipeCategory { MAIN, APPETIZER, DESSERT, BEVERAGE, SNACK, SALAD, SOUP, SAUCE, OTHER }

enum class MailType { VERIFY_EMAIL, RESET_PASSWORD }