 = file_get_contents("php://input");
$fp = fopen("/tmp/data.jpg", 'wb');
fwrite($fp, $data);
fclose($fp);
echo "Image Upload Finish.\n";
