CREATE DATABASE IF NOT EXISTS `SHOPPING`;
USE `SHOPPING`;
CREATE TABLE IF NOT EXISTS `tblUsers`(
    `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `avatar` VARCHAR(255) DEFAULT '',
    `fullname` VARCHAR(255) NOT NULL,
    `hash_password` VARCHAR(60) NOT NULL
);
CREATE TABLE IF NOT EXISTS `tblPasswordResets`(
    `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(255) NOT NULL,
    `token` VARCHAR(255) NOT NULL UNIQUE,
    `created` DATETIME NOT NULL DEFAULT NOW(),
    `available` BIT NOT NULL DEFAULT 1
);
CREATE TABLE IF NOT EXISTS `tblCategories`(
    `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `category_name` VARCHAR(255) NOT NULL,
    `image_url` VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS `tblProducts`(
    `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_name` VARCHAR(255) NOT NULL DEFAULT '',
    `price` DECIMAL(5, 2) NOT NULL,
    `image_url` VARCHAR(255) NOT NULL DEFAULT '',
    `category_id` INT(11) NOT NULL,
    FOREIGN KEY(`category_id`) REFERENCES tblCategories(`id`)
);
CREATE TABLE IF NOT EXISTS `tblCarts` (
    `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT(11) NOT NULL,
    `product_id` INT(11) NOT NULL,
    `quantity` INT(5) NOT NULL DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES `tblUsers` (id),
    FOREIGN KEY (product_id) REFERENCES `tblProducts` (id)
)
SELECT tblcarts.id,
    tblproducts.product_name,
    tblproducts.price,
    tblproducts.image_url,
    tblcarts.quantity
FROM tblcarts
    JOIN tblproducts
    JOIN tblusers ON tblusers.id = tblcarts.user_id
    AND tblproducts.id = tblcarts.product_id
WHERE tblusers.id = 2