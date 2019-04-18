<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['title']) && isset($_POST['uploader']) && isset($_POST['category'])) {
 
    // receiving the post params
    $title = $_POST['title'];
    $uploader = $_POST['uploader'];
    $category = $_POST['category'];
 
    // get the user by email and password
    $notes = $db->getNotes($title,$uploader,$category);
 
    if ($notes != false) {
        // // use is found
        $response["error"] = FALSE;
        // $response["uid"] = $user["unique_id"];
        // $response["user"]["name"] = $user["name"];
        // $response["user"]["email"] = $user["email"];
        // $response["user"]["created_at"] = $user["created_at"];
        // // $response["user"]["updated_at"] = $user["updated_at"];
        // $response["user"]["downloads"] = $user["downloads"];
        // $response["user"]["uploads"] = $user["uploads"];
        $response['notes']=$notes;
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters are missing!";
    echo json_encode($response);
}
?>