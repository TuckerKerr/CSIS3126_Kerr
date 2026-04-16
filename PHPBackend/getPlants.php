<?php
header('Content-Type: application/json');

include("global.php");

if ($connection->connect_error) {
    die(json_encode(["success" => 0, "message" => "Connection failed: " . $connection->connect_error]));
}


$user_token = mysqli_real_escape_string($connection, $_GET['user_token']);

if(isset($user_token)){
    $sql = mysqli_prepare($connection, "SELECT * FROM userplants WHERE user_token = ?");
    mysqli_stmt_bind_param($sql, "s", $user_token);
    mysqli_stmt_execute($sql);
    $result = mysqli_stmt_get_result($sql);
    $rows = mysqli_fetch_all($result, MYSQLI_ASSOC);

    echo json_encode([
        "success" => 1,
        "plants" => $rows
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