-- =====================================================
-- MySQL Database Dump for: chicken_kitchen
-- Version: 8.0.43 / Server: 8.4.6
-- Host: 127.0.0.1
-- Generated on: 2025-10-24 13:17:13
-- Purpose: Clean, readable, and safe-to-run schema
-- =====================================================

-- Tạm tắt kiểm tra khóa ngoại để tránh lỗi thứ tự bảng
SET FOREIGN_KEY_CHECKS = 0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET TIME_ZONE = '+00:00';

-- Sử dụng database (nếu chưa tồn tại thì tạo)
CREATE DATABASE IF NOT EXISTS `chicken_kitchen` CHARACTER SET utf8mb4 COLLATE utc8mb4_0900_ai_ci;
USE `chicken_kitchen`;

-- =====================================================
-- 1. BẢNG CƠ BẢN (Categories, Nutrients, Payment Methods, etc.)
-- =====================================================

-- Danh mục món ăn
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT,
                              `name` VARCHAR(255) NOT NULL,
                              `description` VARCHAR(255) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `UK_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='Danh mục món ăn (ví dụ: Gà rán, Đồ uống, Món phụ)';

-- Chất dinh dưỡng
DROP TABLE IF EXISTS `nutrients`;
CREATE TABLE `nutrients` (
                             `id` BIGINT NOT NULL AUTO_INCREMENT,
                             `name` VARCHAR(255) NOT NULL,
                             `base_unit` ENUM('G', 'ML') NOT NULL COMMENT 'Đơn vị cơ bản: Gram hoặc Mililit',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `UK_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='Các chất dinh dưỡng (Calorie, Protein, Fat, etc.)';

-- Phương thức thanh toán
DROP TABLE IF EXISTS `payment_methods`;
CREATE TABLE `payment_methods` (
                                   `id` BIGINT NOT NULL AUTO_INCREMENT,
                                   `name` VARCHAR(255) NOT NULL,
                                   `description` VARCHAR(255) DEFAULT NULL,
                                   `is_active` BIT(1) NOT NULL DEFAULT 1,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `UK_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='Ví dụ: Tiền mặt, Momo, Thẻ, Ví CK';

-- Nguyên liệu
DROP TABLE IF EXISTS `ingredients`;
CREATE TABLE `ingredients` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT,
                               `name` VARCHAR(255) NOT NULL,
                               `batch_number` VARCHAR(255) NOT NULL COMMENT 'Mã lô nguyên liệu',
                               `base_unit` ENUM('G', 'ML') DEFAULT NULL,
                               `image_url` VARCHAR(255) DEFAULT NULL,
                               `is_active` BIT(1) NOT NULL DEFAULT 1,
                               `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `UK_name` (`name`),
                               UNIQUE KEY `UK_batch_number` (`batch_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='Nguyên liệu đầu vào (gà, dầu, tương ớt, etc.)';

-- Cửa hàng
DROP TABLE IF EXISTS `stores`;
CREATE TABLE `stores` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `name` VARCHAR(255) NOT NULL,
                          `address` VARCHAR(255) NOT NULL,
                          `phone` VARCHAR(100) NOT NULL,
                          `is_active` BIT(1) NOT NULL DEFAULT 1,
                          `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `UK_name` (`name`),
                          UNIQUE KEY `UK_address` (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='Chi nhánh cửa hàng Chicken Kitchen';

-- Người dùng
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                         `full_name` VARCHAR(100) NOT NULL,
                         `email` VARCHAR(100) NOT NULL,
                         `phone` VARCHAR(100) DEFAULT NULL,
                         `birthday` DATE DEFAULT NULL,
                         `image` VARCHAR(100) DEFAULT NULL,
                         `provider` VARCHAR(100) DEFAULT NULL COMMENT 'Google, Facebook, etc.',
                         `uid` VARCHAR(128) DEFAULT NULL COMMENT 'ID từ nhà cung cấp',
                         `role` ENUM('ADMIN','MANAGER','EMPLOYEE','STORE','USER') NOT NULL DEFAULT 'USER',
                         `is_active` BIT(1) NOT NULL DEFAULT 1,
                         `is_verified` BIT(1) NOT NULL DEFAULT 0,
                         `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                         `updated_at` DATETIME(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK_email` (`email`),
                         UNIQUE KEY `UK_uid` (`uid`),
                         INDEX `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='Người dùng hệ thống (khách, nhân viên, quản lý)';

-- =====================================================
-- 2. SẢN PHẨM & MENU
-- =====================================================

-- Món ăn trong menu
DROP TABLE IF EXISTS `menu_items`;
CREATE TABLE `menu_items` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT,
                              `name` VARCHAR(255) NOT NULL,
                              `description` VARCHAR(255) DEFAULT NULL,
                              `price` INT NOT NULL,
                              `cal` INT NOT NULL COMMENT 'Calories',
                              `image_url` VARCHAR(500) DEFAULT NULL,
                              `is_active` BIT(1) NOT NULL DEFAULT 1,
                              `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                              `category_id` BIGINT NOT NULL,
                              PRIMARY KEY (`id`),
                              INDEX `idx_category` (`category_id`),
                              CONSTRAINT `FK_menu_items_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='Món ăn có thể đặt (Gà rán cay, Pepsi, etc.)';

-- Dinh dưỡng của món
DROP TABLE IF EXISTS `menu_item_nutrients`;
CREATE TABLE `menu_item_nutrients` (
                                       `id` BIGINT NOT NULL AUTO_INCREMENT,
                                       `menu_item_id` BIGINT NOT NULL,
                                       `nutrient_id` BIGINT NOT NULL,
                                       `quantity` DECIMAL(10,2) NOT NULL,
                                       PRIMARY KEY (`id`),
                                       INDEX `idx_menu_item` (`menu_item_id`),
                                       INDEX `idx_nutrient` (`nutrient_id`),
                                       CONSTRAINT `FK_nutrient` FOREIGN KEY (`nutrient_id`) REFERENCES `nutrients` (`id`),
                                       CONSTRAINT `FK_menu_item` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='Lượng dinh dưỡng trong mỗi món';

-- Công thức (nguyên liệu cần cho món)
DROP TABLE IF EXISTS `recipes`;
CREATE TABLE `recipes` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                           `menu_item_id` BIGINT NOT NULL,
                           `ingredient_id` BIGINT NOT NULL,
                           PRIMARY KEY (`id`),
                           INDEX `idx_menu_item` (`menu_item_id`),
                           INDEX `idx_ingredient` (`ingredient_id`),
                           CONSTRAINT `FK_recipe_menu_item` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`) ON DELETE CASCADE,
                           CONSTRAINT `FK_recipe_ingredient` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='Công thức: món cần bao nhiêu nguyên liệu';

-- =====================================================
-- 3. MENU HÀNG NGÀY & BƯỚC NẤU
-- =====================================================

-- Menu theo ngày
DROP TABLE IF EXISTS `daily_menus`;
CREATE TABLE `daily_menus` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT,
                               `menu_date` DATETIME(6) NOT NULL COMMENT 'Ngày áp dụng menu',
                               `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Liên kết: Menu ngày → Món
DROP TABLE IF EXISTS `daily_menu_items`;
CREATE TABLE `daily_menu_items` (
                                    `id` BIGINT NOT NULL AUTO_INCREMENT,
                                    `daily_menu_id` BIGINT NOT NULL,
                                    `menu_item_id` BIGINT NOT NULL,
                                    PRIMARY KEY (`id`),
                                    INDEX `idx_daily_menu` (`daily_menu_id`),
                                    INDEX `idx_menu_item` (`menu_item_id`),
                                    CONSTRAINT `FK_daily_menu` FOREIGN KEY (`daily_menu_id`) REFERENCES `daily_menus` (`id`) ON DELETE CASCADE,
                                    CONSTRAINT `FK_daily_menu_item` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Cửa hàng áp dụng menu ngày
DROP TABLE IF EXISTS `daily_menu_stores`;
CREATE TABLE `daily_menu_stores` (
                                     `daily_menu_id` BIGINT NOT NULL,
                                     `store_id` BIGINT NOT NULL,
                                     PRIMARY KEY (`daily_menu_id`, `store_id`),
                                     INDEX `idx_store` (`store_id`),
                                     CONSTRAINT `FK_dms_menu` FOREIGN KEY (`daily_menu_id`) REFERENCES `daily_menus` (`id`) ON DELETE CASCADE,
                                     CONSTRAINT `FK_dms_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Các bước nấu ăn
DROP TABLE IF EXISTS `steps`;
CREATE TABLE `steps` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                         `name` VARCHAR(255) NOT NULL,
                         `description` VARCHAR(255) DEFAULT NULL,
                         `step_number` INT NOT NULL,
                         `is_active` BIT(1) NOT NULL DEFAULT 1,
                         `category_id` BIGINT NOT NULL,
                         PRIMARY KEY (`id`),
                         INDEX `idx_category` (`category_id`),
                         CONSTRAINT `FK_step_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='Bước chế biến (Ướp, Chiên, Đóng gói)';

-- =====================================================
-- 4. ĐƠN HÀNG & THANH TOÁN
-- =====================================================

-- Đơn hàng
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `user_id` BIGINT NOT NULL,
                          `store_id` BIGINT NOT NULL,
                          `pickup_time` DATETIME(6) NOT NULL,
                          `total_price` INT NOT NULL,
                          `status` ENUM('NEW','CONFIRMED','PROCESSING','READY','COMPLETED','CANCELLED','FAILED') NOT NULL DEFAULT 'NEW',
                          `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                          PRIMARY KEY (`id`),
                          INDEX `idx_user` (`user_id`),
                          INDEX `idx_store` (`store_id`),
                          INDEX `idx_status` (`status`),
                          CONSTRAINT `FK_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                          CONSTRAINT `FK_order_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Món trong đơn
DROP TABLE IF EXISTS `dishes`;
CREATE TABLE `dishes` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `order_id` BIGINT NOT NULL,
                          `price` INT NOT NULL,
                          `cal` INT NOT NULL,
                          `note` VARCHAR(255) DEFAULT NULL,
                          `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                          `updated_at` DATETIME(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
                          PRIMARY KEY (`id`),
                          INDEX `idx_order` (`order_id`),
                          CONSTRAINT `FK_dish_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bước thực hiện cho món
DROP TABLE IF EXISTS `order_steps`;
CREATE TABLE `order_steps` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT,
                               `dish_id` BIGINT NOT NULL,
                               `step_id` BIGINT NOT NULL,
                               PRIMARY KEY (`id`),
                               INDEX `idx_dish` (`dish_id`),
                               INDEX `idx_step` (`step_id`),
                               CONSTRAINT `FK_os_dish` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`) ON DELETE CASCADE,
                               CONSTRAINT `FK_os_step` FOREIGN KEY (`step_id`) REFERENCES `steps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Chi tiết món trong bước (số lượng)
DROP TABLE IF EXISTS `order_step_items`;
CREATE TABLE `order_step_items` (
                                    `id` BIGINT NOT NULL AUTO_INCREMENT,
                                    `order_step_id` BIGINT NOT NULL,
                                    `daily_menu_item_id` BIGINT NOT NULL,
                                    `quantity` INT NOT NULL DEFAULT 1,
                                    PRIMARY KEY (`id`),
                                    INDEX `idx_order_step` (`order_step_id`),
                                    INDEX `idx_menu_item` (`daily_menu_item_id`),
                                    CONSTRAINT `FK_osi_step` FOREIGN KEY (`order_step_id`) REFERENCES `order_steps` (`id`) ON DELETE CASCADE,
                                    CONSTRAINT `FK_osi_menu_item` FOREIGN KEY (`daily_menu_item_id`) REFERENCES `daily_menu_items` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Thanh toán
DROP TABLE IF EXISTS `payments`;
CREATE TABLE `payments` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                            `order_id` BIGINT NOT NULL,
                            `user_id` BIGINT NOT NULL,
                            `amount` INT NOT NULL,
                            `discount_amount` INT NOT NULL DEFAULT 0,
                            `final_amount` INT NOT NULL,
                            `status` ENUM('PENDING','FINISHED','CANCELLED') NOT NULL DEFAULT 'PENDING',
                            `note` TEXT DEFAULT NULL,
                            `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `UK_order_id` (`order_id`),
                            INDEX `idx_user` (`user_id`),
                            INDEX `idx_status` (`status`),
                            CONSTRAINT `FK_payment_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT,
                            CONSTRAINT `FK_payment_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Giao dịch ví
DROP TABLE IF EXISTS `wallets`;
CREATE TABLE `wallets` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                           `user_id` BIGINT NOT NULL,
                           `balance` INT NOT NULL DEFAULT 0,
                           `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                           `updated_at` DATETIME(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK_user_id` (`user_id`),
                           CONSTRAINT `FK_wallet_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Chi tiết giao dịch
DROP TABLE IF EXISTS `transactions`;
CREATE TABLE `transactions` (
                                `id` BIGINT NOT NULL AUTO_INCREMENT,
                                `payment_id` BIGINT NOT NULL,
                                `payment_method_id` BIGINT NOT NULL,
                                `wallet_id` BIGINT NOT NULL,
                                `amount` INT NOT NULL,
                                `transaction_type` ENUM('CREDIT','DEBIT') NOT NULL,
                                `note` TEXT DEFAULT NULL,
                                `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                                PRIMARY KEY (`id`),
                                INDEX `idx_payment` (`payment_id`),
                                INDEX `idx_method` (`payment_method_id`),
                                INDEX `idx_wallet` (`wallet_id`),
                                CONSTRAINT `FK_tx_payment` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`),
                                CONSTRAINT `FK_tx_method` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_methods` (`id`),
                                CONSTRAINT `FK_tx_wallet` FOREIGN KEY (`wallet_id`) REFERENCES `wallets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- 5. NHÂN VIÊN, KHUYẾN MÃI, FEEDBACK
-- =====================================================

-- Chi tiết nhân viên
DROP TABLE IF EXISTS `employee_details`;
CREATE TABLE `employee_details` (
                                    `id` BIGINT NOT NULL AUTO_INCREMENT,
                                    `user_id` BIGINT NOT NULL,
                                    `store_id` BIGINT NOT NULL,
                                    `position` VARCHAR(100) DEFAULT NULL,
                                    `note` TEXT DEFAULT NULL,
                                    `is_active` BIT(1) NOT NULL DEFAULT 1,
                                    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                                    `updated_at` DATETIME(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `UK_user_id` (`user_id`),
                                    INDEX `idx_store` (`store_id`),
                                    CONSTRAINT `FK_emp_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                                    CONSTRAINT `FK_emp_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Khuyến mãi
DROP TABLE IF EXISTS `promotions`;
CREATE TABLE `promotions` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT,
                              `discount_type` ENUM('AMOUNT','PERCENT') NOT NULL,
                              `discount_value` INT NOT NULL,
                              `quantity` INT NOT NULL COMMENT 'Số lượt dùng',
                              `start_date` DATETIME(6) NOT NULL,
                              `end_date` DATETIME(6) NOT NULL,
                              `is_active` BIT(1) NOT NULL DEFAULT 1,
                              PRIMARY KEY (`id`),
                              INDEX `idx_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Sử dụng khuyến mãi
DROP TABLE IF EXISTS `order_promotions`;
CREATE TABLE `order_promotions` (
                                    `id` BIGINT NOT NULL AUTO_INCREMENT,
                                    `order_id` BIGINT NOT NULL,
                                    `promotion_id` BIGINT NOT NULL,
                                    `user_id` BIGINT NOT NULL,
                                    `used_date` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                                    PRIMARY KEY (`id`),
                                    INDEX `idx_order` (`order_id`),
                                    INDEX `idx_promo` (`promotion_id`),
                                    INDEX `idx_user` (`user_id`),
                                    CONSTRAINT `FK_op_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
                                    CONSTRAINT `FK_op_promo` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`id`),
                                    CONSTRAINT `FK_op_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Phản hồi
DROP TABLE IF EXISTS `feedbacks`;
CREATE TABLE `feedbacks` (
                             `id` BIGINT NOT NULL AUTO_INCREMENT,
                             `order_id` BIGINT NOT NULL,
                             `store_id` BIGINT NOT NULL,
                             `rating` INT NOT NULL CHECK (`rating` BETWEEN 1 AND 5),
                             `message` TEXT DEFAULT NULL,
                             `reply` TEXT DEFAULT NULL,
                             `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                             `updated_at` DATETIME(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `UK_order_id` (`order_id`),
                             INDEX `idx_store` (`store_id`),
                             CONSTRAINT `FK_fb_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
                             CONSTRAINT `FK_fb_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- 6. TỒN KHO & PHIÊN ĐĂNG NHẬP
-- =====================================================

-- Tồn kho nguyên liệu tại cửa hàng
DROP TABLE IF EXISTS `store_ingredient_batches`;
CREATE TABLE `store_ingredient_batches` (
                                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                                            `store_id` BIGINT NOT NULL,
                                            `ingredient_id` BIGINT NOT NULL,
                                            `quantity` BIGINT NOT NULL DEFAULT 0,
                                            PRIMARY KEY (`id`),
                                            INDEX `idx_store` (`store_id`),
                                            INDEX `idx_ingredient` (`ingredient_id`),
                                            CONSTRAINT `FK_sib_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`) ON DELETE CASCADE,
                                            CONSTRAINT `FK_sib_ingredient` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
COMMENT='Số lượng nguyên liệu còn lại tại từng cửa hàng';

-- Token xác thực email
DROP TABLE IF EXISTS `mail_tokens`;
CREATE TABLE `mail_tokens` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT,
                               `user_id` BIGINT NOT NULL,
                               `token` VARCHAR(255) DEFAULT NULL,
                               `expired_at` DATETIME(6) DEFAULT NULL,
                               `is_cancelled` BIT(1) NOT NULL DEFAULT 0,
                               PRIMARY KEY (`id`),
                               INDEX `idx_user` (`user_id`),
                               CONSTRAINT `FK_mail_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Phiên đăng nhập
DROP TABLE IF EXISTS `user_sessions`;
CREATE TABLE `user_sessions` (
                                 `id` BIGINT NOT NULL AUTO_INCREMENT,
                                 `user_id` BIGINT NOT NULL,
                                 `session_token` VARCHAR(255) DEFAULT NULL,
                                 `refresh_token` VARCHAR(255) DEFAULT NULL,
                                 `device_info` JSON DEFAULT NULL,
                                 `last_activity` DATETIME(6) DEFAULT NULL,
                                 `expires_at` DATETIME(6) DEFAULT NULL,
                                 `is_cancelled` BIT(1) NOT NULL DEFAULT 0,
                                 PRIMARY KEY (`id`),
                                 INDEX `idx_user` (`user_id`),
                                 CONSTRAINT `FK_session_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- Kết thúc: Bật lại kiểm tra khóa ngoại
-- =====================================================
SET FOREIGN_KEY_CHECKS = 1;

-- Hoàn tất
SELECT 'Database chicken_kitchen đã được tạo thành công!' AS `Status`;