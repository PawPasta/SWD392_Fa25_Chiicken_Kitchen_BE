package com.ChickenKitchen.app.enums

enum class Role {
    USER, EMPLOYEE, MANAGER, ADMIN, STORE
}

enum class DiscountType {
    PERCENT, AMOUNT
}

enum class OrderStatus {
    NEW, CONFIRMED, PROCESSING, COMPLETED, CANCELLED
}

enum class TransactionStatus {
    PENDING, SUCCESS, REFUND, FAILED
}

enum class PaymentStatus {
    PENDING, FINISHED, CANCELLED
}

enum class MenuCategory {
    CARB, PROTEIN, VEGETABLE, SAUCE, DAIRY, FRUIT
}

enum class UnitType {
    G, ML
}
