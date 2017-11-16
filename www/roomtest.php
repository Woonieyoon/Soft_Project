<?php

$con = mysqli_connect("localhost","root","","book");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect" . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");


$uid =  $_POST["id"];



$res = mysqli_query($con,"select * from roomtest where id = '$uid'");
$result = array();

while($row = mysqli_fetch_array($res)){
	
	array_push($result,array('Roomid'=>$row[0],'score'=>$row[1],'name'=>$row[2]),'RoomNum'=>$row[3]));
	
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>