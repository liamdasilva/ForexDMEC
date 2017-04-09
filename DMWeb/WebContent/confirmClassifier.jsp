<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel='stylesheet' type='text/css' href='style.css'>
<link rel="icon" type="image/ico" href="favicon.png">
</head>
<body>
	${evaluation}
	Would you like to keep this classification?
	<form method="POST" action="confirm" enctype="multipart/form-data">
	  	<input type="radio" name="confirmSave" value="Yes" checked="checked"> Yes<br>
	  	<input type="radio" name="confirmSave" value="No"> No<br>
	  	If yes, please enter the name you'd like to give the Classifier:
	   <input type="text" name="fileName" id="filename" /> <br>
	  	<input type="submit" value="Confirm" name="confirm" id="confirm" />
	</form>
	
</body>
</html>