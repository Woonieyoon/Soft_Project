<?php

$con = mysqli_connect("localhost","blak29","dlgudwls12!","blak29");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect" . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$res = mysqli_query($con,"select * from room");
$result = array();

while($row = mysqli_fetch_array($res)){
	
	array_push($result,array('roomid'=>$row[0],'roompwd'=>$row[1],'roomdate'=>$row[2]));
	
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>