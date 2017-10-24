<?php
require "conn.php";
$user_name =  $_POST["id"];
$user_title = $_POST["title"];
$user_content = $_POST["content"];
$user_date = $_POST["date"];
$mysql_qry = "insert into jobcontent(id,title,content,date) values('$user_name','$user_title','$user_content','$user_date');";
mysqli_set_charset($conn,"utf8");
if($conn->query($mysql_qry) == TRUE)
{
	echo "1";
}else
{
	echo "Error";
}

?>