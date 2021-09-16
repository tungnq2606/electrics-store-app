<?php
include_once '../models/category.php';
include_once '../services/category_services.php';

class CategoryController
{
    private $category_services;

    // create constructor

    public function __construct()
    {
        $this->category_services = new CategoryServices();
    }

    // get category list from database

    public function getCategory()
    {
        return $this->category_services->getCategory();
    }

    // insert new category

    public function addCategory($category_name, $image_url)
    {
        return $this->category_services->addCategory($category_name, $image_url);
    }

    // update category

    public function updateCategory($id, $category_name, $image_url)
    {
        $category = $this->category_services->getCategoryByID($id);
        if ($category) {
            return $this->category_services->updateCategory($id, $category_name, $image_url);
        }
    }

    // delete category

    public function deleteCategory($id)
    {
        $category = $this->category_services->getCategoryByID($id);
        if ($category) {
            return $this->category_services->deleteCategory($id);
        }
        return false;
    }
}
