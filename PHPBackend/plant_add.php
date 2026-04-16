<?php
header('Content-Type: application/json');

include("global.php");

if ($connection->connect_error) {
    die(json_encode(["success" => 0, "message" => "Connection failed: " . $connection->connect_error]));
}

$jsonData = file_get_contents('php://input');
$data = json_decode($jsonData, true);

if($data === null){
    echo json_encode([
        "success" => 0,
        "errormessage" => "Data missing"
    ]); 
}

$user_token = mysqli_real_escape_string($connection, $data['user_token']);
$plant_name = mysqli_real_escape_string($connection, $data['plant_name']);
$plant_species = mysqli_real_escape_string($connection, $data['plant_species']);
$plant_sunlight = mysqli_real_escape_string($connection, $data['plant_sunlight']);
$plant_watering = mysqli_real_escape_string($connection, $data['plant_watering']);
$plant_flower = mysqli_real_escape_string($connection, $data['plant_flower']);
$plant_image = mysqli_real_escape_string($connection, $data['plant_image']);
$plant_description = mysqli_real_escape_string($connection, $data['plant_description']);



if(isset($data['user_token']) && isset($data['plant_name']) && isset($data['plant_species'])){

    $days_until_water = explode("-",$plant_watering);
    $start = (int)$days_until_water[0];
    $end = (int)$days_until_water[1];
    $average_water = round(($start + $end)/2);

    $water_cycle = $plant_watering . " days";

    $plant_photo = "photo needed";
    $date_planted = date("Y-m-d");

    $sql = mysqli_prepare($connection, "INSERT INTO userplants(user_token, plant_name, plant_species, plant_watering_cycle, plant_watering_days, last_watered, planted, plant_desired_sunlight, plant_photo, plant_base_image, plant_description) VALUES (?,?,?,?,?,NOW(),?,?,?,?,?)");
    mysqli_stmt_bind_param($sql, "ssssisssss", $user_token, $plant_name, $plant_species, $water_cycle, $average_water, $date_planted, $plant_sunlight, $plant_photo, $plant_image, $plant_description);
    mysqli_stmt_execute($sql);

    echo json_encode([
        "success" => 1,
        "errormessage" => "Data put in system"
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