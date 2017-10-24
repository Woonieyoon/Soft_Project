<?php

$con = mysqli_connect("localhost","root","","book");

if(mysqli_connect_errno($con))
{
	echo "Failed to connect" . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$res = mysqli_query($con,"select * from clubcomment");
$result = array();

while($row = mysqli_fetch_array($res)){
	
	array_push($result,array('title'=>$row[0],'content'=>$row[1],'id'=>$row[2],'wid'=>$row[3],'com'=>$row[4],'date'=>$row[5]));
	
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>