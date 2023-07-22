<?php require_once '../test_dto.php';
require_once '../../constants.php';


class TestService
{

    public function loadTest($test_id)
    {
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = "SELECT t.title, t.duration, t.active FROM tests as t WHERE t.test_id=?";
        $stmt = $connection->prepare($query);
        $stmt->bind_param("s", $test_id);

        $stmt->execute();

        $test = new TestDTO();

        $stmt->bind_result($title, $duration, $active);

        if ($stmt->fetch()) {

            $test->setTitle($title);
            $test->setDuration($duration);
            $test->setActive($active);
        }

        $stmt->close();
        $connection->close();

        return $test;
    }

    public function manageTest($active, $test_id)
    {
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = "UPDATE tests SET active=? WHERE test_id=?";
        $stmt = $connection->prepare($query);
        $changed_status = intval(!$active);
        $test_code = intval($test_id);
        $stmt->bind_param("ss", $changed_status, $test_code);

        $stmt->execute();

        $stmt->close();
        $connection->close();

        header('Location: ../load/test.php?id='.$test_code);
    }
}
