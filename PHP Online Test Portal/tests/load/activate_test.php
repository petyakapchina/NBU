<?php

require_once 'test_load_service.php';

// Create an instance of the class
$myClass = new TestService();

$active = $_POST['active'];
$test_id = $_POST['test_id'];
$myClass->manageTest($active, $test_id);
