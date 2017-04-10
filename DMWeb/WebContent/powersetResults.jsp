<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="com.dmec.forex.ClassifierMaster"%>  
          
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel='stylesheet' type='text/css' href='style.css'>
<link rel="icon" type="image/ico" href="favicon.png">
<title>Power Set Results</title>
</head>

<body>
<a href="/DMWeb">Go to Home Page</a><br>

	<%
		int combinationsTried=(int)request.getAttribute("combinationsTried");
		int numCorrect=(int)request.getAttribute("numCorrect");
		int numTotal=(int)request.getAttribute("numTotal");
		ClassifierMaster cmObject= (ClassifierMaster)request.getAttribute("cmObject");
		out.println("Out of "+combinationsTried+" variable combinations tried, the best is:<br>");
		out.println(numCorrect+"/"+numTotal+" correct trading opportunities.<br>");
		out.println(100*(double)numCorrect/(double)numTotal+"% accurate.<br>");
		out.println("Moving Averages: "+cmObject.getMovingAverages()+"<br>");
		out.println("Trend Periods: "+cmObject.getTrendPeriods()+"<br>");
		out.println("Pips Class: "+cmObject.getPips()+"<br>");
		
		
	%>
	<br>
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