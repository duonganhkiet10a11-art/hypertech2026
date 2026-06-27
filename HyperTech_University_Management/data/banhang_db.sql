﻿-- ================= RESET DATABASE =================
IF DB_ID('banhang_db') IS NOT NULL
DROP DATABASE banhang_db;
GO

CREATE DATABASE banhang_db;
GO

USE banhang_db;
GO

-- ================= USERS =================
CREATE TABLE users (
    email NVARCHAR(100) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL,
    password NVARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    address NVARCHAR(255),
    status BIT DEFAULT 1
);

-- ================= ADMINS =================
CREATE TABLE admins (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL,
    password NVARCHAR(255) NOT NULL
);

-- ================= CATEGORIES =================
CREATE TABLE categories (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(255),
    status BIT DEFAULT 1
);

-- ================= PRODUCTS =================
CREATE TABLE products (
    id INT IDENTITY(1,1) PRIMARY KEY,
    category_id INT NOT NULL,
    name NVARCHAR(150) NOT NULL,
    cpu NVARCHAR(100),
    gpu NVARCHAR(100),
    ram NVARCHAR(50),
    ssd NVARCHAR(50),
    screen NVARCHAR(100),
    refresh_rate NVARCHAR(50),
    old_price DECIMAL(12,0) NOT NULL,
    new_price DECIMAL(12,0) NOT NULL,
    stock INT DEFAULT 0,
    description NVARCHAR(MAX),
    image VARCHAR(255),
    status BIT DEFAULT 1,

    FOREIGN KEY (category_id)
    REFERENCES categories(id)
    ON DELETE CASCADE
);

-- ================= CART =================
CREATE TABLE cart (
    email NVARCHAR(100),
    product_id INT,
    quantity INT DEFAULT 1,

    PRIMARY KEY (email, product_id),

    FOREIGN KEY (email)
    REFERENCES users(email)
    ON DELETE CASCADE,

    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE CASCADE
);

-- ================= ORDERS =================
CREATE TABLE orders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(100)  NOT NULL,
    total_price DECIMAL(12,0) NOT NULL,
    status VARCHAR(20)
        CHECK (status IN ('pending','confirmed','shipping','completed','cancelled','outstock')),
    created_at DATETIME DEFAULT GETDATE(),

    FOREIGN KEY (email)
    REFERENCES users(email)
    ON DELETE CASCADE
);

-- ================= ORDER ITEMS =================
CREATE TABLE order_items (
    id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    product_id INT,
    price DECIMAL(12,0),
    quantity INT,

    FOREIGN KEY (order_id)
    REFERENCES orders(id)
    ON DELETE CASCADE,

    FOREIGN KEY (product_id)
    REFERENCES products(id)
);

-- ================= DISCOUNTS =================
CREATE TABLE discounts (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100),
    discount_percent INT CHECK (discount_percent BETWEEN 1 AND 100),
    start_date DATETIME,
    end_date DATETIME
);

-- ================= PRODUCT DISCOUNTS =================
CREATE TABLE product_discounts (
    product_id INT,
    discount_id INT,

    PRIMARY KEY (product_id, discount_id),

    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE CASCADE,

    FOREIGN KEY (discount_id)
    REFERENCES discounts(id)
    ON DELETE CASCADE
);
-- ================= COMPLAINTS =================
CREATE TABLE complaints (
    id INT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(100),
    order_id INT,
    product_id INT,
    title NVARCHAR(200),
    content NVARCHAR(MAX),


    FOREIGN KEY (email) REFERENCES users(email),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- ================= PAYMENTS =================
CREATE TABLE payments (
    id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    email NVARCHAR(100),
    payment_method VARCHAR(30)
        CHECK (payment_method IN ('COD','bank_transfer','momo','vnpay','paypal')),
    amount DECIMAL(12,0),
    status VARCHAR(20)
        CHECK (status IN ('pending','paid','failed','refunded'))
        DEFAULT 'pending',
    transaction_code VARCHAR(100),
    paid_at DATETIME,

    FOREIGN KEY (order_id)
    REFERENCES orders(id)
    ON DELETE CASCADE,

    FOREIGN KEY (email)
    REFERENCES users(email)
);

-- ================= INSERT USERS =================
INSERT INTO users (email, username, password, phone, address) VALUES
('a@gmail.com',N'Nguyễn Văn A','$2a$10$7EqJtq98hPqEX7fNZaFWoOe5n7y4F7n9k1Z8w3YQ7g6X8kFQK7G9r','0900000001',N'Hà Nội'),
('b@gmail.com',N'Trần Thị B','$2a$10$Wl1x0p9vG6kQJ3Yt8Fz0BOd9u4H8n2Lm5PqR7sT1UvXyZ0aBcDeFg','0900000002',N'Hồ Chí Minh'),
('c@gmail.com',N'Lê Văn C','$2a$10$k9H3LpQw8ZrT6YxV1aBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890','0900000003',N'Đà Nẵng'),
('d@gmail.com',N'Phạm Thị D','$2a$10$ZxCvBnM1QwErTyUiOpAsDfGhJkLzXcVbNmQwErTyUiOpAsDfGhJkL','0900000004',N'Cần Thơ');

-- ================= INSERT ADMINS =================
INSERT INTO admins (username,password) VALUES
('truong','admin123'),
('kiet','admin456'),
('trieu','admin789');

-- ================= INSERT CATEGORIES =================
INSERT INTO categories (name,description) VALUES
(N'Laptop',N'Laptop văn phòng và gaming'),
(N'Màn hình',N'Màn hình máy tính'),
(N'Bàn phím',N'Bàn phím cơ'),
(N'Chuột',N'Chuột gaming');

-- ================= INSERT PRODUCTS =================
INSERT INTO products
(category_id,name,cpu,gpu,ram,ssd,screen,refresh_rate,old_price,new_price,stock,description,image)
VALUES
(1,N'Laptop gaming Gigabyte A16 i7 RTX4050','i7-13620H','RTX 4050','16 GB','1 TB','16 inch WUXGA','165 Hz',29930000,28490000,10,N'Gigabyte A16 i7 RTX4050 16GB RAM 1TB SSD','lab1.png'),
(1,N'Laptop gaming Gigabyte A16 i5 RTX3050','i5-12500H','RTX 3050','16 GB','512 GB','15.6 inch','144 Hz',29490000,27990000,10,N'Gigabyte A16 i5 RTX3050 16GB RAM 512GB SSD','lab2.png'),
(1,N'Acer Nitro V ProPanel R5 RTX3050','R5-7535HS','RTX 3050','16 GB','512 GB','15.6 inch FHD','180 Hz',27990000,25990000,10,N'Acer Nitro V Ryzen5 RTX3050','lab3.jpg'),
(1,N'Lenovo LOQ 15IAX9E i5 RTX3050','i5-12450HX','RTX 3050','16 GB','512 GB','15.6 inch FHD','144 Hz',24490000,25490000,10,N'Lenovo LOQ i5 RTX3050','lab4.png'),
(1,N'Lenovo LOQ 15ARP10E R7 RTX4050','R7-7735HS','RTX 4050','16 GB','512 GB','15.6 inch FHD','144 Hz',28990000,26990000,10,N'Lenovo LOQ Ryzen7 RTX4050','lab5.png'),
(1,N'MSI Cyborg 15 A13UC i7 RTX3050','i7-13620H','RTX 3050','16 GB','512 GB','15.6 inch FHD','144 Hz',32990000,29990000,10,N'MSI Cyborg RTX3050','lab6.png'),
(1,N'Acer Nitro Lite 16 NL16 71','i5-13420H','RTX 3050','16 GB','512 GB','16 inch FHD+','165 Hz',23990000,25190000,10,N'Acer Nitro Lite RTX3050','lab7.png'),
(1,N'Gigabyte A16 i7 RTX4050 512GB','i7-13620H','RTX 4050','16 GB','512 GB','16 inch FHD+ IPS','165 Hz',29990000,27990000,10,N'Gigabyte A16 RTX4050','lab8.png'),
(1,N'Acer Nitro V i5 RTX4050 32GB','i5-13420H','RTX 4050','32 GB','512 GB','15.6 inch FHD','180 Hz',31990000,29490000,10,N'Acer Nitro V RTX4050 32GB','lab9.png'),
(1,N'HP Victus 15 i5 RTX3050','i5-13420H','RTX 3050','16 GB','512 GB','15.6 inch FHD','144 Hz',22990000,20990000,10,N'HP Victus RTX3050','lab12.png'),
(1,N'Laptop Gigabyte G5 MD 51S1123SH','i5-11400H','RTX 3050Ti','16 GB','512 GB','15.6 FHD','144 Hz',24490000,21990000,10,N'Gigabyte G5 RTX3050Ti','l2.png'),
(1,N'Laptop gaming MSI Cyborg 15 A13VEK','i7-13620H','RTX 4050','16 GB','512 GB','15.6 FHD','144 Hz',26390000,24990000,10,N'MSI Cyborg RTX4050','l3.png'),
(1,N'Laptop gaming ASUS V16 K3607VJ RP106W','Core 7 240H','RTX 3050','16 GB','512 GB','16 WUXGA','144 Hz',24490000,22490000,10,N'ASUS V16 RTX3050','l4.png'),
(1,N'Laptop gaming HP Victus 16 s0142AX','R5-7640HS','RTX 4060','32 GB','512 GB','16.1 FHD','144 Hz',34490000,24990000,10,N'HP Victus RTX4060','l5.png'),
(1,N'Laptop gaming Gigabyte G5 MF5','i7-13620H','RTX 4050','16 GB','512 GB','15.6 FHD','144 Hz',25490000,21990000,10,N'Gigabyte G5 RTX4050','l6.png'),
(1,N'Laptop gaming MSI Thin 15 B13VE 2824VN','i5-13420H','RTX 4050','16 GB','512 GB','15.6 FHD','144 Hz',22990000,20990000,10,N'MSI Thin RTX4050','l7.png'),
(1,N'Laptop gaming ASUS V16 V3607VU RP290W','Core 5 210H','RTX 4050','16 GB','512 GB','16 WUXGA','144 Hz',25490000,23990000,10,N'ASUS V16 RTX4050','l8.png'),
(1,N'Laptop gaming Gigabyte G6 MF','i7-13700H','RTX 4050','16 GB','1 TB','16 FHD+','165 Hz',25890000,23990000,10,N'Gigabyte G6 RTX4050','l9.png'),
(1,N'Laptop gaming HP VICTUS 15 fb3116AX','R7-7445HS','RTX 3050','16 GB','512 GB','15.6 FHD','144 Hz',25990000,20990000,10,N'HP Victus Ryzen7 RTX3050','l10.png'),
(1,N'Laptop gaming MSI Katana A15 AI B8VE','R7-8845HS','RTX 4050','16 GB','512 GB','15.6 FHD','144 Hz',28990000,23990000,10,N'MSI Katana RTX4050','l11.png'),
(1,N'Laptop gaming Lenovo LOQ 15ARP9','R5-7235HS','RTX 3050','16 GB','1 TB','15.6 FHD','144 Hz',24490000,22290000,10,N'Lenovo LOQ RTX3050','l12.png'),
(1,N'Laptop gaming Acer Nitro ProPanel ANV15',
N'R7-7735HS', N'RTX 4050', N'16 GB', N'512 GB',
N'15.6 inch FHD', N'180 Hz',
31990000,30490000,10,
N'Laptop gaming Acer Nitro ProPanel ANV15','l17.png'),

(1,N'Laptop gaming Acer Predator Helios Neo 16S AI',
N'Ultra 9 275HX', N'RTX 5070Ti', N'64 GB', N'2 TB',
N'16 inch WQXGA OLED', N'240 Hz',
101990000,95990000,10,
N'Laptop gaming Acer Predator Helios Neo 16S AI','l18.png'),

(1,N'Laptop gaming MSI Stealth 18 HX AI',
N'Ultra 9 275HX', N'RTX 5080', N'32 GB', N'2 TB',
N'18 inch UHD+ MiniLED', N'120 Hz',
107990000,102990000,10,
N'Laptop gaming MSI Stealth 18 HX AI','l19.png'),

(1,N'Laptop gaming Acer Gaming Nitro 16S AI',
N'R7 AI 350', N'RTX 5060', N'16 GB', N'512 GB',
N'16 inch FHD+', N'180 Hz',
50990000,48790000,10,
N'Laptop gaming Acer Gaming Nitro 16S AI','l20.png'),

(1,N'Laptop gaming Acer Predator Triton 14 AI',
N'Ultra 9 288V', N'RTX 5070', N'32 GB', N'2 TB',
N'14.5 inch 2.8K OLED', N'120 Hz',
93580000,90490000,10,
N'Laptop gaming Acer Predator Triton 14 AI','l21.png'),

(1,N'Laptop gaming Acer Predator Triton Neo 16',
N'Ultra 7 155H', N'RTX 4060', N'32 GB', N'1 TB',
N'16 inch 2.5K', N'240 Hz',
52490000,49400000,10,
N'Laptop gaming Acer Predator Triton Neo 16','l22.png'),

(1,N'Laptop gaming MSI Sword 16 HX B14VEKG 856VN',
N'Intel Core i7-14700HX', N'RTX 4050 6GB', N'16 GB', N'1 TB',
N'16 inch', N'165 Hz',
35990000,31190000,10,
N'Laptop gaming MSI Sword 16 HX B14VEKG 856VN','l23.png'),

(1,N'Laptop gaming HP OMEN 16-am0178TX BX8Y4PA',
N'Intel Core Ultra 7 255H', N'RTX 5060 8GB', N'16 GB', N'512 GB',
N'16 inch', N'165 Hz',
40890000,39990000,10,
N'Laptop gaming HP OMEN 16-am0178TX BX8Y4PA','l24.png'),

(1,N'Laptop gaming ASUS ROG Strix SCAR 18 G835LW SA193W',
N'Intel Core Ultra 9 275HX', N'RTX 5080 16GB', N'32 GB', N'1 TB',
N'18 inch', N'240 Hz',
88390000,84990000,10,
N'Laptop gaming ASUS ROG Strix SCAR 18 G835LW SA193W','l25.png');
<<<<<<< HEAD
INSERT INTO products
(category_id,name,cpu,gpu,ram,ssd,screen,refresh_rate,old_price,new_price,stock,description,image)
VALUES
(1,N'Laptop gaming Acer Predator Helios PHN',N'Ultra 7 255HX', N'RTX 5060', N'32 GB', N'1 TB',N'16 inch 2K+', N'240 Hz',56990000, 56990000,10,'Acer Predator Helios PHN', 'l16.png'),
(1,N'Laptop gaming Lenovo Legion 5 15AHP10 83M0002XVN',
N'AMD Ryzen 7 260', N'RTX 5050', N'24 GB', N'512 GB',
N'15 inch', N'180 Hz',
39490000,36990000,10,
N'Laptop gaming Lenovo Legion 5 15AHP10 83M0002XVN','l26.png'),

(1,N'Laptop gaming ASUS ROG Zephyrus G14 GA403WM QS058WS',
N'AMD Ryzen 9 8945HS', N'RTX 4070', N'32 GB', N'1 TB',
N'14 inch', N'180 Hz',
58990000,55990000,10,
N'Laptop gaming ASUS ROG Zephyrus G14 GA403WM QS058WS','l27.png');
=======

>>>>>>> 08a896c6e42796eb830424d19d7f6f525ca5c8be
INSERT INTO cart (email,product_id,quantity) VALUES
('a@gmail.com',1,1),
('a@gmail.com',2,1),
('a@gmail.com',3,2),
('a@gmail.com',4,3);

-- ================= INSERT ORDERS =================
INSERT INTO orders (email,total_price,status) VALUES
('a@gmail.com',22500000,'confirmed'),
('a@gmail.com',3000000,'shipping'),
('a@gmail.com',3000000,'completed'),
('a@gmail.com',5400000,'pending');

-- ================= INSERT ORDER ITEMS =================
INSERT INTO order_items (order_id,product_id,price,quantity) VALUES
(1,1,18000000,1),
(1,2,4500000,1),
(2,3,1500000,2),
(3,3,1500000,2),
(4,4,1800000,3);











INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES 
(4, N'Chuột Logitech G502 Hero Gaming', 1090000, 890000, 'm1.png');

DELETE FROM products
WHERE name = N'Chuột Logitech G502 Hero Gaming';

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES 
(4, N'Chuột gaming có dây Rapoo V260 Pro', 499000, 329000, 'm13.jpg');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES 
(4, N'Chuột DareU Không dây EM911T RGB Đen', 690000, 400000, 'm14.jpg'),
(4, N'Chuột ATK A9 SE Tri-mode Nearlink Wireless Black', 590000, 490000, 'm15.jpg');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES 
(4, N'Chuột ASUS ROG Strix Impact III', 1090000, 990000, 'm16.jpg'),
(4, N'Chuột DareU Không dây EM911T RGB Trắng', 690000, 400000, 'm17.jpg'),
(4, N'Chuột Razer DeathAdder Essential White', 790000, 410000, 'm18.png'),
(4, N'Chuột Razer Cobra', 1049000, 990000, 'm19.jpg');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES 
(4, N'Chuột Razer Basilisk V3 Pro 35K Black', 4490000, 4190000, 'm20.png'),
(4, N'Chuột Razer Basilisk V3 Pro White', 4090000, 3390000, 'm21.gif'),
(4, N'Chuột Logitech G502 X Plus LightSpeed Black', 3590000, 3100000, 'm22.png'),
(4, N'Chuột không dây Corsair Nightsabre RGB', 3990000, 3590000, 'm23.png'),
(4, N'Chuột không dây Corsair Darkstar RGB', 3990000, 3590000, 'm24.png');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES
(4, N'Chuột văn phòng Logitech M331 Silent Black', 349000, 340000, 'm25.png'),
(4, N'Chuột văn phòng Logitech M331 Silent Blue', 400000, 340000, 'm26.png'),
(4, N'Chuột văn phòng Logitech M331 Silent Red', 490000, 340000, 'm27.jpg'),
(4, N'Chuột văn phòng Logitech Pebble Mouse 2 M350S Rose', 699000, 450000, 'm28.jpg'),
(4, N'Chuột văn phòng Logitech Pebble Mouse 2 M350S White', 699000, 450000, 'm29.png'),
(4, N'Chuột văn phòng Logitech MX Anywhere 3S Rose', 1750000, 1550000, 'm30.png'),
(4, N'Chuột văn phòng Logitech MX Anywhere 3S Pale Grey', 1750000, 1550000, 'm31.png'),
(4, N'Chuột văn phòng Rapoo M20 Wireless', 150000, 90000, 'm32.png'),
(4, N'Chuột văn phòng Logitech M650 Signature Graphite', 849000, 645000, 'm33.png'),
(4, N'Chuột văn phòng Logitech M650 Off White', 800000, 645000, 'm34.png');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES 
(4, N'Chuột văn phòng Rapoo M20 Wireless', 150000, 90000, 'm32.jpg');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES
(4, N'Chuột văn phòng MonsGeek D1 Pink', 299000, 120000, 'm35.png');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES
(4, N'Chuột văn phòng MonsGeek D1 Black', 299000, 150000, 'm36.png');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES
(3, N'Bàn phím cơ ASUS ROG Falchion Ace HFX Rapid', 5499000, 5224050, 'b1.jpg');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES
(3, N'BÀN PHÍM CƠ E-DRA EK375 V2 ALPHA LINEAR SWITCH', 699000, 664050, 'b2.jpg'),

(3, N'BÀN PHÍM CƠ ASUS TUF K3 GEN II BLACK OPTICAL RED SWITCH', 1999000, 1899050, 'b3.jpg'),

(3, N'BÀN PHÍM CƠ E-DRA EK368HE', 999000, 949050, 'b4.jpg'),

(3, N'BÀN PHÍM CƠ E-DRA EK398S WIRELESS RED SWITCH', 799000, 759050, 'b5.jpg'),

(3, N'BÀN PHÍM CƠ E-DRA EK375S WIRELESS RED SWITCH', 699000, 664050, 'b6.jpg'),

(3, N'BÀN PHÍM CƠ E-DRA EK368S WIRELESS RED SWITCH', 649000, 616550, 'b7.jpg'),

(3, N'BÀN PHÍM CƠ MCHOSE G75 PRO GLACIER GRADIENT CABBAGE', 1399000, 1329050, 'b8.png'),

(3, N'BÀN PHÍM CƠ MCHOSE G75 PRO ARCTIS SNOW MATCHA LATTE', 1399000, 1329050, 'b9.png');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES
(3, N'BÀN PHÍM CƠ MACHENIKE KG98 WHITE BLUE PURPLE GOLD', 1699000, 1349000, 'b10.png'),

(3, N'BÀN PHÍM CƠ MACHENIKE KG98 BLACK GREEN PURPLE GOLD', 1699000, 1349000, 'b11.png'),

(3, N'BÀN PHÍM CƠ MADLIONS MAD68 HE FLAGSHIP V2 WHITE', 1099000, 889000, 'b12.jpg'),

(3, N'BÀN PHÍM CƠ MADLIONS MAD68 HE FLAGSHIP V2 BLACK', 1099000, 889000, 'b13.jpg'),

(3, N'BÀN PHÍM CƠ AULA F75 WHITE BLUE GRADIENT OUTEMU SILENT', 1999000, 1549000, 'b14.webp');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES
(3, N'BÀN PHÍM CƠ AULA F75 3 MODE GLACIER BLUE OUTEMU SILENT', 1999000, 1349000, 'b15.png'),

(3, N'BÀN PHÍM CƠ AULA F75 3 MODE SNOW BLUE OUTEMU SILENT', 1999000, 1349000, 'b16.jpg'),

(3, N'BÀN PHÍM CƠ RAZER HUNTSMAN V3 PRO TKL CS2 COUNTER-STRIKE', 6490000, 5489000, 'b17.jpg'),

(3, N'BÀN PHÍM CƠ MCHOSE G75 PRO BLUE CABBAGE TOFU V2 SWITCH', 1199000, 1049000, 'b18.png'),

(3, N'BÀN PHÍM CƠ MCHOSE G75 PRO GREEN MATCHA LATTE SWITCH', 1399000, 1249000, 'b19.png');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES
(3, N'BÀN PHÍM CƠ MCHOSE GX87 LITE BLUE NANO SPRAYING MATCHA', 2399000, 2199000, 'b20.png'),

(3, N'BÀN PHÍM CƠ MCHOSE GX87 LITE WHITE NANO SPRAYING MATCHA', 2399000, 2199000, 'b21.png'),

(3, N'BÀN PHÍM CƠ MCHOSE X UNBOX THERAPY UT98 CLASSIC', 2899000, 2099000, 'b22.png'),

(3, N'BÀN PHÍM CƠ RAZER HUNTSMAN V3 PRO TKL 8KHZ ANALOG', 5990000, 4989000, 'b23.jpg'),

(3, N'BÀN PHÍM CƠ RAZER HUNTSMAN V3 PRO 8KHZ ANALOG OPTICAL', 6890000, 5889000, 'b24.jpg'),

(3, N'BÀN PHÍM CƠ ATK RS7 ULTRA STELLAR PINK BLAZEBLADE', 4499000, 3889000, 'b25.jpg'),

(3, N'BÀN PHÍM CƠ ATK RS7 ULTRA SILVER EDGE SNOWBLADE SWITCH', 4499000, 3889000, 'b26.jpg'),

(3, N'BÀN PHÍM CƠ ATK 68 RX HE PINK WOLF SWITCH', 1999000, 1589000, 'b27.jpg'),

(3, N'BÀN PHÍM CƠ ATK 68 RX HE WHITE WOLF SWITCH', 1999000, 1589000, 'b28.jpg'),

(3, N'BÀN PHÍM CƠ ATK 68 RX HE BLACK WOLF SWITCH', 1999000, 1589000, 'b29.jpg');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES
(3, N'BÀN PHÍM CƠ ATK 68 RX HE BLACK WOLF SWITCH', 1999000, 1589000, 'b30.jpg');

INSERT INTO products (category_id, name, old_price, new_price, image)
VALUES
(3, N'BÀN PHÍM CƠ ASUS ROG AZOTH X WIRELESS NX SNOW V2 SWITCH', 7999000, 6489000, 'b29.jpg');

INSERT INTO products (category_id, name, screen, old_price, new_price, image) VALUES
(5, N'Màn hình ASUS TUF GAMING VG27AQ5A', '27 inch', 5990000, 4990000, 'mh1.jpg'),
(5, N'Màn hình ViewSonic VX2882-4KP 28 inch', '28 inch', 15990000, 8390000, 'mh2.jpg'),
(5, N'Màn hình ViewSonic VA2432A-H 24 inch', '24 inch', 3590000, 2050000, 'mh3.jpg'),
(5, N'Màn hình ASUS TUF VG259Q5A 25 inch', '25 inch', 4220000, 2950000, 'mh4.jpg'),
(5, N'Màn hình ViewSonic VA2432-H-2 24 inch', '24 inch', 2590000, 1990000, 'mh5.jpg'),
(5, N'Màn hình ASUS VY249HGR 24 inch', '24 inch', 3090000, 2250000, 'mh6.jpg'),
(5, N'Màn hình ASUS TUF VG249Q5A 24 inch', '24 inch', 3990000, 2590000, 'mh7.jpg'),
(5, N'Màn hình ViewSonic VX2479A-HD-PRO 24 inch', '24 inch', 4490000, 2790000, 'mh8.jpg'),
(5, N'Màn hình Acer KG240Y-X1 24 inch', '24 inch', 3790000, 2690000, 'mh9.jpg'),
(5, N'Màn hình ViewSonic VA2708-2K-MHD 27 inch', '27 inch', 4190000, 3690000, 'mh10.jpg'),
(5, N'Màn hình MSI MAG 274QF X24 27 inch', '27 inch', 6990000, 5490000, 'mh11.jpg');

INSERT INTO products (category_id, name, screen, old_price, new_price, image)
VALUES 
(5, N'Màn hình MSI MAG 255F E20 25 inch Rapid IPS 200Hz', '25 inch', 3690000, ROUND(3690000 * 0.9, 0), 'mh25-1.jpg'),
(5, N'Màn hình GIGABYTE GS25F2A 25 inch IPS 240Hz', '25 inch', 3990000, ROUND(3990000 * 0.9, 0), 'mh25-2.jpg'),
(5, N'Màn hình MSI MAG 244F 24 inch Rapid IPS 200Hz', '24 inch', 3990000, ROUND(3990000 * 0.9, 0), 'mh25-3.jpg'),
(5, N'Màn hình AOC CS25G 25 inch Fast IPS 310Hz', '25 inch', 5090000, ROUND(5090000 * 0.9, 0), 'mh25-4.jpg'),
(5, N'Màn hình MSI MAG 255PXF 25 inch Rapid IPS 300Hz', '25 inch', 5990000, ROUND(5990000 * 0.9, 0), 'mh25-5.jpg'),
(5, N'Màn hình Samsung LS25BG400 25 inch IPS 240Hz', '25 inch', 9190000, ROUND(9190000 * 0.9, 0), 'mh25-6.jpg'),
(5, N'Màn hình ASUS ROG Strix XG259QNS 25 inch IPS 380Hz', '25 inch', 11990000, ROUND(11990000 * 0.9, 0), 'mh25-7.jpg');


DELETE FROM products
WHERE name IN (
    N'Màn hình GIGABYTE GS25F2A 25 inch IPS 240Hz',
    N'Màn hình MSI MAG 244F 24 inch Rapid IPS 200Hz',
    N'Màn hình AOC CS25G 25 inch Fast IPS 310Hz',
    N'Màn hình MSI MAG 255PXF 25 inch Rapid IPS 300Hz',
    N'Màn hình Samsung LS25BG400 25 inch IPS 240Hz',
    N'Màn hình ASUS ROG Strix XG259QNS 25 inch IPS 380Hz'
);

DELETE FROM products
WHERE name IN (
N'Màn hình MSI MAG 274QF X24 27 inch'
);

INSERT INTO products 
(category_id, name, screen, refresh_rate, old_price, new_price, image)
VALUES 

-- 1
(5, N'Màn hình Acer KA242Y P6 24 inch IPS 144Hz', '24 inch', '144Hz', 2590000, ROUND(2590000*0.9,0), 'mh25-8.jpg'),

-- 2
(5, N'Màn hình Acer XV242 F 25 inch 540Hz chuyên game', '25 inch', '540Hz', 16990000, ROUND(16990000*0.9,0), 'mh25-9.jpg'),

-- 3
(5, N'Màn hình Acer KG251Q Z1 25 inch 280Hz chuyên game', '25 inch', '280Hz', 4990000, ROUND(4990000*0.9,0), 'mh25-10.jpg'),

-- 4
(5, N'Màn hình AOC 25B36X 25 inch IPS 144Hz chuyên game', '25 inch', '144Hz', 2790000, ROUND(2790000*0.9,0), 'mh25-11.jpg');

DELETE FROM products
WHERE name IN (
    N'Màn hình ViewSonic VA2708-2K-MHD 27 inch',
    N'Màn hình ViewSonic VX2882-4KP 28 inch'
);
<<<<<<< HEAD
=======
select *
from products


DELETE FROM products
WHERE category_id = 5;

SELECT name, COUNT(*) 
FROM products
GROUP BY name
HAVING COUNT(*) > 1

WITH temp AS (
    SELECT *,
           ROW_NUMBER() OVER (PARTITION BY name ORDER BY id) AS rn
    FROM products
)
DELETE FROM temp
WHERE rn > 1;

SELECT name, COUNT(*) 
FROM products
GROUP BY name
HAVING COUNT(*) > 1
>>>>>>> 08a896c6e42796eb830424d19d7f6f525ca5c8be

INSERT INTO products 
(category_id, name, screen, refresh_rate, old_price, new_price, image)
VALUES  
(5, N'Màn hình AOC Q27G11E 27 inch IPS 2K 180Hz', '27 inch', '180Hz', 5990000, 3990000, 'mh27.jpg'),

(5, N'Màn hình Philips 27M2N3500PF 27 inch IPS 2K 260Hz', '27 inch', '260Hz', 5990000, 4690000, 'mh27-2.jpg'),

(5, N'Màn hình GIGABYTE GS27FA 27 inch IPS 180Hz', '27 inch', '180Hz', 4990000, 2990000, 'mh27-3.jpg');

INSERT INTO products (category_id, name, screen, old_price, new_price, image)
VALUES  
(5, N'Màn hình LG 27G610A-B 27 inch IPS 2K 200Hz Gsync', '27 inch', 5990000, 5190000, 'mh27-4.jpg'),

(5, N'Màn hình Acer VG271U M3 27 inch IPS 2K 180Hz', '27 inch', 5490000, 4390000, 'mh27-5.jpg'),

(5, N'Màn hình E-DRA EGM27Q180PVS 27 inch IPS 2K 180Hz', '27 inch', 3990000, 3590000, 'mh27-6.jpg');

INSERT INTO products (category_id, name, screen, old_price, new_price, image)
VALUES  

(5, N'Màn hình E-DRA EGM27F144PVS 27 inch IPS 144Hz', '27 inch', 2990000, 2390000, 'mh27-7.jpg'),

(5, N'Màn hình Asus ROG Strix XG27ACMES 27 inch Fast IPS 2K 255Hz USB-C', '27 inch', 7990000, 7190000, 'mh27-8.jpg'),

(5, N'Màn hình Asus ROG Strix XG27ACMEG-G Hatsune Miku Edition 27 inch Fast IPS 2K 260Hz', '27 inch', 9990000, 8990000, 'mh27-9.jpg'),

(5, N'Màn hình MSI PRO MP273L E14 27 inch IPS 144Hz', '27 inch', 3290000, 2590000, 'mh27-10.jpg'),

(5, N'Màn hình E-DRA EGM27Q165R 27 inch IPS 2K 165Hz', '27 inch', 3990000, 3590000, 'mh27-11.jpg');

INSERT INTO products (category_id, name, screen, old_price, new_price, image)
VALUES
(5, N'Màn hình Asus ROG Swift PG27AQWP-W 27" WOLED 2K 540Hz', '27 inch', 39990000, 31990000, 'oled1.jpg'),

(5, N'Màn hình Asus ROG Strix XG27ACDMS 27" QD-OLED 2K 280Hz', '27 inch', 19990000, 15990000, 'oled2.jpg'),

(5, N'Màn hình LG 27GX704A-B UltraGear 27" OLED 2K 240Hz', '27 inch', 18990000, 15990000, 'oled3.jpg'),

(5, N'Màn hình MSI MAG 272QPW QD-OLED X28 27" 280Hz', '27 inch', 24990000, 16490000, 'oled4.jpg'),

(5, N'Màn hình MSI MAG 273QP QD-OLED X24 27" 240Hz', '27 inch', 23990000, 14990000, 'oled5.jpg'),

(5, N'Màn hình Gigabyte MO27Q28G 27" WOLED 2K 280Hz', '27 inch', 24990000, 16490000, 'oled6.jpg'),

(5, N'Màn hình Asus ROG Strix XG27AQDMG 27" WOLED 2K 240Hz', '27 inch', 29990000, 15590000, 'oled7.jpg'),

(5, N'Màn hình LG 39GX90SA-W 39" OLED 240Hz', '39 inch', 33990000, 30990000, 'oled8.jpg'),

(5, N'Màn hình LG 45GX950A-B UltraGear 45" OLED 4K 165Hz', '45 inch', 54990000, 51990000, 'oled9.jpg'),

(5, N'Màn hình Asus ROG Strix XG32UCWMG 32" OLED 4K 240Hz', '32 inch', 37990000, 29790000, 'oled10.jpg'),

(5, N'Màn hình MSI MAG 272QP QD-OLED X50 27" 500Hz', '27 inch', 26990000, 23990000, 'oled11.jpg'),

(5, N'Màn hình LG 32GX870A-B UltraGear 32" OLED 4K 240Hz', '32 inch', 32990000, 27990000, 'oled12.jpg');

DELETE FROM products
WHERE category_id = 5
AND name LIKE N'%OLED%';

-- 5. GÁN CHO TOÀN BỘ PRODUCT
INSERT INTO product_discounts (product_id, discount_id)
SELECT id, @discountId FROM products;

SELECT * FROM complaints;
INSERT INTO complaints (email, order_id, product_id, title, content)
VALUES 
('a@gmail.com', 1, 1, N'Sản phẩm lỗi', N'Sản phẩm bị hỏng sau khi sử dụng'),
('a@gmail.com', 2, 2, N'Giao hàng chậm', N'Đơn hàng giao trễ hơn dự kiến'),
('b@gmail.com', 3, 3, N'Sai sản phẩm', N'Nhận sai sản phẩm đã đặt'),
('b@gmail.com', 4, 4, N'Thiếu phụ kiện', N'Sản phẩm thiếu phụ kiện'),
('c@gmail.com', 1, 5, N'Chất lượng kém', N'Sản phẩm không như quảng cáo'),
('c@gmail.com', 2, 6, N'Không hoạt động', N'Sản phẩm không khởi động được'),
('d@gmail.com', 3, 7, N'Đóng gói kém', N'Hộp bị móp méo'),
('d@gmail.com', 4, 8, N'Sai màu', N'Màu không đúng mô tả'),
('a@gmail.com', 1, 9, N'Chưa nhận hàng', N'Đã thanh toán nhưng chưa nhận'),
('b@gmail.com', 2, 10, N'Yêu cầu đổi trả', N'Muốn đổi sản phẩm khác');
