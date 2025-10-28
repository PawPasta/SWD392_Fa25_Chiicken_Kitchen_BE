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
import com.ChickenKitchen.app.model.entity.user.EmployeeDetail
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
import com.ChickenKitchen.app.repository.user.EmployeeDetailRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Timestamp
import java.time.LocalDateTime
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random
import kotlin.math.absoluteValue

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
        storeIngredientBatchRepository: StoreIngredientBatchRepository,
        employeeDetailRepository: EmployeeDetailRepository
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
                imageURL = null,
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

            userRepository.save(User(
                role = Role.ADMIN,
                uid = "admin-uid-007",
                email = "PhoenixZ3004@gmail.com",
                isVerified = true,
                phone = "0901234560",
                isActive = true,
                fullName = "Admin Thuan Lua Chua",
                provider = "Local",
                imageURL = null,
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

            userRepository.save(User(
                role = Role.MANAGER,
                uid = "manager-uid-008",
                email = "PhoenixZ3303@gmail.com",
                isVerified = true,
                phone = "0901234591",
                isActive = true,
                fullName = "Manager Thuan Sieu To",
                provider = "Local",
                imageURL = "https://example.com/manager2.jpg"
            ))

            // Employee users
            val employeeData = listOf(
                Triple("employee-uid-001", "baoltgse182138@fpt.edu.vn", "Employee Le"),
                Triple("employee-uid-002", "thuantqse182998@fpt.edu.vn", "Employee Thuan"),
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

        // ==================== EMPLOYEE DETAILS (link EMPLOYEE users to a store) ====================
        run {
            val anyStore = storeRepository.findAll().firstOrNull()
            if (anyStore == null) {
                println("⚠️ No store available, cannot seed EmployeeDetails")
            } else {
                val employees = userRepository.findAll().filter { it.role == Role.EMPLOYEE }
                var created = 0
                employees.forEach { u ->
                    if (u.employeeDetail == null) {
                        employeeDetailRepository.save(
                            EmployeeDetail(user = u, store = anyStore, position = "Staff", isActive = true, note = null)
                        )
                        created++
                    }
                }
                if (created > 0) println("✓ EmployeeDetails seeded: $created (assigned to store '${anyStore.name}')")
                else println("⏭ EmployeeDetails already present for EMPLOYEE users")
            }
        }

        // ==================== PAYMENT METHODS ====================
        if (paymentMethodRepository.count() == 0L) {
            println("Seeding payment methods...")

            val paymentMethods = listOf(
                Triple("Wallet", "Pay with wallet", true),
                Triple("MoMo", "Pay with MoMo e-wallet", true),
                Triple("VNPay", "Pay with VNPay", true),
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
                    name = "Summer Sale",
                    description = "Hot summer discounts for all customers",
                    discountType = DiscountType.PERCENT,
                    discountValue = 20,
                    startDate = LocalDateTime.now().minusDays(5),
                    endDate = LocalDateTime.now().plusDays(25),
                    isActive = true,
                    quantity = 100,
                    code = "SUMMER20",
                ),
                Promotion(
                    name = "Mega Discount",
                    description = "Instant 50,000 VND off your order",
                    discountType = DiscountType.AMOUNT,
                    discountValue = 50000,
                    startDate = LocalDateTime.now().minusDays(3),
                    endDate = LocalDateTime.now().plusDays(10),
                    isActive = true,
                    quantity = 50,
                    code = "SAVE50K",
                ),
                Promotion(
                    name = "October Deal",
                    description = "Get 15% off on all purchases this month",
                    discountType = DiscountType.PERCENT,
                    discountValue = 15,
                    startDate = LocalDateTime.now().minusDays(1),
                    endDate = LocalDateTime.now().plusDays(30),
                    isActive = true,
                    quantity = 200,
                    code = "OCT15",
                ),
                Promotion(
                    name = "Weekly Special",
                    description = "Enjoy an instant 30,000 VND discount this week",
                    discountType = DiscountType.AMOUNT,
                    discountValue = 30000,
                    startDate = LocalDateTime.now(),
                    endDate = LocalDateTime.now().plusDays(7),
                    isActive = true,
                    quantity = 75,
                    code = "WEEK30K",
                ),
                Promotion(
                    name = "Old Event",
                    description = "Expired promotion - no longer available",
                    discountType = DiscountType.PERCENT,
                    discountValue = 25,
                    startDate = LocalDateTime.now().minusDays(30),
                    endDate = LocalDateTime.now().minusDays(1),
                    isActive = false,
                    quantity = 0,
                    code = "EXPIRED25",
                ),
                Promotion(
                    name = "Future Bonus",
                    description = "Upcoming 30% discount event",
                    discountType = DiscountType.PERCENT,
                    discountValue = 30,
                    startDate = LocalDateTime.now().plusDays(5),
                    endDate = LocalDateTime.now().plusDays(15),
                    isActive = true,
                    quantity = 150,
                    code = "FUTURE30",
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
                MenuItemSeed("Steamed White Rice Bowl", MenuCategory.CARB, "https://i.pinimg.com/736x/eb/a2/16/eba216ccd25d09b1e564dc8360db61a7.jpg", 15000),
                MenuItemSeed("Steamed Brown Rice Bowl", MenuCategory.CARB, "https://th.bing.com/th/id/R.592b533040c656c939da1e30e0d48b66?rik=byUrNOA%2frD0kWQ&pid=ImgRaw&r=0", 18000),
                MenuItemSeed("Steamed Quinoa Bowl", MenuCategory.CARB, "https://media.istockphoto.com/id/1210023362/vi/anh/quinoa-lu%E1%BB%99c-trong-b%C3%A1t.jpg?s=2048x2048&w=is&k=20&c=i2cF6eo8YfIRjN61bcf4eB4up9vO9mO4eE2wuDirOx8=", 35000),
                MenuItemSeed("Whole Wheat Pasta with Olive Oil", MenuCategory.CARB, "https://media.istockphoto.com/id/1031519048/vi/anh/c%E1%BA%ADn-c%E1%BA%A3nh-m%C3%AC-%E1%BB%91ng-rotini-n%E1%BA%A5u-ch%C3%ADn-trong-b%C3%A1t-th%E1%BB%A7y-tinh.jpg?s=612x612&w=0&k=20&c=UguSL5UVL2H5sPMy7RHyccdjgja0A8S9nk6lg8YzjKc=", 25000),
                MenuItemSeed("Baked Sweet Potato", MenuCategory.CARB, "https://media.istockphoto.com/id/184855907/photo/baked-sweet-potato.webp?a=1&b=1&s=612x612&w=0&k=20&c=KPHGdZzMNN_O8yGQDSlb84hn4RU95VyHhd8id8KGd6c=", 20000),
                MenuItemSeed("Creamy Mashed Potatoes", MenuCategory.CARB, "https://media.istockphoto.com/id/874918948/photo/mashed-potatoes-with-melted-butter.webp?a=1&b=1&s=612x612&w=0&k=20&c=qmG0lhEXeAJ2WYOoNkyhMBK9y_3cadE4TToo0Ql3oyk=", 22000),
                MenuItemSeed("Stir-fried Udon Noodles", MenuCategory.CARB, "https://images.unsplash.com/photo-1719785046018-8f599ef38984?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8U3Rpci1mcmllZCUyMFVkb24lMjBOb29kbGVzfGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=500", 28000),
                MenuItemSeed("Steamed Couscous", MenuCategory.CARB, "https://plus.unsplash.com/premium_photo-1699986347606-b9f6f935a42b?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8U3RlYW1lZCUyMENvdXNjb3VzfGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=500", 30000),
                MenuItemSeed("Creamy Oatmeal Bowl", MenuCategory.CARB, "https://images.unsplash.com/photo-1680137248876-6ad53db8caef?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8Q3JlYW15JTIwT2F0bWVhbCUyMEJvd2x8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=500", 20000),
                MenuItemSeed("Toasted Garlic Bread", MenuCategory.CARB, "https://images.unsplash.com/photo-1693530238368-20706826d197?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8VG9hc3RlZCUyMEdhcmxpYyUyMEJyZWFkfGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=500", 18000),
                MenuItemSeed("Steamed Jasmine Rice Bowl", MenuCategory.CARB, "https://images.unsplash.com/photo-1648788767168-aa2df5105037?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fFN0ZWFtZWQlMjBKYXNtaW5lJTIwUmljZSUyMEJvd2x8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=500", 15000),
                MenuItemSeed("Steamed Basmati Rice Bowl", MenuCategory.CARB, "https://plus.unsplash.com/premium_photo-1664391850490-8e164423c3a7?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8U3RlYW1lZCUyMEJhc21hdGklMjBSaWNlJTIwQm93bHxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=500", 20000),
                MenuItemSeed("Cold Soba Noodles Salad", MenuCategory.CARB, "https://plus.unsplash.com/premium_photo-1669687759534-1c4437b45605?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8Q29sZCUyMFNvYmElMjBOb29kbGVzJTIwU2FsYWR8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=500", 32000),
                MenuItemSeed("Stir-fried Rice Noodles", MenuCategory.CARB, "https://images.unsplash.com/photo-1649173284332-9d95b317fbd3?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fFN0aXItZnJpZWQlMjBSaWNlJTIwTm9vZGxlc3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=500", 18000),
                MenuItemSeed("Cooked Buckwheat Groats", MenuCategory.CARB, "https://images.unsplash.com/photo-1634045793583-176ea8dc055b?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1074", 25000),
                MenuItemSeed("Pearl Barley Porridge", MenuCategory.CARB, "https://images.unsplash.com/photo-1682692094951-2972f739cc6f?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8UGVhcmwlMjBCYXJsZXklMjBQb3JyaWRnZXxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=500", 22000),
                MenuItemSeed("Seasoned Bulgur Wheat", MenuCategory.CARB, "https://images.unsplash.com/photo-1623066798929-946425dbe1b0?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170", 28000),
                MenuItemSeed("Creamy Polenta Bowl", MenuCategory.CARB, "https://images.unsplash.com/photo-1718009793079-d5b3aa81a62f?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170", 25000),
                MenuItemSeed("Cooked Farro Grain", MenuCategory.CARB, "https://images.unsplash.com/photo-1569684165065-8f86108fdb88?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1182", 30000),
                MenuItemSeed("Toasted Sourdough Bread", MenuCategory.CARB, "https://images.unsplash.com/photo-1593285702005-ccc93dfe7cfb?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170", 20000),
                MenuItemSeed("Fresh Baked Baguette", MenuCategory.CARB, "https://images.unsplash.com/photo-1734988761778-911b94c06373?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=687", 15000),
                MenuItemSeed("Soft Flour Tortilla", MenuCategory.CARB, "https://images.unsplash.com/photo-1625407406725-201fad3682b1?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8U29mdCUyMEZsb3VyJTIwVG9ydGlsbGF8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=500", 18000),

                // PROTEIN (29 items) - Giá từ 25,000 - 80,000 VND
                MenuItemSeed("Grilled Chicken Breast", MenuCategory.PROTEIN, "https://images.unsplash.com/photo-1577194509876-4bb24787a641?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170", 45000),
                MenuItemSeed("Crispy Fried Chicken", MenuCategory.PROTEIN, "https://images.unsplash.com/photo-1605543986865-76ff0c4ee22d?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8Q3Jpc3B5JTIwRnJpZWQlMjBDaGlja2VufGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=500", 45000),
                MenuItemSeed("Seared Beef Steak", MenuCategory.PROTEIN, "https://plus.unsplash.com/premium_photo-1726826431660-e6041e3a9d02?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8U2VhcmVkJTIwQmVlZiUyMFN0ZWFrfGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=500", 80000),
                MenuItemSeed("Pan-fried Pork Chop", MenuCategory.PROTEIN, "https://images.unsplash.com/photo-1734775373504-ff24ea8419b2?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8UGFuLWZyaWVkJTIwUG9yayUyMENob3B8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=500", 50000),
                MenuItemSeed("Pan-seared Tofu Blocks", MenuCategory.PROTEIN, "https://images.unsplash.com/photo-1745582763219-1a5259056ba3?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8UGFuLXNlYXJlZCUyMFRvZnUlMjBCbG9ja3N8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=500", 25000),
                MenuItemSeed("Grilled Salmon Fillet", MenuCategory.PROTEIN, "https://images.unsplash.com/photo-1758157836016-3f3fbc5bf796?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8R3JpbGxlZCUyMFNhbG1vbiUyMEZpbGxldHxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=500", 75000),
                MenuItemSeed("Seared Tuna Steak", MenuCategory.PROTEIN, "https://images.unsplash.com/photo-1719464456515-6d09c34f6f9d?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTJ8fFNlYXJlZCUyMFR1bmElMjBTdGVha3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=500", 60000),
                MenuItemSeed("Garlic Butter Shrimp", MenuCategory.PROTEIN, "https://images.unsplash.com/photo-1706501187020-e92bcaa47f6f?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fEdhcmxpYyUyMEJ1dHRlciUyMFNocmltcHxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=500", 65000),
                MenuItemSeed("Soft Boiled Eggs", MenuCategory.PROTEIN, "https://images.unsplash.com/photo-1680987398307-e1ae27a6ed67?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8U29mdCUyMEJvaWxlZCUyMEVnZ3N8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=500", 10000),
                MenuItemSeed("Roasted Turkey Breast", MenuCategory.PROTEIN, "https://images.unsplash.com/photo-1611489142329-5f62cfa43e6e?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170", 55000),
                MenuItemSeed("Herb Roasted Chicken", MenuCategory.PROTEIN, "https://plus.unsplash.com/premium_photo-1669245207961-0281fd9396eb?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8SGVyYiUyMFJvYXN0ZWQlMjBDaGlja2VufGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=500", 50000),
                MenuItemSeed("Braised Chicken Thighs", MenuCategory.PROTEIN, "https://example.com/images/braised-chicken-thighs.jpg", 40000),
                MenuItemSeed("BBQ Chicken Wings", MenuCategory.PROTEIN, "https://example.com/images/bbq-chicken-wings.jpg", 45000),
                MenuItemSeed("Seasoned Ground Beef", MenuCategory.PROTEIN, "https://example.com/images/seasoned-ground-beef.jpg", 55000),
                MenuItemSeed("Grilled Ribeye Steak", MenuCategory.PROTEIN, "https://example.com/images/grilled-ribeye-steak.jpg", 85000),
                MenuItemSeed("Pan-seared Sirloin", MenuCategory.PROTEIN, "https://example.com/images/seared-sirloin.jpg", 75000),
                MenuItemSeed("Crispy Pork Belly", MenuCategory.PROTEIN, "https://example.com/images/crispy-pork-belly.jpg", 55000),
                MenuItemSeed("Honey Glazed Ham", MenuCategory.PROTEIN, "https://example.com/images/honey-glazed-ham.jpg", 40000),
                MenuItemSeed("Crispy Bacon Strips", MenuCategory.PROTEIN, "https://example.com/images/crispy-bacon-strips.jpg", 45000),
                MenuItemSeed("Grilled Lamb Chops", MenuCategory.PROTEIN, "https://example.com/images/grilled-lamb-chops.jpg", 90000),
                MenuItemSeed("Pan-roasted Duck Breast", MenuCategory.PROTEIN, "https://example.com/images/roasted-duck-breast.jpg", 70000),
                MenuItemSeed("Grilled Tempeh Slices", MenuCategory.PROTEIN, "https://example.com/images/grilled-tempeh.jpg", 30000),
                MenuItemSeed("Marinated Seitan Strips", MenuCategory.PROTEIN, "https://example.com/images/marinated-seitan.jpg", 35000),
                MenuItemSeed("Seasoned Black Beans", MenuCategory.PROTEIN, "https://example.com/images/seasoned-black-beans.jpg", 20000),
                MenuItemSeed("Roasted Chickpeas", MenuCategory.PROTEIN, "https://example.com/images/roasted-chickpeas.jpg", 22000),
                MenuItemSeed("Cooked Lentils", MenuCategory.PROTEIN, "https://example.com/images/cooked-lentils.jpg", 20000),
                MenuItemSeed("Steamed Edamame", MenuCategory.PROTEIN, "https://example.com/images/steamed-edamame.jpg", 25000),
                MenuItemSeed("Smoked Salmon Slices", MenuCategory.PROTEIN, "https://example.com/images/smoked-salmon-slices.jpg", 85000),
                MenuItemSeed("Grilled Sardines", MenuCategory.PROTEIN, "https://example.com/images/grilled-sardines.jpg", 35000),

                // VEGETABLE (24 items) - Giá từ 8,000 - 25,000 VND
                MenuItemSeed("Steamed Broccoli Florets", MenuCategory.VEGETABLE, "https://images.unsplash.com/photo-1708388064828-565ad865e12d?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=1170", 15000),
                MenuItemSeed("Roasted Baby Carrots", MenuCategory.VEGETABLE, "https://plus.unsplash.com/premium_photo-1689596509761-992d9cd41915?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=687", 10000),
                MenuItemSeed("Sautéed Spinach", MenuCategory.VEGETABLE, "https://plus.unsplash.com/premium_photo-1669905375174-126bc215e858?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fFNhdXQlQzMlQTllZCUyMFNwaW5hY2h8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=500", 12000),
                MenuItemSeed("Massaged Kale Salad", MenuCategory.VEGETABLE, "https://images.unsplash.com/photo-1722032617357-7b09276b1a8d?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fE1hc3NhZ2VkJTIwS2FsZSUyMFNhbGFkfGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=500", 18000),
                MenuItemSeed("Fresh Lettuce Mix", MenuCategory.VEGETABLE, "https://images.unsplash.com/photo-1697862446263-2ae694814ccc?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8RnJlc2glMjBMZXR0dWNlJTIwTWl4fGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=500", 10000),
                MenuItemSeed("Sliced Fresh Tomatoes", MenuCategory.VEGETABLE, "https://images.unsplash.com/photo-1566045023250-ccb8bb717880?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTJ8fFNsaWNlZCUyMEZyZXNoJTIwVG9tYXRvZXN8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=500", 12000),
                MenuItemSeed("Fresh Cucumber Slices", MenuCategory.VEGETABLE, "https://images.unsplash.com/photo-1599448191905-7bedab8ced59?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjZ8fEZyZXNoJTIwQ3VjdW1iZXIlMjBTbGljZXN8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=500", 8000),
                MenuItemSeed("Roasted Bell Peppers", MenuCategory.VEGETABLE, "https://plus.unsplash.com/premium_photo-1692131089926-8669f11828d0?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NDl8fFJvYXN0ZWQlMjBCZWxsJTIwUGVwcGVyc3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=500", 15000),
                MenuItemSeed("Grilled Corn on Cob", MenuCategory.VEGETABLE, "https://images.unsplash.com/photo-1601284702796-a27ffd3eff3c?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fEdyaWxsZWQlMjBDb3JuJTIwb24lMjBDb2J8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&q=60&w=500", 12000),
                MenuItemSeed("Blanched Green Beans", MenuCategory.VEGETABLE, "https://plus.unsplash.com/premium_photo-1663844169467-ddb73a03afdc?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8QmxhbmNoZWQlMjBHcmVlbiUyMEJlYW5zfGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=500", 10000),
                MenuItemSeed("Grilled Asparagus Spears", MenuCategory.VEGETABLE, "https://images.unsplash.com/photo-1653107509280-70a87fdac764?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8R3JpbGxlZCUyMEFzcGFyYWd1cyUyMFNwZWFyc3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&q=60&w=500", 25000),
                MenuItemSeed("Sautéed Zucchini Rounds", MenuCategory.VEGETABLE, "https://example.com/images/sauteed-zucchini.jpg", 15000),
                MenuItemSeed("Grilled Eggplant Slices", MenuCategory.VEGETABLE, "https://example.com/images/grilled-eggplant.jpg", 12000),
                MenuItemSeed("Roasted Cauliflower", MenuCategory.VEGETABLE, "https://example.com/images/roasted-cauliflower.jpg", 15000),
                MenuItemSeed("Shredded Cabbage Slaw", MenuCategory.VEGETABLE, "https://example.com/images/cabbage-slaw.jpg", 10000),
                MenuItemSeed("Sautéed Mixed Mushrooms", MenuCategory.VEGETABLE, "https://example.com/images/sauteed-mushrooms.jpg", 20000),
                MenuItemSeed("Sweet Garden Peas", MenuCategory.VEGETABLE, "https://example.com/images/sweet-peas.jpg", 12000),
                MenuItemSeed("Roasted Brussels Sprouts", MenuCategory.VEGETABLE, "https://example.com/images/roasted-brussels-sprouts.jpg", 18000),
                MenuItemSeed("Caramelized Onions", MenuCategory.VEGETABLE, "https://example.com/images/caramelized-onions.jpg", 8000),
                MenuItemSeed("Roasted Garlic Cloves", MenuCategory.VEGETABLE, "https://example.com/images/roasted-garlic.jpg", 10000),
                MenuItemSeed("Pickled Red Cabbage", MenuCategory.VEGETABLE, "https://example.com/images/pickled-red-cabbage.jpg", 12000),
                MenuItemSeed("Fresh Arugula Salad", MenuCategory.VEGETABLE, "https://example.com/images/arugula-salad.jpg", 15000),
                MenuItemSeed("Roasted Beetroot Wedges", MenuCategory.VEGETABLE, "https://example.com/images/roasted-beetroot.jpg", 15000),
                MenuItemSeed("Roasted Pumpkin Cubes", MenuCategory.VEGETABLE, "https://example.com/images/roasted-pumpkin.jpg", 12000),

                // SAUCE (15 items) - Giá từ 5,000 - 15,000 VND
                MenuItemSeed("Teriyaki Glaze Sauce", MenuCategory.SAUCE, "https://images.unsplash.com/photo-1646602482554-6543198c539a?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8VGVyaXlha2klMjBHbGF6ZSUyMFNhdWNlfGVufDB8fDB8fHww&auto=format&fit=crop&q=60&w=500", 10000),
                MenuItemSeed("Light Soy Sauce", MenuCategory.SAUCE, "https://media.istockphoto.com/id/857494286/vi/anh/n%C6%B0%E1%BB%9Bc-t%C6%B0%C6%A1ng-trong-b%C3%A1t-c%C3%A1ch-ly-tr%C3%AAn-n%E1%BB%81n-tr%E1%BA%AFng.jpg?s=612x612&w=0&k=20&c=RXksTP-wY1KCZi9gCcfZio2F9D5JLBCJEeC1_2OKiAs=", 5000),
                MenuItemSeed("Spicy Chili Sauce", MenuCategory.SAUCE, "https://media.istockphoto.com/id/1401340171/vi/anh/chai-t%C6%B0%C6%A1ng-%E1%BB%9Bt-n%C3%B3ng.jpg?s=612x612&w=0&k=20&c=hTFrpSFvFS-fvb4cCpzlrSK6tyLUvPGb3ASNA1bRj-k=", 8000),
                MenuItemSeed("Creamy Garlic Sauce", MenuCategory.SAUCE, "https://media.istockphoto.com/id/1877639481/vi/anh/n%C6%B0%E1%BB%9Bc-s%E1%BB%91t-ngon-v%E1%BB%9Bi-t%E1%BB%8Fi-v%C3%A0-th%C3%ACa-tr%C3%AAn-b%C3%A0n-c%E1%BA%ADn-c%E1%BA%A3nh.jpg?s=612x612&w=0&k=20&c=NDH28SxjmE9tj8LwX8AAQM3qN1vkFMxikwemjlYS8N4=", 8000),
                MenuItemSeed("Smoky BBQ Sauce", MenuCategory.SAUCE, "https://media.istockphoto.com/id/521294143/vi/anh/s%E1%BB%91t-barbeque.jpg?s=612x612&w=0&k=20&c=--j_Huen-jRQrfXlzAOT1xcj72BSS3K7xRIKrzd0yCE=", 10000),
                MenuItemSeed("Japanese Mayo Sauce", MenuCategory.SAUCE, "https://media.istockphoto.com/id/1144262865/vi/anh/th%C3%AAm-mayonnaise-v%C3%A0o-c%E1%BB%A7-c%E1%BA%A3i.jpg?s=612x612&w=0&k=20&c=MJvzZGIe_nvFxvUfrL0sA3u6yLo_FOqUUnr1CV_-i_A=", 8000),
                MenuItemSeed("Tomato Ketchup", MenuCategory.SAUCE, "https://media.istockphoto.com/id/184388608/vi/anh/s%E1%BB%91t-c%C3%A0-chua.jpg?s=612x612&w=0&k=20&c=H0rHTxxzwBK7MhbV0wfsNjscKghNlQlBVorx4iMOuJ4=", 5000),
                MenuItemSeed("Dijon Mustard Sauce", MenuCategory.SAUCE, "https://media.istockphoto.com/id/1143035394/vi/anh/s%E1%BB%91t-m%C3%B9-t%E1%BA%A1t-dijon-kem-trong-l%E1%BB%8D.jpg?s=612x612&w=0&k=20&c=5wuRsg-LT6Xa93kCn84i5zbgRhsOG7h74pV3CiBPJeY=", 7000),
                MenuItemSeed("Creamy Ranch Dressing", MenuCategory.SAUCE, "https://media.istockphoto.com/id/1347705359/vi/anh/trang-tr%E1%BA%A1i-h%E1%BB%AFu-c%C6%A1-t%E1%BB%B1-ch%E1%BA%BF.jpg?s=612x612&w=0&k=20&c=3bZBVvYP2f94gXTrBkZOoWcwH4fCdpYAUYTTzn_BTw8=", 12000),
                MenuItemSeed("Caesar Salad Dressing", MenuCategory.SAUCE, "https://th.bing.com/th/id/OSK.c1c893ab9fd409ed0fa0dd864d185b7f?w=200&h=126&c=7&rs=1&qlt=80&o=6&cdv=1&cb=12&dpr=1.1&pid=16.1", 12000),
                MenuItemSeed("Basil Pesto Sauce", MenuCategory.SAUCE, "https://media.istockphoto.com/id/1211829786/vi/anh/s%E1%BB%91t-pesto-t%E1%BB%B1-l%C3%A0m-trong-m%E1%BB%99t-c%C3%A1i-b%C3%A1t-g%E1%BB%97-v%C3%A0-c%C3%A1c-th%C3%A0nh-ph%E1%BA%A7n-%C4%91%E1%BB%83-n%E1%BA%A5u-%C4%83n-tr%C3%AAn-n%E1%BB%81n-x%C3%A1m.jpg?s=612x612&w=0&k=20&c=lBqU0D_n1BviJOLUkL5C1Mk-ZGS1OvHQ6UkMv5UXi_0=", 15000),
                MenuItemSeed("Hot Sriracha Sauce", MenuCategory.SAUCE, "https://example.com/images/hot-sriracha.jpg", 10000),
                MenuItemSeed("Sweet Honey Mustard", MenuCategory.SAUCE, "https://example.com/images/honey-mustard.jpg", 10000),
                MenuItemSeed("Buffalo Wing Sauce", MenuCategory.SAUCE, "https://example.com/images/buffalo-wing-sauce.jpg", 12000),
                MenuItemSeed("Tartar Dipping Sauce", MenuCategory.SAUCE, "https://example.com/images/tartar-sauce.jpg", 10000),

                // DAIRY (13 items) - Giá từ 10,000 - 35,000 VND
                MenuItemSeed("Shredded Cheddar Cheese", MenuCategory.DAIRY, "https://media.istockphoto.com/id/487136934/vi/anh/ph%C3%B4-mai-cheddar.jpg?s=612x612&w=0&k=20&c=PtrqVz4ru8p3r8AIiSftVJcf0DxF2R879F6Wp3DMkOM=", 25000),
                MenuItemSeed("Fresh Mozzarella Cheese", MenuCategory.DAIRY, "https://media.istockphoto.com/id/181146375/vi/anh/mozzarella-tr%C3%A2u.jpg?s=612x612&w=0&k=20&c=qMjihwbdYNWwSFyzeNX7HjNxML8FEnKyrXGUVOalhsI=", 25000),
                MenuItemSeed("Greek Yogurt Cup", MenuCategory.DAIRY, "https://media.istockphoto.com/id/1388253091/vi/anh/s%E1%BB%AFa-chua-v%C3%A0-tr%C3%A1i-c%C3%A2y.jpg?s=612x612&w=0&k=20&c=cj1M1X9cDYQWw4AXWWQrsL2xt3Bpta7KapNlsVGoAOo=", 20000),
                MenuItemSeed("Melted Butter", MenuCategory.DAIRY, "https://media.istockphoto.com/id/163713484/vi/anh/b%C6%A1-tan-ch%E1%BA%A3y.jpg?s=612x612&w=0&k=20&c=T8fwnU9ltIFSeKyqBvNEz0IxXgMSPwS8sjGtX1HVe8o=", 15000),
                MenuItemSeed("Fresh Whole Milk", MenuCategory.DAIRY, "https://media.istockphoto.com/id/1404644967/vi/anh/to%C3%A0n-b%E1%BB%99-kem-l%E1%BA%A5y-t%E1%BB%AB-m%E1%BB%99t-ly-s%E1%BB%AFa.jpg?s=612x612&w=0&k=20&c=VqVW3SARLlRsE35L0yDPzS5EGeK3TJ9IZ7tUpTbc1dI=", 12000),
                MenuItemSeed("Grated Parmesan Cheese", MenuCategory.DAIRY, "https://media.istockphoto.com/id/471343790/vi/anh/ph%C3%B4-mai-parmesan-nghi%E1%BB%81n.jpg?s=612x612&w=0&k=20&c=tbyFaDTyRK7VJjJ51Evo4cC2s-Lhy4kl84C4K3nQlBY=", 30000),
                MenuItemSeed("Crumbled Feta Cheese", MenuCategory.DAIRY, "https://example.com/images/crumbled-feta.jpg", 28000),
                MenuItemSeed("Blue Cheese Crumbles", MenuCategory.DAIRY, "https://example.com/images/blue-cheese-crumbles.jpg", 35000),
                MenuItemSeed("Sour Cream Dollop", MenuCategory.DAIRY, "https://example.com/images/sour-cream-dollop.jpg", 18000),
                MenuItemSeed("Cream Cheese Spread", MenuCategory.DAIRY, "https://example.com/images/cream-cheese-spread.jpg", 22000),
                MenuItemSeed("Ricotta Cheese", MenuCategory.DAIRY, "https://example.com/images/ricotta-cheese.jpg", 25000),
                MenuItemSeed("Vanilla Ice Cream Scoop", MenuCategory.DAIRY, "https://example.com/images/vanilla-ice-cream.jpg", 20000),
                MenuItemSeed("Cottage Cheese Bowl", MenuCategory.DAIRY, "https://example.com/images/cottage-cheese-bowl.jpg", 22000),

                // FRUIT (24 items) - Giá từ 8,000 - 40,000 VND
                MenuItemSeed("Sliced Fresh Apple", MenuCategory.FRUIT, "https://media.istockphoto.com/id/614871876/vi/anh/t%C3%A1o-b%E1%BB%8B-c%C3%B4-l%E1%BA%ADp-tr%C3%AAn-n%E1%BB%81n-g%E1%BB%97.jpg?s=612x612&w=0&k=20&c=5HGk1ws_SDOZOEsP5lL6upg_ijEFYyvz90797skMvEA=", 15000),
                MenuItemSeed("Peeled Banana", MenuCategory.FRUIT, "https://media.istockphoto.com/id/696544568/vi/anh/chu%E1%BB%91i-g%E1%BB%8Dt-v%E1%BB%8F-tay.jpg?s=612x612&w=0&k=20&c=jp_Xj22wLn5qTrOoXo-1aXt5jBc-BfgdLDi3ikir8tA=", 10000),
                MenuItemSeed("Orange Segments", MenuCategory.FRUIT, "https://media.istockphoto.com/id/617365366/vi/anh/l%C3%A1t-tr%C3%A1i-c%C3%A2y-m%C3%A0u-cam-c%C3%B4-l%E1%BA%ADp-tr%C3%AAn-m%C3%A0u-tr%E1%BA%AFng.jpg?s=612x612&w=0&k=20&c=qjjOGURC7cAcDBW3ZBGbpnN2KDQeeSeIbcxxF-IP_u0=", 12000),
                MenuItemSeed("Fresh Pineapple Chunks", MenuCategory.FRUIT, "https://media.istockphoto.com/id/1412852692/vi/anh/d%E1%BB%A9a-v%E1%BB%9Bi-l%C3%A1-v%C3%A0-l%C3%A1t-b%E1%BB%8B-c%C3%B4-l%E1%BA%ADp-c%E1%BA%AFt-d%E1%BB%A9a-v%E1%BB%9Bi-c%C3%A1c-mi%E1%BA%BFng-tr%C3%AAn-n%E1%BB%81n-tr%E1%BA%AFng-%C4%91%E1%BB%99-s%C3%A2u-tr%C6%B0%E1%BB%9Dng-%E1%BA%A3nh-%C4%91%E1%BA%A7y-%C4%91%E1%BB%A7.jpg?s=612x612&w=0&k=20&c=puhPpBNnntxdewXdfStd-sUGVYfsZp3uqVsMMJxKmds=", 25000),
                MenuItemSeed("Diced Fresh Mango", MenuCategory.FRUIT, "https://media.istockphoto.com/id/187899456/vi/anh/xo%C3%A0i-c%C3%B4-l%E1%BA%ADp-tr%C3%AAn-tr%E1%BA%AFng.jpg?s=612x612&w=0&k=20&c=nrgbInGq8J-nj-Iz_VwBpYTWx1-E00L1FxDH1h2P01g=", 20000),
                MenuItemSeed("Fresh Strawberries", MenuCategory.FRUIT, "https://media.istockphoto.com/id/2004978876/vi/anh/d%C3%A2u-t%C3%A2y-trong-m%E1%BB%99t-c%C3%A1i-%C4%91%C4%A9a-tr%C3%AAn-b%C3%A0n-g%E1%BB%97.jpg?s=612x612&w=0&k=20&c=xFSI9DLwowKUnwEhIkMX60tGmjeYyjQyctvPVjsses8=", 30000),
                MenuItemSeed("Fresh Blueberries", MenuCategory.FRUIT, "https://media.istockphoto.com/id/529677122/vi/anh/si%C3%AAu-th%E1%BB%B1c-ph%E1%BA%A9m-h%E1%BB%AFu-c%C6%A1-ch%E1%BB%91ng-oxy-h%C3%B3a-vi%E1%BB%87t-qu%E1%BA%A5t.jpg?s=612x612&w=0&k=20&c=v-evq654j_xllZ9cG-CLUDD3pgoTOtOsmAo19dDKq4o=", 35000),
                MenuItemSeed("Green Grape Bunch", MenuCategory.FRUIT, "https://media.istockphoto.com/id/1282402219/vi/anh/c%C3%A0nh-nho-xanh-tr%C3%AAn-n%E1%BB%81n.jpg?s=612x612&w=0&k=20&c=CX2ocC8RMLGQSHsHgqCxVeJhy5Ep43ByS4i68mXPplI=", 25000),
                MenuItemSeed("Watermelon Wedges", MenuCategory.FRUIT, "https://media.istockphoto.com/id/181051974/vi/anh/l%C3%A1t-d%C6%B0a-h%E1%BA%A5u-t%C6%B0%C6%A1i.jpg?s=612x612&w=0&k=20&c=C2BykQBZPny3U2HbdCzgMug6y47LUey4DzEVjIe2SEU=", 15000),
                MenuItemSeed("Sliced Kiwi Fruit", MenuCategory.FRUIT, "https://media.istockphoto.com/id/1359560892/vi/video/kiwi-r%C6%A1i-xu%E1%BB%91ng-v%C3%A0-chia-th%C3%A0nh-hai-n%E1%BB%ADa-quay-phim-l%C3%A0-chuy%E1%BB%83n-%C4%91%E1%BB%99ng-ch%E1%BA%ADm-1000-khung-h%C3%ACnh-m%E1%BB%97i-gi%C3%A2y.avif?s=640x640&k=20&c=cr0IROcZTVoD5Bs3H4amA2tJBxQmJJnHSQ2z7w0FRAg=", 18000),
                MenuItemSeed("Fresh Peach Slices", MenuCategory.FRUIT, "https://media.istockphoto.com/id/824163462/vi/anh/%C4%91%C3%A0o-t%C6%B0%C6%A1i-%C4%91%C3%A0o-c%E1%BA%ADn-c%E1%BA%A3nh-n%E1%BB%81n-tr%C3%A1i-c%C3%A2y-%C4%91%C3%A0o-tr%C3%AAn-n%E1%BB%81n-g%E1%BB%97-%C4%91%C3%A0o-ng%E1%BB%8Dt-nh%C3%B3m-%C4%91%C3%A0o-%C4%91%C3%A0o-th%C3%A1i-l%C3%A1t-l%C3%A1t-%C4%91%C3%A0o.jpg?s=612x612&w=0&k=20&c=-_I0fMhysYNWuQhhmfeEbUEr0LTIhMQpNXW0gTJGkPw=", 20000),
                MenuItemSeed("Sliced Pear", MenuCategory.FRUIT, "https://example.com/images/sliced-pear.jpg", 18000),
                MenuItemSeed("Fresh Plums", MenuCategory.FRUIT, "https://example.com/images/fresh-plums.jpg", 15000),
                MenuItemSeed("Pitted Cherries", MenuCategory.FRUIT, "https://example.com/images/pitted-cherries.jpg", 40000),
                MenuItemSeed("Fresh Raspberries", MenuCategory.FRUIT, "https://example.com/images/fresh-raspberries.jpg", 35000),
                MenuItemSeed("Fresh Blackberries", MenuCategory.FRUIT, "https://example.com/images/fresh-blackberries.jpg", 35000),
                MenuItemSeed("Dragon Fruit Cubes", MenuCategory.FRUIT, "https://example.com/images/dragon-fruit-cubes.jpg", 25000),
                MenuItemSeed("Fresh Papaya Slices", MenuCategory.FRUIT, "https://example.com/images/papaya-slices.jpg", 15000),
                MenuItemSeed("Fresh Lemon Wedges", MenuCategory.FRUIT, "https://example.com/images/lemon-wedges.jpg", 8000),
                MenuItemSeed("Lime Wedges", MenuCategory.FRUIT, "https://example.com/images/lime-wedges.jpg", 8000),
                MenuItemSeed("Fresh Coconut Chunks", MenuCategory.FRUIT, "https://example.com/images/coconut-chunks.jpg", 15000),
                MenuItemSeed("Pomegranate Seeds", MenuCategory.FRUIT, "https://example.com/images/pomegranate-seeds.jpg", 30000),
                MenuItemSeed("Grapefruit Segments", MenuCategory.FRUIT, "https://example.com/images/grapefruit-segments.jpg", 18000),
                MenuItemSeed("Sliced Avocado", MenuCategory.FRUIT, "https://example.com/images/sliced-avocado.jpg", 35000)
            )

            fun calFor(categoryName: String, price: Int, name: String): Int {
                val base = when (categoryName) {
                    "Carbohydrates" -> 180
                    "Proteins" -> 220
                    "Vegetables" -> 40
                    "Sauces" -> 60
                    "Dairy" -> 140
                    "Fruits" -> 80
                    else -> 120
                }
                val scale = when (categoryName) {
                    "Carbohydrates" -> 5
                    "Proteins" -> 4
                    "Vegetables" -> 2
                    "Sauces" -> 3
                    "Dairy" -> 4
                    "Fruits" -> 2
                    else -> 3
                }
                val byPrice = (price / 1000) * scale
                val jitter = (name.hashCode().absoluteValue % 21) - 10 // -10..+10
                return (base + byPrice + jitter).coerceAtLeast(10)
            }

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
                val cal = calFor(categoryName, item.price, item.name)
                val desc = "Delicious ${item.name} from ${categoryName.lowercase()} category"
                MenuItem(
                    name = item.name,
                    category = cat,
                    imageUrl = item.imageUrl,
                    price = item.price,
                    cal = cal,
                    description = desc,
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

// ==================== INGREDIENTS (Raw Materials) ====================
        if (ingredientRepository.count() == 0L) {
            println("Seeding ingredients...")

            val ingredientsData = listOf(
                // Raw Proteins
                Triple("Raw Chicken Breast", UnitType.G, "RCB"),
                Triple("Raw Chicken Thigh", UnitType.G, "RCT"),
                Triple("Raw Chicken Wings", UnitType.G, "RCW"),
                Triple("Raw Beef Sirloin", UnitType.G, "RBS"),
                Triple("Raw Ground Beef", UnitType.G, "RGB"),
                Triple("Raw Ribeye Steak", UnitType.G, "RRE"),
                Triple("Raw Pork Loin", UnitType.G, "RPL"),
                Triple("Raw Pork Belly", UnitType.G, "RPB"),
                Triple("Raw Pork Chop", UnitType.G, "RPC"),
                Triple("Raw Salmon Fillet", UnitType.G, "RSF"),
                Triple("Raw Tuna", UnitType.G, "RTN"),
                Triple("Raw Shrimp", UnitType.G, "RSH"),
                Triple("Raw Lamb Chop", UnitType.G, "RLC"),
                Triple("Raw Duck Breast", UnitType.G, "RDB"),
                Triple("Raw Turkey Breast", UnitType.G, "RTB"),
                Triple("Raw Sardines", UnitType.G, "RSD"),
                Triple("Fresh Tofu Block", UnitType.G, "FTB"),
                Triple("Fresh Tempeh", UnitType.G, "FTP"),
                Triple("Fresh Seitan", UnitType.G, "FST"),
                Triple("Fresh Whole Eggs", UnitType.G, "FEG"),

                // Raw Carbs & Grains
                Triple("Uncooked White Rice", UnitType.G, "UWR"),
                Triple("Uncooked Brown Rice", UnitType.G, "UBR"),
                Triple("Uncooked Jasmine Rice", UnitType.G, "UJR"),
                Triple("Uncooked Basmati Rice", UnitType.G, "UBT"),
                Triple("Uncooked Quinoa", UnitType.G, "UQN"),
                Triple("Raw Sweet Potato", UnitType.G, "RSP"),
                Triple("Raw Potato", UnitType.G, "RPT"),
                Triple("Dry Pasta", UnitType.G, "DPA"),
                Triple("Dry Noodles", UnitType.G, "DND"),
                Triple("Dry Udon Noodles", UnitType.G, "DUN"),
                Triple("Dry Soba Noodles", UnitType.G, "DSN"),
                Triple("Dry Rice Noodles", UnitType.G, "DRN"),
                Triple("Raw Buckwheat", UnitType.G, "RBW"),
                Triple("Raw Barley", UnitType.G, "RBL"),
                Triple("Raw Bulgur Wheat", UnitType.G, "RBG"),
                Triple("Raw Farro", UnitType.G, "RFR"),
                Triple("Raw Couscous", UnitType.G, "RCS"),
                Triple("Dry Oats", UnitType.G, "DOT"),
                Triple("Dry Polenta", UnitType.G, "DPL"),
                Triple("Fresh Bread Loaf", UnitType.G, "FBL"),
                Triple("Fresh Baguette", UnitType.G, "FBG"),
                Triple("Fresh Tortillas", UnitType.G, "FTR"),

                // Raw Vegetables
                Triple("Fresh Broccoli", UnitType.G, "FBR"),
                Triple("Fresh Carrots", UnitType.G, "FCR"),
                Triple("Fresh Spinach Leaves", UnitType.G, "FSP"),
                Triple("Fresh Kale Leaves", UnitType.G, "FKL"),
                Triple("Fresh Lettuce", UnitType.G, "FLT"),
                Triple("Fresh Tomatoes", UnitType.G, "FTM"),
                Triple("Fresh Cucumber", UnitType.G, "FCU"),
                Triple("Fresh Bell Peppers", UnitType.G, "FBP"),
                Triple("Fresh Corn", UnitType.G, "FCN"),
                Triple("Fresh Green Beans", UnitType.G, "FGB"),
                Triple("Fresh Asparagus", UnitType.G, "FAS"),
                Triple("Fresh Zucchini", UnitType.G, "FZC"),
                Triple("Fresh Eggplant", UnitType.G, "FEP"),
                Triple("Fresh Cauliflower", UnitType.G, "FCF"),
                Triple("Fresh Cabbage", UnitType.G, "FCB"),
                Triple("Fresh Red Cabbage", UnitType.G, "FRC"),
                Triple("Fresh Mushrooms", UnitType.G, "FMS"),
                Triple("Fresh Peas", UnitType.G, "FPS"),
                Triple("Fresh Brussels Sprouts", UnitType.G, "FBS"),
                Triple("Fresh Onions", UnitType.G, "FON"),
                Triple("Fresh Garlic", UnitType.G, "FGL"),
                Triple("Fresh Arugula", UnitType.G, "FAR"),
                Triple("Fresh Beetroot", UnitType.G, "FBT"),
                Triple("Fresh Pumpkin", UnitType.G, "FPK"),

                // Legumes & Beans (Dry)
                Triple("Dry Black Beans", UnitType.G, "DBB"),
                Triple("Dry Chickpeas", UnitType.G, "DCP"),
                Triple("Dry Lentils", UnitType.G, "DLT"),
                Triple("Dry Edamame", UnitType.G, "DED"),

                // Sauces & Condiments (Ready-made)
                Triple("Teriyaki Sauce Bottle", UnitType.ML, "TSB"),
                Triple("Soy Sauce Bottle", UnitType.ML, "SSB"),
                Triple("BBQ Sauce Bottle", UnitType.ML, "BBS"),
                Triple("Mayonnaise Jar", UnitType.ML, "MYJ"),
                Triple("Chili Sauce Bottle", UnitType.ML, "CSB"),
                Triple("Garlic Sauce Jar", UnitType.ML, "GSJ"),
                Triple("Pesto Jar", UnitType.ML, "PSJ"),
                Triple("Sriracha Bottle", UnitType.ML, "SRB"),
                Triple("Mustard Jar", UnitType.ML, "MSJ"),
                Triple("Ketchup Bottle", UnitType.ML, "KTB"),
                Triple("Ranch Dressing", UnitType.ML, "RND"),
                Triple("Caesar Dressing", UnitType.ML, "CSD"),
                Triple("Buffalo Sauce", UnitType.ML, "BFS"),
                Triple("Tartar Sauce", UnitType.ML, "TTS"),
                Triple("Honey Mustard", UnitType.ML, "HMS"),

                // Cooking Oils
                Triple("Olive Oil", UnitType.ML, "OLO"),
                Triple("Vegetable Oil", UnitType.ML, "VGO"),

                // Dairy Products
                Triple("Cheddar Cheese Block", UnitType.G, "CCB"),
                Triple("Mozzarella Block", UnitType.G, "MZB"),
                Triple("Parmesan Block", UnitType.G, "PMB"),
                Triple("Feta Cheese", UnitType.G, "FTC"),
                Triple("Blue Cheese", UnitType.G, "BLC"),
                Triple("Greek Yogurt Container", UnitType.G, "GYC"),
                Triple("Fresh Milk", UnitType.ML, "FMK"),
                Triple("Unsalted Butter", UnitType.G, "USB"),
                Triple("Heavy Cream", UnitType.ML, "HVC"),
                Triple("Cream Cheese", UnitType.G, "CRC"),
                Triple("Ricotta Cheese", UnitType.G, "RCC"),
                Triple("Sour Cream", UnitType.G, "SRC"),
                Triple("Cottage Cheese", UnitType.G, "CTC"),
                Triple("Vanilla Ice Cream", UnitType.G, "VIC"),

                // Fresh Fruits
                Triple("Fresh Apples", UnitType.G, "FAP"),
                Triple("Fresh Bananas", UnitType.G, "FBN"),
                Triple("Fresh Oranges", UnitType.G, "FOR"),
                Triple("Fresh Pineapple", UnitType.G, "FPN"),
                Triple("Fresh Mango", UnitType.G, "FMG"),
                Triple("Fresh Strawberries", UnitType.G, "FST"),
                Triple("Fresh Blueberries", UnitType.G, "FBY"),
                Triple("Fresh Grapes", UnitType.G, "FGP"),
                Triple("Fresh Watermelon", UnitType.G, "FWM"),
                Triple("Fresh Kiwi", UnitType.G, "FKW"),
                Triple("Fresh Peaches", UnitType.G, "FPC"),
                Triple("Fresh Pears", UnitType.G, "FPR"),
                Triple("Fresh Plums", UnitType.G, "FPL"),
                Triple("Fresh Cherries", UnitType.G, "FCH"),
                Triple("Fresh Raspberries", UnitType.G, "FRB"),
                Triple("Fresh Blackberries", UnitType.G, "FBB"),
                Triple("Fresh Dragon Fruit", UnitType.G, "FDF"),
                Triple("Fresh Papaya", UnitType.G, "FPY"),
                Triple("Fresh Lemons", UnitType.G, "FLM"),
                Triple("Fresh Limes", UnitType.G, "FLI"),
                Triple("Fresh Coconut", UnitType.G, "FCO"),
                Triple("Fresh Pomegranate", UnitType.G, "FPG"),
                Triple("Fresh Grapefruit", UnitType.G, "FGF"),
                Triple("Fresh Avocado", UnitType.G, "FAV"),

                // Spices & Herbs
                Triple("Sea Salt", UnitType.G, "SSL"),
                Triple("Black Pepper", UnitType.G, "BPP"),
                Triple("Paprika Powder", UnitType.G, "PPD"),
                Triple("Cumin Powder", UnitType.G, "CMP"),
                Triple("Fresh Basil", UnitType.G, "FBL"),
                Triple("Fresh Parsley", UnitType.G, "FPL"),
                Triple("Fresh Cilantro", UnitType.G, "FCL"),
                Triple("Fresh Thyme", UnitType.G, "FTH"),
                Triple("Fresh Rosemary", UnitType.G, "FRS"),
                Triple("Fresh Ginger", UnitType.G, "FGR"),
                Triple("Chili Powder", UnitType.G, "CHP"),
                Triple("Garlic Powder", UnitType.G, "GLP"),
                Triple("Onion Powder", UnitType.G, "ONP")
            )

            ingredientsData.forEachIndexed { idx, (name, unit, prefix) ->
                ingredientRepository.save(Ingredient(
                    name = name,
                    description = "Raw Materials for service Menu Items",
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

        // ==================== RECIPES (Updated Mapping) ====================
        if (recipeRepository.count() == 0L) {
            println("Seeding recipes...")
            val items = menuItemRepository.findAll()
            val ingredients = ingredientRepository.findAll()

            val recipeMapping = mapOf(
                // Chicken dishes - using RAW ingredients
                "Grilled Chicken Breast" to listOf("Raw Chicken Breast", "Olive Oil", "Sea Salt", "Black Pepper", "Fresh Garlic"),
                "Crispy Fried Chicken" to listOf("Raw Chicken Thigh", "Vegetable Oil", "Sea Salt", "Black Pepper", "Paprika Powder"),
                "Herb Roasted Chicken" to listOf("Raw Chicken Breast", "Unsalted Butter", "Fresh Garlic", "Fresh Parsley", "Sea Salt", "Fresh Rosemary"),
                "Braised Chicken Thighs" to listOf("Raw Chicken Thigh", "Soy Sauce Bottle", "Fresh Garlic", "Sea Salt", "Fresh Onions"),
                "BBQ Chicken Wings" to listOf("Raw Chicken Wings", "BBQ Sauce Bottle", "Sea Salt", "Black Pepper"),
                "Roasted Turkey Breast" to listOf("Raw Turkey Breast", "Olive Oil", "Sea Salt", "Fresh Parsley", "Fresh Thyme"),

                // Beef dishes
                "Seared Beef Steak" to listOf("Raw Beef Sirloin", "Sea Salt", "Black Pepper", "Olive Oil", "Fresh Garlic"),
                "Seasoned Ground Beef" to listOf("Raw Ground Beef", "Sea Salt", "Black Pepper", "Fresh Onions", "Garlic Powder"),
                "Grilled Ribeye Steak" to listOf("Raw Ribeye Steak", "Unsalted Butter", "Fresh Garlic", "Sea Salt", "Fresh Thyme"),
                "Pan-seared Sirloin" to listOf("Raw Beef Sirloin", "Olive Oil", "Sea Salt", "Black Pepper"),

                // Pork dishes
                "Pan-fried Pork Chop" to listOf("Raw Pork Chop", "Sea Salt", "Black Pepper", "Olive Oil", "Fresh Garlic"),
                "Crispy Pork Belly" to listOf("Raw Pork Belly", "Soy Sauce Bottle", "Fresh Garlic", "Sea Salt"),
                "Honey Glazed Ham" to listOf("Raw Pork Loin", "Sea Salt", "Black Pepper"),
                "Crispy Bacon Strips" to listOf("Raw Pork Belly", "Sea Salt", "Black Pepper"),

                // Lamb & Duck
                "Grilled Lamb Chops" to listOf("Raw Lamb Chop", "Olive Oil", "Fresh Rosemary", "Fresh Garlic", "Sea Salt"),
                "Pan-roasted Duck Breast" to listOf("Raw Duck Breast", "Sea Salt", "Black Pepper", "Fresh Thyme"),

                // Seafood
                "Grilled Salmon Fillet" to listOf("Raw Salmon Fillet", "Olive Oil", "Sea Salt", "Fresh Parsley", "Fresh Garlic", "Fresh Lemons"),
                "Smoked Salmon Slices" to listOf("Raw Salmon Fillet", "Sea Salt", "Black Pepper"),
                "Seared Tuna Steak" to listOf("Raw Tuna", "Olive Oil", "Sea Salt", "Black Pepper"),
                "Garlic Butter Shrimp" to listOf("Raw Shrimp", "Unsalted Butter", "Fresh Garlic", "Sea Salt", "Fresh Parsley"),
                "Grilled Sardines" to listOf("Raw Sardines", "Olive Oil", "Sea Salt", "Fresh Lemons"),

                // Vegetarian proteins
                "Pan-seared Tofu Blocks" to listOf("Fresh Tofu Block", "Soy Sauce Bottle", "Fresh Garlic", "Vegetable Oil"),
                "Grilled Tempeh Slices" to listOf("Fresh Tempeh", "Soy Sauce Bottle", "Fresh Ginger", "Olive Oil"),
                "Marinated Seitan Strips" to listOf("Fresh Seitan", "Soy Sauce Bottle", "Fresh Garlic"),
                "Soft Boiled Eggs" to listOf("Fresh Whole Eggs", "Sea Salt"),

                // Legumes
                "Seasoned Black Beans" to listOf("Dry Black Beans", "Sea Salt", "Fresh Garlic", "Cumin Powder", "Fresh Onions"),
                "Roasted Chickpeas" to listOf("Dry Chickpeas", "Sea Salt", "Olive Oil", "Paprika Powder"),
                "Cooked Lentils" to listOf("Dry Lentils", "Sea Salt", "Fresh Onions", "Cumin Powder"),
                "Steamed Edamame" to listOf("Dry Edamame", "Sea Salt"),

                // Carbs - using UNCOOKED/DRY ingredients
                "Steamed White Rice Bowl" to listOf("Uncooked White Rice", "Sea Salt"),
                "Steamed Brown Rice Bowl" to listOf("Uncooked Brown Rice", "Sea Salt"),
                "Steamed Quinoa Bowl" to listOf("Uncooked Quinoa", "Sea Salt", "Olive Oil"),
                "Baked Sweet Potato" to listOf("Raw Sweet Potato", "Olive Oil", "Sea Salt"),
                "Creamy Mashed Potatoes" to listOf("Raw Potato", "Fresh Milk", "Unsalted Butter", "Sea Salt"),
                "Whole Wheat Pasta with Olive Oil" to listOf("Dry Pasta", "Olive Oil", "Sea Salt", "Fresh Garlic"),
                "Stir-fried Udon Noodles" to listOf("Dry Udon Noodles", "Soy Sauce Bottle", "Vegetable Oil"),
                "Cold Soba Noodles Salad" to listOf("Dry Soba Noodles", "Soy Sauce Bottle", "Fresh Cucumber"),
                "Stir-fried Rice Noodles" to listOf("Dry Rice Noodles", "Vegetable Oil", "Soy Sauce Bottle"),
                "Steamed Couscous" to listOf("Raw Couscous", "Olive Oil", "Sea Salt"),
                "Creamy Oatmeal Bowl" to listOf("Dry Oats", "Fresh Milk", "Sea Salt"),
                "Toasted Garlic Bread" to listOf("Fresh Bread Loaf", "Unsalted Butter", "Fresh Garlic", "Fresh Parsley"),
                "Steamed Jasmine Rice Bowl" to listOf("Uncooked Jasmine Rice", "Sea Salt"),
                "Steamed Basmati Rice Bowl" to listOf("Uncooked Basmati Rice", "Sea Salt"),
                "Cooked Buckwheat Groats" to listOf("Raw Buckwheat", "Sea Salt"),
                "Pearl Barley Porridge" to listOf("Raw Barley", "Fresh Milk", "Sea Salt"),
                "Seasoned Bulgur Wheat" to listOf("Raw Bulgur Wheat", "Olive Oil", "Sea Salt"),
                "Creamy Polenta Bowl" to listOf("Dry Polenta", "Fresh Milk", "Unsalted Butter"),
                "Cooked Farro Grain" to listOf("Raw Farro", "Olive Oil", "Sea Salt"),
                "Toasted Sourdough Bread" to listOf("Fresh Bread Loaf", "Unsalted Butter"),
                "Fresh Baked Baguette" to listOf("Fresh Baguette"),
                "Soft Flour Tortilla" to listOf("Fresh Tortillas"),

                // Vegetables - using FRESH ingredients
                "Steamed Broccoli Florets" to listOf("Fresh Broccoli", "Olive Oil", "Sea Salt", "Fresh Garlic"),
                "Roasted Baby Carrots" to listOf("Fresh Carrots", "Olive Oil", "Sea Salt"),
                "Sautéed Spinach" to listOf("Fresh Spinach Leaves", "Olive Oil", "Fresh Garlic", "Sea Salt"),
                "Massaged Kale Salad" to listOf("Fresh Kale Leaves", "Olive Oil", "Fresh Garlic", "Fresh Lemons"),
                "Fresh Lettuce Mix" to listOf("Fresh Lettuce", "Olive Oil"),
                "Sliced Fresh Tomatoes" to listOf("Fresh Tomatoes", "Olive Oil", "Sea Salt", "Fresh Basil"),
                "Fresh Cucumber Slices" to listOf("Fresh Cucumber", "Sea Salt"),
                "Roasted Bell Peppers" to listOf("Fresh Bell Peppers", "Olive Oil", "Sea Salt", "Fresh Garlic"),
                "Sautéed Mixed Mushrooms" to listOf("Fresh Mushrooms", "Olive Oil", "Fresh Garlic", "Sea Salt", "Fresh Thyme"),
                "Shredded Cabbage Slaw" to listOf("Fresh Cabbage", "Olive Oil", "Sea Salt"),
                "Caramelized Onions" to listOf("Fresh Onions", "Olive Oil", "Sea Salt"),
                "Roasted Garlic Cloves" to listOf("Fresh Garlic", "Olive Oil"),
                "Grilled Asparagus Spears" to listOf("Fresh Asparagus", "Olive Oil", "Sea Salt"),
                "Sautéed Zucchini Rounds" to listOf("Fresh Zucchini", "Olive Oil", "Sea Salt"),
                "Grilled Eggplant Slices" to listOf("Fresh Eggplant", "Olive Oil", "Sea Salt", "Fresh Garlic"),
                "Roasted Cauliflower" to listOf("Fresh Cauliflower", "Olive Oil", "Sea Salt"),
                "Grilled Corn on Cob" to listOf("Fresh Corn", "Unsalted Butter", "Sea Salt"),
                "Blanched Green Beans" to listOf("Fresh Green Beans", "Olive Oil", "Fresh Garlic"),
                "Sweet Garden Peas" to listOf("Fresh Peas", "Unsalted Butter", "Sea Salt"),
                "Roasted Brussels Sprouts" to listOf("Fresh Brussels Sprouts", "Olive Oil", "Sea Salt"),
                "Pickled Red Cabbage" to listOf("Fresh Red Cabbage", "Olive Oil"),
                "Fresh Arugula Salad" to listOf("Fresh Arugula", "Olive Oil", "Fresh Lemons"),
                "Roasted Beetroot Wedges" to listOf("Fresh Beetroot", "Olive Oil", "Sea Salt"),
                "Roasted Pumpkin Cubes" to listOf("Fresh Pumpkin", "Olive Oil", "Sea Salt"),

                // Sauces - using bottled sauces
                "Teriyaki Glaze Sauce" to listOf("Teriyaki Sauce Bottle"),
                "Light Soy Sauce" to listOf("Soy Sauce Bottle"),
                "Spicy Chili Sauce" to listOf("Chili Sauce Bottle"),
                "Creamy Garlic Sauce" to listOf("Garlic Sauce Jar", "Fresh Garlic"),
                "Smoky BBQ Sauce" to listOf("BBQ Sauce Bottle"),
                "Japanese Mayo Sauce" to listOf("Mayonnaise Jar"),
                "Tomato Ketchup" to listOf("Ketchup Bottle"),
                "Dijon Mustard Sauce" to listOf("Mustard Jar"),
                "Creamy Ranch Dressing" to listOf("Ranch Dressing", "Fresh Garlic", "Fresh Parsley"),
                "Caesar Salad Dressing" to listOf("Caesar Dressing", "Fresh Garlic"),
                "Basil Pesto Sauce" to listOf("Pesto Jar", "Fresh Basil", "Fresh Garlic"),
                "Hot Sriracha Sauce" to listOf("Sriracha Bottle"),
                "Sweet Honey Mustard" to listOf("Honey Mustard"),
                "Buffalo Wing Sauce" to listOf("Buffalo Sauce", "Unsalted Butter"),
                "Tartar Dipping Sauce" to listOf("Tartar Sauce", "Fresh Garlic"),

                // Dairy - using raw dairy products
                "Shredded Cheddar Cheese" to listOf("Cheddar Cheese Block"),
                "Fresh Mozzarella Cheese" to listOf("Mozzarella Block"),
                "Greek Yogurt Cup" to listOf("Greek Yogurt Container"),
                "Melted Butter" to listOf("Unsalted Butter"),
                "Fresh Whole Milk" to listOf("Fresh Milk"),
                "Grated Parmesan Cheese" to listOf("Parmesan Block"),
                "Crumbled Feta Cheese" to listOf("Feta Cheese"),
                "Blue Cheese Crumbles" to listOf("Blue Cheese"),
                "Sour Cream Dollop" to listOf("Sour Cream"),
                "Cream Cheese Spread" to listOf("Cream Cheese"),
                "Ricotta Cheese" to listOf("Ricotta Cheese"),
                "Vanilla Ice Cream Scoop" to listOf("Vanilla Ice Cream"),
                "Cottage Cheese Bowl" to listOf("Cottage Cheese"),

                // Fruits - using fresh fruits
                "Sliced Fresh Apple" to listOf("Fresh Apples"),
                "Peeled Banana" to listOf("Fresh Bananas"),
                "Orange Segments" to listOf("Fresh Oranges"),
                "Fresh Pineapple Chunks" to listOf("Fresh Pineapple"),
                "Diced Fresh Mango" to listOf("Fresh Mango"),
                "Fresh Strawberries" to listOf("Fresh Strawberries"),
                "Fresh Blueberries" to listOf("Fresh Blueberries"),
                "Green Grape Bunch" to listOf("Fresh Grapes"),
                "Watermelon Wedges" to listOf("Fresh Watermelon"),
                "Sliced Kiwi Fruit" to listOf("Fresh Kiwi"),
                "Fresh Peach Slices" to listOf("Fresh Peaches"),
                "Sliced Pear" to listOf("Fresh Pears"),
                "Fresh Plums" to listOf("Fresh Plums"),
                "Pitted Cherries" to listOf("Fresh Cherries"),
                "Fresh Raspberries" to listOf("Fresh Raspberries"),
                "Fresh Blackberries" to listOf("Fresh Blackberries"),
                "Dragon Fruit Cubes" to listOf("Fresh Dragon Fruit"),
                "Fresh Papaya Slices" to listOf("Fresh Papaya"),
                "Fresh Lemon Wedges" to listOf("Fresh Lemons"),
                "Lime Wedges" to listOf("Fresh Limes"),
                "Fresh Coconut Chunks" to listOf("Fresh Coconut"),
                "Pomegranate Seeds" to listOf("Fresh Pomegranate"),
                "Grapefruit Segments" to listOf("Fresh Grapefruit"),
                "Sliced Avocado" to listOf("Fresh Avocado")
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
