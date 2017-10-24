<?php
require "conn.php";
$user_name = $_POST["id"];
$user_pass = $_POST["pwd"];
$mysql_qry = "insert into login(id,pwd) values('$user_name','$user_pass');";
$result = mysqli_query($conn,$mysql_qry);
echo $result;

?>