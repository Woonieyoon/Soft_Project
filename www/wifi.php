<?php
require "conn.php";

$uid =  $_POST["id"];
$ustatus =$_POST["ustatus"];


$mysql_qry = "UPDATE wifi SET ustatus='$ustatus' WHERE id='$uid' ;";
mysqli_set_charset($conn,"utf8");
$result = mysqli_query($conn,$mysql_qry);

?>
