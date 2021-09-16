<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title></title>
    <style>
        body {
            margin: 0;
            /* Reset default margin */
        }

        iframe {
            display: block;
            /* iframes are inline by default */
            background: #000;
            border: none;
            /* Reset default border */
            height: 100vh;
            /* Viewport-relative units */
            width: 100vw;
        }
    </style>
</head>

<body>
    <?php
    include_once '../controllers/user_controller.php';

    $email = $_POST['email'];
    $token = $_POST['token'];
    $password = $_POST['password'];
    $confirm_password = $_POST['confirm_password'];

    if ($email && $token && $password == $confirm_password) {
        $status = (new UserController())->updatePasswordAndToken($token, $email, $password);
        if ($status) {
            http_response_code(200); ?>
            <iframe src="../pages/200.html" frameborder="0" width="100%" height="100%" allowTransparency="true" scrolling="no"></iframe>
        <?php  } else {
            http_response_code(404); ?>
            <iframe src="../pages/404.html" frameborder="0" width="100%" height="100%" allowTransparency="true" scrolling="no"></iframe>
        <?php }
    } else {
        http_response_code(404); ?>
        <iframe src="../pages/404.html" frameborder="0" width="100%" height="100%" allowTransparency="true" scrolling="no"></iframe>
    <?php
    }
    ?>
</body>

</html>