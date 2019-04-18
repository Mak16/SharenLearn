<?php
 
/**
 * @author Ravi Tamada
 * @link https://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */
 
class DB_Functions {
 
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
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
    public function storeNotes($user_id, $email, $title,$description,$category,$download_link,$image_link) {
        // $uuid = uniqid('', true);
        // $hash = $this->hashSSHA($password);
        // $encrypted_password = $hash["encrypted"]; // encrypted password
        // $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("INSERT INTO upload_notes(user_id, email, title,description,category,download_link,image_link,uploaded_at ) VALUES(?, ?, ?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssssss", $user_id, $email, $title, $description ,$category , $download_link , $image_link);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM upload_notes WHERE user_id = ? AND title = ?");
            $stmt->bind_param("ss", $user_id,$title);
            $stmt->execute();
            // $notes = $stmt->get_result()->fetch_assoc();
            $result = $stmt->get_result();
            $notes = $result->fetch_all(MYSQLI_ASSOC);
            $stmt->close();
 
            return $notes;
        } else {
            return false;
        }
    }
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
 
    public function getUserByEmail($email) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            // $salt = $user['salt'];
            // $encrypted_password = $user['encrypted_password'];
            // $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            // if ($encrypted_password == $hash) {
                // user authentication details are correct
            return $user;
            // }
        } else {
            return NULL;
        }
    }
 
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
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
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
 
    public function upadateUserUploads($email ) {
 
        $stmt = $this->conn->prepare("SELECT uploads FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // get uploads
            $uploads = $user['uploads'];
            $uploads = $uploads+1;
            
        $stmt = $this->conn->prepare("UPDATE users SET uploads = ?  WHERE email = ?");
 
        $stmt->bind_param("is",$uploads, $email);
 
        if ($stmt->execute()) {
            // $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $uploads;
        }else{
            echo "error in uploads";
            return NULL;
        }
 
        } else {
            echo "error in user";
            return NULL;
        }
    }

    public function getNotes($title,$uploader,$category)
    {
        $res = array();
        $notes=array();
        // echo($title.$uploader.$category);
        if( $title=="all" && $category=="all" )
        {
            $stmt = $this->conn->prepare("SELECT * FROM upload_notes WHERE 1 ");
 
            // $stmt->bind_param("s", $title);
            // echo('<br>if( $title="" && $category="" )<br>');
 
            if ($stmt->execute()) 
            {
                
                $result = $stmt->get_result();
                $notes = $result->fetch_all(MYSQLI_ASSOC);
                $stmt->close();
                // print_r($notes);
                return $notes;
            }
        }
        if($title!="all")
        {
            $stmt = $this->conn->prepare("SELECT * FROM upload_notes WHERE title = ?");
 
            $stmt->bind_param("s", $title);
            // echo('<br> if( $title!="") <br>');
            if ($stmt->execute()) 
            {
                $result = $stmt->get_result();
                $notes = $result->fetch_all(MYSQLI_ASSOC);
                $stmt->close();
                // print_r($notes);
                $res=$notes;
            } 
        }
        
        if($category!="all")
        {
            $stmt = $this->conn->prepare("SELECT * FROM upload_notes WHERE category = ?");
 
            $stmt->bind_param("s", $category);

            // echo('<br>if($category!="" )<br>');
 
            if ($stmt->execute()) 
            {
                $result = $stmt->get_result();
                $notes = $result->fetch_all(MYSQLI_ASSOC);
                $stmt->close();
                // print_r($notes);
                $res = array_merge($notes,$res);
            } 
        }
        // print_r($res);
        return $res;  
    }
}
 
?>