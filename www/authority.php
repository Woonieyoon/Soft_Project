<?php
require "conn.php";

$uid =  $_POST["id"];
$ustate =$_POST["ustate"];


$mysql_qry = "UPDATE authority SET login='$ustate' WHERE id='$uid' ;";
mysqli_set_charset($conn,"utf8");
$result = mysqli_query($conn,$mysql_qry);

?>
