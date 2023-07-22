<?php
if (session_status() <> PHP_SESSION_ACTIVE) {
    session_start();
}

// Check if user is not logged in
if (!isset($_SESSION['loggedin']) || $_SESSION['loggedin'] !== true) {
    header('Location: ../login/login.php'); // Redirect to the login page
    exit;
}

require_once("submit_test.php");

$id = $_GET['id'];
$service = new EvaluateTestService();
$isActive = $service->is_active($id);
if (!$isActive) {
    echo "<h1 style='color: red;'>Page that you are searching for is not reachable.</h1>";
    exit();
}

?>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Take Test</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="./css/main.css" />
</head>

<body>

    <div class="form-wrap border rounded p-4">
        <form method="post" action="student_service.php">
            <h1>Before starting the test, please enter</h1>
            <div class="mb-3">
                <label for="number" class="form-label">Number</label>
                <input type="text" class="form-control" name="number" id="number" required>
            </div>
            <div class="mb-2">
                <label for="name" class="form-label">First and Last Names</label>
                <input type="text" class="form-control" name="name" id="name" required>
            </div>
            <input type="hidden" class="form-control" name="test_id" id="test_id" value="<?php echo $id ?>">
            <div class="mb-3">
                <input type="submit" class="btn btn-primary form-control" name="submit" value="Start the test">
            </div>
        </form>
    </div>

</body>

</html>