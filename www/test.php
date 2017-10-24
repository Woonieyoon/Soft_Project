<?php
require "conn.php";
$u_id = "안";
$u_title = "aa";
$u_content = "555";
$mysql_qry = "insert into content(id,title,content,date,sub,count) values('$u_id','$u_title','$u_content','','','');";
mysqli_set_charset($conn,"utf8");
$result = mysqli_query($conn,$mysql_qry);

if($result == 1)
{
	echo "1";
}else
{
	echo "0";
}
?>
?>