<?php
if (session_status() <> PHP_SESSION_ACTIVE) {
    session_start();
}

// Check if user is not logged in
if (!isset($_SESSION['loggedin']) || $_SESSION['loggedin'] !== true) {
    header('Location: ../login/login.php'); // Redirect to the login page
    exit;
}

$id = $_GET['index'];
?>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Question</title>
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
        <h1>Create Question</h1>
        <form action="question_service.php" method="post">
            <div class="mb-3">
                <label for="question" class="form-label">Question:</label>
                <input type="hidden" id="question_id" name="question_id" value="<?php echo $id; ?>" required>
                <input type="text" class="form-control" id="question" name="question" required>
            </div>

            <div class="mb-3">
                <label for="answers" class="form-label">Answers:</label>
                <div id="answer-container">
                    <div class="input-group">
                        <input type="text" class="form-control" name="answers[]" placeholder="Enter an answer" required>
                        <div class="input-group-text">
                            <input type="radio" name="correct_answers[]" value="0">
                        </div>
                    </div>
                </div>
            </div>

            <button type="button" class="btn btn-primary" onclick="addAnswer()">Add Answer</button>

            <div class="mt-3">
                <input type="submit" class="btn btn-success" value="Save Question">
            </div>
        </form>
    </div>


</body>
<script>
    function addAnswer() {
        var answerContainer = document.getElementById("answer-container");

        var inputGroup = document.createElement("div");
        inputGroup.classList.add("input-group");

        var answerInput = document.createElement("input");
        answerInput.type = "text";
        answerInput.classList.add("form-control");
        answerInput.name = "answers[]";
        answerInput.placeholder = "Enter an answer";
        inputGroup.appendChild(answerInput);

        var inputGroupText = document.createElement("div");
        inputGroupText.classList.add("input-group-text");

        var answerCheckbox = document.createElement("input");
        answerCheckbox.type = "radio";
        answerCheckbox.name = "correct_answers[]";
        answerCheckbox.value = answerContainer.children.length;
        inputGroupText.appendChild(answerCheckbox);

        inputGroup.appendChild(inputGroupText);

        answerContainer.appendChild(inputGroup);
    }


    function logout() {
        window.location.href = "../logout/logout.php";
    }
</script>

</html>