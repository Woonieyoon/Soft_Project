<?php
require "conn.php";
$user_name = $_POST["id"];
$user_title = $_POST["title"];
$user_content = $_POST["content"];
$mysql_qry = "DELETE FROM content WHERE id like '$user_name' and title like '$user_title' and content like '$user_content';";
$result = mysqli_query($conn,$mysql_qry);
echo $result;

?>