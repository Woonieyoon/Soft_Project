<?php
require "conn.php";
$user_sid = $_POST["sid"];
$user_wid = $_POST["wid"];
$user_title =  $_POST["title"];
$user_content =  $_POST["content"];
$user_date = $_POST["date"];
$user_state = $_POST["state"];


$mysql_qry = "insert into report(sid,wid,title,content,date,state) values('$user_sid','$user_wid','$user_title','$user_content','$user_date','$user_state');";
mysqli_set_charset($conn,"utf8");
if($conn->query($mysql_qry) == TRUE)
{
	echo "1";
}else
{
	echo "Error";
}

?>