<?php


require_once 'include/DB_Functions1.php';
$db = new DB_Functions1();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['title']) && isset($_POST['category']) && isset($_POST['download_link'])) {

    // receiving the post params
    $title = $_POST['title'];
    $category = $_POST['category'];
    $download_link = $_POST['download_link'];

    // check if user is already existed with the same email
    if ($db->isBookExisted($title)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Book already existed with " . $title;
    //    header('Content-Type: application/json');
        echo json_encode($response);
    } else {
        // create a new user

        $book = $db->storeBook($title, $category,$download_link);
        if ($book) {
            // user stored successfully
            $response["error"] = FALSE;
        //    $response["uid"] = $book["unique_id"];
            $response["book"]["title"] = $book["title"];
            $response["book"]["category"] = $book["category"];
            $response["book"]["download_link"] = $book["download_link"];

        //    header('Content-Type: application/json');
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration of book!";
          //  header('Content-Type: application/json');
            echo json_encode($response);
        }
    }
}

?>
