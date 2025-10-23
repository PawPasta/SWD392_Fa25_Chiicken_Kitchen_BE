-- ==========================================
-- 🐔 Chicken Kitchen Database Schema
-- Version: 1.0
-- Date: 2025-10-22

-- =============================
-- 🧑 USERS & AUTHENTICATION
-- =============================

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       phone VARCHAR(100),
                       birthday DATE,
                       image VARCHAR(255),
                       provider VARCHAR(100),
                       uid VARCHAR(128) UNIQUE,
                       role ENUM('ADMIN','EMPLOYEE','MANAGER','STORE','USER') NOT NULL,
                       is_active BIT NOT NULL,
                       is_verified BIT NOT NULL,
                       created_at DATETIME(6) NOT NULL,
                       updated_at DATETIME(6),
                       CONSTRAINT idx_email UNIQUE (email)
);

CREATE TABLE wallets (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL UNIQUE,
                         balance INT NOT NULL DEFAULT 0,
                         created_at DATETIME(6) NOT NULL,
                         updated_at DATETIME(6),
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE mail_tokens (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             token VARCHAR(255),
                             expired_at DATETIME(6),
                             is_cancelled BIT NOT NULL,
                             user_id BIGINT NOT NULL,
                             FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE user_sessions (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               session_token VARCHAR(255),
                               refresh_token VARCHAR(255),
                               expires_at DATETIME(6),
                               is_cancelled BIT NOT NULL,
                               last_activity DATETIME(6),
                               device_info JSON,
                               user_id BIGINT NOT NULL,
                               FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =============================
-- 🏬 STORES & INVENTORY
-- =============================

CREATE TABLE stores (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE,
                        address VARCHAR(255) NOT NULL UNIQUE,
                        phone VARCHAR(100) NOT NULL,
                        is_active BIT NOT NULL,
                        created_at DATETIME(6) NOT NULL
);

CREATE TABLE ingredients (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL UNIQUE,
                             batch_number VARCHAR(255) NOT NULL UNIQUE,
                             base_unit ENUM('G', 'ML'),
                             is_active BIT NOT NULL,
                             image_url VARCHAR(255),
                             created_at DATETIME(6) NOT NULL
);

CREATE TABLE store_ingredient_batches (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          ingredient_id BIGINT NOT NULL,
                                          store_id BIGINT NOT NULL,
                                          quantity BIGINT NOT NULL,
                                          FOREIGN KEY (ingredient_id) REFERENCES ingredients(id),
                                          FOREIGN KEY (store_id) REFERENCES stores(id)
);

-- =============================
-- 🍽️ MENU & CATEGORIES
-- =============================

CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            description VARCHAR(255)
);

CREATE TABLE menu_items (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            description VARCHAR(255),
                            price INT NOT NULL,
                            cal INT NOT NULL,
                            image_url VARCHAR(500),
                            is_active BIT NOT NULL,
                            created_at DATETIME(6) NOT NULL,
                            category_id BIGINT NOT NULL,
                            FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE nutrients (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255) NOT NULL UNIQUE,
                           base_unit ENUM('G','ML') NOT NULL
);

CREATE TABLE menu_item_nutrients (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     menu_item_id BIGINT NOT NULL,
                                     nutrient_id BIGINT NOT NULL,
                                     quantity DECIMAL(10,2) NOT NULL,
                                     FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
                                     FOREIGN KEY (nutrient_id) REFERENCES nutrients(id)
);

CREATE TABLE recipes (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         menu_item_id BIGINT NOT NULL,
                         ingredient_id BIGINT NOT NULL,
                         FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
                         FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- =============================
-- 📅 DAILY MENU
-- =============================

CREATE TABLE daily_menus (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             menu_date DATETIME(6) NOT NULL,
                             created_at DATETIME(6)
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

-- =============================
-- 🧾 ORDERS & STEPS
-- =============================

CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        store_id BIGINT NOT NULL,
                        status ENUM('NEW','CONFIRMED','PROCESSING','COMPLETED','CANCELLED') NOT NULL,
                        total_price BIGINT NOT NULL,
                        created_at DATETIME(6) NOT NULL,
                        pickup_time DATETIME(6) NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (store_id) REFERENCES stores(id)
);

-- =============================
-- 👷 EMPLOYEE DETAILS
-- =============================

CREATE TABLE employee_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    store_id BIGINT NOT NULL,
    position VARCHAR(100),
    is_active BIT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    note TEXT,
    INDEX idx_employee_store_id (store_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (store_id) REFERENCES stores(id)
);

-- Enforce at most one NEW order per (user, store)
-- MySQL UNIQUE index + generated column: NULLs are allowed multiple times, so only 'NEW' rows are constrained
ALTER TABLE orders
    ADD COLUMN status_new TINYINT(1) 
        GENERATED ALWAYS AS (CASE WHEN status = 'NEW' THEN 1 ELSE NULL END) STORED;
CREATE UNIQUE INDEX ux_orders_one_new_per_user_store
    ON orders (user_id, store_id, status_new);

CREATE TABLE dishes (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        order_id BIGINT NOT NULL,
                        price INT NOT NULL,
                        cal INT NOT NULL,
                        note VARCHAR(255),
                        created_at DATETIME(6) NOT NULL,
                        updated_at DATETIME(6),
                        FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE steps (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description VARCHAR(255),
                       step_number INT NOT NULL,
                       is_active BIT NOT NULL,
                       category_id BIGINT NOT NULL,
                       FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE order_steps (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             dish_id BIGINT NOT NULL,
                             step_id BIGINT NOT NULL,
                             FOREIGN KEY (dish_id) REFERENCES dishes(id),
                             FOREIGN KEY (step_id) REFERENCES steps(id)
);

CREATE TABLE order_step_items (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  order_step_id BIGINT NOT NULL,
                                  daily_menu_item_id BIGINT NOT NULL,
                                  quantity INT NOT NULL,
                                  FOREIGN KEY (order_step_id) REFERENCES order_steps(id),
                                  FOREIGN KEY (daily_menu_item_id) REFERENCES daily_menu_items(id)
);

-- =============================
-- 🎟️ PROMOTIONS
-- =============================

CREATE TABLE promotions (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            discount_type ENUM('AMOUNT','PERCENT') NOT NULL,
                            discount_value INT NOT NULL,
                            quantity INT NOT NULL,
                            start_date DATETIME(6) NOT NULL,
                            end_date DATETIME(6) NOT NULL,
                            is_active BIT NOT NULL
);

CREATE TABLE order_promotions (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  order_id BIGINT NOT NULL,
                                  user_id BIGINT NOT NULL,
                                  promotion_id BIGINT NOT NULL,
                                  used_date DATETIME(6) NOT NULL,
                                  FOREIGN KEY (order_id) REFERENCES orders(id),
                                  FOREIGN KEY (user_id) REFERENCES users(id),
                                  FOREIGN KEY (promotion_id) REFERENCES promotions(id)
);

-- =============================
-- 💳 PAYMENTS & TRANSACTIONS
-- =============================

CREATE TABLE payment_methods (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL UNIQUE,
                                 description VARCHAR(255),
                                 is_active BIT NOT NULL
);

CREATE TABLE payments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          order_id BIGINT NOT NULL,
                          amount INT NOT NULL,
                          status ENUM('PENDING','FINISHED','CANCELLED') NOT NULL,
                          note TEXT,
                          created_at DATETIME(6) NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              payment_id BIGINT NOT NULL,
                              payment_method_id BIGINT NOT NULL,
                              amount INT NOT NULL,
                              transaction_type ENUM('PENDING','SUCCESS','REFUND','FAILED') NOT NULL,
                              note TEXT,
                              created_at DATETIME(6) NOT NULL,
                              FOREIGN KEY (payment_id) REFERENCES payments(id),
                              FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
);

-- ==========================================
-- ✅ END OF SCHEMA
-- ==========================================
