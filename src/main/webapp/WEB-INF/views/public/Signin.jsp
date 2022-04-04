<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
  	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<c:set var="baseURL" value="${pageContext.servletContext.contextPath}"></c:set>
    <title>UDG-TAP Sign in</title>
  	<jsp:include page="../includes/css-bs-fa-main.jsp" />
  	<jsp:include page="../includes/script-jq-bs.jsp" />
	<script type="text/javascript">
		$(document).ready(function() {
			$("#input-username").focus();
			$("#topNavbar ul:first li").each(function(){
				var href = $(this).children().attr('href');
				$(this).children().attr('href', "home" + href); 
			});
			if($.url('?referrer') != null && $.url('?referrer') != '')
				$('#loginForm').attr('action', $('#loginForm').attr('action') + "?referrer=" + $.url('?referrer'));
		});
	</script>
  </head>
  <body class="body-background">
		<jsp:include page="../global/Navbar.jsp" />		
		<div class="container container-navbar">
			<div class="row">
				<div class="mainbox col-xs-12 col-sm-6 col-sm-offset-3 col-md-4 col-md-offset-4">
					<div class="panel panel-default">
						<div class="panel-heading"> <strong class="">UDG-TAP Sign in</strong></div>
						<div class="panel-body">
							<div class="col-sm-12">
								<form id="loginForm" class="form-horizontal" role="form" action="j_spring_security_check" method="post">
									<div style="margin-bottom: 25px" class="input-group">
										<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
										<input id="input-username" type="text" placeholder="Username" name="j_username" class="form-control" required tabindex=1>
									</div>
									<div style="margin-bottom: 10px" class="input-group">
										<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
										<input type="password" placeholder="Password" name="j_password" class="form-control" required="" tabindex=2>
									</div>
									<div style="margin-bottom: 10px" class="input-group">
											<a href="recovery">Forgot password?</a> 
									</div>
									<div class="input-group col-xs-12 col-sm-12 last" style="margin-top: 20px">
											<button type="submit" class="btn btn-success btn-md" style="width:100%;"><span class="glyphicon glyphicon-log-in"> </span> Sign in with Username/Password</button>
									</div>
								</form>
							</div>
							<div style="margin: 25px 0" class="col-sm-12">
								<hr class="hr-or">
								<span class="span-or">OR</span>
							</div>
							<div class="col-sm-12">
								<form id="openIdLoginForm" class="form-horizontal" role="form">
									<div style="margin-bottom: 10px" class="input-group">
										<span class="input-group-addon"><i class="fa fa-openid"></i></span>
										<input type="text" class="form-control" placeholder="OpenID identifier" name="openid_identifier" action = "j_spring_openid_security_check" class="form-control" required="" disabled>
									</div>
									<div class="input-group col-xs-12 col-sm-12 last" style="margin-top: 20px">
										<button type="submit" class="btn btn-warning btn-md" style="width:100%;"><span class="fa fa-openid"> </span> Sign in with OpenID</button>
									</div>
								</form>
							</div>
						</div>
						<c:if test="${not empty param.error}">
							<div class="panel-footer panel-footer-error">
								<p>Sign in attempt failed</p>
								<p>Caused by: ${param.error}</p>
							</div>
						</c:if>	
						<div class="panel-footer">Need an account? <a href="signup" class="">Sign up here</a>
						</div>
					</div>
				</div>
			</div>
		</div> <!-- end container -->
		<footer class="footer-sticky navbar-footer">
			<div class="container">
 					<a href="http://meteo.unican.es">
	  					<img title="University of Cantabria Meteorology Group" style="max-height: 45px; max-width:100px; " src="${pageContext.servletContext.contextPath}/resources/img/ucmg-logo.png">
	 			 	</a>
			</div>
		</footer>
  </body>
</html>