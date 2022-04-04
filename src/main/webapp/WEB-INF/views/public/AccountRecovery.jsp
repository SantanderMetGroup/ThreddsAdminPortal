<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>UDG-TAP Password recovery</title>
		<c:set var="baseURL" value="${pageContext.servletContext.contextPath}"></c:set>
		<link rel="stylesheet" href="http://getbootstrap.com/examples/sticky-footer-navbar/sticky-footer-navbar.css">
		<jsp:include page="../includes/css-bs-fa-main.jsp" />
		<jsp:include page="../includes/script-jq-bs.jsp" />
		<script src="https://www.google.com/recaptcha/api.js" async defer></script>
		<script type="text/javascript">
			$(document).ready(function() {
				$("#input-email").focus();
				$("#topNavbar ul:first li").each(function(){
					var href = $(this).children().attr('href');
					$(this).children().attr('href', "home" + href); 
				});
			});
		</script>
	</head>
	
	<body class="body-background">   
		<jsp:include page="../global/Navbar.jsp" />
			<div class="container">
				<div class="row">
					<div class="mainbox col-xs-12 col-md-6 col-md-offset-3">
						<div class="panel panel-default">
							<div class="panel-heading"><strong>UDG-TAP Account Recovery</strong></div>
							<div class="panel-body">
								<div class="col-sm-12">
									<form id="accountRecoveryForm" class="form-horizontal" role="form" method="POST"  action="recovery">
										<div class="form-group">
											<div class="input-group">
												<span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
												<input id="input-email" type="text" placeholder="Email" name="email" class="form-control" required="">
											</div>
										</div>
										<div class="form-group">
												<div class="g-recaptcha" data-sitekey="${recaptchaKey}"></div>
										</div>
										<div class="form-group">
											<div class="input-group last">
												<button type="submit" class="btn btn-success btn-sm"><span class="fa fa-key"></span> Submit </button>
											</div>
										</div>
									</form>	
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		<jsp:include page="../global/NavbarFooterFixed.jsp" />
	</body>
</html>