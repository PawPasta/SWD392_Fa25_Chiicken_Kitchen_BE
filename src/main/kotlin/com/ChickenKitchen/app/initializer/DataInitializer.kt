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
    ) = CommandLineRunner {

        // USERS
        if (userRepository.count() == 0L) {
            println("Seeding users...")
            userRepository.save(
                User(
                    role = Role.ADMIN,
                    uid = "admin-uid-001",
                    email = "admin@chickenkitchen.com",
                    isVerified = true,
                    phone = "0901234567",
                    isActive = true,
                    fullName = "Admin Nguyen",
                    provider = "Local",
                    imageURL = null
                )
            )
            userRepository.save(
                User(
                    role = Role.MANAGER,
                    uid = "manager-uid-001",
                    email = "khiem1371@gmail.com",
                    isVerified = true,
                    phone = "0901234568",
                    isActive = true,
                    fullName = "Manager Tran",
                    provider = "Local",
                    imageURL = null
                )
            )
            userRepository.save(
                User(
                    role = Role.EMPLOYEE,
                    uid = "employee-uid-001",
                    email = "employee1@chickenkitchen.com",
                    isVerified = true,
                    phone = "0901234569",
                    isActive = true,
                    fullName = "Employee Le",
                    provider = "Local",
                    imageURL = null
                )
            )
            userRepository.save(
                User(
                    role = Role.EMPLOYEE,
                    uid = "employee-uid-002",
                    email = "employee2@chickenkitchen.com",
                    isVerified = true,
                    phone = "0901234570",
                    isActive = true,
                    fullName = "Employee Pham",
                    provider = "Local",
                    imageURL = null
                )
            )
            userRepository.save(
                User(
                    role = Role.STORE,
                    uid = "store-uid-002",
                    email = "store@chickenkitchen.com",
                    isVerified = true,
                    phone = "0901234570",
                    isActive = true,
                    fullName = "Store",
                    provider = "Local",
                    imageURL = null
                )
            )
            println("✓ Users seeded")
        } else {
            println("⏭ Users table not empty, skipping")
        }

        // STORES
        if (storeRepository.count() == 0L) {
            println("Seeding stores...")
            storeRepository.save(
                Store(
                    name = "Chicken Kitchen District 1",
                    address = "123 Nguyen Hue, District 1, HCMC",
                    phone = "0281234567",
                    isActive = true,
                )
            )
            storeRepository.save(
                Store(
                    name = "Chicken Kitchen District 3",
                    address = "456 Vo Van Tan, District 3, HCMC",
                    phone = "0281234568",
                    isActive = true
                )
            )
            println("✓ Stores seeded")
        } else {
            println("⏭ Stores table not empty, skipping")
        }

        // PAYMENT METHODS
        if (paymentMethodRepository.count() == 0L) {
            println("Seeding payment methods...")
            paymentMethodRepository.save(
                PaymentMethod(
                    name = "Cash",
                    description = "Pay with cash on pickup",
                    isActive = true
                )
            )
            paymentMethodRepository.save(
                PaymentMethod(
                    name = "MoMo",
                    description = "Pay with MoMo e-wallet",
                    isActive = true
                )
            )
            paymentMethodRepository.save(
                PaymentMethod(
                    name = "VNPay",
                    description = "Pay with VNPay",
                    isActive = true
                )
            )
            println("✓ Payment methods seeded")
        } else {
            println("⏭ Payment methods table not empty, skipping")
        }

        // PROMOTIONS
        if (promotionRepository.count() == 0L) {
            println("Seeding promotions...")
            promotionRepository.save(
                Promotion(
                    discountType = DiscountType.PERCENT,
                    discountValue = 20,
                    startDate = LocalDateTime.now().minusDays(5),
                    endDate = LocalDateTime.now().plusDays(25),
                    isActive = true,
                    quantity = 100
                )
            )
            promotionRepository.save(
                Promotion(
                    discountType = DiscountType.AMOUNT,
                    discountValue = 50000,
                    startDate = LocalDateTime.now().minusDays(3),
                    endDate = LocalDateTime.now().plusDays(10),
                    isActive = true,
                    quantity = 50
                )
            )
            println("✓ Promotions seeded")
        } else {
            println("⏭ Promotions table not empty, skipping")
        }

        // CATEGORIES
        if (categoryRepository.count() == 0L) {
            println("Seeding categories...")
            categoryRepository.save(Category(name = "Carbohydrates", description = "Base carb selection"))
            categoryRepository.save(Category(name = "Proteins", description = "Protein selection"))
            categoryRepository.save(Category(name = "Vegetables", description = "Vegetable selection"))
            println("✓ Categories seeded")
        } else {
            println("⏭ Categories table not empty, skipping")
        }

        // STEPS
        if (stepRepository.count() == 0L) {
            println("Seeding steps...")
            val carbCategory = categoryRepository.findByName("Carbohydrates")
                ?: categoryRepository.findAll().firstOrNull()
            val proteinCategory = categoryRepository.findByName("Proteins")
                ?: categoryRepository.findAll().getOrNull(1) ?: carbCategory
            val vegetableCategory = categoryRepository.findByName("Vegetables")
                ?: categoryRepository.findAll().getOrNull(2) ?: carbCategory

            if (carbCategory != null && proteinCategory != null && vegetableCategory != null) {
                stepRepository.save(
                    Step(
                        category = carbCategory,
                        name = "Choose Your Base",
                        description = "Select your carbohydrate base"
                    )
                )
                stepRepository.save(
                    Step(
                        category = proteinCategory,
                        name = "Choose Your Protein",
                        description = "Select your protein"
                    )
                )
                stepRepository.save(
                    Step(
                        category = vegetableCategory,
                        name = "Choose Your Vegetables",
                        description = "Select your vegetables"
                    )
                )
                println("✓ Steps seeded")
            } else {
                println("⚠️ Cannot seed steps: categories missing")
            }
        } else {
            println("⏭ Steps table not empty, skipping")
        }

        // MENU ITEMS
        if (menuItemRepository.count() == 0L) {
            println("Seeding menu items...")
            menuItemRepository.save(MenuItem(name = "White Rice", category = MenuCategory.CARB, isActive = true))
            menuItemRepository.save(MenuItem(name = "Brown Rice", category = MenuCategory.CARB, isActive = true))
            menuItemRepository.save(MenuItem(name = "Grilled Chicken", category = MenuCategory.PROTEIN, isActive = true))
            menuItemRepository.save(MenuItem(name = "Fried Chicken", category = MenuCategory.PROTEIN, isActive = true))
            menuItemRepository.save(MenuItem(name = "Broccoli", category = MenuCategory.VEGETABLE, isActive = true))
            menuItemRepository.save(MenuItem(name = "Carrot", category = MenuCategory.VEGETABLE, isActive = true))
            menuItemRepository.save(MenuItem(name = "Teriyaki Sauce", category = MenuCategory.SAUCE, isActive = true))
            println("✓ Menu items seeded")
        } else {
            println("⏭ Menu items table not empty, skipping")
        }

        // STEP ITEMS (Link menu items to steps)
        if (stepItemRepository.count() == 0L) {
            println("Seeding step items...")
            val steps = stepRepository.findAll()
            val step1 = steps.find { it.name == "Choose Your Base" } ?: steps.firstOrNull()
            val step2 = steps.find { it.name == "Choose Your Protein" } ?: steps.getOrNull(1)
            val step3 = steps.find { it.name == "Choose Your Vegetables" } ?: steps.getOrNull(2)

            val items = menuItemRepository.findAll()
            val rice = items.find { it.name == "White Rice" }
            val brownRice = items.find { it.name == "Brown Rice" }
            val grilledChicken = items.find { it.name == "Grilled Chicken" }
            val friedChicken = items.find { it.name == "Fried Chicken" }
            val broccoli = items.find { it.name == "Broccoli" }
            val carrot = items.find { it.name == "Carrot" }

            val newStepItems = mutableListOf<StepItem>()
            if (step1 != null) {
                if (rice != null) newStepItems.add(StepItem(step = step1, menuItem = rice))
                if (brownRice != null) newStepItems.add(StepItem(step = step1, menuItem = brownRice))
            }
            if (step2 != null) {
                if (grilledChicken != null) newStepItems.add(StepItem(step = step2, menuItem = grilledChicken))
                if (friedChicken != null) newStepItems.add(StepItem(step = step2, menuItem = friedChicken))
            }
            if (step3 != null) {
                if (broccoli != null) newStepItems.add(StepItem(step = step3, menuItem = broccoli))
                if (carrot != null) newStepItems.add(StepItem(step = step3, menuItem = carrot))
            }

            if (newStepItems.isNotEmpty()) {
                stepItemRepository.saveAll(newStepItems)
                println("✓ Step items seeded")
            } else {
                println("⚠️ Skipped seeding step items: missing steps or items")
            }
        } else {
            println("⏭ Step items table not empty, skipping")
        }

        // NUTRIENTS
        if (nutrientRepository.count() == 0L) {
            println("Seeding nutrients...")
            nutrientRepository.save(Nutrient(name = "Protein", baseUnit = UnitType.G))
            nutrientRepository.save(Nutrient(name = "Carbohydrates", baseUnit = UnitType.G))
            nutrientRepository.save(Nutrient(name = "Fat", baseUnit = UnitType.G))
            nutrientRepository.save(Nutrient(name = "Fiber", baseUnit = UnitType.G))
            println("✓ Nutrients seeded")
        } else {
            println("⏭ Nutrients table not empty, skipping")
        }

        // MENU ITEM NUTRIENTS
        if (menuItemNutrientRepository.count() == 0L) {
            println("Seeding menu item nutrients...")
            val items = menuItemRepository.findAll()
            val nutrients = nutrientRepository.findAll()
            val rice = items.find { it.name == "White Rice" }
            val grilledChicken = items.find { it.name == "Grilled Chicken" }
            val broccoli = items.find { it.name == "Broccoli" }
            val protein = nutrients.find { it.name == "Protein" } ?: nutrientRepository.findByName("Protein")
            val carbs = nutrients.find { it.name == "Carbohydrates" } ?: nutrientRepository.findByName("Carbohydrates")
            val fat = nutrients.find { it.name == "Fat" } ?: nutrientRepository.findByName("Fat")
            val fiber = nutrients.find { it.name == "Fiber" } ?: nutrientRepository.findByName("Fiber")

            val newLinks = mutableListOf<MenuItemNutrient>()
            if (rice != null && carbs != null) newLinks.add(MenuItemNutrient(menuItem = rice, nutrient = carbs, quantity = BigDecimal("45.0")))
            if (rice != null && protein != null) newLinks.add(MenuItemNutrient(menuItem = rice, nutrient = protein, quantity = BigDecimal("4.0")))
            if (rice != null && fat != null) newLinks.add(MenuItemNutrient(menuItem = rice, nutrient = fat, quantity = BigDecimal("0.5")))

            if (grilledChicken != null && protein != null) newLinks.add(MenuItemNutrient(menuItem = grilledChicken, nutrient = protein, quantity = BigDecimal("31.0")))
            if (grilledChicken != null && fat != null) newLinks.add(MenuItemNutrient(menuItem = grilledChicken, nutrient = fat, quantity = BigDecimal("3.6")))
            if (grilledChicken != null && carbs != null) newLinks.add(MenuItemNutrient(menuItem = grilledChicken, nutrient = carbs, quantity = BigDecimal("0.0")))

            if (broccoli != null && carbs != null) newLinks.add(MenuItemNutrient(menuItem = broccoli, nutrient = carbs, quantity = BigDecimal("7.0")))
            if (broccoli != null && protein != null) newLinks.add(MenuItemNutrient(menuItem = broccoli, nutrient = protein, quantity = BigDecimal("2.8")))
            if (broccoli != null && fiber != null) newLinks.add(MenuItemNutrient(menuItem = broccoli, nutrient = fiber, quantity = BigDecimal("2.6")))

            if (newLinks.isNotEmpty()) {
                menuItemNutrientRepository.saveAll(newLinks)
                println("✓ Menu item nutrients seeded")
            } else {
                println("⚠️ Skipped seeding menu item nutrients: missing items or nutrients")
            }
        } else {
            println("⏭ Menu item nutrients table not empty, skipping")
        }

        // INGREDIENTS
        if (ingredientRepository.count() == 0L) {
            println("Seeding ingredients...")
            ingredientRepository.save(
                Ingredient(
                    name = "Chicken Breast",
                    baseUnit = UnitType.G,
                    imageUrl = "https://example.com/chicken-breast.jpg",
                    isActive = true,
                    batchNumber = "CB-2024-001"
                )
            )
            ingredientRepository.save(
                Ingredient(
                    name = "White Rice",
                    baseUnit = UnitType.G,
                    imageUrl = "https://example.com/white-rice.jpg",
                    isActive = true,
                    batchNumber = "WR-2024-001"
                )
            )
            ingredientRepository.save(
                Ingredient(
                    name = "Fresh Broccoli",
                    baseUnit = UnitType.G,
                    imageUrl = "https://example.com/broccoli.jpg",
                    isActive = true,
                    batchNumber = "BR-2024-001"
                )
            )
            println("✓ Ingredients seeded")
        } else {
            println("⏭ Ingredients table not empty, skipping")
        }

        // RECIPES (Link ingredients to menu items)
        if (recipeRepository.count() == 0L) {
            println("Seeding recipes...")
            val items = menuItemRepository.findAll()
            val grilledChicken = items.find { it.name == "Grilled Chicken" }
            val friedChicken = items.find { it.name == "Fried Chicken" }
            val rice = items.find { it.name == "White Rice" }
            val broccoli = items.find { it.name == "Broccoli" }

            val chickenBreast = ingredientRepository.findByName("Chicken Breast")
            val riceIngredient = ingredientRepository.findByName("White Rice")
            val broccoliIngredient = ingredientRepository.findByName("Fresh Broccoli")

            val newRecipes = mutableListOf<Recipe>()
            if (chickenBreast != null && grilledChicken != null) newRecipes.add(Recipe(ingredient = chickenBreast, menuItem = grilledChicken))
            if (chickenBreast != null && friedChicken != null) newRecipes.add(Recipe(ingredient = chickenBreast, menuItem = friedChicken))
            if (riceIngredient != null && rice != null) newRecipes.add(Recipe(ingredient = riceIngredient, menuItem = rice))
            if (broccoliIngredient != null && broccoli != null) newRecipes.add(Recipe(ingredient = broccoliIngredient, menuItem = broccoli))

            if (newRecipes.isNotEmpty()) {
                recipeRepository.saveAll(newRecipes)
                println("✓ Recipes seeded")
            } else {
                println("⚠️ Skipped seeding recipes: missing ingredients or items")
            }
        } else {
            println("⏭ Recipes table not empty, skipping")
        }

        // DAILY MENU
        if (dailyMenuRepository.count() == 0L) {
            println("Seeding daily menu...")
            val stores = storeRepository.findAll()
            val items = menuItemRepository.findAll()
            val store1 = stores.getOrNull(0)
            val store2 = stores.getOrNull(1) ?: store1
            val rice = items.find { it.name == "White Rice" }
            val brownRice = items.find { it.name == "Brown Rice" }
            val grilledChicken = items.find { it.name == "Grilled Chicken" }
            val friedChicken = items.find { it.name == "Fried Chicken" }
            val broccoli = items.find { it.name == "Broccoli" }
            val carrot = items.find { it.name == "Carrot" }
            val teriyakiSauce = items.find { it.name == "Teriyaki Sauce" }

            val date = Timestamp.valueOf("2025-10-13 12:30:00")
            val daily = mutableListOf<DailyMenu>()
            if (store1 != null) {
                if (rice != null) daily.add(DailyMenu(store = store1, menuItem = rice, menuDate = date))
                if (brownRice != null) daily.add(DailyMenu(store = store1, menuItem = brownRice, menuDate = date))
                if (grilledChicken != null) daily.add(DailyMenu(store = store1, menuItem = grilledChicken, menuDate = date))
                if (friedChicken != null) daily.add(DailyMenu(store = store1, menuItem = friedChicken, menuDate = date))
                if (broccoli != null) daily.add(DailyMenu(store = store1, menuItem = broccoli, menuDate = date))
                if (carrot != null) daily.add(DailyMenu(store = store1, menuItem = carrot, menuDate = date))
                if (teriyakiSauce != null) daily.add(DailyMenu(store = store1, menuItem = teriyakiSauce, menuDate = date))
            }
            if (store2 != null) {
                if (rice != null) daily.add(DailyMenu(store = store2, menuItem = rice, menuDate = date))
                if (grilledChicken != null) daily.add(DailyMenu(store = store2, menuItem = grilledChicken, menuDate = date))
                if (broccoli != null) daily.add(DailyMenu(store = store2, menuItem = broccoli, menuDate = date))
            }

            if (daily.isNotEmpty()) {
                dailyMenuRepository.saveAll(daily)
                println("✓ Daily menu seeded")
            } else {
                println("⚠️ Skipped seeding daily menu: missing stores or items")
            }
        } else {
            println("⏭ Daily menu table not empty, skipping")
        }
    }
}
