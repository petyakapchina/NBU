<?php
if (session_status() <> PHP_SESSION_ACTIVE) {
    session_start();
}

// Check if user is not logged in
if (!isset($_SESSION['loggedin']) || $_SESSION['loggedin'] !== true) {
    header('Location: ../login/login.php'); // Redirect to the login page
    exit;
}

require_once("dashboard_service.php");

$service = new DashboardService();
$templates = $service->load_available_templates();
$tests = $service->load_submitted_tests();
?>


<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="./css/main.css">
</head>

<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <a class="navbar-brand" href="/petya/dashboards/dashboard.php">Dashboard</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <div class="navbar-nav ml-auto"> <!-- Add new div for right alignment -->
                    <button onclick="createTest()" class="btn btn-outline-success">Create new Test</button>
                    <button onclick="logout()" class="btn btn-outline-info">Log Out</button>
                </div>
            </div>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="row">
            <div class="col-md-6">
                <h3>Available Test Templates</h3>
                <table class="table">
                    <tbody>
                        <?php foreach ($templates as $test) : ?>
                            <tr>
                                <td><a href="/petya/tests/load/test.php?id=<?php echo $test->getId(); ?>"><?= $test->getTitle(); ?></a></td>
                            </tr>
                        <?php endforeach; ?>
                    </tbody>
                </table>
            </div>
            <div class="col-md-6">
                <h3>Submitted Tests</h3>
                <table class="table">
                    <tbody>
                        <?php foreach ($tests as $test) : ?>
                            <tr>
                                <td><a href="/petya/results/result.php?id=<?php echo $test->getId(); ?>"><?= $test->getTitle(); ?></a></td>
                            </tr>
                        <?php endforeach; ?>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        function createTest() {
            window.location.href = "../tests/create/test.php";
        }

        function logout() {
            window.location.href = "../logout/logout.php";
        }
    </script>

</body>

</html>