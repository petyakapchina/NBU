<?php require_once("question_dto.php");
require_once("C:/xampp/htdocs/petya/answers/answer_service.php");
require_once("C:/xampp/htdocs/petya/constants.php");


if (session_status() <> PHP_SESSION_ACTIVE) {
    session_start();
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {


    $question = $_POST["question"];
    $id = $_POST["question_id"];
    $answers = $_POST["answers"];
    $correctAnswers = $_POST["correct_answers"];

    $test_id = $_SESSION['created_test_id'];

    $service = new QuestionService();
    $service->save($id, $test_id, $question);

    $answer_service = new AnswerService();

    foreach ($answers as $index => $answer) {
        $isCorrect = in_array($index, $correctAnswers);
        $answer_service->save($index, $id, $answer, $isCorrect, $test_id);
    }

    header('Location: ../tests/load/test.php?id=' . $test_id);
}


class QuestionService
{

    public function save($id, $test_id, $question)
    {
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = "INSERT INTO questions (question_id, test_id, text) VALUES(?, ?, ?)";
        $stmt = $connection->prepare($query);
        $stmt->bind_param("sss", $id, $test_id, $question);

        $stmt->execute();

        $stmt->close();
        $connection->close();
    }

    public function load_questions_for_test($test_id)
    {
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = "SELECT question_id, text, test_id FROM questions WHERE test_id=?";
        $stmt = $connection->prepare($query);
        $stmt->bind_param("s", $test_id);

        $stmt->execute();
        $result = $stmt->get_result();

        $questions = array();

        if ($result) {
            $answer_service = new AnswerService();

            while ($row = $result->fetch_assoc()) {
                $id = $row['test_id'];
                $text = $row['text'];
                $question_id = $row['question_id'];
                $question = new QuestionDTO();
                $question->setTestId($id);
                $question->setQuestion($text);
                $answers = $answer_service->load_answers_for_question($question_id, $test_id);
                $question->setAnswers($answers);
                $questions[] = $question;
            }
            $result->free();
        }

        $stmt->close();
        $connection->close();

        return $questions;
    }
}
