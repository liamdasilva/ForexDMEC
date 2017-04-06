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
<header>
		<h1>Forex Data Mining</h1>
		<h3>Upload a file and select the object to classify the data with.</h3>
	</header>

<form method="POST" action="uploadTest" enctype="multipart/form-data">
	
		File: <input type="file" name="file" id="file" class="myLabel2" /> <br><br>
		Select the object:<br>
		<select name="objectName">
		<%
		String listOfObjects=request.getAttribute("listOfObjects").toString();
		listOfObjects=listOfObjects.trim();
	/* 	listOfObjects.replaceAll("[", "");
		listOfObjects.replaceAll("]", ""); */

		String []objectArray=listOfObjects.split(",");
		for(String object: objectArray){
			out.println("<option value="+object+">"+object+"</option>");
		}
		out.println(listOfObjects);
		%>
		<select>
		<br>
		<!-- <br> Number of pips: <input type="text" name="numPips"><br><br> -->
		 <br> <input type="submit" value="Upload" name="upload"
			id="upload"/>
	</form>
</body>
</html>