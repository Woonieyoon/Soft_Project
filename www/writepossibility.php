<?php

$con = mysqli_connect("localhost","root","","book");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect" . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$uid =  $_POST["id"];



$res = mysqli_query($con,"select * from authority where id = '$uid'");
$result = array();

while($row = mysqli_fetch_array($res)){
	
	array_push($result,array('id'=>$row[0],'login'=>$row[1],'writing'=>$row[2]));
	
}
echo json_encode(array("result"=>$result));

mysqli_close($con);

?>