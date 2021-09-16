<?php
include_once '../configs/database_config.php';
include_once '../models/category.php';

class ProductService
{
    private $connection;

    // create constructor

    public function __construct()
    {
        $this->connection = (new DatabaseConfig())->getConnection();
    }

    // get products list from database

    public function getProducts()
    {
        try {
            $query = "SELECT  tblproducts.id, tblproducts.product_name, 
            tblproducts.price,tblproducts.image_url, tblcategories.category_name 
            FROM `tblproducts` JOIN `tblcategories` ON tblproducts.category_id = tblcategories.id";
            $stmt = $this->connection->prepare($query);

            $stmt->execute();
            if ($stmt->rowCount() > 0) {
                $categories = array();
                while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                    extract($row);
                    $category = array(
                        "id" => $id,
                        "product_name" => $product_name,
                        "price" => $price,
                        "image_url" => $image_url,
                        "category_name" => $category_name,
                    );
                    array_push($categories, $category);
                }

                return $categories;
            }
        } catch (Exception $e) {
        }
        return null;
    }

    // add product

    public function addProduct($product_name, $price, $image_url, $category_id)
    {
        try {
            $query = "INSERT INTO tblproducts SET product_name = ?, price = ?, image_url = ?, category_id = ? ";
            $stmt = $this->connection->prepare($query);

            $stmt->bindParam(1, $product_name);
            $stmt->bindParam(2, $price);
            $stmt->bindParam(3, $image_url);
            $stmt->bindParam(4, $category_id);


            $this->connection->beginTransaction();
            if ($stmt->execute()) {
                $this->connection->commit();
                return true;
            } else {
                $this->connection->rollBack();
                return false;
            }
        } catch (Exception $e) {
        }
        return false;
    }

    // get category by id 

    public function getProductByID($id)
    {
        try {
            $query = "SELECT id, product_name, price, image_url, category_id 
            FROM tblproducts WHERE id = ? LIMIT 0,1";
            $stmt = $this->connection->prepare($query);

            $stmt->bindParam(1, $id);
            $stmt->execute();
            if ($stmt->rowCount() > 0) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                extract($row);
                return true;
            }
        } catch (Exception $e) {
        }
        return false;
    }

    // update category

    public function updateProduct($id, $product_name, $price, $image_url, $category_id)
    {

        try {
            $query = "UPDATE tblproducts SET product_name = ?, price = ?, image_url = ?, category_id = ? 
            WHERE id = ?";
            $stmt = $this->connection->prepare($query);
            // 
            $stmt->bindParam(1, $product_name);
            $stmt->bindParam(2, $price);
            $stmt->bindParam(3, $image_url);
            $stmt->bindParam(4, $category_id);
            $stmt->bindParam(5, $id);

            $this->connection->beginTransaction();
            if ($stmt->execute()) {
                $this->connection->commit();
                return true;
            } else {
                $this->connection->rollBack();
                return false;
            }
        } catch (Exception $e) {
        }
        return false;
    }

    // delete product

    public function deleteProduct($id)
    {
        try {
            $query = "DELETE FROM tblproducts WHERE id = :id";
            $stmt = $this->connection->prepare($query);
            // 
            $stmt->bindParam("id", $id);

            $this->connection->beginTransaction();
            if ($stmt->execute()) {
                $this->connection->commit();
                return true;
            } else {
                $this->connection->rollBack();
                return false;
            }
        } catch (Exception $e) {
        }
        return false;
    }

    // add product to cart

    public function addToCart($user_id, $product_id)
    {
        try {
            $query = "INSERT INTO tblcarts SET user_id = ?, product_id = ?";
            $stmt = $this->connection->prepare($query);

            $stmt->bindParam(1, $user_id);
            $stmt->bindParam(2, $product_id);

            $this->connection->beginTransaction();
            if ($stmt->execute()) {
                $this->connection->commit();
                return true;
            } else {
                $this->connection->rollBack();
                return false;
            }
        } catch (Exception $e) {
        }
        return false;
    }

    // get cart by user id

    public function getCartByID($user_id)
    {
        try {
            $query = "SELECT tblcarts.id,
                        tblproducts.product_name,
                        tblproducts.price,
                        tblproducts.image_url,
                        tblcarts.quantity
                        FROM tblcarts
                        JOIN tblproducts
                        JOIN tblusers ON tblusers.id = tblcarts.user_id
                        AND tblproducts.id = tblcarts.product_id
                        WHERE tblcarts.user_id = ? ";
            $stmt = $this->connection->prepare($query);
            $stmt->bindParam(1, $user_id);

            $stmt->execute();
            if ($stmt->rowCount() > 0) {
                $carts = array();
                while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                    extract($row);
                    $cart = array(
                        "id" => $id,
                        "product_name" => $product_name,
                        "price" => $price,
                        "image_url" => $image_url,
                        "quantity" => $quantity,
                    );
                    array_push($carts, $cart);
                }

                return $carts;
            }
        } catch (Exception $e) {
        }
        return null;
    }
}
