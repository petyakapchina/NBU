<?php

$routes = [
    '/login' => 'loginHandler',
    '/dashboard' => 'dashboardHandler',
    '/tests/create' => 'createTestHandler',
    '/tests/load/{id}' => 'loadTestHandler'
];

function loginHandler()
{
    header('Location: login/login.php');
}

function dashboardHandler()
{
    header('Location: dashboards/dashboard.php');
}

function createTestHandler()
{
    header('Location: tests/create/test.php');
}

function loadTestHandler($params)
{
    $test_id = $params['id'];
    header('Location: tests/load/test.php?id={$test_id}');
}
