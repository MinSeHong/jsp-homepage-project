<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<style>
	body{
		margin:0px;
		overflow:hidden;
	}
	iframe{
		overflow:scroll;
		border:0px solid red;
		outline:none;
		position:absolute;
		width:100%;
		height:100%;
	}
</style>
<body>
<iframe src="browser.jsp">
</iframe>
</body>
</html>