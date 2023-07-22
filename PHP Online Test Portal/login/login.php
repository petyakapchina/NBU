<?php
if (session_status() <> PHP_SESSION_ACTIVE) {
    session_start();
}
?>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="./css/main.css">
</head>

<body>
    <div class="container">
        <div class="row min-vh-100 justify-content-center align-items-center">
            <div class="col-lg-5">
                <?php if (isset($_SESSION["error"])) : ?>
                    <div class='alert alert-danger'><?php echo $_SESSION["error"]; ?></div>
                <?php endif; ?>
                <div class="form-wrap border rounded p-4">
                    <form method="post" action="login_service.php">
                        <h1>Log In</h1>
                        <div class="mb-3">
                            <label for="user_login" class="form-label">Username</label>
                            <input type="text" class="form-control" name="username" id="username" required>
                        </div>
                        <div class="mb-2">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" class="form-control" name="password" id="password" required>
                        </div>
                        <div class="mb-3">
                            <input type="submit" class="btn btn-primary form-control" name="submit" value="Log In">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>

</html>

<?php
unset($_SESSION["error"]);
?>