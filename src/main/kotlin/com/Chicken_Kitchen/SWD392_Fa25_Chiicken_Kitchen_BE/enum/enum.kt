package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE.enum

enum class Role { USER, EMPLOYEE, ADMIN }

enum class DiscountType { PERCENT, AMOUNT }

enum class OrderStatus { PENDING, CONFIRMED, PREPARING, READY, DELIVERING, COMPLETED, CANCELED }

enum class TransactionType { CAPTURE, REFUND }

enum class TransactionStatus { PENDING, AUTHORIZED, CAPTURED, FAILED, REFUNDED }

enum class ItemType { RECIPE, CUSTOM }

enum class IngredientCategory { GRAIN, VEGETABLE, FRUIT, MEAT, SEAFOOD, SAUCE, SPICE, OTHER }

enum class UnitEnum { G, ML, PIECE }            // đơn vị hiển thị chung

enum class EffectType { INCREASE, DECREASE }     // cooking_effects

enum class PaymentMethodType { CREDIT_CARD, DEBIT_CARD, PAYPAL, CASH_ON_DELIVERY, BANK_TRANSFER }

enum class RecipeCategory { CHICKEN, BEEF, PORK, SEAFOOD, VEGETARIAN, SNACKS, BEVERAGES }