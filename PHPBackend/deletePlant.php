<?php

header('Content-Type: application/json');

include("global.php");

if ($connection->connect_error) {
    die(json_encode(["success" => 0, "message" => "Connection failed: " . $connection->connect_error]));
}

$user_token = mysqli_real_escape_string($connection, $_GET['userToken']);

$plant_id = mysqli_real_escape_string($connection, $_GET['plant_id']);
$user_token = mysqli_real_escape_string($connection, $_GET['userToken']);

if(isset($user_token) && isset($plant_id)){

    $sql = mysqli_prepare($connection, "DELETE FROM userplants WHERE user_token = ? AND id = ?");
    mysqli_stmt_bind_param($sql, "si", $user_token, $plant_id);
    mysqli_stmt_execute($sql);
    $result = mysqli_stmt_get_result($sql);

    echo json_encode([
        "success" => 1,
        "errormessage" => $result
    ]);

}
else{
echo json_encode([
        "success" => 0,
        "errormessage" => "Data missing"
    ]); 
}

?>