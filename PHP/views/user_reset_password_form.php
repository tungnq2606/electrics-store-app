<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset password</title>
    <link rel="stylesheet" href="../pages//resetPassword.css" />
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
    $key = $_GET['key'];
    $token = $_GET['token'];
    if ($key && $token) {
        include_once '../controllers/user_controller.php';
        $reset_token = (new UserController())->getByToken($token, $key);
        if ($reset_token) { ?>
            <div class="contact-wrapper">
                <header class="login-cta">
                    <h2>Reset Password</h2>
                </header>
                <form method="POST" action="user_reset_password.php">
                    <input type="hidden" name="email" value="<?php echo $key; ?>">
                    <input type="hidden" name="token" value="<?php echo $token; ?>">
                    <div class="form-row">
                        <input type="password" required name="password" />
                        <span>Password</span>
                    </div>
                    <div class="form-row">
                        <input type="password" required name="confirm_password" />
                        <span>Confirm password</span>
                    </div>
                    <div class="form-row"></div>
                    <div class="form-row">
                        <input type="submit" value="Rest your password" id="button"></input>
                    </div>
                </form>
</body>
<?php } else { ?>
    <iframe src="../pages/404.html" frameborder="0" width="100%" height="100%" allowTransparency="true" scrolling="no"></iframe>
<?php }
    } ?>

</body>

</html>