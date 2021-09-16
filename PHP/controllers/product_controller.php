<?php
include_once '../models/category.php';
include_once '../services/product_services.php';
include_once '../services/category_services.php';

class ProductController
{
    private $product_services;
    private $category_services;

    // create constructor

    public function __construct()
    {
        $this->product_services = new ProductService();
        $this->category_services = new CategoryServices();
    }

    // get product list from database

    public function getProducts()
    {
        return $this->product_services->getProducts();
    }

    // add product

    public function addProduct($product_name, $price, $image_url, $category_name)
    {
        $category_id = $this->category_services->getIDCategoryByName($category_name);
        if ($category_id) {
            return $this->product_services->addProduct($product_name, $price, $image_url, $category_id);
        }
        return false;
    }

    // update product

    public function updateProduct($id, $product_name, $price, $image_url, $category_name)
    {
        $product = $this->product_services->getProductByID($id);
        $category_id = $this->category_services->getIDCategoryByName($category_name);
        if ($product && $category_id) {
            return $this->product_services->updateProduct($id, $product_name, $price, $image_url, $category_id);
        }
        return false;
    }

    // delete product

    public function deleteProduct($id)
    {
        $product = $this->product_services->getProductByID($id);
        if ($product) {
            return $this->product_services->deleteProduct($id);
        }
        return false;
    }

    // add product to cart

    public function addToCart($user_id, $product_id)
    {
        return  $this->product_services->addToCart($user_id, $product_id);
    }

    // get cart by idnSupported

    public function getCartByID($user_id)
    {
        return $this->product_services->getCartByID($user_id);
    }
}
