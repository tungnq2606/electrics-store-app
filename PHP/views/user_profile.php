<?php
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
$data = json_decode(file_get_contents("php://input"));

include_once '../configs/core.php';
include_once '../libs/php-jwt-master/src/BeforeValidException.php';
include_once '../libs/php-jwt-master/src/SignatureInvalidException.php';
include_once '../libs/php-jwt-master/src/JWT.php';
include_once '../helpers/auth.php';

use Firebase\JWT\JWT;

$access_token = getBearerToken();

if ($access_token) {
    try {
        $decoded = JWT::decode($access_token, $key, array('HS256'));
        http_response_code(200);
        echo json_encode(array(
            "id" => $decoded->id,
            "email" => $decoded->email,
            "avatar" => $decoded->avatar,
            "fullname" => $decoded->fullname
        ));
    } catch (Exception $e) {
        http_response_code(401);
        echo json_encode(array(
            "msg" => "Access denied",
            "error" => $e->getMessage()
        ));
    }
} else {
    http_response_code(401);
    echo json_encode(array("msg" => "Access denied"));
}
