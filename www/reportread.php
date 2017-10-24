<?php
require "conn.php";

$usid = $_POST["sid"];
$uwid = $_POST["wid"];
$utitle = $_POST["title"];
$ucontent = $_POST["content"];
$udate = $_POST["date"];
$ustate = $_POST["state"];


$mysql_qry = "UPDATE report SET state='$ustate' WHERE sid='$usid' and wid='$uwid' and title='$utitle' and content='$ucontent' and date='$udate' ;";
mysqli_set_charset($conn,"utf8");
$result = mysqli_query($conn,$mysql_qry);

?>
