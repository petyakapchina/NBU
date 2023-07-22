<?php require_once("user_dto.php");

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'];
    $password = $_POST['password'];
    $user = new UserDTO($username, $password);

    $user->login();

    if (session_status() <> PHP_SESSION_ACTIVE) {
        session_start();
    }

    if (!$user->getError()) {
        $_SESSION['user'] =  $username;
        header('Location: ../dashboards/dashboard.php');
        exit;
    }
    $_SESSION["error"] = "Invalid combination from username and password!";
    header('Location: login.php');
}
