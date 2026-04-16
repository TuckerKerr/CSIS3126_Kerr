<?php
header('Content-Type: application/json');

include("global.php");

if ($connection->connect_error) {
    die(json_encode(["success" => 0, "message" => "Connection failed: " . $connection->connect_error]));
}

$plant_id = mysqli_real_escape_string($connection, $_GET['plant_id']);
$user_token = mysqli_real_escape_string($connection, $_GET['user_token']);

if(isset($user_token) && isset($plant_id)){
    $sql = mysqli_prepare($connection, "UPDATE userplants SET last_watered = NOW() WHERE id= ? AND user_token = ?");
    mysqli_stmt_bind_param($sql, "is", $plant_id, $user_token);
    mysqli_stmt_execute($sql);

    echo json_encode([
        "success" => 1,
        "errormessage" => "plant watered!"
    ]);

}
else{
   echo json_encode([
        "success" => 0,
        "errormessage" => "Data missing"
    ]); 
}

$connection->close();
?>