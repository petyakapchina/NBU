<?php require_once("../test_dto.php");

require_once("../../constants.php");

if (session_status() <> PHP_SESSION_ACTIVE) {
    session_start();
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {

    $title = $_POST["title"];
    $duration = $_POST["duration"];

    $service = new TestCreateService();
    $test_id = $service->save($title, $duration);

    $_SESSION['created_test_id'] = $test_id;

    header('Location: ../load/test.php?id=' . $test_id);

    exit;
}


class TestCreateService
{

    public function save($title, $duration)
    {
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $query = "INSERT INTO tests (title, duration, active, user) VALUES(?, ?, ?, ?)";
        $stmt = $connection->prepare($query);

        $active = false;
        $user = $_SESSION['user'];

        $stmt->bind_param("ssss", $title, $duration, $active, $user);

        $stmt->execute();
        $generatedId = $stmt->insert_id;

        $stmt->close();
        $connection->close();

        return $generatedId;
    }
}
