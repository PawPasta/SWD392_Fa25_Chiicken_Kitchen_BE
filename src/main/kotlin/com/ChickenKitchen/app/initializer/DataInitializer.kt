package com.ChickenKitchen.app.initializer

import com.ChickenKitchen.app.enums.DiscountType
import com.ChickenKitchen.app.enums.Role
import com.ChickenKitchen.app.enums.UnitType
import com.ChickenKitchen.app.model.entity.category.Category
import com.ChickenKitchen.app.enums.MenuCategory
import com.ChickenKitchen.app.model.entity.ingredient.Ingredient
import com.ChickenKitchen.app.model.entity.ingredient.Recipe
import com.ChickenKitchen.app.model.entity.ingredient.Store
import com.ChickenKitchen.app.model.entity.ingredient.StoreIngredientBatch
import com.ChickenKitchen.app.model.entity.menu.DailyMenu
import com.ChickenKitchen.app.model.entity.menu.DailyMenuItem
import com.ChickenKitchen.app.model.entity.menu.MenuItem
import com.ChickenKitchen.app.model.entity.menu.MenuItemNutrient
import com.ChickenKitchen.app.model.entity.menu.Nutrient
import com.ChickenKitchen.app.model.entity.payment.PaymentMethod
import com.ChickenKitchen.app.model.entity.promotion.Promotion
import com.ChickenKitchen.app.model.entity.step.Step
import com.ChickenKitchen.app.model.entity.user.User
import com.ChickenKitchen.app.repository.category.CategoryRepository
import com.ChickenKitchen.app.repository.ingredient.IngredientRepository
import com.ChickenKitchen.app.repository.ingredient.RecipeRepository
import com.ChickenKitchen.app.repository.ingredient.StoreIngredientBatchRepository
import com.ChickenKitchen.app.repository.ingredient.StoreRepository
import com.ChickenKitchen.app.repository.menu.DailyMenuRepository
import com.ChickenKitchen.app.repository.menu.MenuItemNutrientRepository
import com.ChickenKitchen.app.repository.menu.MenuItemRepository
import com.ChickenKitchen.app.repository.menu.NutrientRepository
import com.ChickenKitchen.app.repository.payment.PaymentMethodRepository
import com.ChickenKitchen.app.repository.promotion.PromotionRepository
import com.ChickenKitchen.app.repository.step.StepRepository
import com.ChickenKitchen.app.repository.user.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Timestamp
import java.time.LocalDateTime
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Configuration
class DataInitializer {

    @Bean
    @Transactional
    fun initData(
        userRepository: UserRepository,
        storeRepository: StoreRepository,
        categoryRepository: CategoryRepository,
        stepRepository: StepRepository,
        menuItemRepository: MenuItemRepository,
        nutrientRepository: NutrientRepository,
        menuItemNutrientRepository: MenuItemNutrientRepository,
        ingredientRepository: IngredientRepository,
        recipeRepository: RecipeRepository,
        promotionRepository: PromotionRepository,
        paymentMethodRepository: PaymentMethodRepository,
        dailyMenuRepository: DailyMenuRepository,
        storeIngredientBatchRepository: StoreIngredientBatchRepository
    ) = CommandLineRunner {

        // ==================== USERS ====================
        if (userRepository.count() == 0L) {
            println("Seeding users...")

            // Admin users
            userRepository.save(User(
                role = Role.ADMIN,
                uid = "admin-uid-001",
                email = "chickenkitchen785@gmail.com",
                isVerified = true,
                phone = "0901234567",
                isActive = true,
                fullName = "Admin Con Ga",
                provider = "Local",
                imageURL = null
            ))

            userRepository.save(User(
                role = Role.ADMIN,
                uid = "admin-uid-002",
                email = "admin2@chickenkitchen.com",
                isVerified = true,
                phone = "0901234590",
                isActive = true,
                fullName = "Admin Nguyen Van A",
                provider = "Google",
                imageURL = "https://example.com/admin2.jpg"
            ))

            // Manager users
            userRepository.save(User(
                role = Role.MANAGER,
                uid = "manager-uid-001",
                email = "khiem1371@gmail.com",
                isVerified = true,
                phone = "0901234568",
                isActive = true,
                fullName = "Manager Tran",
                provider = "Local",
                imageURL = null
            ))

            userRepository.save(User(
                role = Role.MANAGER,
                uid = "manager-uid-002",
                email = "manager2@chickenkitchen.com",
                isVerified = true,
                phone = "0901234591",
                isActive = true,
                fullName = "Manager Le Thi B",
                provider = "Local",
                imageURL = "https://example.com/manager2.jpg"
            ))

            // Employee users
            val employeeData = listOf(
                Triple("employee-uid-001", "baoltgse182138@fpt.edu.vn", "Employee Le"),
                Triple("employee-uid-002", "employee2@chickenkitchen.com", "Employee Pham"),
                Triple("employee-uid-003", "employee3@chickenkitchen.com", "Employee Vo Minh C"),
                Triple("employee-uid-004", "employee4@chickenkitchen.com", "Employee Hoang Thi D"),
                Triple("employee-uid-005", "employee5@chickenkitchen.com", "Employee Tran Van E"),
                Triple("employee-uid-006", "employee6@chickenkitchen.com", "Employee Nguyen Thi F")
            )

            employeeData.forEachIndexed { idx, (uid, email, name) ->
                userRepository.save(User(
                    role = Role.EMPLOYEE,
                    uid = uid,
                    email = email,
                    isVerified = true,
                    phone = "090123456${9 + idx}",
                    isActive = true,
                    fullName = name,
                    provider = "Local",
                    imageURL = null
                ))
            }

            // Store user
            userRepository.save(User(
                role = Role.STORE,
                uid = "store-uid-002",
                email = "letrangiabao2004@gmail.com",
                isVerified = true,
                phone = "0901234570",
                isActive = true,
                fullName = "Store",
                provider = "Local",
                imageURL = null
            ))

            println("✓ Users seeded: ${1 + 1 + employeeData.size + 1 + 2}")
        } else {
            println("⏭ Users table not empty, skipping")
        }

        // ==================== STORES ====================
        if (storeRepository.count() == 0L) {
            println("Seeding stores...")

            val storesData = listOf(
                Triple("Chicken Kitchen Binh Thanh", "720A Điện Biên Phủ, Phường 22, Bình Thạnh, Thành phố Hồ Chí Minh", "0281234567"),
                Triple("Chicken Kitchen High Technology bay", "Lô E2a-7, Đường D1, Khu Công nghệ cao, Phường Long Thạnh Mỹ, Thành phố Thủ Đức, Thành phố Hồ Chí Minh", "0281234568"),
                Triple("Chicken Kitchen District 3", "643 Điện Biên Phủ, Quận 3, Thành phố Hồ Chí Minh.", "0281234569"),
                Triple("Chicken Kitchen Binh Duong", "Đường Lưu Hữu Phước, phường Đông Hòa, thành phố Dĩ An, tỉnh Bình Dương", "0281234570"),
                Triple("Chicken Kitchen Phu Nhuan", "124 Phan Xích Long, Phường 2, Phú Nhuận, Thành phố Hồ Chí Minh ", "0281234571"),
                Triple("Chicken Kitchen Binh Chanh", "240-242 Phạm Văn Đồng, phường Hiệp Bình Chánh, quận Thủ Đức, Thành phố Hồ Chí Minh", "0281234572")
            )

            storesData.forEach { (name, address, phone) ->
                storeRepository.save(Store(
                    name = name,
                    address = address,
                    phone = phone,
                    isActive = true
                ))
            }

            println("✓ Stores seeded: ${storesData.size}")
        } else {
            println("⏭ Stores table not empty, skipping")
        }

        // ==================== PAYMENT METHODS ====================
        if (paymentMethodRepository.count() == 0L) {
            println("Seeding payment methods...")

            val paymentMethods = listOf(
                Triple("Cash", "Pay with cash on pickup", true),
                Triple("MoMo", "Pay with MoMo e-wallet", true),
                Triple("VNPay", "Pay with VNPay", true),
                Triple("ZaloPay", "Pay with ZaloPay e-wallet", true),
                Triple("Bank Transfer", "Pay via bank transfer", true),
                Triple("Credit Card", "Pay with Visa/Mastercard", false) // Inactive for testing
            )

            paymentMethods.forEach { (name, desc, active) ->
                paymentMethodRepository.save(PaymentMethod(
                    name = name,
                    description = desc,
                    isActive = active
                ))
            }

            println("✓ Payment methods seeded: ${paymentMethods.size}")
        } else {
            println("⏭ Payment methods table not empty, skipping")
        }

        // ==================== PROMOTIONS ====================
        if (promotionRepository.count() == 0L) {
            println("Seeding promotions...")

            val promotions = listOf(
                // Active promotions
                Promotion(
                    discountType = DiscountType.PERCENT,
                    discountValue = 20,
                    startDate = LocalDateTime.now().minusDays(5),
                    endDate = LocalDateTime.now().plusDays(25),
                    isActive = true,
                    quantity = 100
                ),
                Promotion(
                    discountType = DiscountType.AMOUNT,
                    discountValue = 50000,
                    startDate = LocalDateTime.now().minusDays(3),
                    endDate = LocalDateTime.now().plusDays(10),
                    isActive = true,
                    quantity = 50
                ),
                Promotion(
                    discountType = DiscountType.PERCENT,
                    discountValue = 15,
                    startDate = LocalDateTime.now().minusDays(1),
                    endDate = LocalDateTime.now().plusDays(30),
                    isActive = true,
                    quantity = 200
                ),
                Promotion(
                    discountType = DiscountType.AMOUNT,
                    discountValue = 30000,
                    startDate = LocalDateTime.now(),
                    endDate = LocalDateTime.now().plusDays(7),
                    isActive = true,
                    quantity = 75
                ),
                // Expired promotion
                Promotion(
                    discountType = DiscountType.PERCENT,
                    discountValue = 25,
                    startDate = LocalDateTime.now().minusDays(30),
                    endDate = LocalDateTime.now().minusDays(1),
                    isActive = false,
                    quantity = 0
                ),
                // Future promotion
                Promotion(
                    discountType = DiscountType.PERCENT,
                    discountValue = 30,
                    startDate = LocalDateTime.now().plusDays(5),
                    endDate = LocalDateTime.now().plusDays(15),
                    isActive = true,
                    quantity = 150
                )
            )

            promotionRepository.saveAll(promotions)
            println("✓ Promotions seeded: ${promotions.size}")
        } else {
            println("⏭ Promotions table not empty, skipping")
        }

        // ==================== CATEGORIES ====================
        run {
            val categories = listOf(
                "Carbohydrates" to "Base carb selection",
                "Proteins" to "Protein selection",
                "Vegetables" to "Vegetable selection",
                "Sauces" to "Sauce selection",
                "Dairy" to "Dairy selection",
                "Fruits" to "Fruit selection"
            )
            var created = 0
            categories.forEach { (name, desc) ->
                if (!categoryRepository.existsByName(name)) {
                    categoryRepository.save(Category(name = name, description = desc))
                    created++
                }
            }
            if (created > 0) println("✓ Categories ensured/seeded: $created new")
            else println("⏭ Categories already present")
        }

        // ==================== STEPS ====================
        if (stepRepository.count() == 0L) {
            println("Seeding steps...")
            val carbCategory = categoryRepository.findByName("Carbohydrates")
            val proteinCategory = categoryRepository.findByName("Proteins")
            val vegetableCategory = categoryRepository.findByName("Vegetables")
            val sauceCategory = categoryRepository.findByName("Sauces")
            val dairyCategory = categoryRepository.findByName("Dairy")
            val fruitCategory = categoryRepository.findByName("Fruits")

            val steps = mutableListOf<Step>()

            if (carbCategory != null) {
                steps.add(Step(
                    category = carbCategory,
                    name = "Choose Your Base",
                    description = "Select your carbohydrate base",
                    stepNumber = 1
                ))
            }

            if (proteinCategory != null) {
                steps.add(Step(
                    category = proteinCategory,
                    name = "Choose Your Protein",
                    description = "Select your protein",
                    stepNumber = 2
                ))
            }

            if (vegetableCategory != null) {
                steps.add(Step(
                    category = vegetableCategory,
                    name = "Choose Your Vegetables",
                    description = "Select your vegetables",
                    stepNumber = 3
                ))
            }

            if (sauceCategory != null) {
                steps.add(Step(
                    category = sauceCategory,
                    name = "Add Your Sauce",
                    description = "Select your favorite sauce",
                    stepNumber = 4
                ))
            }

            if (dairyCategory != null) {
                steps.add(Step(
                    category = dairyCategory,
                    name = "Add Dairy (Optional)",
                    description = "Add cheese or dairy products",
                    stepNumber = 5
                ))
            }

            if (fruitCategory != null) {
                steps.add(Step(
                    category = fruitCategory,
                    name = "Add Fruit (Optional)",
                    description = "Add fresh fruits to your meal",
                    stepNumber = 6
                ))
            }

            if (steps.isNotEmpty()) {
                stepRepository.saveAll(steps)
                println("✓ Steps seeded: ${steps.size}")
            } else {
                println("⚠️ Cannot seed steps: categories missing")
            }
        } else {
            println("⏭ Steps table not empty, skipping")
        }

        // ==================== MENU ITEMS ====================
        if (menuItemRepository.count() == 0L) {
            println("Seeding menu items...")

            // Đổi từ Triple thành data class hoặc dùng Quadruple
            data class MenuItemSeed(val name: String, val category: Any, val imageUrl: String, val price: Int)

            val items: List<MenuItemSeed> = listOf(
                // CARB (22 items) - Giá từ 15,000 - 35,000 VND
                MenuItemSeed("White Rice", MenuCategory.CARB, "https://example.com/images/white-rice.jpg", 15000),
                MenuItemSeed("Brown Rice", MenuCategory.CARB, "https://example.com/images/brown-rice.jpg", 18000),
                MenuItemSeed("Quinoa", MenuCategory.CARB, "https://example.com/images/quinoa.jpg", 35000),
                MenuItemSeed("Whole Wheat Pasta", MenuCategory.CARB, "https://example.com/images/whole-wheat-pasta.jpg", 25000),
                MenuItemSeed("Sweet Potato", MenuCategory.CARB, "https://example.com/images/sweet-potato.jpg", 20000),
                MenuItemSeed("Mashed Potato", MenuCategory.CARB, "https://example.com/images/mashed-potato.jpg", 22000),
                MenuItemSeed("Udon Noodles", MenuCategory.CARB, "https://example.com/images/udon-noodles.jpg", 28000),
                MenuItemSeed("Couscous", MenuCategory.CARB, "https://example.com/images/couscous.jpg", 30000),
                MenuItemSeed("Oatmeal", MenuCategory.CARB, "https://example.com/images/oatmeal.jpg", 20000),
                MenuItemSeed("Garlic Bread", MenuCategory.CARB, "https://example.com/images/garlic-bread.jpg", 18000),
                MenuItemSeed("Jasmine Rice", MenuCategory.CARB, "https://example.com/images/jasmine-rice.jpg", 15000),
                MenuItemSeed("Basmati Rice", MenuCategory.CARB, "https://example.com/images/basmati-rice.jpg", 20000),
                MenuItemSeed("Soba Noodles", MenuCategory.CARB, "https://example.com/images/soba-noodles.jpg", 32000),
                MenuItemSeed("Rice Noodles", MenuCategory.CARB, "https://example.com/images/rice-noodles.jpg", 18000),
                MenuItemSeed("Buckwheat", MenuCategory.CARB, "https://example.com/images/buckwheat.jpg", 25000),
                MenuItemSeed("Barley", MenuCategory.CARB, "https://example.com/images/barley.jpg", 22000),
                MenuItemSeed("Bulgur", MenuCategory.CARB, "https://example.com/images/bulgur.jpg", 28000),
                MenuItemSeed("Polenta", MenuCategory.CARB, "https://example.com/images/polenta.jpg", 25000),
                MenuItemSeed("Farro", MenuCategory.CARB, "https://example.com/images/farro.jpg", 30000),
                MenuItemSeed("Sourdough Bread", MenuCategory.CARB, "https://example.com/images/sourdough-bread.jpg", 20000),
                MenuItemSeed("Baguette", MenuCategory.CARB, "https://example.com/images/baguette.jpg", 15000),
                MenuItemSeed("Tortilla", MenuCategory.CARB, "https://example.com/images/tortilla.jpg", 18000),

                // PROTEIN (29 items) - Giá từ 25,000 - 80,000 VND
                MenuItemSeed("Grilled Chicken", MenuCategory.PROTEIN, "https://example.com/images/grilled-chicken.jpg", 45000),
                MenuItemSeed("Fried Chicken", MenuCategory.PROTEIN, "https://example.com/images/fried-chicken.jpg", 45000),
                MenuItemSeed("Beef Steak", MenuCategory.PROTEIN, "https://example.com/images/beef-steak.jpg", 80000),
                MenuItemSeed("Pork Chop", MenuCategory.PROTEIN, "https://example.com/images/pork-chop.jpg", 50000),
                MenuItemSeed("Firm Tofu", MenuCategory.PROTEIN, "https://example.com/images/firm-tofu.jpg", 25000),
                MenuItemSeed("Salmon Fillet", MenuCategory.PROTEIN, "https://example.com/images/salmon-fillet.jpg", 75000),
                MenuItemSeed("Tuna", MenuCategory.PROTEIN, "https://example.com/images/tuna.jpg", 60000),
                MenuItemSeed("Shrimp", MenuCategory.PROTEIN, "https://example.com/images/shrimp.jpg", 65000),
                MenuItemSeed("Boiled Egg", MenuCategory.PROTEIN, "https://example.com/images/boiled-egg.jpg", 10000),
                MenuItemSeed("Turkey Breast", MenuCategory.PROTEIN, "https://example.com/images/turkey-breast.jpg", 55000),
                MenuItemSeed("Roast Chicken", MenuCategory.PROTEIN, "https://example.com/images/roast-chicken.jpg", 50000),
                MenuItemSeed("Chicken Thigh", MenuCategory.PROTEIN, "https://example.com/images/chicken-thigh.jpg", 40000),
                MenuItemSeed("Chicken Wings", MenuCategory.PROTEIN, "https://example.com/images/chicken-wings.jpg", 45000),
                MenuItemSeed("Ground Beef", MenuCategory.PROTEIN, "https://example.com/images/ground-beef.jpg", 55000),
                MenuItemSeed("Ribeye Steak", MenuCategory.PROTEIN, "https://example.com/images/ribeye-steak.jpg", 85000),
                MenuItemSeed("Sirloin", MenuCategory.PROTEIN, "https://example.com/images/sirloin.jpg", 75000),
                MenuItemSeed("Pork Belly", MenuCategory.PROTEIN, "https://example.com/images/pork-belly.jpg", 55000),
                MenuItemSeed("Ham", MenuCategory.PROTEIN, "https://example.com/images/ham.jpg", 40000),
                MenuItemSeed("Bacon", MenuCategory.PROTEIN, "https://example.com/images/bacon.jpg", 45000),
                MenuItemSeed("Lamb Chops", MenuCategory.PROTEIN, "https://example.com/images/lamb-chops.jpg", 90000),
                MenuItemSeed("Duck Breast", MenuCategory.PROTEIN, "https://example.com/images/duck-breast.jpg", 70000),
                MenuItemSeed("Tempeh", MenuCategory.PROTEIN, "https://example.com/images/tempeh.jpg", 30000),
                MenuItemSeed("Seitan", MenuCategory.PROTEIN, "https://example.com/images/seitan.jpg", 35000),
                MenuItemSeed("Black Beans", MenuCategory.PROTEIN, "https://example.com/images/black-beans.jpg", 20000),
                MenuItemSeed("Chickpeas", MenuCategory.PROTEIN, "https://example.com/images/chickpeas.jpg", 22000),
                MenuItemSeed("Lentils", MenuCategory.PROTEIN, "https://example.com/images/lentils.jpg", 20000),
                MenuItemSeed("Edamame", MenuCategory.PROTEIN, "https://example.com/images/edamame.jpg", 25000),
                MenuItemSeed("Smoked Salmon", MenuCategory.PROTEIN, "https://example.com/images/smoked-salmon.jpg", 85000),
                MenuItemSeed("Sardines", MenuCategory.PROTEIN, "https://example.com/images/sardines.jpg", 35000),

                // VEGETABLE (24 items) - Giá từ 8,000 - 25,000 VND
                MenuItemSeed("Broccoli", MenuCategory.VEGETABLE, "https://example.com/images/broccoli.jpg", 15000),
                MenuItemSeed("Carrot", MenuCategory.VEGETABLE, "https://example.com/images/carrot.jpg", 10000),
                MenuItemSeed("Spinach", MenuCategory.VEGETABLE, "https://example.com/images/spinach.jpg", 12000),
                MenuItemSeed("Kale", MenuCategory.VEGETABLE, "https://example.com/images/kale.jpg", 18000),
                MenuItemSeed("Lettuce", MenuCategory.VEGETABLE, "https://example.com/images/lettuce.jpg", 10000),
                MenuItemSeed("Tomato", MenuCategory.VEGETABLE, "https://example.com/images/tomato.jpg", 12000),
                MenuItemSeed("Cucumber", MenuCategory.VEGETABLE, "https://example.com/images/cucumber.jpg", 8000),
                MenuItemSeed("Bell Pepper", MenuCategory.VEGETABLE, "https://example.com/images/bell-pepper.jpg", 15000),
                MenuItemSeed("Corn", MenuCategory.VEGETABLE, "https://example.com/images/corn.jpg", 12000),
                MenuItemSeed("Green Beans", MenuCategory.VEGETABLE, "https://example.com/images/green-beans.jpg", 10000),
                MenuItemSeed("Asparagus", MenuCategory.VEGETABLE, "https://example.com/images/asparagus.jpg", 25000),
                MenuItemSeed("Zucchini", MenuCategory.VEGETABLE, "https://example.com/images/zucchini.jpg", 15000),
                MenuItemSeed("Eggplant", MenuCategory.VEGETABLE, "https://example.com/images/eggplant.jpg", 12000),
                MenuItemSeed("Cauliflower", MenuCategory.VEGETABLE, "https://example.com/images/cauliflower.jpg", 15000),
                MenuItemSeed("Cabbage", MenuCategory.VEGETABLE, "https://example.com/images/cabbage.jpg", 10000),
                MenuItemSeed("Mushrooms", MenuCategory.VEGETABLE, "https://example.com/images/mushrooms.jpg", 20000),
                MenuItemSeed("Peas", MenuCategory.VEGETABLE, "https://example.com/images/peas.jpg", 12000),
                MenuItemSeed("Brussels Sprouts", MenuCategory.VEGETABLE, "https://example.com/images/brussels-sprouts.jpg", 18000),
                MenuItemSeed("Onion", MenuCategory.VEGETABLE, "https://example.com/images/onion.jpg", 8000),
                MenuItemSeed("Garlic", MenuCategory.VEGETABLE, "https://example.com/images/garlic.jpg", 10000),
                MenuItemSeed("Red Cabbage", MenuCategory.VEGETABLE, "https://example.com/images/red-cabbage.jpg", 12000),
                MenuItemSeed("Arugula", MenuCategory.VEGETABLE, "https://example.com/images/arugula.jpg", 15000),
                MenuItemSeed("Beetroot", MenuCategory.VEGETABLE, "https://example.com/images/beetroot.jpg", 15000),
                MenuItemSeed("Pumpkin", MenuCategory.VEGETABLE, "https://example.com/images/pumpkin.jpg", 12000),

                // SAUCE (15 items) - Giá từ 5,000 - 15,000 VND
                MenuItemSeed("Teriyaki Sauce", MenuCategory.SAUCE, "https://example.com/images/teriyaki-sauce.jpg", 10000),
                MenuItemSeed("Soy Sauce", MenuCategory.SAUCE, "https://example.com/images/soy-sauce.jpg", 5000),
                MenuItemSeed("Chili Sauce", MenuCategory.SAUCE, "https://example.com/images/chili-sauce.jpg", 8000),
                MenuItemSeed("Garlic Sauce", MenuCategory.SAUCE, "https://example.com/images/garlic-sauce.jpg", 8000),
                MenuItemSeed("BBQ Sauce", MenuCategory.SAUCE, "https://example.com/images/bbq-sauce.jpg", 10000),
                MenuItemSeed("Mayonnaise", MenuCategory.SAUCE, "https://example.com/images/mayonnaise.jpg", 8000),
                MenuItemSeed("Ketchup", MenuCategory.SAUCE, "https://example.com/images/ketchup.jpg", 5000),
                MenuItemSeed("Mustard", MenuCategory.SAUCE, "https://example.com/images/mustard.jpg", 7000),
                MenuItemSeed("Ranch", MenuCategory.SAUCE, "https://example.com/images/ranch.jpg", 12000),
                MenuItemSeed("Caesar", MenuCategory.SAUCE, "https://example.com/images/caesar.jpg", 12000),
                MenuItemSeed("Pesto", MenuCategory.SAUCE, "https://example.com/images/pesto.jpg", 15000),
                MenuItemSeed("Sriracha", MenuCategory.SAUCE, "https://example.com/images/sriracha.jpg", 10000),
                MenuItemSeed("Honey Mustard", MenuCategory.SAUCE, "https://example.com/images/honey-mustard.jpg", 10000),
                MenuItemSeed("Buffalo Sauce", MenuCategory.SAUCE, "https://example.com/images/buffalo-sauce.jpg", 12000),
                MenuItemSeed("Tartar Sauce", MenuCategory.SAUCE, "https://example.com/images/tartar-sauce.jpg", 10000),

                // DAIRY (13 items) - Giá từ 10,000 - 35,000 VND
                MenuItemSeed("Cheddar Cheese", MenuCategory.DAIRY, "https://example.com/images/cheddar-cheese.jpg", 25000),
                MenuItemSeed("Mozzarella", MenuCategory.DAIRY, "https://example.com/images/mozzarella.jpg", 25000),
                MenuItemSeed("Greek Yogurt", MenuCategory.DAIRY, "https://example.com/images/greek-yogurt.jpg", 20000),
                MenuItemSeed("Butter", MenuCategory.DAIRY, "https://example.com/images/butter.jpg", 15000),
                MenuItemSeed("Milk", MenuCategory.DAIRY, "https://example.com/images/milk.jpg", 12000),
                MenuItemSeed("Parmesan", MenuCategory.DAIRY, "https://example.com/images/parmesan.jpg", 30000),
                MenuItemSeed("Feta", MenuCategory.DAIRY, "https://example.com/images/feta.jpg", 28000),
                MenuItemSeed("Blue Cheese", MenuCategory.DAIRY, "https://example.com/images/blue-cheese.jpg", 35000),
                MenuItemSeed("Sour Cream", MenuCategory.DAIRY, "https://example.com/images/sour-cream.jpg", 18000),
                MenuItemSeed("Cream Cheese", MenuCategory.DAIRY, "https://example.com/images/cream-cheese.jpg", 22000),
                MenuItemSeed("Ricotta", MenuCategory.DAIRY, "https://example.com/images/ricotta.jpg", 25000),
                MenuItemSeed("Ice Cream", MenuCategory.DAIRY, "https://example.com/images/ice-cream.jpg", 20000),
                MenuItemSeed("Cottage Cheese", MenuCategory.DAIRY, "https://example.com/images/cottage-cheese.jpg", 22000),

                // FRUIT (24 items) - Giá từ 8,000 - 40,000 VND
                MenuItemSeed("Apple", MenuCategory.FRUIT, "https://example.com/images/apple.jpg", 15000),
                MenuItemSeed("Banana", MenuCategory.FRUIT, "https://example.com/images/banana.jpg", 10000),
                MenuItemSeed("Orange", MenuCategory.FRUIT, "https://example.com/images/orange.jpg", 12000),
                MenuItemSeed("Pineapple", MenuCategory.FRUIT, "https://example.com/images/pineapple.jpg", 25000),
                MenuItemSeed("Mango", MenuCategory.FRUIT, "https://example.com/images/mango.jpg", 20000),
                MenuItemSeed("Strawberry", MenuCategory.FRUIT, "https://example.com/images/strawberry.jpg", 30000),
                MenuItemSeed("Blueberry", MenuCategory.FRUIT, "https://example.com/images/blueberry.jpg", 35000),
                MenuItemSeed("Grape", MenuCategory.FRUIT, "https://example.com/images/grape.jpg", 25000),
                MenuItemSeed("Watermelon", MenuCategory.FRUIT, "https://example.com/images/watermelon.jpg", 15000),
                MenuItemSeed("Kiwi", MenuCategory.FRUIT, "https://example.com/images/kiwi.jpg", 18000),
                MenuItemSeed("Peach", MenuCategory.FRUIT, "https://example.com/images/peach.jpg", 20000),
                MenuItemSeed("Pear", MenuCategory.FRUIT, "https://example.com/images/pear.jpg", 18000),
                MenuItemSeed("Plum", MenuCategory.FRUIT, "https://example.com/images/plum.jpg", 15000),
                MenuItemSeed("Cherry", MenuCategory.FRUIT, "https://example.com/images/cherry.jpg", 40000),
                MenuItemSeed("Raspberry", MenuCategory.FRUIT, "https://example.com/images/raspberry.jpg", 35000),
                MenuItemSeed("Blackberry", MenuCategory.FRUIT, "https://example.com/images/blackberry.jpg", 35000),
                MenuItemSeed("Dragon Fruit", MenuCategory.FRUIT, "https://example.com/images/dragon-fruit.jpg", 25000),
                MenuItemSeed("Papaya", MenuCategory.FRUIT, "https://example.com/images/papaya.jpg", 15000),
                MenuItemSeed("Lemon", MenuCategory.FRUIT, "https://example.com/images/lemon.jpg", 8000),
                MenuItemSeed("Lime", MenuCategory.FRUIT, "https://example.com/images/lime.jpg", 8000),
                MenuItemSeed("Coconut", MenuCategory.FRUIT, "https://example.com/images/coconut.jpg", 15000),
                MenuItemSeed("Pomegranate", MenuCategory.FRUIT, "https://example.com/images/pomegranate.jpg", 30000),
                MenuItemSeed("Grapefruit", MenuCategory.FRUIT, "https://example.com/images/grapefruit.jpg", 18000),
                MenuItemSeed("Avocado", MenuCategory.FRUIT, "https://example.com/images/avocado.jpg", 35000)
            )

            val entities = items.map { item ->
                val categoryName = when (item.category) {
                    is String -> item.category
                    is MenuCategory -> when (item.category) {
                        MenuCategory.CARB -> "Carbohydrates"
                        MenuCategory.PROTEIN -> "Proteins"
                        MenuCategory.VEGETABLE -> "Vegetables"
                        MenuCategory.SAUCE -> "Sauces"
                        MenuCategory.DAIRY -> "Dairy"
                        MenuCategory.FRUIT -> "Fruits"
                    }
                    else -> throw IllegalArgumentException("Unsupported category type: ${item.category}")
                }
                val cat = categoryRepository.findByName(categoryName)
                    ?: throw IllegalStateException("Category not found for seeding: $categoryName")
                MenuItem(
                    name = item.name,
                    category = cat,
                    imageUrl = item.imageUrl,
                    price = item.price,
                    isActive = true
                )
            }
            menuItemRepository.saveAll(entities)
            println("✓ Menu items seeded: ${entities.size}")
        } else {
            println("⏭ Menu items table not empty, skipping")
        }

        // ==================== NUTRIENTS ====================
        if (nutrientRepository.count() == 0L) {
            println("Seeding nutrients...")
            val nutrientNames = listOf(
                "Protein",
                "Carbohydrates",
                "Fat",
                "Fiber",
                "Sugar",
                "Sodium",
                "Potassium",
                "Calcium",
                "Iron",
                "Vitamin C",
                "Vitamin A",
                "Cholesterol",
                "Saturated Fat",
                "Trans Fat",
                "Omega-3",
                "Omega-6",
                "Vitamin D",
                "Vitamin E",
                "Vitamin K",
                "Vitamin B6",
                "Vitamin B12",
                "Folate",
                "Niacin",
                "Thiamin",
                "Riboflavin",
                "Magnesium",
                "Phosphorus",
                "Zinc",
                "Copper",
                "Manganese",
                "Selenium",
                "Iodine",
                "Chloride",
                "Biotin",
                "Pantothenic Acid"
            )
            val nutrients = nutrientNames.map { Nutrient(name = it, baseUnit = UnitType.G) }
            nutrientRepository.saveAll(nutrients)
            println("✓ Nutrients seeded: ${nutrients.size}")
        } else {
            println("⏭ Nutrients table not empty, skipping")
        }

        // ==================== MENU ITEM NUTRIENTS ====================
        if (menuItemNutrientRepository.count() == 0L) {
            println("Seeding menu item nutrients...")
            val items = menuItemRepository.findAll()
            val nutrients = nutrientRepository.findAll()
            val nutrientByName = nutrients.associateBy { it.name }

            fun jitter(base: BigDecimal, itemName: String, nutrientName: String, percent: Int = 10): BigDecimal {
                val seed = (itemName + ":" + nutrientName).hashCode()
                val delta = (((kotlin.math.abs(seed) % (percent * 2 + 1)) - percent).toDouble()) / 100.0
                val factor = BigDecimal.valueOf(1.0 + delta)
                return base.multiply(factor).setScale(2, RoundingMode.HALF_UP)
            }

            fun nutrientsForCategory(categoryName: String): List<Pair<String, BigDecimal>> = when (categoryName) {
                "Carbohydrates" -> listOf(
                    "Carbohydrates" to BigDecimal("35.0"),
                    "Protein" to BigDecimal("4.0"),
                    "Fat" to BigDecimal("1.5"),
                    "Fiber" to BigDecimal("3.0"),
                    "Sodium" to BigDecimal("0.10"),
                    "Potassium" to BigDecimal("0.15"),
                )
                "Proteins" -> listOf(
                    "Protein" to BigDecimal("28.0"),
                    "Fat" to BigDecimal("6.0"),
                    "Carbohydrates" to BigDecimal("0.5"),
                    "Cholesterol" to BigDecimal("0.20"),
                    "Sodium" to BigDecimal("0.30"),
                    "Iron" to BigDecimal("0.01"),
                )
                "Vegetables" -> listOf(
                    "Carbohydrates" to BigDecimal("5.0"),
                    "Fiber" to BigDecimal("3.5"),
                    "Protein" to BigDecimal("2.0"),
                    "Vitamin C" to BigDecimal("0.05"),
                    "Vitamin A" to BigDecimal("0.03"),
                    "Potassium" to BigDecimal("0.25"),
                )
                "Sauces" -> listOf(
                    "Sodium" to BigDecimal("1.20"),
                    "Sugar" to BigDecimal("6.0"),
                    "Fat" to BigDecimal("2.0"),
                    "Carbohydrates" to BigDecimal("8.0"),
                    "Protein" to BigDecimal("0.5"),
                    "Cholesterol" to BigDecimal("0.05"),
                )
                "Dairy" -> listOf(
                    "Protein" to BigDecimal("6.0"),
                    "Fat" to BigDecimal("8.0"),
                    "Carbohydrates" to BigDecimal("4.0"),
                    "Calcium" to BigDecimal("0.25"),
                    "Sodium" to BigDecimal("0.20"),
                    "Vitamin D" to BigDecimal("0.01"),
                )
                "Fruits" -> listOf(
                    "Carbohydrates" to BigDecimal("12.0"),
                    "Sugar" to BigDecimal("10.0"),
                    "Fiber" to BigDecimal("2.5"),
                    "Vitamin C" to BigDecimal("0.06"),
                    "Potassium" to BigDecimal("0.20"),
                    "Protein" to BigDecimal("1.0"),
                )
                else -> emptyList()
            }

            fun specificForItem(name: String): List<Pair<String, BigDecimal>>? {
                return when (name.lowercase()) {
                    "white rice" -> listOf(
                        "Carbohydrates" to BigDecimal("45.0"),
                        "Protein" to BigDecimal("4.0"),
                        "Fat" to BigDecimal("0.5"),
                        "Fiber" to BigDecimal("0.4"),
                        "Sodium" to BigDecimal("0.01"),
                        "Potassium" to BigDecimal("0.03")
                    )
                    "brown rice" -> listOf(
                        "Carbohydrates" to BigDecimal("44.0"),
                        "Protein" to BigDecimal("5.0"),
                        "Fat" to BigDecimal("1.0"),
                        "Fiber" to BigDecimal("3.5"),
                        "Sodium" to BigDecimal("0.01"),
                        "Potassium" to BigDecimal("0.05")
                    )
                    "quinoa" -> listOf(
                        "Carbohydrates" to BigDecimal("39.0"),
                        "Protein" to BigDecimal("8.0"),
                        "Fat" to BigDecimal("3.5"),
                        "Fiber" to BigDecimal("5.0"),
                        "Sodium" to BigDecimal("0.01"),
                        "Potassium" to BigDecimal("0.32")
                    )
                    "sweet potato" -> listOf(
                        "Carbohydrates" to BigDecimal("27.0"),
                        "Protein" to BigDecimal("2.0"),
                        "Fat" to BigDecimal("0.1"),
                        "Fiber" to BigDecimal("4.0"),
                        "Vitamin A" to BigDecimal("0.02"),
                        "Potassium" to BigDecimal("0.44")
                    )
                    "grilled chicken" -> listOf(
                        "Protein" to BigDecimal("31.0"),
                        "Fat" to BigDecimal("3.6"),
                        "Carbohydrates" to BigDecimal("0.0"),
                        "Sodium" to BigDecimal("0.20"),
                        "Cholesterol" to BigDecimal("0.09"),
                        "Iron" to BigDecimal("0.01")
                    )
                    "fried chicken" -> listOf(
                        "Protein" to BigDecimal("25.0"),
                        "Fat" to BigDecimal("13.0"),
                        "Carbohydrates" to BigDecimal("8.0"),
                        "Sodium" to BigDecimal("0.40"),
                        "Cholesterol" to BigDecimal("0.12"),
                        "Iron" to BigDecimal("0.01")
                    )
                    "beef steak" -> listOf(
                        "Protein" to BigDecimal("26.0"),
                        "Fat" to BigDecimal("20.0"),
                        "Carbohydrates" to BigDecimal("0.0"),
                        "Iron" to BigDecimal("0.03"),
                        "Zinc" to BigDecimal("0.005"),
                        "Cholesterol" to BigDecimal("0.09")
                    )
                    "firm tofu" -> listOf(
                        "Protein" to BigDecimal("8.0"),
                        "Fat" to BigDecimal("4.0"),
                        "Carbohydrates" to BigDecimal("2.0"),
                        "Calcium" to BigDecimal("0.35"),
                        "Iron" to BigDecimal("0.05"),
                        "Sodium" to BigDecimal("0.01")
                    )
                    "salmon fillet" -> listOf(
                        "Protein" to BigDecimal("25.0"),
                        "Fat" to BigDecimal("14.0"),
                        "Omega-3" to BigDecimal("1.5"),
                        "Cholesterol" to BigDecimal("0.06"),
                        "Sodium" to BigDecimal("0.09"),
                        "Potassium" to BigDecimal("0.36")
                    )
                    "shrimp" -> listOf(
                        "Protein" to BigDecimal("24.0"),
                        "Fat" to BigDecimal("0.3"),
                        "Carbohydrates" to BigDecimal("0.2"),
                        "Cholesterol" to BigDecimal("0.22"),
                        "Sodium" to BigDecimal("0.11"),
                        "Iron" to BigDecimal("0.003")
                    )
                    "boiled egg" -> listOf(
                        "Protein" to BigDecimal("13.0"),
                        "Fat" to BigDecimal("11.0"),
                        "Carbohydrates" to BigDecimal("1.1"),
                        "Cholesterol" to BigDecimal("0.37"),
                        "Sodium" to BigDecimal("0.12"),
                        "Vitamin D" to BigDecimal("0.002")
                    )
                    "broccoli" -> listOf(
                        "Carbohydrates" to BigDecimal("7.0"),
                        "Protein" to BigDecimal("2.8"),
                        "Fiber" to BigDecimal("2.6"),
                        "Vitamin C" to BigDecimal("0.09"),
                        "Vitamin A" to BigDecimal("0.003"),
                        "Potassium" to BigDecimal("0.316")
                    )
                    "carrot" -> listOf(
                        "Carbohydrates" to BigDecimal("10.0"),
                        "Protein" to BigDecimal("0.9"),
                        "Fiber" to BigDecimal("2.8"),
                        "Vitamin A" to BigDecimal("0.009"),
                        "Sugar" to BigDecimal("4.7"),
                        "Potassium" to BigDecimal("0.320")
                    )
                    "spinach" -> listOf(
                        "Carbohydrates" to BigDecimal("3.6"),
                        "Protein" to BigDecimal("2.9"),
                        "Fiber" to BigDecimal("2.2"),
                        "Vitamin A" to BigDecimal("0.009"),
                        "Vitamin C" to BigDecimal("0.028"),
                        "Iron" to BigDecimal("0.026")
                    )
                    "tomato" -> listOf(
                        "Carbohydrates" to BigDecimal("3.9"),
                        "Protein" to BigDecimal("0.9"),
                        "Fiber" to BigDecimal("1.2"),
                        "Vitamin C" to BigDecimal("0.014"),
                        "Potassium" to BigDecimal("0.237"),
                        "Sugar" to BigDecimal("2.6")
                    )
                    "cucumber" -> listOf(
                        "Carbohydrates" to BigDecimal("3.6"),
                        "Protein" to BigDecimal("0.7"),
                        "Fiber" to BigDecimal("0.5"),
                        "Vitamin K" to BigDecimal("0.016"),
                        "Potassium" to BigDecimal("0.147"),
                        "Sodium" to BigDecimal("0.002")
                    )
                    "apple" -> listOf(
                        "Carbohydrates" to BigDecimal("14.0"),
                        "Protein" to BigDecimal("0.3"),
                        "Fiber" to BigDecimal("2.4"),
                        "Sugar" to BigDecimal("10.0"),
                        "Potassium" to BigDecimal("0.107"),
                        "Vitamin C" to BigDecimal("0.004")
                    )
                    "banana" -> listOf(
                        "Carbohydrates" to BigDecimal("23.0"),
                        "Protein" to BigDecimal("1.3"),
                        "Fiber" to BigDecimal("2.6"),
                        "Sugar" to BigDecimal("12.0"),
                        "Potassium" to BigDecimal("0.358"),
                        "Vitamin C" to BigDecimal("0.009")
                    )
                    "strawberry" -> listOf(
                        "Carbohydrates" to BigDecimal("7.7"),
                        "Protein" to BigDecimal("0.7"),
                        "Fiber" to BigDecimal("2.0"),
                        "Sugar" to BigDecimal("4.9"),
                        "Vitamin C" to BigDecimal("0.059"),
                        "Potassium" to BigDecimal("0.153")
                    )
                    "teriyaki sauce" -> listOf(
                        "Sodium" to BigDecimal("0.55"),
                        "Sugar" to BigDecimal("8.0"),
                        "Carbohydrates" to BigDecimal("9.0"),
                        "Protein" to BigDecimal("1.0"),
                        "Fat" to BigDecimal("0.1"),
                        "Cholesterol" to BigDecimal("0.00")
                    )
                    "bbq sauce" -> listOf(
                        "Sugar" to BigDecimal("10.0"),
                        "Carbohydrates" to BigDecimal("12.0"),
                        "Sodium" to BigDecimal("0.50"),
                        "Protein" to BigDecimal("0.6"),
                        "Fat" to BigDecimal("0.2"),
                        "Cholesterol" to BigDecimal("0.00")
                    )
                    "mayonnaise" -> listOf(
                        "Fat" to BigDecimal("10.0"),
                        "Carbohydrates" to BigDecimal("0.6"),
                        "Protein" to BigDecimal("0.1"),
                        "Sodium" to BigDecimal("0.09"),
                        "Cholesterol" to BigDecimal("0.015"),
                        "Sugar" to BigDecimal("0.5")
                    )
                    "cheddar cheese" -> listOf(
                        "Protein" to BigDecimal("25.0"),
                        "Fat" to BigDecimal("33.0"),
                        "Carbohydrates" to BigDecimal("1.3"),
                        "Calcium" to BigDecimal("0.721"),
                        "Sodium" to BigDecimal("0.621"),
                        "Vitamin A" to BigDecimal("0.265")
                    )
                    "greek yogurt" -> listOf(
                        "Protein" to BigDecimal("10.0"),
                        "Fat" to BigDecimal("0.4"),
                        "Carbohydrates" to BigDecimal("3.6"),
                        "Calcium" to BigDecimal("0.11"),
                        "Sodium" to BigDecimal("0.036"),
                        "Vitamin B12" to BigDecimal("0.0005")
                    )
                    "milk" -> listOf(
                        "Protein" to BigDecimal("3.4"),
                        "Fat" to BigDecimal("3.7"),
                        "Carbohydrates" to BigDecimal("4.8"),
                        "Calcium" to BigDecimal("0.125"),
                        "Sodium" to BigDecimal("0.044"),
                        "Vitamin D" to BigDecimal("0.001")
                    )
                    else -> null
                }
            }

            val links = items.flatMap { item ->
                val base = specificForItem(item.name)
                    ?: nutrientsForCategory(item.category.name).map { (n, v) ->
                        n to jitter(v, item.name, n)
                    }
                base.mapNotNull { (nName, qty) ->
                    val nutrient = nutrientByName[nName]
                    if (nutrient != null) MenuItemNutrient(menuItem = item, nutrient = nutrient, quantity = qty) else null
                }
            }

            if (links.isNotEmpty()) {
                menuItemNutrientRepository.saveAll(links)
                println("✓ Menu item nutrients seeded for ${items.size} items, total links: ${links.size}")
            } else {
                println("⚠️ Skipped seeding menu item nutrients: missing items or nutrients")
            }
        } else {
            println("⏭ Menu item nutrients table not empty, skipping")
        }

        // ==================== INGREDIENTS ====================
        if (ingredientRepository.count() == 0L) {
            println("Seeding ingredients...")

            val ingredientsData = listOf(
                // Proteins
                Triple("Chicken Breast", UnitType.G, "CB"),
                Triple("Chicken Thigh", UnitType.G, "CT"),
                Triple("Beef Sirloin", UnitType.G, "BS"),
                Triple("Ground Beef", UnitType.G, "GB"),
                Triple("Pork Loin", UnitType.G, "PL"),
                Triple("Pork Belly", UnitType.G, "PB"),
                Triple("Salmon", UnitType.G, "SM"),
                Triple("Tuna", UnitType.G, "TN"),
                Triple("Shrimp", UnitType.G, "SH"),
                Triple("Tofu Block", UnitType.G, "TF"),
                Triple("Eggs", UnitType.G, "EG"),
                Triple("Turkey Breast", UnitType.G, "TB"),

                // Carbs
                Triple("White Rice", UnitType.G, "WR"),
                Triple("Brown Rice", UnitType.G, "BR"),
                Triple("Quinoa", UnitType.G, "QN"),
                Triple("Sweet Potato", UnitType.G, "SP"),
                Triple("Regular Potato", UnitType.G, "RP"),
                Triple("Pasta", UnitType.G, "PA"),
                Triple("Noodles", UnitType.G, "ND"),
                Triple("Bread", UnitType.G, "BD"),

                // Vegetables
                Triple("Fresh Broccoli", UnitType.G, "BR"),
                Triple("Fresh Carrot", UnitType.G, "CR"),
                Triple("Fresh Spinach", UnitType.G, "SN"),
                Triple("Fresh Tomato", UnitType.G, "TM"),
                Triple("Fresh Cucumber", UnitType.G, "CU"),
                Triple("Fresh Lettuce", UnitType.G, "LT"),
                Triple("Fresh Bell Pepper", UnitType.G, "BP"),
                Triple("Fresh Onion", UnitType.G, "ON"),
                Triple("Fresh Garlic", UnitType.G, "GL"),
                Triple("Fresh Mushrooms", UnitType.G, "MS"),
                Triple("Fresh Kale", UnitType.G, "KL"),
                Triple("Fresh Cabbage", UnitType.G, "CB"),

                // Sauces & Condiments
                Triple("Teriyaki Sauce", UnitType.ML, "TS"),
                Triple("Soy Sauce", UnitType.ML, "SS"),
                Triple("BBQ Sauce", UnitType.ML, "BB"),
                Triple("Mayonnaise", UnitType.ML, "MY"),
                Triple("Chili Sauce", UnitType.ML, "CS"),
                Triple("Garlic Sauce", UnitType.ML, "GS"),
                Triple("Pesto", UnitType.ML, "PS"),
                Triple("Olive Oil", UnitType.ML, "OO"),
                Triple("Vegetable Oil", UnitType.ML, "VO"),

                // Dairy
                Triple("Cheddar Cheese", UnitType.G, "CC"),
                Triple("Mozzarella", UnitType.G, "MZ"),
                Triple("Greek Yogurt", UnitType.G, "GY"),
                Triple("Milk", UnitType.ML, "MK"),
                Triple("Butter", UnitType.G, "BT"),
                Triple("Cream", UnitType.ML, "CM"),

                // Fruits
                Triple("Fresh Apple", UnitType.G, "AP"),
                Triple("Fresh Banana", UnitType.G, "BN"),
                Triple("Fresh Strawberry", UnitType.G, "ST"),
                Triple("Fresh Blueberry", UnitType.G, "BL"),
                Triple("Fresh Orange", UnitType.G, "OR"),
                Triple("Fresh Mango", UnitType.G, "MG"),

                // Spices & Herbs
                Triple("Salt", UnitType.G, "SL"),
                Triple("Black Pepper", UnitType.G, "PP"),
                Triple("Paprika", UnitType.G, "PK"),
                Triple("Cumin", UnitType.G, "CM"),
                Triple("Fresh Basil", UnitType.G, "BS"),
                Triple("Fresh Parsley", UnitType.G, "PR"),
                Triple("Fresh Cilantro", UnitType.G, "CL")
            )

            ingredientsData.forEachIndexed { idx, (name, unit, prefix) ->
                ingredientRepository.save(Ingredient(
                    name = name,
                    baseUnit = unit,
                    imageUrl = "https://example.com/${name.lowercase().replace(" ", "-")}.jpg",
                    isActive = true,
                    batchNumber = "$prefix-2024-${(idx + 1).toString().padStart(3, '0')}"
                ))
            }

            println("✓ Ingredients seeded: ${ingredientsData.size}")
        } else {
            println("⏭ Ingredients table not empty, skipping")
        }

        // ==================== STORE INGREDIENT BATCHES ====================
        if (storeIngredientBatchRepository.count() == 0L) {
            println("Seeding store ingredient batches...")
            val stores = storeRepository.findAll()
            val ingredients = ingredientRepository.findAll()

            val batches = mutableListOf<StoreIngredientBatch>()

            stores.forEach { store ->
                ingredients.forEach { ingredient ->
                    // Random quantity between 1000g-10000g or 500ml-5000ml
                    val baseQuantity = if (ingredient.baseUnit == UnitType.ML) {
                        (Random.nextInt(1, 11) * 500).toLong()
                    } else {
                        (Random.nextInt(1, 11) * 500).toLong()
                    }

                    batches.add(StoreIngredientBatch(
                        store = store,
                        ingredient = ingredient,
                        quantity = baseQuantity
                    ))
                }
            }

            if (batches.isNotEmpty()) {
                storeIngredientBatchRepository.saveAll(batches)
                println("✓ Store ingredient batches seeded: ${batches.size} (${stores.size} stores × ${ingredients.size} ingredients)")
            } else {
                println("⚠️ Skipped seeding store ingredient batches: missing stores or ingredients")
            }
        } else {
            println("⏭ Store ingredient batches table not empty, skipping")
        }

        // ==================== RECIPES ====================
        if (recipeRepository.count() == 0L) {
            println("Seeding recipes...")
            val items = menuItemRepository.findAll()
            val ingredients = ingredientRepository.findAll()

            val recipeMapping = mapOf(
                // Chicken dishes
                "Grilled Chicken" to listOf("Chicken Breast", "Olive Oil", "Salt", "Black Pepper", "Fresh Garlic"),
                "Fried Chicken" to listOf("Chicken Thigh", "Vegetable Oil", "Salt", "Black Pepper", "Paprika"),
                "Roast Chicken" to listOf("Chicken Breast", "Butter", "Fresh Garlic", "Fresh Parsley", "Salt"),
                "Chicken Thigh" to listOf("Chicken Thigh", "Soy Sauce", "Fresh Garlic", "Salt"),
                "Chicken Wings" to listOf("Chicken Thigh", "BBQ Sauce", "Salt", "Black Pepper"),
                "Turkey Breast" to listOf("Turkey Breast", "Olive Oil", "Salt", "Fresh Parsley"),

                // Beef dishes
                "Beef Steak" to listOf("Beef Sirloin", "Salt", "Black Pepper", "Olive Oil", "Fresh Garlic"),
                "Ground Beef" to listOf("Ground Beef", "Salt", "Black Pepper", "Fresh Onion"),
                "Ribeye Steak" to listOf("Beef Sirloin", "Butter", "Fresh Garlic", "Salt"),
                "Sirloin" to listOf("Beef Sirloin", "Olive Oil", "Salt", "Black Pepper"),

                // Pork dishes
                "Pork Chop" to listOf("Pork Loin", "Salt", "Black Pepper", "Olive Oil"),
                "Pork Belly" to listOf("Pork Belly", "Soy Sauce", "Fresh Garlic", "Salt"),
                "Ham" to listOf("Pork Loin", "Salt", "Black Pepper"),
                "Bacon" to listOf("Pork Belly", "Salt", "Black Pepper"),

                // Seafood
                "Salmon Fillet" to listOf("Salmon", "Olive Oil", "Salt", "Fresh Parsley", "Fresh Garlic"),
                "Smoked Salmon" to listOf("Salmon", "Salt", "Black Pepper"),
                "Tuna" to listOf("Tuna", "Olive Oil", "Salt"),
                "Shrimp" to listOf("Shrimp", "Olive Oil", "Fresh Garlic", "Salt"),
                "Sardines" to listOf("Tuna", "Olive Oil", "Salt"),

                // Vegetarian proteins
                "Firm Tofu" to listOf("Tofu Block", "Soy Sauce", "Fresh Garlic"),
                "Tempeh" to listOf("Tofu Block", "Soy Sauce", "Fresh Ginger"),
                "Seitan" to listOf("Tofu Block", "Soy Sauce"),
                "Boiled Egg" to listOf("Eggs", "Salt"),

                // Legumes
                "Black Beans" to listOf("Salt", "Fresh Garlic", "Cumin"),
                "Chickpeas" to listOf("Salt", "Olive Oil", "Paprika"),
                "Lentils" to listOf("Salt", "Fresh Onion", "Cumin"),
                "Edamame" to listOf("Salt"),

                // Carbs
                "White Rice" to listOf("White Rice", "Salt"),
                "Brown Rice" to listOf("Brown Rice", "Salt"),
                "Quinoa" to listOf("Quinoa", "Salt", "Olive Oil"),
                "Sweet Potato" to listOf("Sweet Potato", "Olive Oil", "Salt"),
                "Mashed Potato" to listOf("Regular Potato", "Milk", "Butter", "Salt"),
                "Whole Wheat Pasta" to listOf("Pasta", "Olive Oil", "Salt"),
                "Udon Noodles" to listOf("Noodles", "Soy Sauce"),
                "Soba Noodles" to listOf("Noodles", "Soy Sauce"),
                "Rice Noodles" to listOf("Noodles", "Vegetable Oil"),
                "Couscous" to listOf("Quinoa", "Olive Oil", "Salt"),
                "Oatmeal" to listOf("Quinoa", "Milk", "Salt"),
                "Garlic Bread" to listOf("Bread", "Butter", "Fresh Garlic", "Fresh Parsley"),
                "Sourdough Bread" to listOf("Bread", "Salt"),
                "Baguette" to listOf("Bread", "Salt"),
                "Tortilla" to listOf("Bread", "Salt"),

                // Vegetables
                "Broccoli" to listOf("Fresh Broccoli", "Olive Oil", "Salt", "Fresh Garlic"),
                "Carrot" to listOf("Fresh Carrot", "Olive Oil", "Salt"),
                "Spinach" to listOf("Fresh Spinach", "Olive Oil", "Fresh Garlic", "Salt"),
                "Kale" to listOf("Fresh Kale", "Olive Oil", "Fresh Garlic"),
                "Lettuce" to listOf("Fresh Lettuce", "Olive Oil"),
                "Tomato" to listOf("Fresh Tomato", "Olive Oil", "Salt", "Fresh Basil"),
                "Cucumber" to listOf("Fresh Cucumber", "Salt"),
                "Bell Pepper" to listOf("Fresh Bell Pepper", "Olive Oil", "Salt"),
                "Mushrooms" to listOf("Fresh Mushrooms", "Olive Oil", "Fresh Garlic", "Salt"),
                "Cabbage" to listOf("Fresh Cabbage", "Olive Oil", "Salt"),
                "Onion" to listOf("Fresh Onion", "Olive Oil"),
                "Garlic" to listOf("Fresh Garlic", "Olive Oil"),
                "Asparagus" to listOf("Fresh Broccoli", "Olive Oil", "Salt"),
                "Zucchini" to listOf("Fresh Cucumber", "Olive Oil", "Salt"),
                "Eggplant" to listOf("Fresh Bell Pepper", "Olive Oil", "Salt", "Fresh Garlic"),
                "Cauliflower" to listOf("Fresh Broccoli", "Olive Oil", "Salt"),
                "Corn" to listOf("Fresh Carrot", "Butter", "Salt"),
                "Green Beans" to listOf("Fresh Broccoli", "Olive Oil", "Fresh Garlic"),
                "Peas" to listOf("Fresh Broccoli", "Butter", "Salt"),
                "Brussels Sprouts" to listOf("Fresh Cabbage", "Olive Oil", "Salt"),
                "Red Cabbage" to listOf("Fresh Cabbage", "Olive Oil"),
                "Arugula" to listOf("Fresh Lettuce", "Olive Oil"),
                "Beetroot" to listOf("Fresh Carrot", "Olive Oil", "Salt"),
                "Pumpkin" to listOf("Fresh Carrot", "Olive Oil", "Salt"),

                // Sauces
                "Teriyaki Sauce" to listOf("Teriyaki Sauce"),
                "Soy Sauce" to listOf("Soy Sauce"),
                "Chili Sauce" to listOf("Chili Sauce"),
                "Garlic Sauce" to listOf("Garlic Sauce", "Fresh Garlic"),
                "BBQ Sauce" to listOf("BBQ Sauce"),
                "Mayonnaise" to listOf("Mayonnaise"),
                "Ketchup" to listOf("BBQ Sauce", "Salt"),
                "Mustard" to listOf("Mayonnaise", "Salt"),
                "Ranch" to listOf("Mayonnaise", "Fresh Garlic", "Fresh Parsley"),
                "Caesar" to listOf("Mayonnaise", "Fresh Garlic"),
                "Pesto" to listOf("Pesto", "Fresh Basil", "Fresh Garlic"),
                "Sriracha" to listOf("Chili Sauce"),
                "Honey Mustard" to listOf("Mayonnaise", "Salt"),
                "Buffalo Sauce" to listOf("Chili Sauce", "Butter"),
                "Tartar Sauce" to listOf("Mayonnaise", "Fresh Garlic"),

                // Dairy
                "Cheddar Cheese" to listOf("Cheddar Cheese"),
                "Mozzarella" to listOf("Mozzarella"),
                "Greek Yogurt" to listOf("Greek Yogurt"),
                "Butter" to listOf("Butter"),
                "Milk" to listOf("Milk"),
                "Parmesan" to listOf("Cheddar Cheese"),
                "Feta" to listOf("Cheddar Cheese"),
                "Blue Cheese" to listOf("Cheddar Cheese"),
                "Sour Cream" to listOf("Cream", "Salt"),
                "Cream Cheese" to listOf("Cream"),
                "Ricotta" to listOf("Mozzarella"),
                "Ice Cream" to listOf("Milk", "Cream"),
                "Cottage Cheese" to listOf("Cheddar Cheese"),

                // Fruits
                "Apple" to listOf("Fresh Apple"),
                "Banana" to listOf("Fresh Banana"),
                "Orange" to listOf("Fresh Orange"),
                "Pineapple" to listOf("Fresh Mango"),
                "Mango" to listOf("Fresh Mango"),
                "Strawberry" to listOf("Fresh Strawberry"),
                "Blueberry" to listOf("Fresh Blueberry"),
                "Grape" to listOf("Fresh Apple"),
                "Watermelon" to listOf("Fresh Mango"),
                "Kiwi" to listOf("Fresh Apple"),
                "Peach" to listOf("Fresh Mango"),
                "Pear" to listOf("Fresh Apple"),
                "Plum" to listOf("Fresh Apple"),
                "Cherry" to listOf("Fresh Strawberry"),
                "Raspberry" to listOf("Fresh Strawberry"),
                "Blackberry" to listOf("Fresh Blueberry"),
                "Dragon Fruit" to listOf("Fresh Mango"),
                "Papaya" to listOf("Fresh Mango"),
                "Lemon" to listOf("Fresh Orange"),
                "Lime" to listOf("Fresh Orange"),
                "Coconut" to listOf("Fresh Mango"),
                "Pomegranate" to listOf("Fresh Apple"),
                "Grapefruit" to listOf("Fresh Orange"),
                "Avocado" to listOf("Fresh Mango")
            )

            val newRecipes = mutableListOf<Recipe>()

            recipeMapping.forEach { (itemName, ingredientNames) ->
                val menuItem = items.find { it.name == itemName }
                if (menuItem != null) {
                    ingredientNames.forEach { ingName ->
                        val ingredient = ingredients.find { it.name == ingName }
                        if (ingredient != null) {
                            newRecipes.add(Recipe(
                                ingredient = ingredient,
                                menuItem = menuItem
                            ))
                        }
                    }
                }
            }

            if (newRecipes.isNotEmpty()) {
                recipeRepository.saveAll(newRecipes)
                println("✓ Recipes seeded: ${newRecipes.size}")
            } else {
                println("⚠️ Skipped seeding recipes: missing ingredients or items")
            }
        } else {
            println("⏭ Recipes table not empty, skipping")
        }

        // ==================== DAILY MENU ====================
        if (dailyMenuRepository.count() == 0L) {
            println("Seeding daily menus...")

            val stores = storeRepository.findAll()
            val items = menuItemRepository.findAll()

            // Create daily menus for the past 7 days and next 7 days
            val dailyMenus = mutableListOf<DailyMenu>()

            for (dayOffset in -7..7) {
                val menuDate = Timestamp.valueOf(LocalDateTime.now().plusDays(dayOffset.toLong()).withHour(12).withMinute(0).withSecond(0).withNano(0))

                val dailyMenu = DailyMenu(menuDate = menuDate)

                // Add all stores to this daily menu
                dailyMenu.stores.addAll(stores)

                // Select random items from each category for variety
                val carbItems = items.filter { it.category.name == "Carbohydrates" }.shuffled().take(5)
                val proteinItems = items.filter { it.category.name == "Proteins" }.shuffled().take(8)
                val vegItems = items.filter { it.category.name == "Vegetables" }.shuffled().take(6)
                val sauceItems = items.filter { it.category.name == "Sauces" }.shuffled().take(4)
                val dairyItems = items.filter { it.category.name == "Dairy" }.shuffled().take(3)
                val fruitItems = items.filter { it.category.name == "Fruits" }.shuffled().take(3)

                val selectedItems = carbItems + proteinItems + vegItems + sauceItems + dairyItems + fruitItems

                selectedItems.forEach { menuItem ->
                    val dailyMenuItem = DailyMenuItem(
                        dailyMenu = dailyMenu,
                        menuItem = menuItem
                    )
                    dailyMenu.dailyMenuItems.add(dailyMenuItem)
                }

                dailyMenus.add(dailyMenu)
            }

            if (dailyMenus.isNotEmpty()) {
                dailyMenuRepository.saveAll(dailyMenus)
                println("✓ Daily menus seeded: ${dailyMenus.size} days with varying items")
            } else {
                println("⚠️ Skipped seeding daily menus: missing stores or items")
            }
        } else {
            println("⏭ Daily menu table not empty, skipping")
        }

        println("\n" + "=".repeat(50))
        println("✅ DATA INITIALIZATION COMPLETE!")
        println("=".repeat(50))
        println("Summary:")
        println("- Users: ${userRepository.count()}")
        println("- Stores: ${storeRepository.count()}")
        println("- Categories: ${categoryRepository.count()}")
        println("- Steps: ${stepRepository.count()}")
        println("- Menu Items: ${menuItemRepository.count()}")
        println("- Nutrients: ${nutrientRepository.count()}")
        println("- Menu Item Nutrients: ${menuItemNutrientRepository.count()}")
        println("- Ingredients: ${ingredientRepository.count()}")
        println("- Recipes: ${recipeRepository.count()}")
        println("- Store Ingredient Batches: ${storeIngredientBatchRepository.count()}")
        println("- Payment Methods: ${paymentMethodRepository.count()}")
        println("- Promotions: ${promotionRepository.count()}")
        println("- Daily Menus: ${dailyMenuRepository.count()}")
        println("=".repeat(50))
    }
}