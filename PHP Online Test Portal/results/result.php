<?php

if (session_status() <> PHP_SESSION_ACTIVE) {
    session_start();
}

// Check if user is not logged in
if (!isset($_SESSION['loggedin']) || $_SESSION['loggedin'] !== true) {
    header('Location: ../login/login.php'); // Redirect to the login page
    exit;
}

require_once 'results_service.php';


$id = $_GET['id'];
$service = new ResultService();
$results = $service->load_result($id);

?>


<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="./css/main.css">
</head>

<body>
    <div class="container">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container">
                <a class="navbar-brand" href="/petya/dashboards/dashboard.php">Dashboard</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <div class="navbar-nav ml-auto"> <!-- Add new div for right alignment -->
                        <button onclick="logout()" class="btn btn-outline-info">Log Out</button>
                    </div>
                </div>
            </div>
        </nav>
    </div>
    <div class="container mt-5">
        <?php if (!empty($results)) {
            // Group the ResultDTO objects by student
            $resultByStudent = [];

            $testTitle = $results[0]->getTestTitle();
            echo "<h3>Test Title: $testTitle</h3>";

            foreach ($results as $resultDTO) {
                $studentName = $resultDTO->getStudentName();
                if (!isset($resultByStudent[$studentName])) {
                    $resultByStudent[$studentName] = [];
                }
                $resultByStudent[$studentName][] = $resultDTO;
            }

            // Display the result in separate tables for each student
            foreach ($resultByStudent as $studentName => $studentResults) {
                $correct = 0;
                $all = 0;
        ?>
                <h4>Student: <?php echo $studentName; ?></h4>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Question</th>
                            <th>Selected Answer</th>
                            <th>Correct Answer</th>
                            <th>Correctness</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($studentResults as $resultDTO) {
                            $all++;
                            if ($resultDTO->getIsCorrect()) {
                                $correct++;
                            }
                        ?>
                            <tr>
                                <td><?php echo $resultDTO->getQuestion(); ?></td>
                                <td><?php echo $resultDTO->getSelectedAnswer(); ?></td>
                                <td><?php echo $resultDTO->getCorrectAnswer(); ?></td>
                                <td><?php echo $resultDTO->getIsCorrect() ? "Correct" : "Incorrect"; ?></td>
                            </tr>
                        <?php } ?>
                    </tbody>
                </table>
                <p>Total: <?php echo $correct; ?> out of <?php echo $all; ?></p>
        <?php
            }
        } ?>
    </div>

</body>

</html>