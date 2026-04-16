<?php
header('Content-Type: application/json');

include("global.php");

if ($connection->connect_error) {
    die(json_encode(["success" => 0, "message" => "Connection failed: " . $connection->connect_error]));
}

$user_token = mysqli_real_escape_string($connection, $_GET['user_token']);

if(isset($user_token)){
    $sql = mysqli_prepare($connection, "UPDATE userplants SET last_watered = NOW() WHERE user_token = ?");
    mysqli_stmt_bind_param($sql, "s",  $user_token);
    mysqli_stmt_execute($sql);

    echo json_encode([
        "success" => 1,
        "errormessage" => "all plants watered!"
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