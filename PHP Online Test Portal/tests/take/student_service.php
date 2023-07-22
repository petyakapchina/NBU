<?php require_once("../../constants.php");

if (session_status() <> PHP_SESSION_ACTIVE) {
    session_start();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $number = $_POST['number'];
    $name = $_POST['name'];
    $test_id = $_POST['test_id'];

    $student_service = new StudentService();
    $cheater = $student_service->create_if_new($number, $name, $test_id);

    if ($cheater) {
        echo "<h2 style='color:red;'>You have already taken this test!</h2>";
        exit();
    }

    $_SESSION["student"] = $number;
    header('Location: test.php?id=' . $test_id);
}


class StudentService
{
    public function create_if_new($number, $name, $test)
    {
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = "SELECT * FROM students WHERE student_id=?";
        $stmt = $connection->prepare($query);

        $stmt->bind_param("s", $number);

        $stmt->execute();
        $result = $stmt->get_result();
        $cheater = false;

        if ($result->num_rows == 0) {
            $insert = "INSERT INTO students (student_id, name) VALUES(?, ?)";
            $stmt = $connection->prepare($insert);

            $stmt->bind_param("ss", $number, $name);

            $stmt->execute();
            $stmt->close();
        } else {
            $insert = "SELECT * FROM student_answers WHERE student_id=? AND test_id=?";
            $stmt = $connection->prepare($insert);

            $stmt->bind_param("ss", $number, $test);

            $stmt->execute();
            $matches = $stmt->get_result();
            $cheater = ($matches->num_rows > 0);

            $stmt->close();
        }

        $connection->close();
        return $cheater;
    }
}
