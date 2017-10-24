<?php
require "conn.php";

$uid =  $_POST["id"];
$utitle =$_POST["title"];
$ucontent =  $_POST["content"];
$ucount = $_POST["count"];


$mysql_qry = "UPDATE content SET count='$ucount' WHERE id='$uid' and title='$utitle' and content='$ucontent';";
mysqli_set_charset($conn,"utf8");
$result = mysqli_query($conn,$mysql_qry);

?>
