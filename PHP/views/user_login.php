<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
$data = json_decode(file_get_contents("php://input"));

include_once '../controllers/user_controller.php';
include_once '../configs/core.php';
include_once '../libs/php-jwt-master/src/BeforeValidException.php';
include_once '../libs/php-jwt-master/src/SignatureInvalidException.php';
include_once '../libs/php-jwt-master/src/JWT.php';

use Firebase\JWT\JWT;

if ($data->email && $data->password) {
    $status = (new UserController())->login($data->email, $data->password);
    if ($status) {
        $token = array(
            "id" => $status->getID(),
            "email" => $status->getEmail(),
            "avatar" => $status->getAvatar(),
            "fullname" => $status->getFullname(),
        );
        http_response_code(200);
        $access_token = JWT::encode($token, $key);
        if ($status->getEmail() == 'admin') {
            echo json_encode(array(
                'role' => 0,
                "access_token" => $access_token
            ));
        } else {
            echo json_encode(array(
                'role' => 1,
                "access_token" => $access_token
            ));
        }
    } else {
        http_response_code(401);
    }
} else {
    http_response_code(401);
}
