<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="/smarttrip-web/file/upload/notExe" method="post" enctype="multipart/form-data">
		<label>title:</label>
		<input type="title" name="title" id="title" /> <br />
		<label for="file">文件:</label> 
		<input type="file" name="fileUpload" id="fileUpload" /> <br /> 
		<input type="submit" name="submit" value="Submit" />
	</form>
</body>
</html>