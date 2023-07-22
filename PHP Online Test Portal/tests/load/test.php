<?php

if (session_status() <> PHP_SESSION_ACTIVE) {
    session_start();
}

// Check if user is not logged in
if (!isset($_SESSION['loggedin']) || $_SESSION['loggedin'] !== true) {
    header('Location: ../login/login.php'); // Redirect to the login page
    exit;
}

require_once 'test_load_service.php';
require_once '../../questions/question_service.php';

$service = new TestService();

$id = $_GET['id'];
$test = $service->loadtest($id);

$question_service = new QuestionService();
$questions = $question_service->load_questions_for_test($id);

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
        <button onclick="addQuestion()" class="btn btn-primary">Add Question</button>

        <br />

        <table class="table">
            <tr>
                <td>Title</td>
                <td colspan="2"><?php echo $test->getTitle(); ?></td>
            </tr>
            <tr>
                <td>Active</td>
                <td>
                    <?php if ($test->getActive()) : ?>
                        <span class="text-success">&#x2714;</span>
                        <?php $text = "Disable Test";?>
                        <?php $color = "btn-secondary";?>
                    <?php else : ?>
                        <span class="text-danger">&#x2718;</span>
                        <?php $text = "Enable Test";?>
                        <?php $color = "btn-success";?>
                    <?php endif; ?>
                </td>
                <td>
                    <form action="activate_test.php" method="post">
                        <input type="hidden" name="active" value="<?php echo $test->getActive(); ?>">
                        <input type="hidden" name="test_id" value="<?php echo $id; ?>">
                        <input type="submit" value="<?php echo $text; ?>" class="btn <?php echo $color; ?>">
                    </form>
                </td>
            </tr>
            <tr>
                <td>Duration</td>
                <td colspan="2"><?php echo $test->getDuration(); ?> mins</td>
            </tr>
            <?php if ($test->getActive()) : ?>
                <tr>
                    <td>Access</td>
                    <td colspan="2"><?php echo $_SERVER['HTTP_HOST'] . "/petya/tests/take/student.php?id=" . $id; ?></td>
                </tr>
            <?php endif; ?>
        </table>

        <table class="table">
            <h3>Questions</h3>
            <?php foreach ($questions as $i => $q) : ?>
                <tr>
                    <td><?php echo $q->getQuestion(); ?></td>
                </tr>
                <tr>
                    <td>
                        <ul>
                            <?php foreach ($q->getAnswers() as $a) : ?>
                                <li>
                                    <?php echo $a->getAnswer(); ?>
                                    <?php if ($a->isCorrect()) : ?>
                                        <span class="text-success">&#x2714;</span>
                                    <?php endif; ?>
                                </li>
                            <?php endforeach; ?>
                        </ul>
                    </td>
                </tr>
            <?php endforeach; ?>
        </table>
    </div>

</body>

<script>
    function addQuestion() {
        window.location.href = "../../questions/question.php?index=<?php echo count($questions); ?>";
    }

    function logout() {
        window.location.href = "../logout/logout.php";
    }
</script>

</html>