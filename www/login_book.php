<?php
require "conn.php";
$user_name = $_POST["id"];
$user_pass = $_POST["pwd"];
$mysql_qry = "insert into login(id,pwd) values('$user_name','$user_pass');";
$result = mysqli_query($conn,$mysql_qry);
echo $result;
$mysql_qry = "insert into wifi(id,ustatus) values('$user_name','off');";
$result = mysqli_query($conn,$mysql_qry);
$mysql_qry = "insert into authority(id,login,writing) values('$user_name','on','on');";
$result = mysqli_query($conn,$mysql_qry);


?>