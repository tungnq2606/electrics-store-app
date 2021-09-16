<?php
include_once '../services/user_service.php';
include_once '../models/user.php';
include_once '../libs/PHPMailer-master/src/PHPMailer.php';
include_once '../libs/PHPMailer-master/src/SMTP.php';
include_once '../libs/PHPMailer-master/src/Exception.php';

use Firebase\JWT\JWT;
use PHPMailer\PHPMailer\PHPMailer;

class UserController
{

    private $user_service;

    // create constructor

    public function __construct()
    {
        $this->user_service = new UserService();
    }

    // login 

    public function login($email, $password)
    {
        $user = $this->user_service->getUserByEmail($email);
        if ($user) {
            $password_valid = password_verify($password, $user->getHashPassword());
            if ($password_valid) {
                return $user;
            }
        }
        return null;
    }

    // register new user

    public function register($email, $password, $fullname)
    {
        $user = $this->user_service->getUserByEmail($email);
        if (!$user) {
            $hash_password = password_hash($password, PASSWORD_BCRYPT);
            $new_user = new User(1, $email, null, $fullname, $hash_password);
            $status = $this->user_service->register($new_user);
            if ($status) {
                return true;
            }
        }
        return false;
    }

    // change password 

    public function changePassword($email, $password, $newPassword)
    {
        $hash_password = password_hash($newPassword, PASSWORD_BCRYPT);
        $user = $this->user_service->getUserByEmail($email);
        if ($user) {
            $password_valid = password_verify($password, $user->getHashPassword());
            if ($password_valid) {
                $status = $this->user_service->changePassword($email, $hash_password);
                if ($status) {
                    return true;
                }
            }
        }
        return false;
    }

    // send email reset password

    public function sendEmailResetPassToUser($email)
    {
        $_email = $this->user_service->getUserByEmail($email);
        if ($_email) {
            $count = $this->user_service->getCountTokenByEmail($email);
            if ($count == 0) {
                $token = $this->user_service->generateResetToken($email);
                if ($token) {
                    // gui email
                    if ($this->sendEmail($email, $token)) return 1;
                }
            } else {
                return 3;
            }
        }
        return 2;
    }

    // send email 

    private function sendEmail($email, $token)
    {
        $link = "<a href='http://127.0.0.1:8081/views/user_reset_password_form.php?key="
            . $email . "&token=" . $token . "'><br /><br />Reset password</strong></a>";
        $mail = new PHPMailer();

        $mail->CharSet = "utf-8";
        $mail->isSMTP();
        $mail->SMTPAuth = true;
        $mail->Username = "quoctungdev.test";
        $mail->Password = "26062000Aa";
        $mail->SMTPSecure = "ssl";
        $mail->Host = "ssl://smtp.gmail.com";
        $mail->Port = "465";
        $mail->From = "quoctungdev.test@gmail.com";
        $mail->FromName = "Ngô Quốc Tùng";
        $mail->addAddress($email, 'Hello');
        $mail->Subject = "Reset Password";
        $mail->isHTML(true);
        $mail->Body = "<h3>Hello " . $email . "</h3><span>To continue reset password, 
        please click to this link</span>" . $link;

        if ($mail->Send()) {
            return 1;
        }
        return 2;
        // https://www.google.com/settings/security/lesssecureapps
    }

    // get user by token

    public function getByToken($token, $email)
    {
        return $this->user_service->getByToken($token, $email);
    }

    // update password and clear token

    public function updatePasswordAndToken($token, $email, $password)
    {
        $reset_token = $this->user_service->getByToken($token, $email);
        if ($reset_token) {
            $hash_password = password_hash($password, PASSWORD_BCRYPT);
            $change_password = $this->user_service->changePassword($email, $hash_password);
            if ($change_password) {
                return $this->user_service->clearToken($email);
            }
        }
        return false;
    }
}
