package com.ChickenKitchen.app.initializer


import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.model.entity.user.UserAddress
import com.ChickenKitchen.app.model.entity.user.Wallet
import com.ChickenKitchen.app.model.entity.ingredient.Nutrient
import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
import com.ChickenKitchen.app.model.entity.ingredient.IngredientNutrient
import com.ChickenKitchen.app.model.entity.recipe.Recipe
import com.ChickenKitchen.app.model.entity.recipe.RecipeIngredient
import com.ChickenKitchen.app.model.entity.category.Category
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.repository.user.UserAddressRepository
import com.ChickenKitchen.app.repository.user.WalletRepository
import com.ChickenKitchen.app.repository.ingredient.NutrientRepository
import com.ChickenKitchen.app.repository.ingredient.IngredientRepository
import com.ChickenKitchen.app.repository.ingredient.IngredientNutrientRepository
import com.ChickenKitchen.app.repository.recipe.RecipeRepository
import com.ChickenKitchen.app.repository.recipe.RecipeIngredientRepository
import com.ChickenKitchen.app.repository.category.CategoryRepository
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.repository.combo.ComboRepository
import com.ChickenKitchen.app.repository.combo.ComboItemRepository
import com.ChickenKitchen.app.repository.promotion.PromotionRepository
import com.ChickenKitchen.app.repository.menu.DailyMenuRepository
import com.ChickenKitchen.app.repository.menu.DailyMenuItemRepository
import com.ChickenKitchen.app.enum.Role
import com.ChickenKitchen.app.enum.UnitEnum
import com.ChickenKitchen.app.enum.RecipeCategory
import com.ChickenKitchen.app.enum.PaymentMethodType
import com.ChickenKitchen.app.enum.DiscountType
import java.time.LocalDate
import java.time.LocalDateTime
import java.math.BigDecimal
import java.math.RoundingMode
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import com.ChickenKitchen.app.model.entity.combo.Combo
import com.ChickenKitchen.app.model.entity.combo.ComboItem
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod
import com.ChickenKitchen.app.model.entity.promotion.Promotion
import com.ChickenKitchen.app.model.entity.menu.DailyMenu
import com.ChickenKitchen.app.model.entity.menu.DailyMenuItem
import com.ChickenKitchen.app.enum.MenuType
import java.sql.Timestamp
import java.sql.Date

@Configuration
class DataInitializer {

    @Bean
    fun initData(
        userRepository: UserRepository,
        userAddressRepository: UserAddressRepository,
        walletRepository: WalletRepository,
        nutrientRepository: NutrientRepository,
        categoryRepository: CategoryRepository,
        ingredientRepository: IngredientRepository,
        recipeRepository: RecipeRepository,
        ingredientNutrientRepository: IngredientNutrientRepository,
        transactionManager: PlatformTransactionManager,
        paymentMethodRepository: PaymentMethodRepository,
        comboRepository: ComboRepository,
        promotionRepository: PromotionRepository,
        dailyMenuRepository: DailyMenuRepository,
        passwordEncoder: PasswordEncoder
    ) = CommandLineRunner {

        println("⏳ Checking existing data...")

        // ====== ADMIN ======

        if (!userRepository.existsByUsername("admin")) {
            val admin = User(
                username = "admin",
                email = "admin@example.com",
                password = passwordEncoder.encode("123456"),
                role = Role.ADMIN,
                isVerify = true,
                firstName = "System",
                lastName = "Admin",
                birthday = LocalDate.of(1990, 1, 1)
            )
            userRepository.save(admin)

            val adminAddress = UserAddress(
                recipientName = "Admin Example",
                phone = "0123456789",
                addressLine = "123 Main St",
                city = "Hanoi",
                isDefault = true,
                user = admin
            )
            userAddressRepository.save(adminAddress)

            admin.wallet = Wallet(
                balance = BigDecimal(1_000_000),
                user = admin
            )
            walletRepository.save(admin.wallet!!)
            println("👤 Admin user created")
        }

        // ====== SAMPLE USERS ======

        if (userRepository.count() < 2L) {
            val sampleUsers = listOf(
                Triple("user1", Role.USER, "User One"),
                Triple("user2", Role.USER, "User Two"),
                Triple("user3", Role.USER, "User Three"),
                Triple("user4", Role.USER, "User Four"),
                Triple("user5", Role.USER, "User Five"),
                Triple("user6", Role.USER, "User Six"),
                Triple("employee1", Role.EMPLOYEE, "Emp One"),
                Triple("employee2", Role.EMPLOYEE, "Emp Two"),
                Triple("employee3", Role.EMPLOYEE, "Emp Three"),
                Triple("employee4", Role.EMPLOYEE, "Emp Four")
            )
    
            sampleUsers.forEach { (username, role, fullname) ->
                if (!userRepository.existsByUsername(username)) {
                    val (firstName, lastName) = fullname.split(" ", limit = 2)
    
                    val user = User(
                        username = username,
                        email = "$username@example.com",
                        password = passwordEncoder.encode("123456"),
                        role = role,
                        isVerify = true,
                        firstName = firstName,
                        lastName = lastName,
                        birthday = LocalDate.of(1995, 5, 15)
                    )
                    userRepository.save(user)
    
                    val address = UserAddress(
                        recipientName = fullname,
                        phone = "0987654321",
                        addressLine = "456 Example St",
                        city = "HCM",
                        isDefault = true,
                        user = user
                    )
                    userAddressRepository.save(address)
    
                    user.wallet = Wallet(
                        balance = BigDecimal(500_000),
                        user = user
                    )
                    walletRepository.save(user.wallet!!)
    
                    println("👤 Sample user created: $username ($role)")
                }
            }
    
            println("✅ Inserted 10 sample users")    
        }

        
        // ===== NUTRIENTS =====

        if (nutrientRepository.count() < 1L) {
            val nutrients = listOf(
                Nutrient(name = "Protein", baseUnit = UnitEnum.G),
                Nutrient(name = "Carbohydrate", baseUnit = UnitEnum.G),
                Nutrient(name = "Fat", baseUnit = UnitEnum.G),
                Nutrient(name = "Fiber", baseUnit = UnitEnum.G),
                Nutrient(name = "Sugar", baseUnit = UnitEnum.G),
                Nutrient(name = "Sodium", baseUnit = UnitEnum.MG),
                Nutrient(name = "Potassium", baseUnit = UnitEnum.MG),
                Nutrient(name = "Calcium", baseUnit = UnitEnum.MG),
                Nutrient(name = "Iron", baseUnit = UnitEnum.MG),
                Nutrient(name = "Vitamin A", baseUnit = UnitEnum.MG),
                Nutrient(name = "Vitamin B1", baseUnit = UnitEnum.MG),
                Nutrient(name = "Vitamin B2", baseUnit = UnitEnum.MG),
                Nutrient(name = "Vitamin B6", baseUnit = UnitEnum.MG),
                Nutrient(name = "Vitamin B12", baseUnit = UnitEnum.MG),
                Nutrient(name = "Vitamin C", baseUnit = UnitEnum.MG),
                Nutrient(name = "Vitamin D", baseUnit = UnitEnum.MG),
                Nutrient(name = "Vitamin E", baseUnit = UnitEnum.MG),
                Nutrient(name = "Vitamin K", baseUnit = UnitEnum.MG),
                Nutrient(name = "Magnesium", baseUnit = UnitEnum.MG),
                Nutrient(name = "Zinc", baseUnit = UnitEnum.MG),
                Nutrient(name = "Cholesterol", baseUnit = UnitEnum.MG),
            )
            val savedNutrients = nutrientRepository.saveAll(nutrients)
            val nutMap = savedNutrients.associateBy { it.name }
            println("✅ Inserted ${savedNutrients.size} nutrients")
        }
    
        // ===== CATEGORIES =====
        if (categoryRepository.count() == 0L) {
            val categoryNames = listOf(
                "Grains", "Vegetables", "Fruits", "Meats", "Seafood",
                "Sauces", "Spices", "Dairy", "Baked", "Other"
            )
            val categories = categoryNames.map { name -> Category(name = name, description = "$name category") }
            categoryRepository.saveAll(categories)
            println("✅ Inserted ${categories.size} categories")
        }

        // Helper maps
        val categoryByName = categoryRepository.findAll().associateBy { it.name }

        // ===== INGREDIENTS (> 50) =====
        if (ingredientRepository.count() < 50L) {
            data class IngredientDef(
                val name: String,
                val unit: UnitEnum,
                val baseQty: Int,
                val price: BigDecimal,
                val cal: Int,
                val category: String,
                val image: String? = null
            )

            val ingredientDefs = listOf(
                // Grains
                IngredientDef("Rice", UnitEnum.G, 100, BigDecimal("0.50"), 130, "Grains"),
                IngredientDef("Brown Rice", UnitEnum.G, 100, BigDecimal("0.60"), 111, "Grains"),
                IngredientDef("Quinoa", UnitEnum.G, 100, BigDecimal("1.20"), 120, "Grains"),
                IngredientDef("Pasta", UnitEnum.G, 100, BigDecimal("0.70"), 131, "Grains"),
                IngredientDef("Bread", UnitEnum.PIECE, 1, BigDecimal("0.30"), 79, "Grains"),
                IngredientDef("Noodles", UnitEnum.G, 100, BigDecimal("0.65"), 138, "Grains"),
                IngredientDef("Oats", UnitEnum.G, 100, BigDecimal("0.80"), 389, "Grains"),
                IngredientDef("Couscous", UnitEnum.G, 100, BigDecimal("0.90"), 112, "Grains"),

                // Vegetables
                IngredientDef("Lettuce", UnitEnum.G, 100, BigDecimal("0.40"), 15, "Vegetables"),
                IngredientDef("Tomato", UnitEnum.G, 100, BigDecimal("0.50"), 18, "Vegetables"),
                IngredientDef("Cucumber", UnitEnum.G, 100, BigDecimal("0.45"), 16, "Vegetables"),
                IngredientDef("Bell Pepper", UnitEnum.G, 100, BigDecimal("0.70"), 26, "Vegetables"),
                IngredientDef("Onion", UnitEnum.G, 100, BigDecimal("0.30"), 40, "Vegetables"),
                IngredientDef("Garlic", UnitEnum.G, 100, BigDecimal("1.50"), 149, "Vegetables"),
                IngredientDef("Carrot", UnitEnum.G, 100, BigDecimal("0.35"), 41, "Vegetables"),
                IngredientDef("Broccoli", UnitEnum.G, 100, BigDecimal("0.90"), 34, "Vegetables"),
                IngredientDef("Spinach", UnitEnum.G, 100, BigDecimal("0.85"), 23, "Vegetables"),
                IngredientDef("Kale", UnitEnum.G, 100, BigDecimal("1.10"), 49, "Vegetables"),
                IngredientDef("Corn", UnitEnum.G, 100, BigDecimal("0.50"), 96, "Vegetables"),
                IngredientDef("Peas", UnitEnum.G, 100, BigDecimal("0.75"), 81, "Vegetables"),
                IngredientDef("Mushrooms", UnitEnum.G, 100, BigDecimal("1.20"), 22, "Vegetables"),
                IngredientDef("Zucchini", UnitEnum.G, 100, BigDecimal("0.60"), 17, "Vegetables"),
                IngredientDef("Eggplant", UnitEnum.G, 100, BigDecimal("0.65"), 25, "Vegetables"),
                IngredientDef("Parsley", UnitEnum.G, 100, BigDecimal("1.20"), 36, "Spices"),

                // Fruits
                IngredientDef("Apple", UnitEnum.G, 100, BigDecimal("0.60"), 52, "Fruits"),
                IngredientDef("Banana", UnitEnum.G, 100, BigDecimal("0.55"), 96, "Fruits"),
                IngredientDef("Orange", UnitEnum.G, 100, BigDecimal("0.65"), 47, "Fruits"),
                IngredientDef("Pineapple", UnitEnum.G, 100, BigDecimal("0.70"), 50, "Fruits"),
                IngredientDef("Mango", UnitEnum.G, 100, BigDecimal("0.90"), 60, "Fruits"),
                IngredientDef("Lemon", UnitEnum.G, 100, BigDecimal("0.50"), 29, "Fruits"),
                IngredientDef("Lime", UnitEnum.G, 100, BigDecimal("0.50"), 30, "Fruits"),
                IngredientDef("Strawberry", UnitEnum.G, 100, BigDecimal("1.50"), 33, "Fruits"),
                IngredientDef("Blueberry", UnitEnum.G, 100, BigDecimal("2.00"), 57, "Fruits"),
                IngredientDef("Avocado", UnitEnum.G, 100, BigDecimal("1.80"), 160, "Fruits"),

                // Meats
                IngredientDef("Chicken Breast", UnitEnum.G, 100, BigDecimal("1.50"), 165, "Meats"),
                IngredientDef("Chicken Thigh", UnitEnum.G, 100, BigDecimal("1.30"), 209, "Meats"),
                IngredientDef("Beef", UnitEnum.G, 100, BigDecimal("2.50"), 250, "Meats"),
                IngredientDef("Pork", UnitEnum.G, 100, BigDecimal("2.00"), 242, "Meats"),
                IngredientDef("Bacon", UnitEnum.G, 100, BigDecimal("3.00"), 541, "Meats"),
                IngredientDef("Ham", UnitEnum.G, 100, BigDecimal("1.80"), 145, "Meats"),
                IngredientDef("Sausage", UnitEnum.G, 100, BigDecimal("1.60"), 301, "Meats"),

                // Seafood
                IngredientDef("Shrimp", UnitEnum.G, 100, BigDecimal("2.80"), 99, "Seafood"),
                IngredientDef("Salmon", UnitEnum.G, 100, BigDecimal("3.50"), 208, "Seafood"),
                IngredientDef("Tuna", UnitEnum.G, 100, BigDecimal("2.90"), 132, "Seafood"),
                IngredientDef("Squid", UnitEnum.G, 100, BigDecimal("2.40"), 92, "Seafood"),

                // Sauces
                IngredientDef("Ketchup", UnitEnum.ML, 100, BigDecimal("0.80"), 112, "Sauces"),
                IngredientDef("Mayonnaise", UnitEnum.ML, 100, BigDecimal("1.20"), 680, "Sauces"),
                IngredientDef("Mustard", UnitEnum.ML, 100, BigDecimal("0.90"), 66, "Sauces"),
                IngredientDef("BBQ Sauce", UnitEnum.ML, 100, BigDecimal("1.10"), 220, "Sauces"),
                IngredientDef("Soy Sauce", UnitEnum.ML, 100, BigDecimal("0.70"), 53, "Sauces"),
                IngredientDef("Hot Sauce", UnitEnum.ML, 100, BigDecimal("1.00"), 45, "Sauces"),
                IngredientDef("Fish Sauce", UnitEnum.ML, 100, BigDecimal("0.85"), 60, "Sauces"),
                IngredientDef("Teriyaki Sauce", UnitEnum.ML, 100, BigDecimal("1.30"), 89, "Sauces"),
                IngredientDef("Olive Oil", UnitEnum.ML, 100, BigDecimal("1.50"), 884, "Sauces"),
                IngredientDef("Honey", UnitEnum.ML, 100, BigDecimal("1.80"), 304, "Sauces"),

                // Spices
                IngredientDef("Salt", UnitEnum.G, 100, BigDecimal("0.20"), 0, "Spices"),
                IngredientDef("Pepper", UnitEnum.G, 100, BigDecimal("1.50"), 251, "Spices"),
                IngredientDef("Chili Powder", UnitEnum.G, 100, BigDecimal("1.40"), 282, "Spices"),
                IngredientDef("Paprika", UnitEnum.G, 100, BigDecimal("1.40"), 282, "Spices"),
                IngredientDef("Oregano", UnitEnum.G, 100, BigDecimal("1.60"), 265, "Spices"),
                IngredientDef("Basil", UnitEnum.G, 100, BigDecimal("1.60"), 251, "Spices"),
                IngredientDef("Cumin", UnitEnum.G, 100, BigDecimal("1.50"), 375, "Spices"),
                IngredientDef("Turmeric", UnitEnum.G, 100, BigDecimal("1.70"), 354, "Spices"),
                IngredientDef("Curry Powder", UnitEnum.G, 100, BigDecimal("1.80"), 325, "Spices"),
                IngredientDef("Cinnamon", UnitEnum.G, 100, BigDecimal("1.90"), 247, "Spices"),
                IngredientDef("Sugar", UnitEnum.G, 100, BigDecimal("0.50"), 387, "Spices"),

                // Dairy
                IngredientDef("Milk", UnitEnum.ML, 100, BigDecimal("0.50"), 42, "Dairy"),
                IngredientDef("Cheese", UnitEnum.G, 100, BigDecimal("2.20"), 402, "Dairy"),
                IngredientDef("Butter", UnitEnum.G, 100, BigDecimal("2.00"), 717, "Dairy"),
                IngredientDef("Yogurt", UnitEnum.ML, 100, BigDecimal("0.80"), 59, "Dairy"),
                IngredientDef("Cream", UnitEnum.ML, 100, BigDecimal("1.50"), 340, "Dairy")
            )

            var inserted = 0
            ingredientDefs.forEach { d ->
                if (!ingredientRepository.existsByName(d.name)) {
                    val cat = categoryByName[d.category]
                        ?: categoryRepository.save(Category(name = d.category, description = "${d.category} category"))
                    val ing = Ingredient(
                        name = d.name,
                        baseUnit = d.unit,
                        baseQuantity = d.baseQty,
                        price = d.price,
                        cal = d.cal,
                        category = cat,
                        image = d.image,
                        isActive = true
                    )
                    ingredientRepository.save(ing)
                    inserted++
                }
            }
            println("✅ Inserted $inserted ingredients (total now ${ingredientRepository.count()})")
        }

        // ===== RECIPES (> 40) =====
        if (recipeRepository.count() < 40L) {
            data class ItemDef(val ingredientName: String, val quantity: Int)
            data class RecipeDef(val name: String, val category: RecipeCategory, val items: List<ItemDef>, val description: String? = null)

            val ingredientMap = ingredientRepository.findAll().associateBy { it.name }

            fun items(vararg pairs: Pair<String, Int>) = pairs.map { ItemDef(it.first, it.second) }

            val recipes = listOf(
                // MAIN (10)
                RecipeDef("Grilled Chicken Bowl", RecipeCategory.MAIN, items("Rice" to 150, "Chicken Breast" to 150, "Broccoli" to 80, "Carrot" to 50, "Soy Sauce" to 20)),
                RecipeDef("Beef Stir Fry", RecipeCategory.MAIN, items("Rice" to 150, "Beef" to 120, "Bell Pepper" to 60, "Onion" to 40, "Garlic" to 10, "Soy Sauce" to 20)),
                RecipeDef("Pork Teriyaki", RecipeCategory.MAIN, items("Rice" to 150, "Pork" to 120, "Teriyaki Sauce" to 30, "Onion" to 40, "Broccoli" to 60)),
                RecipeDef("Shrimp Pasta", RecipeCategory.MAIN, items("Pasta" to 120, "Shrimp" to 120, "Garlic" to 10, "Butter" to 20, "Pepper" to 2)),
                RecipeDef("Salmon Quinoa", RecipeCategory.MAIN, items("Quinoa" to 120, "Salmon" to 150, "Spinach" to 60, "Lemon" to 20, "Salt" to 2)),
                RecipeDef("Chicken Fried Rice", RecipeCategory.MAIN, items("Rice" to 200, "Chicken Thigh" to 120, "Peas" to 50, "Carrot" to 50, "Soy Sauce" to 20)),
                RecipeDef("Beef Noodles", RecipeCategory.MAIN, items("Noodles" to 150, "Beef" to 120, "Onion" to 40, "Chili Powder" to 5, "Soy Sauce" to 20)),
                RecipeDef("BBQ Pork Sandwich", RecipeCategory.MAIN, items("Bread" to 2, "Pork" to 100, "BBQ Sauce" to 30, "Onion" to 30)),
                RecipeDef("Ham Cheese Pasta", RecipeCategory.MAIN, items("Pasta" to 120, "Ham" to 80, "Cheese" to 50, "Cream" to 40, "Pepper" to 2)),
                RecipeDef("Sausage Rice Bowl", RecipeCategory.MAIN, items("Rice" to 180, "Sausage" to 120, "Bell Pepper" to 60, "Onion" to 40, "Ketchup" to 20)),

                // SALAD (8)
                RecipeDef("Garden Salad", RecipeCategory.SALAD, items("Lettuce" to 80, "Tomato" to 60, "Cucumber" to 60, "Carrot" to 40, "Olive Oil" to 0)),
                RecipeDef("Spinach Avocado Salad", RecipeCategory.SALAD, items("Spinach" to 80, "Avocado" to 80, "Tomato" to 50, "Lemon" to 20, "Salt" to 2)),
                RecipeDef("Kale Quinoa Salad", RecipeCategory.SALAD, items("Kale" to 80, "Quinoa" to 100, "Cucumber" to 60, "Lemon" to 20, "Pepper" to 2)),
                RecipeDef("Chicken Salad", RecipeCategory.SALAD, items("Lettuce" to 80, "Chicken Breast" to 120, "Tomato" to 50, "Cucumber" to 50, "Mayonnaise" to 20)),
                RecipeDef("Shrimp Avocado Salad", RecipeCategory.SALAD, items("Lettuce" to 80, "Shrimp" to 100, "Avocado" to 80, "Lemon" to 20, "Pepper" to 2)),
                RecipeDef("Broccoli Apple Salad", RecipeCategory.SALAD, items("Broccoli" to 80, "Apple" to 80, "Carrot" to 40, "Yogurt" to 40, "Pepper" to 2)),
                RecipeDef("Corn Pea Salad", RecipeCategory.SALAD, items("Corn" to 80, "Peas" to 80, "Tomato" to 60, "Onion" to 40, "Mayonnaise" to 20)),
                RecipeDef("Mediterranean Salad", RecipeCategory.SALAD, items("Lettuce" to 80, "Tomato" to 60, "Cucumber" to 60, "Onion" to 40, "Oregano" to 3, "Basil" to 3)),

                // SOUP (6)
                RecipeDef("Tomato Soup", RecipeCategory.SOUP, items("Tomato" to 200, "Onion" to 50, "Garlic" to 10, "Salt" to 2, "Pepper" to 2)),
                RecipeDef("Chicken Soup", RecipeCategory.SOUP, items("Chicken Thigh" to 150, "Carrot" to 60, "Onion" to 40, "Salt" to 2, "Pepper" to 2)),
                RecipeDef("Mushroom Soup", RecipeCategory.SOUP, items("Mushrooms" to 180, "Onion" to 40, "Garlic" to 10, "Cream" to 50, "Pepper" to 2)),
                RecipeDef("Corn Soup", RecipeCategory.SOUP, items("Corn" to 200, "Onion" to 40, "Butter" to 20, "Salt" to 2, "Pepper" to 2)),
                RecipeDef("Vegetable Soup", RecipeCategory.SOUP, items("Carrot" to 80, "Peas" to 80, "Tomato" to 80, "Onion" to 40, "Salt" to 2)),
                RecipeDef("Seafood Soup", RecipeCategory.SOUP, items("Shrimp" to 120, "Squid" to 100, "Onion" to 40, "Garlic" to 10, "Pepper" to 2)),

                // SNACK (5)
                RecipeDef("Fruit Mix", RecipeCategory.SNACK, items("Apple" to 80, "Banana" to 80, "Orange" to 80, "Strawberry" to 60)),
                RecipeDef("Yogurt Parfait", RecipeCategory.SNACK, items("Yogurt" to 150, "Oats" to 50, "Blueberry" to 60, "Strawberry" to 60, "Honey" to 0)),
                RecipeDef("Cheese Sandwich", RecipeCategory.SNACK, items("Bread" to 2, "Cheese" to 50, "Butter" to 10)),
                RecipeDef("Garlic Bread", RecipeCategory.SNACK, items("Bread" to 2, "Butter" to 15, "Garlic" to 10, "Parsley" to 0)),
                RecipeDef("Avocado Toast", RecipeCategory.SNACK, items("Bread" to 1, "Avocado" to 80, "Lemon" to 10, "Pepper" to 2, "Salt" to 2)),

                // APPETIZER (4)
                RecipeDef("Bruschetta", RecipeCategory.APPETIZER, items("Bread" to 2, "Tomato" to 60, "Garlic" to 10, "Basil" to 3, "Pepper" to 2)),
                RecipeDef("Stuffed Mushrooms", RecipeCategory.APPETIZER, items("Mushrooms" to 120, "Cheese" to 40, "Garlic" to 10, "Pepper" to 2)),
                RecipeDef("Shrimp Cocktail", RecipeCategory.APPETIZER, items("Shrimp" to 120, "Lemon" to 20, "Ketchup" to 30, "Pepper" to 2)),
                RecipeDef("Veggie Sticks Dip", RecipeCategory.APPETIZER, items("Carrot" to 80, "Cucumber" to 80, "Bell Pepper" to 60, "Mayonnaise" to 40, "Mustard" to 20)),

                // DESSERT (6)
                RecipeDef("Fruit Salad", RecipeCategory.DESSERT, items("Apple" to 60, "Banana" to 60, "Mango" to 60, "Pineapple" to 60, "Yogurt" to 80)),
                RecipeDef("Cinnamon Oats", RecipeCategory.DESSERT, items("Oats" to 60, "Milk" to 150, "Cinnamon" to 3, "Honey" to 0)),
                RecipeDef("Mango Yogurt", RecipeCategory.DESSERT, items("Mango" to 120, "Yogurt" to 120, "Milk" to 50)),
                RecipeDef("Banana Yogurt", RecipeCategory.DESSERT, items("Banana" to 150, "Yogurt" to 120, "Milk" to 50)),
                RecipeDef("Blueberry Yogurt", RecipeCategory.DESSERT, items("Blueberry" to 120, "Yogurt" to 120, "Milk" to 50)),
                RecipeDef("Strawberry Yogurt", RecipeCategory.DESSERT, items("Strawberry" to 150, "Yogurt" to 120, "Milk" to 50)),

                // BEVERAGE (2)
                RecipeDef("Mango Smoothie", RecipeCategory.BEVERAGE, items("Mango" to 150, "Milk" to 120, "Yogurt" to 80)),
                RecipeDef("Strawberry Smoothie", RecipeCategory.BEVERAGE, items("Strawberry" to 150, "Milk" to 120, "Yogurt" to 80)),

                // SAUCE (4)
                RecipeDef("Spicy Mayo", RecipeCategory.SAUCE, items("Mayonnaise" to 80, "Hot Sauce" to 20, "Lemon" to 10)),
                RecipeDef("Teriyaki Dip", RecipeCategory.SAUCE, items("Teriyaki Sauce" to 80, "Soy Sauce" to 20, "Sugar" to 0)),
                RecipeDef("BBQ Mix", RecipeCategory.SAUCE, items("BBQ Sauce" to 80, "Ketchup" to 40, "Mustard" to 20)),
                RecipeDef("Fish Sauce Mix", RecipeCategory.SAUCE, items("Fish Sauce" to 60, "Lemon" to 20, "Sugar" to 0, "Chili Powder" to 3)),

                // OTHER (additional to surpass 40)
                RecipeDef("Tuna Sandwich", RecipeCategory.OTHER, items("Bread" to 2, "Tuna" to 120, "Mayonnaise" to 20, "Onion" to 20)),
                RecipeDef("Salmon Rice", RecipeCategory.OTHER, items("Rice" to 200, "Salmon" to 120, "Lemon" to 10, "Pepper" to 2, "Salt" to 2))
            )

            var insertedRecipes = 0
            recipes.forEach { r ->
                if (recipeRepository.findByName(r.name).isEmpty) {
                    // Build components and compute totals
                    data class Component(val ingredient: Ingredient, val qty: Int, val unit: UnitEnum, val price: BigDecimal, val cal: Int)
                    val components = r.items.mapNotNull { item ->
                        val ing = ingredientMap[item.ingredientName]
                        if (ing == null) {
                            println("⚠️ Missing ingredient for recipe '${r.name}': ${item.ingredientName}")
                            null
                        } else {
                            val ratio = BigDecimal(item.quantity).divide(BigDecimal(ing.baseQuantity))
                            val price = ing.price.multiply(ratio)
                            val cal = (ing.cal * item.quantity) / ing.baseQuantity
                            Component(ing, item.quantity, ing.baseUnit, price, cal)
                        }
                    }

                    val totalPrice = components.fold(BigDecimal.ZERO) { acc, c -> acc + c.price }
                    val totalCal = components.sumOf { it.cal }

                    val recipe = Recipe(
                        name = r.name,
                        description = r.description,
                        isCustomizable = true,
                        price = totalPrice,
                        cal = totalCal,
                        ingredientSnapshot = null,
                        isActive = true,
                        image = null,
                        category = r.category
                    )

                    // Attach components
                    components.forEach { c ->
                        recipe.recipe_ingredients.add(
                            RecipeIngredient(
                                recipe = recipe,
                                ingredient = c.ingredient,
                                quantity = c.qty,
                                baseUnit = c.unit,
                                price = c.price,
                                cal = c.cal,
                                itemSnapshot = null
                            )
                        )
                    }

                    recipeRepository.save(recipe)
                    insertedRecipes++
                }
            }

            println("✅ Inserted $insertedRecipes recipes (total now ${recipeRepository.count()})")
        }

        // ===== PAYMENT METHODS =====
        if (paymentMethodRepository.count() == 0L) {
            val methods = PaymentMethodType.entries.map {
                PaymentMethod(name = it, description = it.name.replace('_', ' ').lowercase().replaceFirstChar { c -> c.titlecase() })
            }
            paymentMethodRepository.saveAll(methods)
            println("✅ Inserted ${methods.size} payment methods")
        }

        // ===== PROMOTIONS =====
        if (promotionRepository.count() == 0L) {
            val now = LocalDateTime.now()
            val promos = listOf(
                Promotion(
                    name = "Welcome 10%",
                    description = "Giảm 10% cho đơn đầu tiên",
                    discountType = DiscountType.PERCENT,
                    discountValue = BigDecimal("10.00"),
                    isActive = true,
                    startDate = Timestamp.valueOf(now.minusDays(1)),
                    endDate = Timestamp.valueOf(now.plusMonths(1)),
                    quantity = 100
                ),
                Promotion(
                    name = "Giảm 20k cuối tuần",
                    description = "Áp dụng thứ 6 - CN",
                    discountType = DiscountType.AMOUNT,
                    discountValue = BigDecimal("20000.00"),
                    isActive = true,
                    startDate = Timestamp.valueOf(now.minusDays(1)),
                    endDate = Timestamp.valueOf(now.plusWeeks(2)),
                    quantity = 200
                ),
                Promotion(
                    name = "Member 15%",
                    description = "Ưu đãi thành viên",
                    discountType = DiscountType.PERCENT,
                    discountValue = BigDecimal("15.00"),
                    isActive = true,
                    startDate = Timestamp.valueOf(now.minusDays(1)),
                    endDate = Timestamp.valueOf(now.plusMonths(2)),
                    quantity = 150
                ),
                Promotion(
                    name = "Flash sale -50k",
                    description = "Đã kết thúc",
                    discountType = DiscountType.AMOUNT,
                    discountValue = BigDecimal("50000.00"),
                    isActive = false,
                    startDate = Timestamp.valueOf(now.minusMonths(1)),
                    endDate = Timestamp.valueOf(now.minusDays(5)),
                    quantity = 0
                )
            )
            promotionRepository.saveAll(promos)
            println("✅ Inserted ${promos.size} promotions")
        }

        // ===== COMBOS =====
        if (comboRepository.count() == 0L) {
            val rmap = recipeRepository.findAll().associateBy { it.name }
            fun r(name: String) = rmap[name]
            data class CI(val r: String, val q: Int = 1)
            data class ComboDef(val name: String, val items: List<CI>)

            val combos = listOf(
                ComboDef("Classic Chicken Combo", listOf(CI("Grilled Chicken Bowl"), CI("Garden Salad"), CI("Tomato Soup"))),
                ComboDef("Seafood Delight Combo", listOf(CI("Shrimp Pasta"), CI("Seafood Soup"), CI("Mediterranean Salad"))),
                ComboDef("Beef Lover Combo", listOf(CI("Beef Stir Fry"), CI("Beef Noodles"), CI("Mushroom Soup"))),
                ComboDef("Light & Fresh Combo", listOf(CI("Spinach Avocado Salad"), CI("Vegetable Soup"), CI("Fruit Mix"))),
                ComboDef("Sandwich Snack Combo", listOf(CI("BBQ Pork Sandwich"), CI("Garlic Bread"), CI("Yogurt Parfait"))),
                ComboDef("Pasta Feast Combo", listOf(CI("Ham Cheese Pasta"), CI("Shrimp Pasta"), CI("Bruschetta"))),
                ComboDef("Chicken Rice Duo", listOf(CI("Chicken Fried Rice"), CI("Garden Salad"))),
                ComboDef("Salmon Healthy Set", listOf(CI("Salmon Quinoa"), CI("Kale Quinoa Salad")))
            )

            var insertedCombos = 0
            combos.forEach { def ->
                if (comboRepository.findByName(def.name).isEmpty) {
                    val items = def.items.mapNotNull { ci ->
                        val recipe = r(ci.r)
                        if (recipe == null) {
                            println("⚠️ Missing recipe for combo '${def.name}': ${ci.r}")
                            null
                        } else Pair(recipe, ci.q)
                    }
                    if (items.isEmpty()) return@forEach

                    val sumPrice = items.fold(BigDecimal.ZERO) { acc, (rec, q) -> acc + rec.price.multiply(BigDecimal(q)) }
                    val sumCal = items.sumOf { (rec, q) -> rec.cal * q }
                    val combo = Combo(
                        name = def.name,
                        price = sumPrice.multiply(BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP), // 10% off
                        cal = sumCal,
                        isActive = true
                    )
                    items.forEach { (rec, q) ->
                        combo.combo_items.add(ComboItem(combo = combo, recipe = rec, quantity = q))
                    }
                    comboRepository.save(combo)
                    insertedCombos++
                }
            }
            println("✅ Inserted $insertedCombos combos (total now ${comboRepository.count()})")
        }

        // ===== DAILY MENUS =====
        if (dailyMenuRepository.count() == 0L) {
            val rmap = recipeRepository.findAll().associateBy { it.name }
            val cmap = comboRepository.findAll().associateBy { it.name }

            fun r(name: String) = rmap[name]
            fun c(name: String) = cmap[name]

            data class DM(
                val date: LocalDate,
                val name: String,
                val meals: List<String>,
                val combos: List<String>
            )

            val dms = listOf(
                DM(
                    LocalDate.now(),
                    "Menu Today",
                    meals = listOf("Grilled Chicken Bowl", "Garden Salad", "Tomato Soup", "Shrimp Pasta"),
                    combos = listOf("Classic Chicken Combo", "Light & Fresh Combo")
                ),
                DM(
                    LocalDate.now().plusDays(1),
                    "Menu Tomorrow",
                    meals = listOf("Beef Stir Fry", "Vegetable Soup", "Ham Cheese Pasta", "Kale Quinoa Salad"),
                    combos = listOf("Beef Lover Combo", "Salmon Healthy Set")
                ),
                DM(
                    LocalDate.now().plusDays(2),
                    "Menu Next Day",
                    meals = listOf("Chicken Fried Rice", "Mediterranean Salad", "Seafood Soup", "Bruschetta"),
                    combos = listOf("Seafood Delight Combo", "Pasta Feast Combo")
                )
            )

            var insertedDM = 0
            dms.forEach { def ->
                val dm = DailyMenu(
                    date = Date.valueOf(def.date),
                    name = def.name
                )

                def.meals.forEach { name ->
                    val rec = r(name)
                    if (rec == null) {
                        println("⚠️ Missing recipe for daily menu '${def.name}': $name")
                    } else {
                        dm.dailyMenuItems.add(
                            DailyMenuItem(
                                dailyMenu = dm,
                                name = rec.name,
                                menuType = MenuType.MEAL,
                                refId = rec.id!!,
                                cal = rec.cal,
                                price = rec.price
                            )
                        )
                    }
                }
                def.combos.forEach { name ->
                    val combo = c(name)
                    if (combo == null) {
                        println("⚠️ Missing combo for daily menu '${def.name}': $name")
                    } else {
                        dm.dailyMenuItems.add(
                            DailyMenuItem(
                                dailyMenu = dm,
                                name = combo.name,
                                menuType = MenuType.COMBO,
                                refId = combo.id!!,
                                cal = combo.cal,
                                price = combo.price
                            )
                        )
                    }
                }

                dailyMenuRepository.save(dm)
                insertedDM++
            }
            println("✅ Inserted $insertedDM daily menus (total now ${dailyMenuRepository.count()})")
        }

        // ===== INGREDIENT NUTRIENTS (populate if missing) =====
        TransactionTemplate(transactionManager).execute {
            val nutNames = listOf(
                "Protein", "Carbohydrate", "Fat", "Fiber", "Sugar",
                "Sodium", "Potassium", "Calcium", "Iron", "Cholesterol"
            )
            val nutMap = nutrientRepository.findAll().associateBy { it.name }
            val requiredNutrients = nutNames.mapNotNull { name ->
                val n = nutMap[name]
                if (n == null) {
                    println("⚠️ Nutrient not found, skipping: $name")
                }
                n
            }

            fun bd(d: Double) = BigDecimal.valueOf(d)
            fun round2(v: BigDecimal) = v.setScale(2, RoundingMode.HALF_UP)
            fun clamp(v: BigDecimal, max: Double) = if (v > bd(max)) bd(max) else v

            fun isName(ing: Ingredient, vararg keys: String) =
                keys.any { k -> ing.name.contains(k, ignoreCase = true) }

            val allIngredients = ingredientRepository.findAll()
            var updated = 0
            allIngredients.forEach { ing ->
                val ingId = ing.id
                if (ingId != null && ingredientNutrientRepository.findAllByIngredientId(ingId).isEmpty()) {
                    val cat = ing.category.name.lowercase()
                    val cal = ing.cal

                    val (pPct, cPct, fPct) = when {
                        cat.contains("meat") -> Triple(0.55, 0.03, 0.42)
                        cat.contains("seafood") -> Triple(0.55, 0.02, 0.43)
                        cat.contains("grain") -> Triple(0.12, 0.75, 0.13)
                        cat.contains("vegetable") -> Triple(0.20, 0.70, 0.10)
                        cat.contains("fruit") -> Triple(0.03, 0.95, 0.02)
                        cat.contains("dairy") -> Triple(0.20, 0.35, 0.45)
                        cat.contains("sauce") -> Triple(0.05, 0.60, 0.35)
                        cat.contains("spice") -> Triple(0.10, 0.70, 0.20)
                        else -> Triple(0.20, 0.60, 0.20)
                    }

                    val proteinG = round2(bd(cal.toDouble() * pPct / 4.0))
                    val carbG = round2(bd(cal.toDouble() * cPct / 4.0))
                    val fatG = round2(bd(cal.toDouble() * fPct / 9.0))

                    val fiberG = when {
                        cat.contains("vegetable") -> clamp(round2(carbG.multiply(bd(0.30))), 12.0)
                        cat.contains("fruit") -> clamp(round2(carbG.multiply(bd(0.20))), 10.0)
                        cat.contains("grain") -> clamp(round2(carbG.multiply(bd(0.12))), 10.0)
                        cat.contains("spice") -> clamp(round2(carbG.multiply(bd(0.35))), 30.0)
                        else -> clamp(round2(carbG.multiply(bd(0.05))), 4.0)
                    }

                    val sugarG = when {
                        cat.contains("fruit") -> round2(carbG.multiply(bd(0.70)))
                        isName(ing, "Ketchup", "BBQ", "Teriyaki", "Hot Sauce") -> round2(carbG.multiply(bd(0.65)))
                        cat.contains("sauce") -> round2(carbG.multiply(bd(0.50)))
                        cat.contains("dairy") -> round2(carbG.multiply(bd(0.30)))
                        cat.contains("meat") || cat.contains("seafood") -> bd(0.0)
                        else -> round2(carbG.multiply(bd(0.15)))
                    }

                    val sodiumMg = when {
                        isName(ing, "Soy Sauce") -> 5000
                        isName(ing, "Fish Sauce") -> 4000
                        isName(ing, "Ketchup") -> 900
                        isName(ing, "BBQ Sauce") -> 1200
                        isName(ing, "Mustard") -> 1100
                        isName(ing, "Mayonnaise") -> 700
                        isName(ing, "Cheese") -> 600
                        cat.contains("sauce") -> 800
                        cat.contains("meat") -> 70
                        cat.contains("seafood") -> 150
                        cat.contains("dairy") -> 60
                        cat.contains("grain") -> 5
                        cat.contains("vegetable") || cat.contains("fruit") -> 5
                        else -> 50
                    }

                    val potassiumMg = when {
                        cat.contains("vegetable") || cat.contains("fruit") -> 250
                        cat.contains("meat") || cat.contains("seafood") -> 300
                        cat.contains("dairy") -> 150
                        else -> 120
                    }

                    val calciumMg = when {
                        isName(ing, "Cheese") -> 700
                        cat.contains("dairy") -> 120
                        cat.contains("vegetable") -> 50
                        else -> 30
                    }

                    val ironMg = when {
                        isName(ing, "Spinach", "Kale") -> 2
                        cat.contains("meat") || cat.contains("seafood") -> 2
                        cat.contains("grain") -> 1
                        else -> 0
                    }

                    val cholesterolMg = when {
                        isName(ing, "Butter") -> 215
                        isName(ing, "Shrimp") -> 150
                        cat.contains("meat") -> 70
                        cat.contains("seafood") -> 100
                        cat.contains("dairy") -> 20
                        else -> 0
                    }

                    val valuesByName: Map<String, BigDecimal> = mapOf(
                        "Protein" to proteinG,
                        "Carbohydrate" to carbG,
                        "Fat" to fatG,
                        "Fiber" to fiberG,
                        "Sugar" to sugarG,
                        "Sodium" to bd(sodiumMg.toDouble()),
                        "Potassium" to bd(potassiumMg.toDouble()),
                        "Calcium" to bd(calciumMg.toDouble()),
                        "Iron" to bd(ironMg.toDouble()),
                        "Cholesterol" to bd(cholesterolMg.toDouble())
                    )

                    // Create IngredientNutrient for available nutrients only, avoid touching lazy collection
                    val relations = valuesByName.mapNotNull { (name, amount) ->
                        val nutrient = nutMap[name]
                        if (nutrient != null) IngredientNutrient(
                            ingredient = ing,
                            nutrient = nutrient,
                            amount = amount
                        ) else null
                    }
                    if (relations.isNotEmpty()) ingredientNutrientRepository.saveAll(relations)
                    updated++
                }
            }
            println("✅ Populated nutrients for $updated ingredients")
        }

        println("✅ Sample data initialized!")

    }
}
