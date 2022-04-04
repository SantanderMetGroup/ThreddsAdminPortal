<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title><spring:message code="app.system.message"/></title>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://getbootstrap.com/examples/sticky-footer-navbar/sticky-footer-navbar.css">
	<jsp:include page="../includes/css-bs-fa-main.jsp" />
	<jsp:include page="../includes/script-jq-bs.jsp" />
	<script src="https://www.google.com/recaptcha/api.js" async defer></script>
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
	<body class="body-background">   
		<jsp:include page="../global/Navbar.jsp" />
			<div class="container">
				<div class="row">
					<div class="mainbox col-xs-12 col-md-8 col-md-offset-2">
						<div class="panel panel-default">
							<div class="panel-heading"><strong><spring:message code="app.system.message"/></strong></div>
							<div class="panel-body">
								<p> <i class="fa fa-check-square"></i> ${message} </p>		
								<a href="${location}">${location}</a>	
							</div>
						</div>
					</div>
				</div>
			</div>
		<jsp:include page="../global/NavbarFooterFixed.jsp" />
</body>
</html>