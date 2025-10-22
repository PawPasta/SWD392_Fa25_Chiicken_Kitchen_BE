-- =============================================================
-- CHICKEN KITCHEN DATABASE SCHEMA
-- Version: 8.0+
-- Description: Clean & readable schema for Chicken Kitchen app
-- =============================================================

DROP DATABASE IF EXISTS chicken_kitchen;
CREATE DATABASE chicken_kitchen CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE chicken_kitchen;

-- =============================================================
-- TABLE GROUP 1: USERS & AUTHENTICATION
-- =============================================================

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       birthday DATE DEFAULT NULL,
                       created_at DATETIME(6) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       full_name VARCHAR(100) NOT NULL,
                       image VARCHAR(100) DEFAULT NULL,
                       is_active BIT(1) NOT NULL,
                       is_verified BIT(1) NOT NULL,
                       phone VARCHAR(100) DEFAULT NULL,
                       provider VARCHAR(100) DEFAULT NULL,
                       role ENUM('ADMIN','EMPLOYEE','MANAGER','STORE','USER') NOT NULL,
                       uid VARCHAR(128) DEFAULT NULL UNIQUE,
                       updated_at DATETIME(6) DEFAULT NULL,
                       INDEX idx_email (email)
);

CREATE TABLE user_sessions (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               device_info JSON DEFAULT NULL,
                               expires_at DATETIME(6) DEFAULT NULL,
                               is_cancelled BIT(1) NOT NULL,
                               last_activity DATETIME(6) DEFAULT NULL,
                               refresh_token VARCHAR(255) DEFAULT NULL,
                               session_token VARCHAR(255) DEFAULT NULL,
                               user_id BIGINT NOT NULL,
                               INDEX idx_user_id (user_id),
                               FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE mail_tokens (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             expired_at DATETIME(6) DEFAULT NULL,
                             is_cancelled BIT(1) NOT NULL,
                             token VARCHAR(255) DEFAULT NULL,
                             user_id BIGINT NOT NULL,
                             INDEX idx_user_id (user_id),
                             FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =============================================================
-- TABLE GROUP 2: STORES & INVENTORY
-- =============================================================

CREATE TABLE stores (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        address VARCHAR(255) NOT NULL UNIQUE,
                        created_at DATETIME(6) NOT NULL,
                        is_active BIT(1) NOT NULL,
                        name VARCHAR(255) NOT NULL UNIQUE,
                        phone VARCHAR(100) NOT NULL
);

CREATE TABLE ingredients (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             base_unit ENUM('G','ML') DEFAULT NULL,
                             batch_number VARCHAR(255) NOT NULL UNIQUE,
                             created_at DATETIME(6) NOT NULL,
                             image_url VARCHAR(255) DEFAULT NULL,
                             is_active BIT(1) NOT NULL,
                             name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE store_ingredient_batches (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          quantity BIGINT NOT NULL,
                                          ingredient_id BIGINT NOT NULL,
                                          store_id BIGINT NOT NULL,
                                          FOREIGN KEY (ingredient_id) REFERENCES ingredients(id),
                                          FOREIGN KEY (store_id) REFERENCES stores(id)
);

-- =============================================================
-- TABLE GROUP 3: MENU & FOOD ITEMS
-- =============================================================

CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            description VARCHAR(255),
                            name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE menu_items (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            created_at DATETIME(6) NOT NULL,
                            image_url VARCHAR(255),
                            is_active BIT(1) NOT NULL,
                            name VARCHAR(255) NOT NULL,
                            price INT NOT NULL,
                            category_id BIGINT NOT NULL,
                            FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE nutrients (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           base_unit ENUM('G','ML') NOT NULL,
                           name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE menu_item_nutrients (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     quantity DECIMAL(10,2) NOT NULL,
                                     menu_item_id BIGINT NOT NULL,
                                     nutrient_id BIGINT NOT NULL,
                                     FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
                                     FOREIGN KEY (nutrient_id) REFERENCES nutrients(id)
);

CREATE TABLE recipes (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         ingredient_id BIGINT NOT NULL,
                         menu_item_id BIGINT NOT NULL,
                         FOREIGN KEY (ingredient_id) REFERENCES ingredients(id),
                         FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- =============================================================
-- TABLE GROUP 4: DAILY MENUS
-- =============================================================

CREATE TABLE daily_menus (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             created_at DATETIME(6),
                             menu_date DATETIME(6) NOT NULL
);

CREATE TABLE daily_menu_items (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  daily_menu_id BIGINT NOT NULL,
                                  menu_item_id BIGINT NOT NULL,
                                  FOREIGN KEY (daily_menu_id) REFERENCES daily_menus(id),
                                  FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

CREATE TABLE daily_menu_stores (
                                   daily_menu_id BIGINT NOT NULL,
                                   store_id BIGINT NOT NULL,
                                   PRIMARY KEY (daily_menu_id, store_id),
                                   FOREIGN KEY (daily_menu_id) REFERENCES daily_menus(id),
                                   FOREIGN KEY (store_id) REFERENCES stores(id)
);

-- =============================================================
-- TABLE GROUP 5: ORDERS & PAYMENTS
-- =============================================================

CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        created_at DATETIME(6) NOT NULL,
                        pickup_time DATETIME(6) NOT NULL,
                        status ENUM('CANCELLED','COMPLETED','CONFIRMED','NEW','PROCESSING') NOT NULL,
                        total_price BIGINT NOT NULL,
                        store_id BIGINT NOT NULL,
                        user_id BIGINT NOT NULL,
                        INDEX idx_user_id (user_id),
                        INDEX idx_store_id (store_id),
                        INDEX idx_status (status),
                        FOREIGN KEY (store_id) REFERENCES stores(id),
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE dishes (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        is_active BIT(1) NOT NULL,
                        is_customizable BIT(1) NOT NULL,
                        name VARCHAR(255),
                        order_id BIGINT NOT NULL,
                        price INT NOT NULL DEFAULT 0,
                        cal INT NOT NULL DEFAULT 0,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        note TEXT,
                        FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE steps (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       description VARCHAR(255),
                       is_active BIT(1) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       step_number INT NOT NULL,
                       category_id BIGINT NOT NULL,
                       FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- MENU ITEMS
CREATE TABLE menu_items (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            name NVARCHAR(255) NOT NULL,
                            category ENUM('CARB', 'PROTEIN', 'VEGETABLE', 'SAUCE', 'DAIRY', 'FRUIT') NOT NULL,
                            is_active BOOLEAN DEFAULT TRUE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            price INT NOT NULL DEFAULT 0,
                            cal INT NOT NULL DEFAULT 0,
                            description NVARCHAR(255),
                            image_url NVARCHAR(255)
);

-- STEP ITEMS
CREATE TABLE step_items (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            step_id BIGINT NOT NULL,
                            menu_item_id BIGINT NOT NULL,
                            FOREIGN KEY (step_id) REFERENCES steps(id),
                            FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- NUTRIENTS
CREATE TABLE nutrients (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           name NVARCHAR(255) NOT NULL,
                           base_unit ENUM('G', 'ML') NOT NULL
);

-- MENU ITEM NUTRIENTS
CREATE TABLE menu_item_nutrients (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     menu_item_id BIGINT NOT NULL,
                                     nutrient_id BIGINT NOT NULL,
                                     quantity DECIMAL(10,2) NOT NULL,
                                     FOREIGN KEY (nutrient_id) REFERENCES nutrients(id),
                                     FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- DAILY MENU
CREATE TABLE daily_menu (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            store_id BIGINT NOT NULL,
                            menu_item_id BIGINT NOT NULL,
                            menu_date TIMESTAMP NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (store_id) REFERENCES stores(id),
                            FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- INGREDIENTS
CREATE TABLE ingredients (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             name NVARCHAR(255),
                             base_unit ENUM('G', 'ML'),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             image_url NVARCHAR(255),
                             is_active BOOLEAN DEFAULT TRUE,
                             batch_number NVARCHAR(255) NOT NULL
);

-- STORE INGREDIENT BATCHES
CREATE TABLE store_ingredient_batches (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          store_id BIGINT NOT NULL,
                                          ingredient_id BIGINT NOT NULL,
                                          quantity BIGINT NOT NULL,
                                          FOREIGN KEY (store_id) REFERENCES stores(id),
                                          FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- RECIPES
CREATE TABLE recipes (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         ingredient_id BIGINT NOT NULL,
                         menu_item_id BIGINT NOT NULL,
                         FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
                         FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- ORDER STEPS (Dish -> Step)
CREATE TABLE order_steps (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             dish_id BIGINT NOT NULL,
                             step_id BIGINT NOT NULL,
                             FOREIGN KEY (dish_id) REFERENCES dishes(id),
                             FOREIGN KEY (step_id) REFERENCES steps(id)
);

-- ORDER STEP ITEMS (OrderStep <-> DailyMenuItem with quantity)
CREATE TABLE order_step_items (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 order_step_id BIGINT NOT NULL,
                                 daily_menu_item_id BIGINT NOT NULL,
                                 quantity INT NOT NULL,
                                 FOREIGN KEY (order_step_id) REFERENCES order_steps(id),
                                 FOREIGN KEY (daily_menu_item_id) REFERENCES daily_menu_items(id)
);
