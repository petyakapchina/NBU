<?php
require_once("answer_dto.php");


class AnswerService
{

    public function save($index, $question_id, $text, $correct, $test_id)
    {
        // Connect to the database
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        // Check connection
        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = "INSERT INTO answers (answer_id, question_id, text, correct, test_id) VALUES(?, ?, ?, ?, ?)";
        $stmt = $connection->prepare($query);
        $stmt->bind_param("sssss", $index, $question_id, $text, $correct, $test_id);

        $stmt->execute();
        $connection->close();
    }

    public function load_answers_for_question($question_id, $test_id)
    {
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = "SELECT a.answer_id, a.text, a.correct, a.question_id FROM answers AS a WHERE a.question_id=? AND a.test_id=?";
        $stmt = $connection->prepare($query);
        $stmt->bind_param("ss", $question_id, $test_id);

        $stmt->execute();
        $result = $stmt->get_result();

        $answers = array();

        if ($result) {
            while ($row = $result->fetch_assoc()) {
                $correct = $row['correct'];
                $text = $row['text'];
                $id = $row['answer_id'];
                $answer = new AnswerDTO();
                $answer->setAnswer($text);
                $answer->setCorrect($correct);
                $answer->setAnswerId($id);
                $answer->setQuestionId($question_id);
                $answers[] = $answer;
            }
            $result->free();
        }

        $stmt->close();
        $connection->close();

        return $answers;
    }
}
