CREATE SCHEMA chicken_kitchen;
USE chicken_kitchen;

-- USERS
CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       role ENUM('CUSTOMER', 'EMPLOYEE', 'STORE', 'MANAGER', 'ADMIN') NOT NULL,
                       uid VARCHAR(128) UNIQUE,
                       email NVARCHAR(100) NOT NULL UNIQUE,
                       password NVARCHAR(255),
                       is_verified BOOLEAN DEFAULT FALSE,
                       phone NVARCHAR(100),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       is_active BOOLEAN DEFAULT TRUE,
                       full_name NVARCHAR(100) NOT NULL,
                       providers NVARCHAR(100) NOT NULL,
                       INDEX (email)
);

-- USER SESSIONS
CREATE TABLE user_sessions (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT NOT NULL,
                               session_token NVARCHAR(255),
                               refresh_token NVARCHAR(255),
                               expires_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               device_info TEXT default null, --Cai nay la JSON moi dung, nhma de TEXT o day cho dung bao loi
                               last_activity TIMESTAMP,
                               is_cancelled BOOLEAN DEFAULT FALSE,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               INDEX (user_id)
);

-- MAIL TOKENS
CREATE TABLE mail_tokens (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             user_id BIGINT NOT NULL,
                             token NVARCHAR(255),
                             expired_at TIMESTAMP,
                             is_cancelled BOOLEAN DEFAULT FALSE,
                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                             INDEX (user_id)
);

-- WALLETS
CREATE TABLE wallets (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT NOT NULL,
                         balance INT DEFAULT 0,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                         UNIQUE (user_id)
);

-- STORES
CREATE TABLE stores (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name NVARCHAR(255) NOT NULL,
                        address NVARCHAR(255) NOT NULL,
                        phone NVARCHAR(100) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        is_active BOOLEAN DEFAULT TRUE
);

-- PROMOTIONS
CREATE TABLE promotions (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            discount_type ENUM('PERCENT', 'AMOUNT') NOT NULL,
                            discount_value INT NOT NULL,
                            start_date TIMESTAMP NOT NULL,
                            end_date TIMESTAMP NOT NULL,
                            is_active BOOLEAN DEFAULT FALSE,
                            quantity INT DEFAULT 0,
                            INDEX (is_active)
);

-- ORDERS
CREATE TABLE orders (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        store_id BIGINT NOT NULL,
                        total_price BIGINT NOT NULL,
                        status ENUM('NEW', 'CONFIRMED', 'PROCESSING', 'COMPLETED', 'CANCELLED') NOT NULL,
                        pickup_time TIMESTAMP NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (store_id) REFERENCES stores(id),
                        INDEX (user_id),
                        INDEX (store_id),
                        INDEX (status)
);

-- ORDER PROMOTIONS
CREATE TABLE order_promotions (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  promotion_id BIGINT NOT NULL,
                                  order_id BIGINT NOT NULL,
                                  user_id BIGINT NOT NULL,
                                  used_date TIMESTAMP NOT NULL,
                                  FOREIGN KEY (user_id) REFERENCES users(id),
                                  FOREIGN KEY (promotion_id) REFERENCES promotions(id),
                                  FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- PAYMENT METHODS
CREATE TABLE payment_methods (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 name NVARCHAR(255) NOT NULL,
                                 description NVARCHAR(255),
                                 is_active BOOLEAN DEFAULT TRUE
);

-- TRANSACTIONS
CREATE TABLE transactions (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id BIGINT NOT NULL,
                              order_id BIGINT NOT NULL,
                              payment_method_id BIGINT NOT NULL,
                              transaction_status ENUM('PENDING', 'SUCCESS', 'REFUND', 'FAILED') NOT NULL,
                              amount INT NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              note TEXT,
                              FOREIGN KEY (user_id) REFERENCES users(id),
                              FOREIGN KEY (order_id) REFERENCES orders(id),
                              FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id),
                              INDEX (user_id),
                              INDEX (order_id)
);

-- CATEGORIES
CREATE TABLE categories (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            name NVARCHAR(255) NOT NULL,
                            description NVARCHAR(255)
);

-- DISHES
CREATE TABLE dishes (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        order_id BIGINT NOT NULL,
                        name NVARCHAR(255),
                        is_customizable BOOLEAN DEFAULT FALSE,
                        is_active BOOLEAN DEFAULT TRUE,
                        FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- STEPS
CREATE TABLE steps (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       category_id BIGINT NOT NULL,
                       name NVARCHAR(255) NOT NULL,
                       description NVARCHAR(255),
                       FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- MENU ITEMS
CREATE TABLE menu_items (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            name NVARCHAR(255) NOT NULL,
                            category ENUM('CARB', 'PROTEIN', 'VEGETABLE', 'SAUCE', 'DAIRY', 'FRUIT') NOT NULL,
                            is_active BOOLEAN DEFAULT TRUE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
