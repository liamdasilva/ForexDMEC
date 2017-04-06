<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.ArrayList" %>
    <%@ page import="java.util.Arrays" %>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test Data Results</title>
</head>
<style>
table {
    font-family: arial, sans-serif;
    border-collapse: collapse;
    width: 100%;
}

td, th {
    border: 1px solid #dddddd;
    text-align: left;
    padding: 8px;
}

tr:nth-child(even) {
    background-color: #dddddd;
}
</style>
<body>
<a href="/DMWeb">Go to homepage</a><br>


	<%
	ArrayList<ArrayList<String>> results=(ArrayList<ArrayList<String>>) request.getAttribute("results");
	out.println("Here is the classification of the last/only row from the test data that was provided(prediction): "+results.get(results.size()-1).get(1)+"<br>");
	int counter=0;
	int correctlyClassifiedNonRangingInstances=0;
	int totalNonRangingClassifications=0;
	String table="";
	table+="<table>";
	/* out.println("<table>"); */
	for(int i=0;i<results.size();i++){
			/* out.println("<tr>"); */
			table+="<tr>";
			if(results.get(i).get(0).equals(results.get(i).get(1))){
				counter++;
			}
			
			if(i!=0&&i!=(results.size()-1)&&!results.get(i).get(1).equals("RANGING")){
				if(results.get(i).get(0).equals(results.get(i).get(1))){
					correctlyClassifiedNonRangingInstances++;
				}
				totalNonRangingClassifications++;
			}
			for(int j=0;j<results.get(i).size();j++){
				if(i==0){
					/* out.print("<th>"+results.get(i).get(j)+"</th>"); */
					table+="<th>"+results.get(i).get(j)+"</th>";

				}else if (i!=(results.size()-1)){
					 /* out.print("<td>"+results.get(i).get(j)+"</td>");  */
					table+="<td>"+results.get(i).get(j)+"</td>";
					
				}
			}
			
			/* out.println("</tr>"); */	
			table+="</tr>";
	}
	
	/* out.println("</table>"); */
	table+="</table>";
	out.println(correctlyClassifiedNonRangingInstances+"/"+totalNonRangingClassifications+" successful trading opportunities were captured in the test data set<br>");
	Double accuracy=(double)counter/(double)(results.size());
	out.println("Total correctly classified instances (from test data): "+accuracy*100+"%"+"<br>");
	out.println("Here are the classification results of the test data:<br>");
	out.println(table);
	
	%>
</body>
</html>