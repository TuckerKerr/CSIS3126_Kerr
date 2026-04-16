<?php
header('Content-Type: application/json');

include("global.php");

if ($connection->connect_error) {
    die(json_encode(["success" => 0, "message" => "Connection failed: " . $connection->connect_error]));
}
$action = mysqli_real_escape_string($connection, $_GET['action']);

if($action === "getPlant"){    
    $plant_id = mysqli_real_escape_string($connection, $_GET['plant_id']);
    $user_token = mysqli_real_escape_string($connection, $_GET['userToken']);

    if(isset($user_token) && isset($plant_id)){

        $sql = mysqli_prepare($connection, "SELECT plant_name, plant_species, plant_watering_days, last_watered, planted, plant_base_image, plant_desired_sunlight FROM userplants WHERE user_token=? AND id=?");
        mysqli_stmt_bind_param($sql, "si", $user_token, $plant_id);
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
}
if($action === "getTime"){
    $user_token = mysqli_real_escape_string($connection, $_GET['userToken']);

    if(isset($user_token)){

        $sql = mysqli_prepare($connection, "SELECT id, plant_name, plant_species, plant_base_image, last_watered, plant_watering_days FROM userplants WHERE user_token = ?");
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
}


$connection->close();
?>