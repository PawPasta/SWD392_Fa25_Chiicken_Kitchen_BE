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
import com.ChickenKitchen.app.model.entity.cooking.CookingMethod
import com.ChickenKitchen.app.model.entity.cooking.CookingEffect
import com.ChickenKitchen.app.model.entity.recipe.Recipe
import com.ChickenKitchen.app.model.entity.recipe.RecipeIngredient
import com.ChickenKitchen.app.repository.user.UserRepository
import com.ChickenKitchen.app.repository.user.UserAddressRepository
import com.ChickenKitchen.app.repository.user.WalletRepository
import com.ChickenKitchen.app.repository.ingredient.NutrientRepository
import com.ChickenKitchen.app.repository.ingredient.IngredientRepository
import com.ChickenKitchen.app.repository.ingredient.IngredientNutrientRepository
import com.ChickenKitchen.app.repository.cooking.CookingMethodRepository
import com.ChickenKitchen.app.repository.cooking.CookingEffectRepository
import com.ChickenKitchen.app.repository.recipe.RecipeRepository
import com.ChickenKitchen.app.enum.Role
import com.ChickenKitchen.app.enum.UnitEnum
import com.ChickenKitchen.app.enum.IngredientCategory
import com.ChickenKitchen.app.enum.EffectType
import com.ChickenKitchen.app.enum.RecipeCategory
import java.time.LocalDate
import java.math.BigDecimal

@Configuration
class DataInitializer {

    @Bean
    fun initData(
        userRepository: UserRepository,
        userAddressRepository: UserAddressRepository,
        walletRepository: WalletRepository,
        nutrientRepository: NutrientRepository,
        ingredientRepository: IngredientRepository,
        ingredientNutrientRepository: IngredientNutrientRepository,
        cookingMethodRepository: CookingMethodRepository,
        cookingEffectRepository: CookingEffectRepository,
        // recipeRepository: RecipeRepository,
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
                Nutrient(name = "Calories", baseUnit = UnitEnum.G),
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


            // ===== INGREDIENTS =====

            if (ingredientRepository.count() < 1L) {
                val ingredients = listOf(
                    // === VEGETABLE ===
                    Ingredient(name = "Cà chua",  category = IngredientCategory.VEGETABLE, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("5000")),
                    Ingredient(name = "Dưa leo",  category = IngredientCategory.VEGETABLE, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("4000")),
                    Ingredient(name = "Rau muống",category = IngredientCategory.VEGETABLE, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("3000")),
                    Ingredient(name = "Khoai tây",category = IngredientCategory.VEGETABLE, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("6000")),
                    Ingredient(name = "Cà rốt",   category = IngredientCategory.VEGETABLE, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("7000")),
        
                    // === MEAT ===
                    Ingredient(name = "Thịt gà",  category = IngredientCategory.MEAT, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("25000")),
                    Ingredient(name = "Thịt bò",  category = IngredientCategory.MEAT, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("40000")),
                    Ingredient(name = "Thịt heo", category = IngredientCategory.MEAT, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("20000")),
                    Ingredient(name = "Thịt vịt", category = IngredientCategory.MEAT, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("22000")),
                    Ingredient(name = "Thịt dê",  category = IngredientCategory.MEAT, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("35000")),
        
                    // === SEAFOOD ===
                    Ingredient(name = "Cá hồi",   category = IngredientCategory.SEAFOOD, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("60000")),
                    Ingredient(name = "Cá thu",   category = IngredientCategory.SEAFOOD, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("30000")),
                    Ingredient(name = "Tôm sú",   category = IngredientCategory.SEAFOOD, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("45000")),
                    Ingredient(name = "Mực ống",  category = IngredientCategory.SEAFOOD, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("38000")),
                    Ingredient(name = "Ghẹ biển", category = IngredientCategory.SEAFOOD, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("50000")),
        
                    // === GRAIN ===
                    Ingredient(name = "Cơm trắng", category = IngredientCategory.GRAIN, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("12000")),
                    Ingredient(name = "Cơm gạo lứt",   category = IngredientCategory.GRAIN, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("18000")),
                    Ingredient(name = "Bún tươi",  category = IngredientCategory.GRAIN, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("15000")),
                    Ingredient(name = "Mì trứng",  category = IngredientCategory.GRAIN, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("20000")),
                    Ingredient(name = "Bánh mì",   category = IngredientCategory.GRAIN, baseUnit = UnitEnum.G, baseQuantity = 100, basePrice = BigDecimal("10000"))
                )
                val savedIngredients = ingredientRepository.saveAll(ingredients)
                val ingMap = savedIngredients.associateBy { it.name }
                println("✅ Inserted ${savedIngredients.size} ingredients")

                    // ===== INGREDIENT_NUTRIENTS =====

                if (ingredientNutrientRepository.count() < 1L) {
                    val ingredientNutrients = listOf(
                        // Cà chua
                        IngredientNutrient(ingredient = ingMap["Cà chua"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("18.0")),
                        IngredientNutrient(ingredient = ingMap["Cà chua"]!!, nutrient = nutMap["Vitamin C"]!!, amount = BigDecimal("14.0")),
                        IngredientNutrient(ingredient = ingMap["Cà chua"]!!, nutrient = nutMap["Carbohydrate"]!!, amount = BigDecimal("3.9")),
                        IngredientNutrient(ingredient = ingMap["Cà chua"]!!, nutrient = nutMap["Fiber"]!!, amount = BigDecimal("1.2")),
                        IngredientNutrient(ingredient = ingMap["Cà chua"]!!, nutrient = nutMap["Sugar"]!!, amount = BigDecimal("2.6")),
                        IngredientNutrient(ingredient = ingMap["Cà chua"]!!, nutrient = nutMap["Potassium"]!!, amount = BigDecimal("237.0")),
    
                        // Dưa leo
                        IngredientNutrient(ingredient = ingMap["Dưa leo"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("15.0")),
                        IngredientNutrient(ingredient = ingMap["Dưa leo"]!!, nutrient = nutMap["Vitamin K"]!!, amount = BigDecimal("16.4")),
                        IngredientNutrient(ingredient = ingMap["Dưa leo"]!!, nutrient = nutMap["Fiber"]!!, amount = BigDecimal("0.5")),
                        IngredientNutrient(ingredient = ingMap["Dưa leo"]!!, nutrient = nutMap["Potassium"]!!, amount = BigDecimal("147.0")),
                        IngredientNutrient(ingredient = ingMap["Dưa leo"]!!, nutrient = nutMap["Vitamin C"]!!, amount = BigDecimal("3.0")),
    
                        // Rau muống
                        IngredientNutrient(ingredient = ingMap["Rau muống"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("19.0")),
                        IngredientNutrient(ingredient = ingMap["Rau muống"]!!, nutrient = nutMap["Iron"]!!, amount = BigDecimal("1.6")),
                        IngredientNutrient(ingredient = ingMap["Rau muống"]!!, nutrient = nutMap["Vitamin A"]!!, amount = BigDecimal("315.0")),
                        IngredientNutrient(ingredient = ingMap["Rau muống"]!!, nutrient = nutMap["Calcium"]!!, amount = BigDecimal("77.0")),
                        IngredientNutrient(ingredient = ingMap["Rau muống"]!!, nutrient = nutMap["Magnesium"]!!, amount = BigDecimal("71.0")),
                        IngredientNutrient(ingredient = ingMap["Rau muống"]!!, nutrient = nutMap["Vitamin C"]!!, amount = BigDecimal("55.0")),
    
                        // Khoai tây
                        IngredientNutrient(ingredient = ingMap["Khoai tây"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("77.0")),
                        IngredientNutrient(ingredient = ingMap["Khoai tây"]!!, nutrient = nutMap["Carbohydrate"]!!, amount = BigDecimal("17.0")),
                        IngredientNutrient(ingredient = ingMap["Khoai tây"]!!, nutrient = nutMap["Potassium"]!!, amount = BigDecimal("429.0")),
                        IngredientNutrient(ingredient = ingMap["Khoai tây"]!!, nutrient = nutMap["Fiber"]!!, amount = BigDecimal("2.2")),
                        IngredientNutrient(ingredient = ingMap["Khoai tây"]!!, nutrient = nutMap["Vitamin C"]!!, amount = BigDecimal("19.7")),
                        IngredientNutrient(ingredient = ingMap["Khoai tây"]!!, nutrient = nutMap["Magnesium"]!!, amount = BigDecimal("23.0")),
    
                        // Cà rốt
                        IngredientNutrient(ingredient = ingMap["Cà rốt"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("41.0")),
                        IngredientNutrient(ingredient = ingMap["Cà rốt"]!!, nutrient = nutMap["Vitamin A"]!!, amount = BigDecimal("835.0")),
                        IngredientNutrient(ingredient = ingMap["Cà rốt"]!!, nutrient = nutMap["Fiber"]!!, amount = BigDecimal("2.8")),
                        IngredientNutrient(ingredient = ingMap["Cà rốt"]!!, nutrient = nutMap["Sugar"]!!, amount = BigDecimal("4.7")),
                        IngredientNutrient(ingredient = ingMap["Cà rốt"]!!, nutrient = nutMap["Potassium"]!!, amount = BigDecimal("320.0")),
                        IngredientNutrient(ingredient = ingMap["Cà rốt"]!!, nutrient = nutMap["Vitamin K"]!!, amount = BigDecimal("13.0")),
    
                        // Thịt gà
                        IngredientNutrient(ingredient = ingMap["Thịt gà"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("239.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt gà"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("27.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt gà"]!!, nutrient = nutMap["Fat"]!!, amount = BigDecimal("14.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt gà"]!!, nutrient = nutMap["Sodium"]!!, amount = BigDecimal("70.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt gà"]!!, nutrient = nutMap["Cholesterol"]!!, amount = BigDecimal("85.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt gà"]!!, nutrient = nutMap["Vitamin B6"]!!, amount = BigDecimal("0.5")),
    
                        // Thịt bò
                        IngredientNutrient(ingredient = ingMap["Thịt bò"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("250.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt bò"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("26.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt bò"]!!, nutrient = nutMap["Iron"]!!, amount = BigDecimal("2.6")),
                        IngredientNutrient(ingredient = ingMap["Thịt bò"]!!, nutrient = nutMap["Sodium"]!!, amount = BigDecimal("72.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt bò"]!!, nutrient = nutMap["Zinc"]!!, amount = BigDecimal("4.8")),
                        IngredientNutrient(ingredient = ingMap["Thịt bò"]!!, nutrient = nutMap["Vitamin B12"]!!, amount = BigDecimal("2.6")),
    
                        // Thịt heo
                        IngredientNutrient(ingredient = ingMap["Thịt heo"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("242.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt heo"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("27.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt heo"]!!, nutrient = nutMap["Fat"]!!, amount = BigDecimal("14.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt heo"]!!, nutrient = nutMap["Sodium"]!!, amount = BigDecimal("62.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt heo"]!!, nutrient = nutMap["Magnesium"]!!, amount = BigDecimal("20.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt heo"]!!, nutrient = nutMap["Vitamin B1"]!!, amount = BigDecimal("0.7")),
    
                        // Thịt vịt
                        IngredientNutrient(ingredient = ingMap["Thịt vịt"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("337.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt vịt"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("19.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt vịt"]!!, nutrient = nutMap["Fat"]!!, amount = BigDecimal("28.0")),
    
                        // === Thịt dê ===
                        IngredientNutrient(ingredient = ingMap["Thịt dê"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("143.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt dê"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("27.0")),
                        IngredientNutrient(ingredient = ingMap["Thịt dê"]!!, nutrient = nutMap["Iron"]!!, amount = BigDecimal("3.7")),
                        
                        // Cá hồi
                        IngredientNutrient(ingredient = ingMap["Cá hồi"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("208.0")),
                        IngredientNutrient(ingredient = ingMap["Cá hồi"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("20.0")),
                        IngredientNutrient(ingredient = ingMap["Cá hồi"]!!, nutrient = nutMap["Cholesterol"]!!, amount = BigDecimal("55.0")),
                        IngredientNutrient(ingredient = ingMap["Cá hồi"]!!, nutrient = nutMap["Vitamin D"]!!, amount = BigDecimal("10.9")),
                        IngredientNutrient(ingredient = ingMap["Cá hồi"]!!, nutrient = nutMap["Vitamin B12"]!!, amount = BigDecimal("3.2")),
                        IngredientNutrient(ingredient = ingMap["Cá hồi"]!!, nutrient = nutMap["Sodium"]!!, amount = BigDecimal("59.0")),
    
                        // Cá thu
                        IngredientNutrient(ingredient = ingMap["Cá thu"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("205.0")),
                        IngredientNutrient(ingredient = ingMap["Cá thu"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("19.0")),
                        IngredientNutrient(ingredient = ingMap["Cá thu"]!!, nutrient = nutMap["Fat"]!!, amount = BigDecimal("13.0")),
                        IngredientNutrient(ingredient = ingMap["Cá thu"]!!, nutrient = nutMap["Vitamin D"]!!, amount = BigDecimal("16.1")),
                        IngredientNutrient(ingredient = ingMap["Cá thu"]!!, nutrient = nutMap["Vitamin B6"]!!, amount = BigDecimal("0.7")),
                        IngredientNutrient(ingredient = ingMap["Cá thu"]!!, nutrient = nutMap["Cholesterol"]!!, amount = BigDecimal("70.0")),
    
                        // Tôm sú
                        IngredientNutrient(ingredient = ingMap["Tôm sú"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("99.0")),
                        IngredientNutrient(ingredient = ingMap["Tôm sú"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("24.0")),
                        IngredientNutrient(ingredient = ingMap["Tôm sú"]!!, nutrient = nutMap["Cholesterol"]!!, amount = BigDecimal("189.0")),
                        IngredientNutrient(ingredient = ingMap["Tôm sú"]!!, nutrient = nutMap["Sodium"]!!, amount = BigDecimal("111.0")),
                        IngredientNutrient(ingredient = ingMap["Tôm sú"]!!, nutrient = nutMap["Vitamin B12"]!!, amount = BigDecimal("1.1")),
                        IngredientNutrient(ingredient = ingMap["Tôm sú"]!!, nutrient = nutMap["Zinc"]!!, amount = BigDecimal("1.3")),
    
                        // Mực ống
                        IngredientNutrient(ingredient = ingMap["Mực ống"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("92.0")),
                        IngredientNutrient(ingredient = ingMap["Mực ống"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("15.0")),
                        IngredientNutrient(ingredient = ingMap["Mực ống"]!!, nutrient = nutMap["Cholesterol"]!!, amount = BigDecimal("233.0")),
                        IngredientNutrient(ingredient = ingMap["Mực ống"]!!, nutrient = nutMap["Sodium"]!!, amount = BigDecimal("44.0")),
                        IngredientNutrient(ingredient = ingMap["Mực ống"]!!, nutrient = nutMap["Vitamin B2"]!!, amount = BigDecimal("0.5")),
                        IngredientNutrient(ingredient = ingMap["Mực ống"]!!, nutrient = nutMap["Zinc"]!!, amount = BigDecimal("1.5")),
    
                        // Ghẹ biển
                        IngredientNutrient(ingredient = ingMap["Ghẹ biển"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("97.0")),
                        IngredientNutrient(ingredient = ingMap["Ghẹ biển"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("21.0")),
                        IngredientNutrient(ingredient = ingMap["Ghẹ biển"]!!, nutrient = nutMap["Zinc"]!!, amount = BigDecimal("7.6")),
    
                        // Cơm trắng
                        IngredientNutrient(ingredient = ingMap["Cơm trắng"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("130.0")),
                        IngredientNutrient(ingredient = ingMap["Cơm trắng"]!!, nutrient = nutMap["Carbohydrate"]!!, amount = BigDecimal("28.0")),
                        IngredientNutrient(ingredient = ingMap["Cơm trắng"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("2.7")),
                        IngredientNutrient(ingredient = ingMap["Cơm trắng"]!!, nutrient = nutMap["Fat"]!!, amount = BigDecimal("0.3")),
                        IngredientNutrient(ingredient = ingMap["Cơm trắng"]!!, nutrient = nutMap["Fiber"]!!, amount = BigDecimal("0.4")),
    
                        // Cơm gạo lứt
                        IngredientNutrient(ingredient = ingMap["Cơm gạo lứt"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("111.0")),
                        IngredientNutrient(ingredient = ingMap["Cơm gạo lứt"]!!, nutrient = nutMap["Carbohydrate"]!!, amount = BigDecimal("23.0")),
                        IngredientNutrient(ingredient = ingMap["Cơm gạo lứt"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("2.6")),
                        IngredientNutrient(ingredient = ingMap["Cơm gạo lứt"]!!, nutrient = nutMap["Fat"]!!, amount = BigDecimal("0.9")),
                        IngredientNutrient(ingredient = ingMap["Cơm gạo lứt"]!!, nutrient = nutMap["Fiber"]!!, amount = BigDecimal("1.8")),
    
                        // === Bún tươi ===
                        IngredientNutrient(ingredient = ingMap["Bún tươi"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("109.0")),
                        IngredientNutrient(ingredient = ingMap["Bún tươi"]!!, nutrient = nutMap["Carbohydrate"]!!, amount = BigDecimal("25.0")),
                        IngredientNutrient(ingredient = ingMap["Bún tươi"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("2.0")),
    
                        // Mì trứng
                        IngredientNutrient(ingredient = ingMap["Mì trứng"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("138.0")),
                        IngredientNutrient(ingredient = ingMap["Mì trứng"]!!, nutrient = nutMap["Carbohydrate"]!!, amount = BigDecimal("25.0")),
                        IngredientNutrient(ingredient = ingMap["Mì trứng"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("4.5")),
                        IngredientNutrient(ingredient = ingMap["Mì trứng"]!!, nutrient = nutMap["Fat"]!!, amount = BigDecimal("4.0")),
                        IngredientNutrient(ingredient = ingMap["Mì trứng"]!!, nutrient = nutMap["Sodium"]!!, amount = BigDecimal("140.0")),
                        IngredientNutrient(ingredient = ingMap["Mì trứng"]!!, nutrient = nutMap["Vitamin B2"]!!, amount = BigDecimal("0.3")),
    
                        // === Bánh mì ===
                        IngredientNutrient(ingredient = ingMap["Bánh mì"]!!, nutrient = nutMap["Calories"]!!, amount = BigDecimal("265.0")),
                        IngredientNutrient(ingredient = ingMap["Bánh mì"]!!, nutrient = nutMap["Carbohydrate"]!!, amount = BigDecimal("49.0")),
                        IngredientNutrient(ingredient = ingMap["Bánh mì"]!!, nutrient = nutMap["Protein"]!!, amount = BigDecimal("9.0")),
                        IngredientNutrient(ingredient = ingMap["Bánh mì"]!!, nutrient = nutMap["Fiber"]!!, amount = BigDecimal("2.7"))
                    )
    
                    ingredientNutrientRepository.saveAll(ingredientNutrients)
                    println("✅ Inserted ${ingredientNutrients.size} ingredient_nutrients")
                }

            }



        }
        

        if (cookingMethodRepository.count() < 1L) {
            // ===== COOKING METHODS ======
            val cookingMethods = listOf(
                CookingMethod(
                    name = "Luộc",
                    note = "Giữ lại nhiều vitamin nhưng có thể làm mất khoáng chất tan trong nước.",
                    basePrice = BigDecimal("1000.00")
                ),
                CookingMethod(
                    name = "Chiên",
                    note = "Tăng hương vị, nhưng thường làm tăng chất béo và calo.",
                    basePrice = BigDecimal("3000.00")
                ),
                CookingMethod(
                    name = "Nướng",
                    note = "Giảm chất béo do mỡ chảy ra ngoài, giữ được hương vị đặc trưng.",
                    basePrice = BigDecimal("4000.00")
                ),
                CookingMethod(
                    name = "Hấp",
                    note = "Giữ được nhiều vitamin, không cần thêm dầu mỡ.",
                    basePrice = BigDecimal("2000.00")
                ),
                CookingMethod(
                    name = "Xào",
                    note = "Nấu nhanh với ít dầu, giữ màu sắc và độ giòn của rau củ.",
                    basePrice = BigDecimal("2500.00")
                ),
                CookingMethod(
                    name = "Kho",
                    note = "Thường dùng nước mắm, đường và gia vị để tạo vị đậm đà.",
                    basePrice = BigDecimal("2200.00")
                ),
                CookingMethod(
                    name = "Hầm",
                    note = "Nấu lâu ở lửa nhỏ, làm mềm thịt và giải phóng chất dinh dưỡng trong xương.",
                    basePrice = BigDecimal("5000.00")
                ),
                CookingMethod(
                    name = "Ninh",
                    note = "Tương tự hầm nhưng chủ yếu để lấy nước dùng trong, ngọt từ xương/rau.",
                    basePrice = BigDecimal("4800.00")
                ),
                CookingMethod(
                    name = "Rang",
                    note = "Dùng ít dầu hoặc muối để tạo hương vị thơm đặc trưng.",
                    basePrice = BigDecimal("2000.00")
                ),
                CookingMethod(
                    name = "Muối chua/Ngâm",
                    note = "Dùng lên men tự nhiên hoặc ngâm gia vị để bảo quản thực phẩm.",
                    basePrice = BigDecimal("1500.00")
                ),
                CookingMethod(
                    name = "Không",
                    note = "Giữ nguyên trạng thái tươi sống của thực phẩm.",
                    basePrice = BigDecimal("0.00")
                )
            )
            
            val savedMethods = cookingMethodRepository.saveAll(cookingMethods)
            println("✅ Inserted ${savedMethods.size} cooking methods")

            if (cookingEffectRepository.count() < 1L) {
                // ===== COOKING EFFECTS ======
                
                val effects = listOf(
                    // Luộc (id = 1)
                    CookingEffect(method=savedMethods[0], nutrientId=16L, effectType=EffectType.DECREASE, value=20, description="Luộc làm giảm ~20% Vitamin C tan trong nước"),
                    CookingEffect(method=savedMethods[0], nutrientId=8L, effectType=EffectType.DECREASE, value=15, description="Khoáng chất Kali tan trong nước giảm"),
                    CookingEffect(method=savedMethods[0], nutrientId=5L, effectType=EffectType.DECREASE, value=10, description="Luộc làm rau mềm, hao hụt chất xơ nhẹ"),

                    // Chiên (id = 2)
                    CookingEffect(method=savedMethods[1], nutrientId=4L, effectType=EffectType.INCREASE, value=30, description="Chiên tăng hàm lượng chất béo ~30%"),
                    CookingEffect(method=savedMethods[1], nutrientId=1L, effectType=EffectType.INCREASE, value=25, description="Chiên làm tăng calo tổng thể do dầu"),
                    CookingEffect(method=savedMethods[1], nutrientId=16L, effectType=EffectType.DECREASE, value=15, description="Vitamin C dễ phân hủy ở nhiệt độ cao khi chiên"),

                    // Nướng (id = 3)
                    CookingEffect(method=savedMethods[2], nutrientId=4L, effectType=EffectType.DECREASE, value=10, description="Nướng giúp giảm một phần chất béo chảy ra ngoài"),
                    CookingEffect(method=savedMethods[2], nutrientId=1L, effectType=EffectType.DECREASE, value=5,  description="Lượng calo giảm nhẹ nhờ mất chất béo"),
                    CookingEffect(method=savedMethods[2], nutrientId=11L, effectType=EffectType.DECREASE, value=20, description="Vitamin A giảm do nhiệt độ cao"),

                    // Hấp (id = 4)
                    CookingEffect(method=savedMethods[3], nutrientId=16L, effectType=EffectType.DECREASE, value=5, description="Hấp làm hao hụt rất ít vitamin C"),
                    CookingEffect(method=savedMethods[3], nutrientId=8L, effectType=EffectType.DECREASE, value=5, description="Khoáng chất Kali giảm nhẹ do hơi nước"),

                    // Xào (id = 5)
                    CookingEffect(method=savedMethods[4], nutrientId=16L, effectType=EffectType.DECREASE, value=10, description="Xào làm mất một phần vitamin C"),
                    CookingEffect(method=savedMethods[4], nutrientId=4L, effectType=EffectType.INCREASE, value=15, description="Dầu ăn bổ sung thêm chất béo"),
                    CookingEffect(method=savedMethods[4], nutrientId=1L, effectType=EffectType.INCREASE, value=10, description="Calo tăng nhẹ khi xào"),

                    // Kho (id = 6)
                    CookingEffect(method=savedMethods[5], nutrientId=7L, effectType=EffectType.INCREASE, value=20, description="Kho với nước mắm làm tăng lượng Natri"),
                    CookingEffect(method=savedMethods[5], nutrientId=6L, effectType=EffectType.INCREASE, value=15, description="Đường và gia vị làm tăng đường tổng thể"),
                    CookingEffect(method=savedMethods[5], nutrientId=2L, effectType=EffectType.DECREASE, value=5, description="Protein hao hụt nhẹ do nấu lâu"),

                    // Hầm (id = 7)
                    CookingEffect(method=savedMethods[6], nutrientId=9L, effectType=EffectType.INCREASE, value=30, description="Hầm giúp canxi và khoáng chất tan vào nước"),
                    CookingEffect(method=savedMethods[6], nutrientId=20L, effectType=EffectType.INCREASE, value=25, description="Magie và vi chất trong xương tan ra"),
                    CookingEffect(method=savedMethods[6], nutrientId=16L, effectType=EffectType.DECREASE, value=10, description="Vitamin C giảm khi nấu lâu"),

                    // Ninh (id = 8)
                    CookingEffect(method=savedMethods[7], nutrientId=9L, effectType=EffectType.INCREASE, value=35, description="Ninh lấy nước dùng giàu canxi"),
                    CookingEffect(method=savedMethods[7], nutrientId=10L, effectType=EffectType.INCREASE, value=10, description="Sắt từ xương/rau củ tan ra nước"),
                    CookingEffect(method=savedMethods[7], nutrientId=2L, effectType=EffectType.DECREASE, value=8, description="Protein bị phân hủy nhẹ khi ninh lâu"),

                    // Rang (id = 9)
                    CookingEffect(method=savedMethods[8], nutrientId=16L, effectType=EffectType.DECREASE, value=12, description="Rang làm mất một ít vitamin C"),
                    CookingEffect(method=savedMethods[8], nutrientId=11L, effectType=EffectType.DECREASE, value=15, description="Vitamin A hao hụt do nhiệt khô"),
                    CookingEffect(method=savedMethods[8], nutrientId=1L, effectType=EffectType.INCREASE, value=5, description="Calo tăng nhẹ khi rang khô"),

                    // Muối chua/Ngâm (id = 10)
                    CookingEffect(method=savedMethods[9], nutrientId=16L, effectType=EffectType.DECREASE, value=25, description="Muối chua làm hao hụt vitamin C"),
                    CookingEffect(method=savedMethods[9], nutrientId=7L, effectType=EffectType.INCREASE, value=40, description="Tăng natri do muối"),
                    CookingEffect(method=savedMethods[9], nutrientId=5L, effectType=EffectType.INCREASE, value=10, description="Quá trình lên men tạo thêm chất xơ hòa tan"),
                )

                cookingEffectRepository.saveAll(effects)
                println("✅ Inserted ${effects.size} cooking effects")
            }
        
        }

        

        

        // // ===== RECIPES =====
        // val recipes = listOf(
        //     Recipe(
        //         name = "Cơm gà luộc",
        //         description = "Cơm trắng ăn kèm thịt gà luộc và rau muống.",
        //         isCustomizable = false,
        //         basePrice = BigDecimal("35000"),
        //         baseCal = 500,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Cơm bò xào",
        //         description = "Cơm gạo lứt với thịt bò xào cà rốt và dưa leo.",
        //         isCustomizable = false,
        //         basePrice = BigDecimal("45000"),
        //         baseCal = 600,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Mì trứng hải sản",
        //         description = "Mì trứng ăn kèm tôm sú, mực ống và cà chua.",
        //         isCustomizable = true,
        //         basePrice = BigDecimal("50000"),
        //         baseCal = 650,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Bánh mì thịt bò nướng",
        //         description = "Ổ bánh mì kẹp thịt bò nướng, dưa leo và cà chua.",
        //         isCustomizable = true,
        //         basePrice = BigDecimal("40000"),
        //         baseCal = 550,
        //         category = RecipeCategory.SNACK,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Salad rau củ trộn",
        //         description = "Rau muống, cà rốt, cà chua, dưa leo trộn dầu giấm.",
        //         isCustomizable = true,
        //         basePrice = BigDecimal("30000"),
        //         baseCal = 200,
        //         category = RecipeCategory.SNACK,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Cơm cá hồi hấp",
        //         description = "Cơm trắng ăn kèm cá hồi hấp và cà rốt.",
        //         isCustomizable = false,
        //         basePrice = BigDecimal("60000"),
        //         baseCal = 550,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Cơm sườn heo kho",
        //         description = "Cơm gạo lứt với thịt heo kho, kèm rau muống.",
        //         isCustomizable = false,
        //         basePrice = BigDecimal("50000"),
        //         baseCal = 650,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Bún bò xào",
        //         description = "Bún tươi với thịt bò xào khoai tây và cà rốt.",
        //         isCustomizable = false,
        //         basePrice = BigDecimal("45000"),
        //         baseCal = 580,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Mực nướng cơm trắng",
        //         description = "Mực ống nướng ăn kèm cơm trắng và rau muống.",
        //         isCustomizable = true,
        //         basePrice = BigDecimal("55000"),
        //         baseCal = 500,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Ghẹ rang me",
        //         description = "Ghẹ biển rang me ăn kèm cơm trắng.",
        //         isCustomizable = false,
        //         basePrice = BigDecimal("65000"),
        //         baseCal = 700,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Cơm gà chiên mắm",
        //         description = "Cơm gạo lứt với gà chiên mắm, kèm dưa leo.",
        //         isCustomizable = false,
        //         basePrice = BigDecimal("52000"),
        //         baseCal = 680,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Mì trứng thịt heo xào",
        //         description = "Mì trứng xào thịt heo và cà chua.",
        //         isCustomizable = true,
        //         basePrice = BigDecimal("40000"),
        //         baseCal = 520,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Cơm bò nướng rau củ",
        //         description = "Cơm trắng ăn kèm thịt bò nướng, cà rốt, khoai tây.",
        //         isCustomizable = false,
        //         basePrice = BigDecimal("60000"),
        //         baseCal = 700,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Salad hải sản",
        //         description = "Rau củ trộn cùng tôm sú, mực và cà chua.",
        //         isCustomizable = true,
        //         basePrice = BigDecimal("55000"),
        //         baseCal = 400,
        //         category = RecipeCategory.SNACK,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Cơm dê hấp",
        //         description = "Cơm gạo lứt ăn kèm thịt dê hấp.",
        //         isCustomizable = false,
        //         basePrice = BigDecimal("58000"),
        //         baseCal = 620,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Bánh mì ốp la bò",
        //         description = "Bánh mì kẹp thịt bò và trứng ốp la.",
        //         isCustomizable = true,
        //         basePrice = BigDecimal("45000"),
        //         baseCal = 500,
        //         category = RecipeCategory.SNACK,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Cơm vịt kho gừng",
        //         description = "Cơm trắng ăn với thịt vịt kho gừng.",
        //         isCustomizable = false,
        //         basePrice = BigDecimal("55000"),
        //         baseCal = 650,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Bún thịt gà xào",
        //         description = "Bún tươi xào thịt gà và rau muống.",
        //         isCustomizable = true,
        //         basePrice = BigDecimal("45000"),
        //         baseCal = 520,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Mì trứng cá thu chiên",
        //         description = "Mì trứng ăn kèm cá thu chiên và cà chua.",
        //         isCustomizable = true,
        //         basePrice = BigDecimal("50000"),
        //         baseCal = 600,
        //         category = RecipeCategory.MAIN,
        //         isActive = true
        //     ),
        //     Recipe(
        //         name = "Cơm gà xào rau củ",
        //         description = "Cơm trắng xào gà với cà rốt và khoai tây.",
        //         isCustomizable = true,
        //         basePrice = BigDecimal("48000"),
        //         baseCal = 550,
        //         category = RecipeCategory.MAIN,
        //         isActive = true 
        //     )
        // )

        // val savedRecipes = recipeRepository.saveAll(recipes)
        // println("✅ Inserted ${savedRecipes.size} recipes")

        // // ===== RECIPE INGREDIENTS =====
        // val recipeIngredients = listOf(
        //     // 1. Cơm gà luộc
        //     RecipeIngredient(recipe = savedRecipes[0], ingredient = ingMap["Cơm trắng"]!!, quantity = 150, cookingMethod = savedMethods[0]), // cơm luộc
        //     RecipeIngredient(recipe = savedRecipes[0], ingredient = ingMap["Thịt gà"]!!, quantity = 120, cookingMethod = savedMethods[0]),   // gà luộc
        //     RecipeIngredient(recipe = savedRecipes[0], ingredient = ingMap["Rau muống"]!!, quantity = 80, cookingMethod = savedMethods[0]),

        //     // 2. Cơm bò xào
        //     RecipeIngredient(recipe = savedRecipes[1], ingredient = ingMap["Cơm gạo lứt"]!!, quantity = 150, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[1], ingredient = ingMap["Thịt bò"]!!, quantity = 100, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[1], ingredient = ingMap["Cà rốt"]!!, quantity = 50, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[1], ingredient = ingMap["Dưa leo"]!!, quantity = 40, cookingMethod = savedMethods[4]),

        //     // 3. Mì trứng hải sản
        //     RecipeIngredient(recipe = savedRecipes[2], ingredient = ingMap["Mì trứng"]!!, quantity = 120, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[2], ingredient = ingMap["Tôm sú"]!!, quantity = 80, cookingMethod = savedMethods[1]),
        //     RecipeIngredient(recipe = savedRecipes[2], ingredient = ingMap["Mực ống"]!!, quantity = 80, cookingMethod = savedMethods[2]),
        //     RecipeIngredient(recipe = savedRecipes[2], ingredient = ingMap["Cà chua"]!!, quantity = 40, cookingMethod = savedMethods[0]),

        //     // 4. Bánh mì thịt bò nướng
        //     RecipeIngredient(recipe = savedRecipes[3], ingredient = ingMap["Bánh mì"]!!, quantity = 100, cookingMethod = savedMethods[10]), // không nấu
        //     RecipeIngredient(recipe = savedRecipes[3], ingredient = ingMap["Thịt bò"]!!, quantity = 120, cookingMethod = savedMethods[2]),
        //     RecipeIngredient(recipe = savedRecipes[3], ingredient = ingMap["Dưa leo"]!!, quantity = 30, cookingMethod = savedMethods[10]),
        //     RecipeIngredient(recipe = savedRecipes[3], ingredient = ingMap["Cà chua"]!!, quantity = 30, cookingMethod = savedMethods[10]),

        //     // 5. Salad rau củ trộn
        //     RecipeIngredient(recipe = savedRecipes[4], ingredient = ingMap["Rau muống"]!!, quantity = 60, cookingMethod = savedMethods[10]),
        //     RecipeIngredient(recipe = savedRecipes[4], ingredient = ingMap["Cà rốt"]!!, quantity = 40, cookingMethod = savedMethods[10]),
        //     RecipeIngredient(recipe = savedRecipes[4], ingredient = ingMap["Cà chua"]!!, quantity = 40, cookingMethod = savedMethods[10]),
        //     RecipeIngredient(recipe = savedRecipes[4], ingredient = ingMap["Dưa leo"]!!, quantity = 40, cookingMethod = savedMethods[10]),

        //     // 6. Cơm cá hồi hấp
        //     RecipeIngredient(recipe = savedRecipes[5], ingredient = ingMap["Cơm trắng"]!!, quantity = 150, cookingMethod = savedMethods[0]),
        //     RecipeIngredient(recipe = savedRecipes[5], ingredient = ingMap["Cá hồi"]!!, quantity = 120, cookingMethod = savedMethods[3]),
        //     RecipeIngredient(recipe = savedRecipes[5], ingredient = ingMap["Cà rốt"]!!, quantity = 60, cookingMethod = savedMethods[3]),

        //     // 7. Cơm sườn heo kho
        //     RecipeIngredient(recipe = savedRecipes[6], ingredient = ingMap["Cơm gạo lứt"]!!, quantity = 150, cookingMethod = savedMethods[0]),
        //     RecipeIngredient(recipe = savedRecipes[6], ingredient = ingMap["Thịt heo"]!!, quantity = 120, cookingMethod = savedMethods[5]),
        //     RecipeIngredient(recipe = savedRecipes[6], ingredient = ingMap["Rau muống"]!!, quantity = 70, cookingMethod = savedMethods[0]),

        //     // 8. Bún bò xào
        //     RecipeIngredient(recipe = savedRecipes[7], ingredient = ingMap["Bún tươi"]!!, quantity = 150, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[7], ingredient = ingMap["Thịt bò"]!!, quantity = 100, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[7], ingredient = ingMap["Khoai tây"]!!, quantity = 60, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[7], ingredient = ingMap["Cà rốt"]!!, quantity = 50, cookingMethod = savedMethods[4]),

        //     // 9. Mực nướng cơm trắng
        //     RecipeIngredient(recipe = savedRecipes[8], ingredient = ingMap["Cơm trắng"]!!, quantity = 150, cookingMethod = savedMethods[0]),
        //     RecipeIngredient(recipe = savedRecipes[8], ingredient = ingMap["Mực ống"]!!, quantity = 100, cookingMethod = savedMethods[2]),
        //     RecipeIngredient(recipe = savedRecipes[8], ingredient = ingMap["Rau muống"]!!, quantity = 70, cookingMethod = savedMethods[0]),

        //     // 10. Ghẹ rang me
        //     RecipeIngredient(recipe = savedRecipes[9], ingredient = ingMap["Ghẹ biển"]!!, quantity = 150, cookingMethod = savedMethods[8]),
        //     RecipeIngredient(recipe = savedRecipes[9], ingredient = ingMap["Cơm trắng"]!!, quantity = 150, cookingMethod = savedMethods[0]),

        //     // 11. Cơm gà chiên mắm
        //     RecipeIngredient(recipe = savedRecipes[10], ingredient = ingMap["Cơm gạo lứt"]!!, quantity = 150, cookingMethod = savedMethods[0]),
        //     RecipeIngredient(recipe = savedRecipes[10], ingredient = ingMap["Thịt gà"]!!, quantity = 120, cookingMethod = savedMethods[1]),
        //     RecipeIngredient(recipe = savedRecipes[10], ingredient = ingMap["Dưa leo"]!!, quantity = 40, cookingMethod = savedMethods[10]),

        //     // 12. Mì trứng thịt heo xào
        //     RecipeIngredient(recipe = savedRecipes[11], ingredient = ingMap["Mì trứng"]!!, quantity = 150, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[11], ingredient = ingMap["Thịt heo"]!!, quantity = 100, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[11], ingredient = ingMap["Cà chua"]!!, quantity = 40, cookingMethod = savedMethods[0]),

        //     // 13. Cơm bò nướng rau củ
        //     RecipeIngredient(recipe = savedRecipes[12], ingredient = ingMap["Cơm trắng"]!!, quantity = 150, cookingMethod = savedMethods[0]),
        //     RecipeIngredient(recipe = savedRecipes[12], ingredient = ingMap["Thịt bò"]!!, quantity = 120, cookingMethod = savedMethods[2]),
        //     RecipeIngredient(recipe = savedRecipes[12], ingredient = ingMap["Cà rốt"]!!, quantity = 50, cookingMethod = savedMethods[3]),
        //     RecipeIngredient(recipe = savedRecipes[12], ingredient = ingMap["Khoai tây"]!!, quantity = 60, cookingMethod = savedMethods[3]),

        //     // 14. Salad hải sản
        //     RecipeIngredient(recipe = savedRecipes[13], ingredient = ingMap["Tôm sú"]!!, quantity = 70, cookingMethod = savedMethods[3]),
        //     RecipeIngredient(recipe = savedRecipes[13], ingredient = ingMap["Mực ống"]!!, quantity = 70, cookingMethod = savedMethods[3]),
        //     RecipeIngredient(recipe = savedRecipes[13], ingredient = ingMap["Cà chua"]!!, quantity = 40, cookingMethod = savedMethods[10]),
        //     RecipeIngredient(recipe = savedRecipes[13], ingredient = ingMap["Dưa leo"]!!, quantity = 40, cookingMethod = savedMethods[10]),

        //     // 15. Cơm dê hấp
        //     RecipeIngredient(recipe = savedRecipes[14], ingredient = ingMap["Cơm gạo lứt"]!!, quantity = 150, cookingMethod = savedMethods[0]),
        //     RecipeIngredient(recipe = savedRecipes[14], ingredient = ingMap["Thịt dê"]!!, quantity = 120, cookingMethod = savedMethods[3]),

        //     // 16. Bánh mì ốp la bò
        //     RecipeIngredient(recipe = savedRecipes[15], ingredient = ingMap["Bánh mì"]!!, quantity = 100, cookingMethod = savedMethods[10]),
        //     RecipeIngredient(recipe = savedRecipes[15], ingredient = ingMap["Thịt bò"]!!, quantity = 100, cookingMethod = savedMethods[2]),
        //     RecipeIngredient(recipe = savedRecipes[15], ingredient = ingMap["Mì trứng"]!!, quantity = 50, cookingMethod = savedMethods[6]),

        //     // 17. Cơm vịt kho gừng
        //     RecipeIngredient(recipe = savedRecipes[16], ingredient = ingMap["Cơm trắng"]!!, quantity = 150, cookingMethod = savedMethods[0]),
        //     RecipeIngredient(recipe = savedRecipes[16], ingredient = ingMap["Thịt vịt"]!!, quantity = 120, cookingMethod = savedMethods[5]),

        //     // 18. Bún thịt gà xào
        //     RecipeIngredient(recipe = savedRecipes[17], ingredient = ingMap["Bún tươi"]!!, quantity = 150, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[17], ingredient = ingMap["Thịt gà"]!!, quantity = 100, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[17], ingredient = ingMap["Rau muống"]!!, quantity = 60, cookingMethod = savedMethods[4]),

        //     // 19. Mì trứng cá thu chiên
        //     RecipeIngredient(recipe = savedRecipes[18], ingredient = ingMap["Mì trứng"]!!, quantity = 150, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[18], ingredient = ingMap["Cá thu"]!!, quantity =  100, cookingMethod = savedMethods[1]),
        //     RecipeIngredient(recipe = savedRecipes[18], ingredient = ingMap["Cà chua"]!!, quantity = 40, cookingMethod = savedMethods[0]),

        //     // 20. Cơm gà xào rau củ
        //     RecipeIngredient(recipe = savedRecipes[19], ingredient = ingMap["Cơm trắng"]!!, quantity = 150, cookingMethod = savedMethods[0]),
        //     RecipeIngredient(recipe = savedRecipes[19], ingredient = ingMap["Thịt gà"]!!, quantity =  100, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[19], ingredient = ingMap["Cà rốt"]!!, quantity = 50, cookingMethod = savedMethods[4]),
        //     RecipeIngredient(recipe = savedRecipes[19], ingredient = ingMap["Khoai tây"]!!, quantity = 60, cookingMethod = savedMethods[4])
        // )

        // recipeIngredientRepository.saveAll(recipeIngredients)
        // println("✅ Inserted ${recipeIngredients.size} recipe_ingredients")

        println("✅ Sample data initialized!")




    }
}
