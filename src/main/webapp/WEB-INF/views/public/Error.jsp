<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
 	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
 	<!-- Global variables -->
 	<c:set var="baseURL" value="${pageContext.servletContext.contextPath}" />
	<title> <spring:message code="app.login.title"></spring:message></title>
	<link rel="stylesheet" type="text/css" href="${baseURL}/resources/styles/style.css" media="screen"/>
	<link rel="stylesheet" type="text/css" href="${baseURL}/resources/styles/style-login.css" media="screen"/>
	<script type="text/javascript">
		if('${redirect}'){
			var timeout = '${timeout}';
			if(timeout == ''){
				window.location.replace('${location}');
			}else{
				setTimeout(function(){
					window.location.replace('${location}');	
				}, timeout);
			}
		}
	</script>
</head>
<body>
	<div id="wrapper">
		<div id="top-menu">
			<img src="resources/img/logo.png" alt="tap logo" width="100px" height="37.5px"/>
		</div>
		<section id="content">	
			<p> <img src="resources/icons/error_24px.png" alt="error" /> ${message} </p>
			<a href="${location}">${location}</a>
		</section>
	</div>
</body>
</html>