<?php
$conn = mysqli_connect("localhost","root","","book");
mysqli_set_charset($conn,"utf8");
$user_name = $_POST["id"];
$user_title = $_POST["title"];
$user_content = $_POST["content"];
$mysql_qry = "DELETE FROM content WHERE id='$user_name' and title='$user_title' and content='$user_content';";
$result = mysqli_query($conn,$mysql_qry);
echo $result;

?>