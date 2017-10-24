<?php

$con = mysqli_connect("localhost","root","","book");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect" . mysqli_connect_error();
}
$user_send = $_POST["send"];
mysqli_set_charset($con,"utf8");

$res = mysqli_query($con,"select * from message where send like '$user_send' or receive like '$user_send'");
$result = array();

while($row = mysqli_fetch_array($res)){
	
	array_push($result,array('send'=>$row[0],'receive'=>$row[1],'content'=>$row[2],'date'=>$row[3]));
	
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>