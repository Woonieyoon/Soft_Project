<?php
require "conn.php";
$user_id = $_POST["roid"];
$user_pwd = $_POST["ropwd"];
$user_date =  $_POST["rodate"];


$mysql_qry = "insert into test(roid,ropwd,rodate) values('$user_id','$user_pwd','$user_date');";
mysqli_set_charset($conn,"utf8");
if($conn->query($mysql_qry) == TRUE)
{
	echo "1";
}else
{
	echo "Error";
}

?>