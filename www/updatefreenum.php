<?php
require "conn.php";

$mysql_qry = "delete from freenum";
mysqli_set_charset($conn,"utf8");
mysqli_query($conn,$mysql_qry);

$unum = $_POST["num"];

$mysql_qry = "insert into freenum(num) values('$unum');";
mysqli_set_charset($conn,"utf8");
mysqli_query($conn,$mysql_qry);

?>
