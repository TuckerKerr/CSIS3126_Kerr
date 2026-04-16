<?php
header('Content-Type: application/json');

include('global.php');

if ($connection->connect_error) {
    die(json_encode(["success" => false, "message" => "Connection failed: " . $connection->connect_error]));
}

if (isset($_GET['username']) && isset($_GET['password']) && isset($_GET['email'])) {
    $email = mysqli_real_escape_string($connection, $_GET['email']);
    $username = mysqli_real_escape_string($connection, $_GET['username']);
    $password = mysqli_real_escape_string($connection, $_GET['password']);
    $buttonPressed = mysqli_real_escape_string($connection, $_GET['buttonPressed']);
    
    
    //sql code goes here to look into the database and see if the user exists and if they do
//to check the passwords together and if match, login
//do an if or switch statement to switch between login and register
switch($buttonPressed){
    case 'login':
        $sql = mysqli_prepare($connection, "SELECT hashed_pass, token FROM users WHERE username = ? AND email = ?");
        mysqli_stmt_bind_param($sql, "ss", $username, $email);
        mysqli_stmt_execute($sql);
        $result = mysqli_stmt_get_result($sql);
        $row = mysqli_fetch_assoc($result);
        mysqli_stmt_close($sql);

        if($row){
            if(password_verify($password, $row["hashed_pass"])){
                echo json_encode([
                    "success" => 1,
                    "token" => $row['token'],
                    "errormessage" => ""
                ]);
            }
            else {
                echo json_encode([
                "success" => 0,
                "errormessage" => "User not found. Check your inputs or Register"
                    ]);
                } 
                break;
            }
        else{
            echo json_encode([
                "success" => 0,
                "errormessage" => "User not found. Error in code"
            ]);
            break;
        }
    case 'register':
        $sql = mysqli_prepare($connection, "SELECT hashed_pass, token FROM users WHERE username = ? AND email = ?");
        mysqli_stmt_bind_param($sql, "ss", $username, $email);
        mysqli_stmt_execute($sql);
        $result = mysqli_stmt_get_result($sql);
        $row = mysqli_fetch_assoc($result);
        mysqli_stmt_close($sql);
        
        if($row['hashed_pass'] == null){
            $hashed_password = password_hash($password, PASSWORD_DEFAULT);
            $token = bin2hex(random_bytes(32));

            $addSql = mysqli_prepare($connection, "INSERT INTO users (username, email, hashed_pass, token) VALUES (?,?,?,?)");
            mysqli_stmt_bind_param($addSql, "ssss", $username, $email, $hashed_password, $token);
            mysqli_stmt_execute($addSql);

            echo json_encode([
                "success" => 1,
                "errormessage" => "User Created! Welcome $username"
            ]);
        }
        else{
            echo json_encode([
                "success" => 0,
                "errormessage" => "User already exists, please log in"
            ]);
            break;
        }
        

    default:
        echo json_encode([
                "success" => false,
                "errormessage" => "Could not login/register"
            ]);
            exit;
}
  
}
$connection->close();
?>