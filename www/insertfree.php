<?php
require "conn.php";
$user_num = $_POST["num"];
$user_id = $_POST["id"];
$user_content = $_POST["content"];
$user_date =  $_POST["date"];

$mysql_qry = "insert into free(num,id,content,date) values('$user_num','$user_id','$user_content','$user_date');";
mysqli_set_charset($conn,"utf8");
if($conn->query($mysql_qry) == TRUE)
{
	echo "1";
}else
{
	echo "Error";
}

?>