<?php


header('Content-Type: application/json');

    $connect =  mysqli_connect("localhost", "root", "TUK123", "android_api");
    $sql = "SELECT * from upload_notes" ;
    $result= mysqli_query($connect,$sql);
    $json_array = array();


    
    $outp = $result->fetch_all(MYSQLI_ASSOC);

    $notes['error']=false;
    $notes['notes']=$outp;

    echo json_encode($notes);

?>