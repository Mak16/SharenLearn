<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['user_id']) && isset($_POST['email']) && isset($_POST['title']) && isset($_POST['description']) && isset($_POST['download_link']) && isset($_POST['image_link']) && isset($_POST['category'])) {
 
    // receiving the post params
    $user_id = $_POST['user_id'];
    $user_email = $_POST['email'];
    $title = $_POST['title'];
    $description = $_POST['description'];
    $download_link = $_POST['download_link'];
    $image_link = $_POST['image_link'];
    $category = $_POST['category'];
 
        // create a new notes
        $notes = $db->storeNotes($user_id, $user_email, $title,$description,$category,$download_link,$image_link);
        if ($notes) {
            $user_uploads = $db->upadateUserUploads($user_email );
            // user stored successfully
            $response["user_uploads"]=0;
            if($user_uploads)
            {
                $response["user_uploads"]=$user_uploads;
            }
             $response["error"] = FALSE;
            // $response["notes_id"] = $notes["notes_id"];
            // $response["note"]["title"] = $notes["title"];
            // $response["note"]["category"] = $notes["category"];
            // $response["note"]["description"] = $notes["description"];
            // $response["note"]["download_link"] = $notes["download_link"];
            // $response["note"]["image_link"] = $notes["image_link"];
            // $response["note"]["uploaded_by"] = $notes["user_id"];
            // $response["note"]["uploader_email"] = $notes["email"];
            // $response["note"]["uploaded_at"] = $notes["uploaded_at"];
            $response["note"] = $notes;

            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters is missing!";
    echo json_encode($response);
}
?>