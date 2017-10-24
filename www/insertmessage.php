<?php
require "conn.php";
$user_send = $_POST["send"];
$user_receive = $_POST["receive"];
$user_message =  $_POST["message"];
$user_date =  $_POST["date"];

$mysql_qry = "insert into message(send,receive,message,date) values('$user_send','$user_receive','$user_message','$user_date');";
mysqli_set_charset($conn,"utf8");
if($conn->query($mysql_qry) == TRUE)
{
	echo "1";
}else
{
	echo "Error";
}

?>