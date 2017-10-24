<?php
require "conn.php";

$uid =  $_POST["id"];
$ulogin =$_POST["login"];

$mysql_qry = "UPDATE authority SET login='$ulogin' WHERE id='$uid';";
mysqli_set_charset($conn,"utf8");
$result = mysqli_query($conn,$mysql_qry);

?>
