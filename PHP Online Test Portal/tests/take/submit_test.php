<?php
require_once("../../constants.php");

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $test = $_POST['test_id'];
    $student = $_POST['student'];
    $answers = array();
    foreach ($_POST as $key => $value) {
        if (strpos($key, 'answer_') === 0) {
            $formatted = str_replace('answer_', '', $value);
            $answers[] = $formatted;
        }
    }

    $serivce = new EvaluateTestService();
    $serivce->submit_test($test, $student, $answers);

    header('Location: test_end.php');
}


class EvaluateTestService
{

    public function submit_test($test, $user, $answers)
    {

        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }
        foreach ($answers as $a) {
            $pks = explode("_", $a);
            $question_id = $pks[0];
            $answer_id = $pks[1];

            $query = "INSERT INTO student_answers (student_id, question_id, answer_id, test_id) VALUES(?, ?, ?, ?)";
            $stmt = $connection->prepare($query);
            $stmt->bind_param("ssss", $user, $question_id, $answer_id, $test);
            $stmt->execute();

            $stmt->close();
        }


        $connection->close();
    }

    public function is_active($test)
    {

        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = "SELECT active FROM tests WHERE test_id=?";
        $stmt = $connection->prepare($query);
        $stmt->bind_param("s", $test);
        $stmt->execute();
        $stmt->bind_result($status);

        $result = false;
        if ($stmt->fetch()) {
            $result = $status;
        }

        $stmt->close();
        $connection->close();

        return $result;
    }
}
