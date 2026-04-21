<?php
header('Content-Type: application/json');

include("global.php");

$action = mysqli_real_escape_string($connection, $_GET['action']);

$filepath = "Keys.json";
$jsonString = file_get_contents($filepath);
$jsonKeys = json_decode($jsonString, true);


if(isset($action)){
    switch($action){
        //api calls to get data from them
        case("perenualFindID"):
            //call to that api
            $plant_species = mysqli_real_escape_string($connection, $_GET['plant_species']);
            $token = $jsonKeys["perenualAPIKey"];

            $params = [
                "q" => $plant_species,
                "key" => $token
            ];

            $url = "https://perenual.com/api/v2/species-list?" . http_build_query($params);

            $ch = curl_init($url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
            $response = curl_exec($ch);

            $data = json_decode($response, true);

            echo json_encode([
                "success" => 1,
                "data" => $data,
                "curlError" => curl_error($ch)
            ]);

        case("perenualLoadPlantData"):
            //call to that api
            $plant_id = mysqli_real_escape_string($connection, $_GET['plant_id']);
            $token = $jsonKeys["perenualAPIKey"];

            $params = [
                "key" => $token
            ];

            $url = "https://perenual.com/api/v2/species/details/$plant_id?" . http_build_query($params);

            $ch = curl_init($url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
            $response = curl_exec($ch);

            $data = json_decode($response, true);

            echo json_encode([
                "success" => 1,
                "data" => $data,
                "curlError" => curl_error($ch)
            ]);
        case("trefleSearch"):
            //call to that api
            $search = mysqli_real_escape_string($connection, $_GET['search']);
            $token = $jsonKeys["trefleAPIKey"];

            if($search == ""){
                $search = "a";
            }

            $url = "https://trefle.io/api/v1/plants/search?token=$token&q=$search&filter_not[common_name]=null";

            $ch = curl_init($url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
            $response = curl_exec($ch);

            $data = json_decode($response, true);

            echo json_encode([
                "success" => 1,
                "data" => $data,
                "curlError" => curl_error($ch)
            ]);
        case("trefleLoad"):
            //call to that api
            $token = $jsonKeys["trefleAPIKey"];

            $url = "https://trefle.io/api/v1/plants?token=$token";

            $ch = curl_init($url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
            $response = curl_exec($ch);

            $data = json_decode($response, true);

            echo json_encode([
                "success" => 1,
                "data" => $data,
                "curlError" => curl_error($ch)
            ]);
        case("GetWeather"):
            //call to that api
            $latitude = mysqli_real_escape_string($connection, $_GET['latitude']);
            $longitude = mysqli_real_escape_string($connection, $_GET['longitude']);
            $token = $jsonKeys["openWeatherAPIKey"];

            $url = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$token";

            $ch = curl_init($url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
            $response = curl_exec($ch);

            $data = json_decode($response, true);

            echo json_encode([
                "success" => 1,
                "data" => $data,
                "curlError" => curl_error($ch)
            ]);
    }
}
else{
   echo json_encode([
        "success" => 0,
        "errormessage" => "Data missing"
    ]); 
}

?>