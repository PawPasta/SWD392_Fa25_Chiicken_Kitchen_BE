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

CREATE TABLE order_steps (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             quantity INT NOT NULL,
                             menu_item_id BIGINT NOT NULL,
                             order_id BIGINT NOT NULL,
                             step_id BIGINT NOT NULL,
                             FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
                             FOREIGN KEY (order_id) REFERENCES orders(id),
                             FOREIGN KEY (step_id) REFERENCES steps(id)
);

CREATE TABLE payment_methods (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 description VARCHAR(255),
                                 is_active BIT(1) NOT NULL,
                                 name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE payments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          amount INT NOT NULL,
                          created_at DATETIME(6) NOT NULL,
                          note TEXT,
                          status ENUM('CANCELLED','FINISHED','PENDING') NOT NULL,
                          order_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          FOREIGN KEY (order_id) REFERENCES orders(id),
                          FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              amount INT NOT NULL,
                              created_at DATETIME(6) NOT NULL,
                              note TEXT,
                              transaction_type ENUM('FAILED','PENDING','REFUND','SUCCESS') NOT NULL,
                              payment_id BIGINT NOT NULL,
                              payment_method_id BIGINT NOT NULL,
                              FOREIGN KEY (payment_id) REFERENCES payments(id),
                              FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
);

CREATE TABLE wallets (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         balance INT NOT NULL,
                         created_at DATETIME(6) NOT NULL,
                         updated_at DATETIME(6),
                         user_id BIGINT NOT NULL UNIQUE,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =============================================================
-- TABLE GROUP 6: PROMOTIONS
-- =============================================================

CREATE TABLE promotions (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            discount_type ENUM('AMOUNT','PERCENT') NOT NULL,
                            discount_value INT NOT NULL,
                            end_date DATETIME(6) NOT NULL,
                            is_active BIT(1) NOT NULL,
                            quantity INT NOT NULL,
                            start_date DATETIME(6) NOT NULL,
                            INDEX idx_is_active (is_active)
);

CREATE TABLE order_promotions (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  used_date DATETIME(6) NOT NULL,
                                  order_id BIGINT NOT NULL,
                                  promotion_id BIGINT NOT NULL,
                                  user_id BIGINT NOT NULL,
                                  FOREIGN KEY (order_id) REFERENCES orders(id),
                                  FOREIGN KEY (promotion_id) REFERENCES promotions(id),
                                  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =============================================================
-- END OF SCHEMA
-- =============================================================
