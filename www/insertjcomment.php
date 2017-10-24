<?php
require "conn.php";
$user_title = $_POST["title"];
$user_content = $_POST["content"];
$user_name =  $_POST["id"];
$user_wname =  $_POST["wid"];
$user_comment = $_POST["com"];
$user_date = $_POST["date"];

$mysql_qry = "insert into jobcomment(title,content,id,wid,com,date) values('$user_title','$user_content','$user_name','$user_wname','$user_comment','$user_date');";
mysqli_set_charset($conn,"utf8");
if($conn->query($mysql_qry) == TRUE)
{
	echo "1";
}else
{
	echo "Error";
}

?>