<?php

$con = mysqli_connect("localhost","root","","book");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect" . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$res = mysqli_query($con,"select * from report");
$result = array();

while($row = mysqli_fetch_array($res)){
	
	array_push($result,array('sid'=>$row[0],'wid'=>$row[1],'title'=>$row[2],'content'=>$row[3],'date'=>$row[4],'state'=>$row[5]));
	
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>