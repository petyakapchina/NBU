<?php

require_once("../constants.php");
require_once("../tests/test_dto.php");

class DashboardService
{
    public function load_available_templates()
    {
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = "SELECT test_id, title FROM tests WHERE user = ?";
        $stmt = $connection->prepare($query);

        $user = $_SESSION['user'];
        $stmt->bind_param("s", $user);

        $stmt->execute();
        $result = $stmt->get_result();

        $tests = array();

        if ($result) {
            while ($row = $result->fetch_assoc()) {
                $id = $row['test_id'];
                $title = $row['title'];
                $test = new TestDTO();
                $test->setId($id);
                $test->setTitle($title);
                $tests[] = $test;
            }
            $result->free();
        }


        $stmt->close();
        $connection->close();

        return $tests;
    }

    public function load_submitted_tests()
    {
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = "SELECT DISTINCT(s.test_id), t.title FROM tests as t JOIN student_answers as s ON s.test_id=t.test_id WHERE t.user = ?";
        $stmt = $connection->prepare($query);

        $user = $_SESSION['user'];
        $stmt->bind_param("s", $user);

        $stmt->execute();
        $result = $stmt->get_result();

        $tests = array();

        if ($result) {
            while ($row = $result->fetch_assoc()) {
                $id = $row['test_id'];
                $title = $row['title'];
                $test = new TestDTO();
                $test->setId($id);
                $test->setTitle($title);
                $tests[] = $test;
            }
            $result->free();
        }


        $stmt->close();
        $connection->close();

        return $tests;
    }
}
