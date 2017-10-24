<?php
require "conn.php";

$uid =  $_POST["id"];
$uwriting =$_POST["writing"];

$mysql_qry = "UPDATE authority SET writing='$uwriting' WHERE id='$uid';";
mysqli_set_charset($conn,"utf8");
$result = mysqli_query($conn,$mysql_qry);

?>
