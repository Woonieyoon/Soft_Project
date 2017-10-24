<?php
require "conn.php";
$u_id = "1";
$u_title = "2";
$u_content = "555";
$mysql_qry = "UPDATE content SET sub='1',count='3' WHERE id like '456' and title like '32' and content like '123124';";
mysqli_set_charset($conn,"utf8");
$result = mysqli_query($conn,$mysql_qry);

?>