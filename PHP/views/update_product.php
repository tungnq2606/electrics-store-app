<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
$data = json_decode(file_get_contents("php://input"));

include_once '../controllers/product_controller.php';

$status = (new ProductController())->updateProduct($data->id, $data->product_name, $data->price, $data->image_url, $data->category_name);

if ($status) {
    http_response_code(200);
    echo 'Success!';
} else {
    http_response_code(401);
    echo 'Failed!';
}
