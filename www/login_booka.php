<?php
require "conn.php";
mysqli_set_charset($con,"utf8");
$user_name = $_POST["id"];
$user_pass = $_POST["pwd"];
$user_kname = $_POST["knuid"];
$user_kpass = $_POST["knupwd"];
$mysql_qry = "insert into logindata(id,pwd,knuid,knupwd) values('$user_name','$user_pass','$user_kname','$user_kpass');";
$result = mysqli_query($conn,$mysql_qry);
echo $result;


?>