<?php
require "conn.php";
$user_name = $_POST["id"];

$mysql_qry = "select * from login where id='$user_name';";
$result = mysqli_query($conn,$mysql_qry);
if(mysqli_num_rows($result) > 0){
	echo "1";
}else{
	echo "0";
}

?>