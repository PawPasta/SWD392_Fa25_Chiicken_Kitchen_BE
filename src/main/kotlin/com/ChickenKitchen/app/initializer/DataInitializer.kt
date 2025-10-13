package com.ChickenKitchen.app.initializer


import com.ChickenKitchen.app.enum.DiscountType
import com.ChickenKitchen.app.enum.MenuCategory
import com.ChickenKitchen.app.enum.Role
import com.ChickenKitchen.app.enum.UnitType
import com.ChickenKitchen.app.model.entity.category.Category
import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
import com.ChickenKitchen.app.model.entity.ingredient.Recipe
import com.ChickenKitchen.app.model.entity.ingredient.Store
import com.ChickenKitchen.app.model.entity.menu.DailyMenu
import com.ChickenKitchen.app.model.entity.menu.MenuItem
import com.ChickenKitchen.app.model.entity.menu.MenuItemNutrient
import com.ChickenKitchen.app.model.entity.menu.Nutrient
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod
import com.ChickenKitchen.app.model.entity.promotion.Promotion
import com.ChickenKitchen.app.model.entity.step.Step
import com.ChickenKitchen.app.model.entity.step.StepItem
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.repository.category.CategoryRepository
import com.ChickenKitchen.app.repository.ingredient.IngredientRepository
import com.ChickenKitchen.app.repository.ingredient.RecipeRepository
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.repository.menu.DailyMenuRepository
import com.ChickenKitchen.app.repository.menu.MenuItemNutrientRepository
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
import com.ChickenKitchen.app.repository.menu.NutrientRepository
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.repository.promotion.PromotionRepository
import com.ChickenKitchen.app.repository.step.StepItemRepository
import com.ChickenKitchen.app.repository.step.StepRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

@Configuration
class DataInitializer {

    @Bean
    fun initData(
        userRepository: UserRepository,
        storeRepository: StoreRepository,
        categoryRepository: CategoryRepository,
        stepRepository: StepRepository,
        menuItemRepository: MenuItemRepository,
        stepItemRepository: StepItemRepository,
        nutrientRepository: NutrientRepository,
        menuItemNutrientRepository: MenuItemNutrientRepository,
        ingredientRepository: IngredientRepository,
        recipeRepository: RecipeRepository,
        promotionRepository: PromotionRepository,
        paymentMethodRepository: PaymentMethodRepository,
        dailyMenuRepository: DailyMenuRepository,
        passwordEncoder: PasswordEncoder
    ) = CommandLineRunner {

        // Kiểm tra nếu đã có data thì không init nữa
        if (userRepository.count() > 0) {
            println("Data already initialized. Skipping...")
            return@CommandLineRunner
        }

        println("Starting data initialization...")

        // ============================================================
        // 1. USERS (Admin, Manager, 2 Employees)
        // ============================================================
        val admin = userRepository.save(
            User(
                role = Role.ADMIN,
                uid = "admin-uid-001",
                email = "admin@chickenkitchen.com",
                password = passwordEncoder.encode("Admin@123"),
                isVerified = true,
                phone = "0901234567",
                isActive = true,
                fullName = "Admin Nguyen",
                provider = "Local",
                imageURL = null
            )
        )

        val manager = userRepository.save(
            User(
                role = Role.MANAGER,
                uid = "manager-uid-001",
                email = "manager@chickenkitchen.com",
                password = passwordEncoder.encode("Manager@123"),
                isVerified = true,
                phone = "0901234568",
                isActive = true,
                fullName = "Manager Tran",
                provider = "Local",
                imageURL = null
            )
        )

        val employee1 = userRepository.save(
            User(
                role = Role.EMPLOYEE,
                uid = "employee-uid-001",
                email = "employee1@chickenkitchen.com",
                password = passwordEncoder.encode("Employee@123"),
                isVerified = true,
                phone = "0901234569",
                isActive = true,
                fullName = "Employee Le",
                provider = "Local",
                imageURL = null
            )
        )

        val employee2 = userRepository.save(
            User(
                role = Role.EMPLOYEE,
                uid = "employee-uid-002",
                email = "employee2@chickenkitchen.com",
                password = passwordEncoder.encode("Employee@123"),
                isVerified = true,
                phone = "0901234570",
                isActive = true,
                fullName = "Employee Pham",
                provider = "Local",
                imageURL = null
            )
        )

        val store = userRepository.save(
            User(
                role = Role.STORE,
                uid = "store-uid-002",
                email = "store@chickenkitchen.com",
                password = passwordEncoder.encode("Store@123"),
                isVerified = true,
                phone = "0901234570",
                isActive = true,
                fullName = "Store",
                provider = "Local",
                imageURL = null
            )
        )


        println("✓ Users created: Admin, Manager, 2 Employees, 1 Customer, 1 Store")

        // ============================================================
        // 2. STORES
        // ============================================================
        val store1 = storeRepository.save(
            Store(
                name = "Chicken Kitchen District 1",
                address = "123 Nguyen Hue, District 1, HCMC",
                phone = "0281234567",
                isActive = true,
            )
        )

        val store2 = storeRepository.save(
            Store(
                name = "Chicken Kitchen District 3",
                address = "456 Vo Van Tan, District 3, HCMC",
                phone = "0281234568",
                isActive = true
            )
        )

        println("✓ Stores created: 2 stores")

        // ============================================================
        // 3. PAYMENT METHODS
        // ============================================================
        val cashPayment = paymentMethodRepository.save(
            PaymentMethod(
                name = "Cash",
                description = "Pay with cash on pickup",
                isActive = true
            )
        )

        val momoPayment = paymentMethodRepository.save(
            PaymentMethod(
                name = "MoMo",
                description = "Pay with MoMo e-wallet",
                isActive = true
            )
        )

        val vnpayPayment = paymentMethodRepository.save(
            PaymentMethod(
                name = "VNPay",
                description = "Pay with VNPay",
                isActive = true
            )
        )

        println("✓ Payment methods created: Cash, MoMo, VNPay")

        // ============================================================
        // 4. PROMOTIONS
        // ============================================================
        val promotion1 = promotionRepository.save(
            Promotion(
                discountType = DiscountType.PERCENT,
                discountValue = 20,
                startDate = LocalDateTime.now().minusDays(5),
                endDate = LocalDateTime.now().plusDays(25),
                isActive = true,
                quantity = 100
            )
        )

        val promotion2 = promotionRepository.save(
            Promotion(
                discountType = DiscountType.AMOUNT,
                discountValue = 50000,
                startDate = LocalDateTime.now().minusDays(3),
                endDate = LocalDateTime.now().plusDays(10),
                isActive = true,
                quantity = 50
            )
        )

        println("✓ Promotions created: 20% discount, 50k discount")

        // ============================================================
        // 5. CATEGORIES & STEPS
        // ============================================================
        val carbCategory = categoryRepository.save(
            Category(
                name = "Carbohydrates",
                description = "Base carb selection"
            )
        )

        val proteinCategory = categoryRepository.save(
            Category(
                name = "Proteins",
                description = "Protein selection"
            )
        )

        val vegetableCategory = categoryRepository.save(
            Category(
                name = "Vegetables",
                description = "Vegetable selection"
            )
        )

        val step1 = stepRepository.save(
            Step(
                category = carbCategory,
                name = "Choose Your Base",
                description = "Select your carbohydrate base"
            )
        )

        val step2 = stepRepository.save(
            Step(
                category = proteinCategory,
                name = "Choose Your Protein",
                description = "Select your protein"
            )
        )

        val step3 = stepRepository.save(
            Step(
                category = vegetableCategory,
                name = "Choose Your Vegetables",
                description = "Select your vegetables"
            )
        )

        println("✓ Categories & Steps created")

        // ============================================================
        // 6. MENU ITEMS
        // ============================================================
        val rice = menuItemRepository.save(
            MenuItem(
                name = "White Rice",
                category = MenuCategory.CARB,
                isActive = true
            )
        )

        val brownRice = menuItemRepository.save(
            MenuItem(
                name = "Brown Rice",
                category = MenuCategory.CARB,
                isActive = true
            )
        )

        val grilledChicken = menuItemRepository.save(
            MenuItem(
                name = "Grilled Chicken",
                category = MenuCategory.PROTEIN,
                isActive = true
            )
        )

        val friedChicken = menuItemRepository.save(
            MenuItem(
                name = "Fried Chicken",
                category = MenuCategory.PROTEIN,
                isActive = true
            )
        )

        val broccoli = menuItemRepository.save(
            MenuItem(
                name = "Broccoli",
                category = MenuCategory.VEGETABLE,
                isActive = true
            )
        )

        val carrot = menuItemRepository.save(
            MenuItem(
                name = "Carrot",
                category = MenuCategory.VEGETABLE,
                isActive = true
            )
        )

        val teriyakiSauce = menuItemRepository.save(
            MenuItem(
                name = "Teriyaki Sauce",
                category = MenuCategory.SAUCE,
                isActive = true
            )
        )

        println("✓ Menu items created: Rice, Chicken, Vegetables, Sauce")

        // ============================================================
        // 7. STEP ITEMS (Link menu items to steps)
        // ============================================================
        stepItemRepository.saveAll(
            listOf(
                StepItem(step = step1, menuItem = rice),
                StepItem(step = step1, menuItem = brownRice),
                StepItem(step = step2, menuItem = grilledChicken),
                StepItem(step = step2, menuItem = friedChicken),
                StepItem(step = step3, menuItem = broccoli),
                StepItem(step = step3, menuItem = carrot)
            )
        )

        println("✓ Step items linked")

        // ============================================================
        // 8. NUTRIENTS
        // ============================================================
        val protein = nutrientRepository.save(
            Nutrient(
                name = "Protein",
                baseUnit = UnitType.G
            )
        )

        val carbs = nutrientRepository.save(
            Nutrient(
                name = "Carbohydrates",
                baseUnit = UnitType.G
            )
        )

        val fat = nutrientRepository.save(
            Nutrient(
                name = "Fat",
                baseUnit = UnitType.G
            )
        )

        val fiber = nutrientRepository.save(
            Nutrient(
                name = "Fiber",
                baseUnit = UnitType.G
            )
        )

        println("✓ Nutrients created")

        // ============================================================
        // 9. MENU ITEM NUTRIENTS
        // ============================================================
        menuItemNutrientRepository.saveAll(
            listOf(
                // White Rice
                MenuItemNutrient(menuItem = rice, nutrient = carbs, quantity = BigDecimal("45.0")),
                MenuItemNutrient(menuItem = rice, nutrient = protein, quantity = BigDecimal("4.0")),
                MenuItemNutrient(menuItem = rice, nutrient = fat, quantity = BigDecimal("0.5")),

                // Grilled Chicken
                MenuItemNutrient(menuItem = grilledChicken, nutrient = protein, quantity = BigDecimal("31.0")),
                MenuItemNutrient(menuItem = grilledChicken, nutrient = fat, quantity = BigDecimal("3.6")),
                MenuItemNutrient(menuItem = grilledChicken, nutrient = carbs, quantity = BigDecimal("0.0")),

                // Broccoli
                MenuItemNutrient(menuItem = broccoli, nutrient = carbs, quantity = BigDecimal("7.0")),
                MenuItemNutrient(menuItem = broccoli, nutrient = protein, quantity = BigDecimal("2.8")),
                MenuItemNutrient(menuItem = broccoli, nutrient = fiber, quantity = BigDecimal("2.6"))
            )
        )

        println("✓ Menu item nutrients created")

        // ============================================================
        // 10. INGREDIENTS
        // ============================================================
        val chickenBreast = ingredientRepository.save(
            Ingredient(
                name = "Chicken Breast",
                baseUnit = UnitType.G,
                imageUrl = "https://example.com/chicken-breast.jpg",
                isActive = true,
                batchNumber = "CB-2024-001"
            )
        )

        val riceIngredient = ingredientRepository.save(
            Ingredient(
                name = "White Rice",
                baseUnit = UnitType.G,
                imageUrl = "https://example.com/white-rice.jpg",
                isActive = true,
                batchNumber = "WR-2024-001"
            )
        )

        val broccoliIngredient = ingredientRepository.save(
            Ingredient(
                name = "Fresh Broccoli",
                baseUnit = UnitType.G,
                imageUrl = "https://example.com/broccoli.jpg",
                isActive = true,
                batchNumber = "BR-2024-001"
            )
        )

        println("✓ Ingredients created")

        // ============================================================
        // 11. RECIPES (Link ingredients to menu items)
        // ============================================================
        recipeRepository.saveAll(
            listOf(
                Recipe(ingredient = chickenBreast, menuItem = grilledChicken),
                Recipe(ingredient = chickenBreast, menuItem = friedChicken),
                Recipe(ingredient = riceIngredient, menuItem = rice),
                Recipe(ingredient = broccoliIngredient, menuItem = broccoli)
            )
        )

        println("✓ Recipes created")

        // ============================================================
        // 12. DAILY MENU
        // ============================================================
        dailyMenuRepository.saveAll(
            listOf(
                DailyMenu(store = store1, menuItem = rice, menuDate = Timestamp.valueOf("2025-10-13 12:30:00")),
                DailyMenu(store = store1, menuItem = brownRice, menuDate = Timestamp.valueOf("2025-10-13 12:30:00")),
                DailyMenu(
                    store = store1,
                    menuItem = grilledChicken,
                    menuDate = Timestamp.valueOf("2025-10-13 12:30:00")
                ),
                DailyMenu(store = store1, menuItem = friedChicken, menuDate = Timestamp.valueOf("2025-10-13 12:30:00")),
                DailyMenu(store = store1, menuItem = broccoli, menuDate = Timestamp.valueOf("2025-10-13 12:30:00")),
                DailyMenu(store = store1, menuItem = carrot, menuDate = Timestamp.valueOf("2025-10-13 12:30:00")),
                DailyMenu(
                    store = store1,
                    menuItem = teriyakiSauce,
                    menuDate = Timestamp.valueOf("2025-10-13 12:30:00")
                ),

                DailyMenu(store = store2, menuItem = rice, menuDate = Timestamp.valueOf("2025-10-13 12:30:00")),
                DailyMenu(
                    store = store2,
                    menuItem = grilledChicken,
                    menuDate = Timestamp.valueOf("2025-10-13 12:30:00")
                ),
                DailyMenu(store = store2, menuItem = broccoli, menuDate = Timestamp.valueOf("2025-10-13 12:30:00"))
            )
        )
    }
}