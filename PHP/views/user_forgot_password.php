<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

$data = json_decode(file_get_contents("php://input"));

include_once '../controllers/user_controller.php';

if ($data->email) {
    $status = (new UserController())->sendEmailResetPassToUser($data->email);
    echo $status;
    switch ($status) {
        case 3:
            http_response_code(401);
            echo "You can send email after 10 minutes";
            break;
        case 1:
            http_response_code(200);
            echo "Success";
            break;
        case 2:
            http_response_code(404);
            echo "Not found";
            break;
    }
} else {
    http_response_code(404);
    echo "Email is valid";
}
