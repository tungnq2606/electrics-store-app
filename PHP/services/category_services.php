<?php
include_once '../configs/database_config.php';
include_once '../models/category.php';

class CategoryServices
{
    private $connection;
    // create constructor
    public function __construct()
    {
        $this->connection = (new DatabaseConfig())->getConnection();
    }

    // get category list from database

    public function getCategory()
    {
        try {
            $query = "SELECT id, category_name, image_url FROM tblcategories";
            $stmt = $this->connection->prepare($query);

            $stmt->execute();
            if ($stmt->rowCount() > 0) {
                $categories = array();
                while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                    extract($row);
                    $category = array(
                        "id" => $id,
                        "category_name" => $category_name,
                        "image_url" => $image_url,
                    );
                    array_push($categories, $category);
                }

                return $categories;
            }
        } catch (Exception $e) {
        }
        return null;
    }

    // insert new category

    public function addCategory($category_name, $image_url)
    {
        try {
            $query = "INSERT INTO tblcategories SET category_name = ?, image_url = ? ";
            $stmt = $this->connection->prepare($query);


            $stmt->bindParam(1, $category_name);
            $stmt->bindParam(2, $image_url);


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

    public function getCategoryByID($id)
    {
        try {
            $query = "SELECT id, category_name, image_url FROM tblcategories WHERE id = ? LIMIT 0,1";
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

    public function updateCategory($id, $category_name, $image_url)
    {

        try {
            $query = "UPDATE tblcategories SET category_name = :category_name, image_url = :image_url WHERE id = :id";
            $stmt = $this->connection->prepare($query);
            // 
            $stmt->bindParam("category_name", $category_name);
            $stmt->bindParam("image_url", $image_url);
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

    // delete category

    public function deleteCategory($id)
    {
        try {
            $query = "DELETE FROM tblcategories WHERE id = :id";
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

    public function getIDCategoryByName($category_name)
    {

        try {
            $query = "SELECT id FROM tblcategories WHERE category_name = ? LIMIT 0,1";
            $stmt = $this->connection->prepare($query);

            $stmt->bindParam(1, $category_name);
            $stmt->execute();
            if ($stmt->rowCount() > 0) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                extract($row);
                return $row["id"];
            }
        } catch (Exception $e) {
        }
        return null;
    }
}
