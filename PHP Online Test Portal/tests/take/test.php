<?php
if (session_status() <> PHP_SESSION_ACTIVE) {
    session_start();
}

// Check if user is not logged in
if (!isset($_SESSION['loggedin']) || $_SESSION['loggedin'] !== true) {
    header('Location: ../login/login.php'); // Redirect to the login page
    exit;
}

require_once '../../questions/question_service.php';
require_once("../load/test_load_service.php");

$service = new TestService();

$id = $_GET['id'];
$test = $service->loadtest($id);

$question_service = new QuestionService();
$questions = $question_service->load_questions_for_test($id);

$student = $_SESSION["student"];
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
    <div class="container mt-5">
        <p class="text-primary"><?php echo $test->getTitle(); ?></p>
        <div class="form-wrap border rounded p-4">
            <form action="submit_test.php" method="post">
                <input type="hidden" name="test_id" value="<?php echo $id; ?>" />
                <input type="hidden" name="student" value="<?php echo $student; ?>" />
                <input type="submit" value="Submit Test" class="btn btn-primary mb-3" />
                <table class="table">
                    <?php foreach ($questions as $q) : ?>
                        <tr>
                            <td><?php echo $q->getQuestion(); ?></td>
                            <td>
                                <table class="table">
                                    <?php foreach ($q->getAnswers() as $a) : ?>
                                        <tr>
                                            <td>
                                                <input type="radio" name="answer_<?php echo $a->getAnwserPK(); ?>" value="<?php echo $a->getAnwserPK(); ?>" id="a_<?php echo $a->getAnwserPK(); ?>" />
                                                <label for="a_<?php echo $a->getAnwserPK(); ?>" class="text-success"><?php echo $a->getAnswer(); ?></label>
                                            </td>
                                        </tr>
                                    <?php endforeach; ?>
                                </table>
                            </td>
                        </tr>
                    <?php endforeach; ?>
                </table>
            </form>
        </div>
    </div>

</body>

</html>