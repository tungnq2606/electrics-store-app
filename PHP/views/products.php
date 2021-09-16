<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
include_once '../controllers/product_controller.php';

$product = (new ProductController())->getProducts();

if ($product) {
    http_response_code(200);
    echo json_encode($product);
} else {
    http_response_code(401);
    echo 'Failed';
}
