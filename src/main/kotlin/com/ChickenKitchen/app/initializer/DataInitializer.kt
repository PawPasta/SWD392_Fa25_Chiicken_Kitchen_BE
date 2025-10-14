package com.ChickenKitchen.app.initializer


import com.ChickenKitchen.app.enum.DiscountType
import com.ChickenKitchen.app.enum.Role
import com.ChickenKitchen.app.enum.UnitType
import com.ChickenKitchen.app.model.entity.category.Category
import com.ChickenKitchen.app.enum.MenuCategory
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
import java.math.RoundingMode
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import org.springframework.transaction.annotation.Transactional

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

        // CATEGORIES (ensure all needed categories exist)
        run {
            val categories = listOf(
                "Carbohydrates" to "Base carb selection",
                "Proteins" to "Protein selection",
                "Vegetables" to "Vegetable selection",
                "Sauces" to "Sauce selection",
                "Dairy" to "Dairy selection",
                "Fruits" to "Fruit selection",
            )
            var created = 0
            categories.forEach { (name, desc) ->
                if (!categoryRepository.existsByName(name)) {
                    categoryRepository.save(Category(name = name, description = desc))
                    created++
                }
            }
            if (created > 0) println("✓ Categories ensured/seeded: $created new") else println("⏭ Categories already present")
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
            val items: List<Triple<String, Any, String>> = listOf(
                // CARB
                Triple("White Rice", MenuCategory.CARB, "https://example.com/images/white-rice.jpg"),
                Triple("Brown Rice", MenuCategory.CARB, "https://example.com/images/brown-rice.jpg"),
                Triple("Quinoa", MenuCategory.CARB, "https://example.com/images/quinoa.jpg"),
                Triple("Whole Wheat Pasta", MenuCategory.CARB, "https://example.com/images/whole-wheat-pasta.jpg"),
                Triple("Sweet Potato", MenuCategory.CARB, "https://example.com/images/sweet-potato.jpg"),
                Triple("Mashed Potato", MenuCategory.CARB, "https://example.com/images/mashed-potato.jpg"),
                Triple("Udon Noodles", MenuCategory.CARB, "https://example.com/images/udon-noodles.jpg"),
                Triple("Couscous", MenuCategory.CARB, "https://example.com/images/couscous.jpg"),
                Triple("Oatmeal", MenuCategory.CARB, "https://example.com/images/oatmeal.jpg"),
                Triple("Garlic Bread", MenuCategory.CARB, "https://example.com/images/garlic-bread.jpg"),
                Triple("Jasmine Rice", MenuCategory.CARB, "https://example.com/images/jasmine-rice.jpg"),
                Triple("Basmati Rice", MenuCategory.CARB, "https://example.com/images/basmati-rice.jpg"),
                Triple("Soba Noodles", MenuCategory.CARB, "https://example.com/images/soba-noodles.jpg"),
                Triple("Rice Noodles", MenuCategory.CARB, "https://example.com/images/rice-noodles.jpg"),
                Triple("Buckwheat", MenuCategory.CARB, "https://example.com/images/buckwheat.jpg"),
                Triple("Barley", MenuCategory.CARB, "https://example.com/images/barley.jpg"),
                Triple("Bulgur", MenuCategory.CARB, "https://example.com/images/bulgur.jpg"),
                Triple("Polenta", MenuCategory.CARB, "https://example.com/images/polenta.jpg"),
                Triple("Farro", MenuCategory.CARB, "https://example.com/images/farro.jpg"),
                Triple("Sourdough Bread", MenuCategory.CARB, "https://example.com/images/sourdough-bread.jpg"),
                Triple("Baguette", MenuCategory.CARB, "https://example.com/images/baguette.jpg"),
                Triple("Tortilla", MenuCategory.CARB, "https://example.com/images/tortilla.jpg"),

                // PROTEIN
                Triple("Grilled Chicken", "Proteins", "https://example.com/images/grilled-chicken.jpg"),
                Triple("Fried Chicken", "Proteins", "https://example.com/images/fried-chicken.jpg"),
                Triple("Beef Steak", MenuCategory.PROTEIN, "https://example.com/images/beef-steak.jpg"),
                Triple("Pork Chop", MenuCategory.PROTEIN, "https://example.com/images/pork-chop.jpg"),
                Triple("Firm Tofu", MenuCategory.PROTEIN, "https://example.com/images/firm-tofu.jpg"),
                Triple("Salmon Fillet", MenuCategory.PROTEIN, "https://example.com/images/salmon-fillet.jpg"),
                Triple("Tuna", MenuCategory.PROTEIN, "https://example.com/images/tuna.jpg"),
                Triple("Shrimp", MenuCategory.PROTEIN, "https://example.com/images/shrimp.jpg"),
                Triple("Boiled Egg", MenuCategory.PROTEIN, "https://example.com/images/boiled-egg.jpg"),
                Triple("Turkey Breast", MenuCategory.PROTEIN, "https://example.com/images/turkey-breast.jpg"),
                Triple("Roast Chicken", MenuCategory.PROTEIN, "https://example.com/images/roast-chicken.jpg"),
                Triple("Chicken Thigh", MenuCategory.PROTEIN, "https://example.com/images/chicken-thigh.jpg"),
                Triple("Chicken Wings", MenuCategory.PROTEIN, "https://example.com/images/chicken-wings.jpg"),
                Triple("Ground Beef", MenuCategory.PROTEIN, "https://example.com/images/ground-beef.jpg"),
                Triple("Ribeye Steak", MenuCategory.PROTEIN, "https://example.com/images/ribeye-steak.jpg"),
                Triple("Sirloin", MenuCategory.PROTEIN, "https://example.com/images/sirloin.jpg"),
                Triple("Pork Belly", MenuCategory.PROTEIN, "https://example.com/images/pork-belly.jpg"),
                Triple("Ham", MenuCategory.PROTEIN, "https://example.com/images/ham.jpg"),
                Triple("Bacon", MenuCategory.PROTEIN, "https://example.com/images/bacon.jpg"),
                Triple("Lamb Chops", MenuCategory.PROTEIN, "https://example.com/images/lamb-chops.jpg"),
                Triple("Duck Breast", MenuCategory.PROTEIN, "https://example.com/images/duck-breast.jpg"),
                Triple("Tempeh", MenuCategory.PROTEIN, "https://example.com/images/tempeh.jpg"),
                Triple("Seitan", MenuCategory.PROTEIN, "https://example.com/images/seitan.jpg"),
                Triple("Black Beans", MenuCategory.PROTEIN, "https://example.com/images/black-beans.jpg"),
                Triple("Chickpeas", MenuCategory.PROTEIN, "https://example.com/images/chickpeas.jpg"),
                Triple("Lentils", MenuCategory.PROTEIN, "https://example.com/images/lentils.jpg"),
                Triple("Edamame", MenuCategory.PROTEIN, "https://example.com/images/edamame.jpg"),
                Triple("Smoked Salmon", MenuCategory.PROTEIN, "https://example.com/images/smoked-salmon.jpg"),
                Triple("Sardines", MenuCategory.PROTEIN, "https://example.com/images/sardines.jpg"),

                // VEGETABLE
                Triple("Broccoli", MenuCategory.VEGETABLE, "https://example.com/images/broccoli.jpg"),
                Triple("Carrot", MenuCategory.VEGETABLE, "https://example.com/images/carrot.jpg"),
                Triple("Spinach", MenuCategory.VEGETABLE, "https://example.com/images/spinach.jpg"),
                Triple("Kale", MenuCategory.VEGETABLE, "https://example.com/images/kale.jpg"),
                Triple("Lettuce", MenuCategory.VEGETABLE, "https://example.com/images/lettuce.jpg"),
                Triple("Tomato", MenuCategory.VEGETABLE, "https://example.com/images/tomato.jpg"),
                Triple("Cucumber", MenuCategory.VEGETABLE, "https://example.com/images/cucumber.jpg"),
                Triple("Bell Pepper", MenuCategory.VEGETABLE, "https://example.com/images/bell-pepper.jpg"),
                Triple("Corn", MenuCategory.VEGETABLE, "https://example.com/images/corn.jpg"),
                Triple("Green Beans", MenuCategory.VEGETABLE, "https://example.com/images/green-beans.jpg"),
                Triple("Asparagus", MenuCategory.VEGETABLE, "https://example.com/images/asparagus.jpg"),
                Triple("Zucchini", MenuCategory.VEGETABLE, "https://example.com/images/zucchini.jpg"),
                Triple("Eggplant", MenuCategory.VEGETABLE, "https://example.com/images/eggplant.jpg"),
                Triple("Cauliflower", MenuCategory.VEGETABLE, "https://example.com/images/cauliflower.jpg"),
                Triple("Cabbage", MenuCategory.VEGETABLE, "https://example.com/images/cabbage.jpg"),
                Triple("Mushrooms", MenuCategory.VEGETABLE, "https://example.com/images/mushrooms.jpg"),
                Triple("Peas", MenuCategory.VEGETABLE, "https://example.com/images/peas.jpg"),
                Triple("Brussels Sprouts", MenuCategory.VEGETABLE, "https://example.com/images/brussels-sprouts.jpg"),
                Triple("Onion", MenuCategory.VEGETABLE, "https://example.com/images/onion.jpg"),
                Triple("Garlic", MenuCategory.VEGETABLE, "https://example.com/images/garlic.jpg"),
                Triple("Red Cabbage", MenuCategory.VEGETABLE, "https://example.com/images/red-cabbage.jpg"),
                Triple("Arugula", MenuCategory.VEGETABLE, "https://example.com/images/arugula.jpg"),
                Triple("Beetroot", MenuCategory.VEGETABLE, "https://example.com/images/beetroot.jpg"),
                Triple("Pumpkin", MenuCategory.VEGETABLE, "https://example.com/images/pumpkin.jpg"),

                // SAUCE
                Triple("Teriyaki Sauce", MenuCategory.SAUCE, "https://example.com/images/teriyaki-sauce.jpg"),
                Triple("Soy Sauce", MenuCategory.SAUCE, "https://example.com/images/soy-sauce.jpg"),
                Triple("Chili Sauce", MenuCategory.SAUCE, "https://example.com/images/chili-sauce.jpg"),
                Triple("Garlic Sauce", MenuCategory.SAUCE, "https://example.com/images/garlic-sauce.jpg"),
                Triple("BBQ Sauce", MenuCategory.SAUCE, "https://example.com/images/bbq-sauce.jpg"),
                Triple("Mayonnaise", MenuCategory.SAUCE, "https://example.com/images/mayonnaise.jpg"),
                Triple("Ketchup", MenuCategory.SAUCE, "https://example.com/images/ketchup.jpg"),
                Triple("Mustard", MenuCategory.SAUCE, "https://example.com/images/mustard.jpg"),
                Triple("Ranch", MenuCategory.SAUCE, "https://example.com/images/ranch.jpg"),
                Triple("Caesar", MenuCategory.SAUCE, "https://example.com/images/caesar.jpg"),
                Triple("Pesto", MenuCategory.SAUCE, "https://example.com/images/pesto.jpg"),
                Triple("Sriracha", MenuCategory.SAUCE, "https://example.com/images/sriracha.jpg"),
                Triple("Honey Mustard", MenuCategory.SAUCE, "https://example.com/images/honey-mustard.jpg"),
                Triple("Buffalo Sauce", MenuCategory.SAUCE, "https://example.com/images/buffalo-sauce.jpg"),
                Triple("Tartar Sauce", MenuCategory.SAUCE, "https://example.com/images/tartar-sauce.jpg"),

                // DAIRY
                Triple("Cheddar Cheese", MenuCategory.DAIRY, "https://example.com/images/cheddar-cheese.jpg"),
                Triple("Mozzarella", MenuCategory.DAIRY, "https://example.com/images/mozzarella.jpg"),
                Triple("Greek Yogurt", MenuCategory.DAIRY, "https://example.com/images/greek-yogurt.jpg"),
                Triple("Butter", MenuCategory.DAIRY, "https://example.com/images/butter.jpg"),
                Triple("Milk", MenuCategory.DAIRY, "https://example.com/images/milk.jpg"),
                Triple("Parmesan", MenuCategory.DAIRY, "https://example.com/images/parmesan.jpg"),
                Triple("Feta", MenuCategory.DAIRY, "https://example.com/images/feta.jpg"),
                Triple("Blue Cheese", MenuCategory.DAIRY, "https://example.com/images/blue-cheese.jpg"),
                Triple("Sour Cream", MenuCategory.DAIRY, "https://example.com/images/sour-cream.jpg"),
                Triple("Cream Cheese", MenuCategory.DAIRY, "https://example.com/images/cream-cheese.jpg"),
                Triple("Ricotta", MenuCategory.DAIRY, "https://example.com/images/ricotta.jpg"),
                Triple("Ice Cream", MenuCategory.DAIRY, "https://example.com/images/ice-cream.jpg"),
                Triple("Cottage Cheese", MenuCategory.DAIRY, "https://example.com/images/cottage-cheese.jpg"),

                // FRUIT
                Triple("Apple", MenuCategory.FRUIT, "https://example.com/images/apple.jpg"),
                Triple("Banana", MenuCategory.FRUIT, "https://example.com/images/banana.jpg"),
                Triple("Orange", MenuCategory.FRUIT, "https://example.com/images/orange.jpg"),
                Triple("Pineapple", MenuCategory.FRUIT, "https://example.com/images/pineapple.jpg"),
                Triple("Mango", MenuCategory.FRUIT, "https://example.com/images/mango.jpg"),
                Triple("Strawberry", MenuCategory.FRUIT, "https://example.com/images/strawberry.jpg"),
                Triple("Blueberry", MenuCategory.FRUIT, "https://example.com/images/blueberry.jpg"),
                Triple("Grape", MenuCategory.FRUIT, "https://example.com/images/grape.jpg"),
                Triple("Watermelon", MenuCategory.FRUIT, "https://example.com/images/watermelon.jpg"),
                Triple("Kiwi", MenuCategory.FRUIT, "https://example.com/images/kiwi.jpg"),
                Triple("Peach", MenuCategory.FRUIT, "https://example.com/images/peach.jpg"),
                Triple("Pear", MenuCategory.FRUIT, "https://example.com/images/pear.jpg"),
                Triple("Plum", MenuCategory.FRUIT, "https://example.com/images/plum.jpg"),
                Triple("Cherry", MenuCategory.FRUIT, "https://example.com/images/cherry.jpg"),
                Triple("Raspberry", MenuCategory.FRUIT, "https://example.com/images/raspberry.jpg"),
                Triple("Blackberry", MenuCategory.FRUIT, "https://example.com/images/blackberry.jpg"),
                Triple("Dragon Fruit", MenuCategory.FRUIT, "https://example.com/images/dragon-fruit.jpg"),
                Triple("Papaya", MenuCategory.FRUIT, "https://example.com/images/papaya.jpg"),
                Triple("Lemon", MenuCategory.FRUIT, "https://example.com/images/lemon.jpg"),
                Triple("Lime", MenuCategory.FRUIT, "https://example.com/images/lime.jpg"),
                Triple("Coconut", MenuCategory.FRUIT, "https://example.com/images/coconut.jpg"),
                Triple("Pomegranate", MenuCategory.FRUIT, "https://example.com/images/pomegranate.jpg"),
                Triple("Grapefruit", MenuCategory.FRUIT, "https://example.com/images/grapefruit.jpg"),
                Triple("Avocado", MenuCategory.FRUIT, "https://example.com/images/avocado.jpg")
            )

            val entities = items.map { (name, catAny, img) ->
                val categoryName = when (catAny) {
                    is String -> catAny
                    is com.ChickenKitchen.app.enum.MenuCategory -> when (catAny) {
                        com.ChickenKitchen.app.enum.MenuCategory.CARB -> "Carbohydrates"
                        com.ChickenKitchen.app.enum.MenuCategory.PROTEIN -> "Proteins"
                        com.ChickenKitchen.app.enum.MenuCategory.VEGETABLE -> "Vegetables"
                        com.ChickenKitchen.app.enum.MenuCategory.SAUCE -> "Sauces"
                        com.ChickenKitchen.app.enum.MenuCategory.DAIRY -> "Dairy"
                        com.ChickenKitchen.app.enum.MenuCategory.FRUIT -> "Fruits"
                    }
                    else -> throw IllegalArgumentException("Unsupported category type: ${'$'}catAny")
                }
                val cat = categoryRepository.findByName(categoryName)
                    ?: throw IllegalStateException("Category not found for seeding: $categoryName")
                MenuItem(name = name, category = cat, isActive = true, imageUrl = img)
            }
            menuItemRepository.saveAll(entities)
            println("✓ Menu items seeded: ${'$'}{entities.size}")
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

        // NUTRIENTS (expanded)
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
            println("✓ Nutrients seeded: ${'$'}{nutrients.size}")
        } else {
            println("⏭ Nutrients table not empty, skipping")
        }

        // MENU ITEM NUTRIENTS
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
                println("✓ Menu item nutrients seeded for ${'$'}{items.size} items, total links: ${'$'}{links.size}")
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
