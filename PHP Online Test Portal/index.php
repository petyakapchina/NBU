<?php


require_once 'routes.php';

$requestPath = $_SERVER['REQUEST_URI'];
echo $requestPath;

// $routeFound = false;
// foreach ($routes as $route => $handler) {
//     $regex = '#^' . preg_replace('/:([^/]+)/', '(?<$1>[^/]+)', $route) . '$#';

//     if (preg_match($regex, $requestUrl, $matches)) {
//         $routeFound = true;
//         break;
//     }
// }

// if ($routeFound) {
//     $params = array_filter($matches, 'is_string', ARRAY_FILTER_USE_KEY);

//     list($controllerName, $methodName) = explode('@', $handler);

//     // Include the controller file (if needed) and create an instance
//     // Alternatively, you can use an autoloader to automatically load controllers
//     // require_once 'controllers/' . $controllerName . '.php';
//     // $controller = new $controllerName();

//     // Call the appropriate method on the controller
//     // Pass any parameters if needed
//     // $controller->$methodName($params);

// } else {
//     echo "404 Not Found";
// }







