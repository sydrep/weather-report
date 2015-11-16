<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Weather Report</title>


<spring:url value="/resources/js/jquery-2.1.4.js" var="jqueryJs"></spring:url>
<script type="text/javascript" src="${jqueryJs}"></script>





<script type="text/javascript">
	jQuery(document).ready(function($) {

		$('#msg').html("Weather Report")

	});
</script>
</head>
<body>
	<h1><div id="msg"/></h1>
	<form:form commandName="city">
		<form:select path="cityName" onchange="submit()">
			<form:option value=""></form:option>
			<form:options items="${citiesList}"></form:options>
		</form:select>
		<form:errors path="cityName" cssStyle="color: #ff0000;" />
	</form:form>
	<p>${message}</p>
	<table border="1">
		<tbody>
			<tr>
				<td>City</td>
				<td>${weather.city}</td>
			</tr>
			<tr>
				<td>Updated time</td>
				<td>${weather.updatedTime}</td>
			</tr>
			<tr>
				<td>Weather</td>
				<td>${weather.weather}</td>
			</tr>
			<tr>
				<td>Temperature</td>
				<td>${weather.temperature}</td>
			</tr>
			<tr>
				<td>Wind</td>
				<td>${weather.wind}</td>
			</tr>
		</tbody>
	</table>
	


</body>
</html>
