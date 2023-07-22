<?php require_once '../constants.php';

class UserDTO
{
    private $username;
    private $password;
    private $error;

    public function __construct($username, $password)
    {
        $this->username = $username;
        $this->password = $password;
    }

    public function setError($error)
    {
        $this->error = $error;
    }

    public function getError()
    {
        return $this->error;
    }

    public function login()
    {
        // Connect to the database
        $connection = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

        // Check connection
        if ($connection->connect_error) {
            die("Connection failed: " . $connection->connect_error);
        }

        $encoded_password = md5($this->password);
        $query = "SELECT * FROM users WHERE username=? AND password=?";
        $stmt = $connection->prepare($query);
        $stmt->bind_param("ss", $this->username, $encoded_password);

        $stmt->execute();
        $result = $stmt->get_result();

        $stmt->close();
        $connection->close();

        if ($result->num_rows == 1) {
            session_start();
            $_SESSION['loggedin'] = true;
        } else {
            $this->error = true;
        }
    }
}
