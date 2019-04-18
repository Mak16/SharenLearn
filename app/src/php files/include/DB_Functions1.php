<?php

/**
 * @author Ravi Tamada
 * @link https://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

class DB_Functions1 {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {

    }


    public function storeBook( $title, $category,$download_link) {


        $stmt = $this->conn->prepare("INSERT INTO books(title,category,download_link) VALUES(?,?,?)");
        $stmt->bind_param("sss",$title, $category,$download_link );
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM books WHERE title = ?");
            $stmt->bind_param("s", $title);
            $stmt->execute();
            $book = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $book;
        } else {
            return false;
        }
    }

        public function isBookExisted($title) {
        $stmt = $this->conn->prepare("SELECT title from books WHERE title = ?");

        $stmt->bind_param("s", $title);
      //  $this->conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);


        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
}
   $connect =  mysqli_connect("localhost", "root", "TUK123", "android_api");
    $sql = "SELECT * from books" ;
   $result= mysqli_query($connect,$sql);
   $json_array = array();


   while($row = mysqli_fetch_assoc($result))
   {
   $json_array[] = $row;

  }
  echo json_encode($json_array);

  ?>
