<?php

require_once 'result_dto.php';
require_once '../constants.php';

class ResultService
{
    public function load_result($test_id)
    {
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = 'SELECT t.title AS test_title, s.name AS student_name, q.text AS question, a.text AS selected_answer, a.correct AS is_correct, c.text AS correct_answer
        FROM tests t
        INNER JOIN questions q ON t.test_id = q.test_id
        INNER JOIN student_answers sa ON q.question_id = sa.question_id AND q.test_id = sa.test_id
        INNER JOIN answers a ON sa.answer_id = a.answer_id AND sa.question_id = a.question_id AND sa.test_id = a.test_id
        INNER JOIN students s ON sa.student_id = s.student_id
        INNER JOIN answers c ON q.question_id = c.question_id AND q.test_id = c.test_id AND c.correct = 1
        WHERE t.test_id = ?';
        $stmt = $connection->prepare($query);

        $stmt->bind_param("s", $test_id);

        $stmt->execute();
        $result = $stmt->get_result();

        $resultDTOs = [];

        if ($result) {
            while ($row = $result->fetch_assoc()) {
                $resultDTO = new ResultDTO(
                    $row['test_title'],
                    $row['student_name'],
                    $row['question'],
                    $row['selected_answer'],
                    $row['correct_answer'],
                    $row['is_correct']
                );
                $resultDTOs[] = $resultDTO;
            }
            $result->free();
        }


        $stmt->close();
        $connection->close();

        return $resultDTOs;
    }
}
