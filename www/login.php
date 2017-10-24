<?php
require "conn.php";
$user_name = $_POST["id"];
$user_pass = $_POST["pwd"];
$mysql_qry = "select * from login where id like '$user_name' and pwd like '$user_pass';";
$result = mysqli_query($conn,$mysql_qry);
if(mysqli_num_rows($result) > 0){
	echo "1";
}else{
	echo "0";
}

?>