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
</head>
<body class="body-background">   
	<jsp:include page="../global/Navbar.jsp" />
	<div class="container">
		<div class="row">
			<div class="mainbox col-xs-12 col-md-6 col-md-offset-3">
				<div class="panel panel-default">
					<div class="panel-heading"><strong><spring:message code="app.user.restorepassword.title" /></strong></div>
					<div class="panel-body">
						<div class="col-sm-12">
							<form id="resetPassword" class="form-horizontal" role="form" method="POST"  action="reset">
								<input id="username" type="hidden" name="username" path="username" readonly="${not empty user.username}" value="${user.username}"/>
								<div class="row">
									<div class="col-xs-12 col-sm-12 col-md-12">
										<div class="form-group">
											<label for="inputPassword">New Password</label>
											<div class="input-group">
												<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
												<input id="inputPassword" type="password" placeholder="New Password" name="password" class="form-control" required="">
											</div>
										</div>
									</div>
									<div class="col-xs-12 col-sm-12 col-md-12">
										<div class="form-group">
											<label for="inputConfirmPassword">Confirm New Password</label>
											<div class="input-group">
												<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
												<input id="inputConfirmPassword" type="password" placeholder="Confirm new Password" name="confirmPassword" class="form-control" required="">
											</div>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="input-group last">
										<button type="submit" class="btn btn-success btn-sm"><span class="fa fa-key"></span> Submit </button>
									</div>
								</div>
							</form>	
							<c:choose>
							<c:when test="${not empty  errors}">
								<div class="error">
								<c:forEach items="${errors}" var="err">
									${err.defaultMessage}
									<br/>
								</c:forEach>
								</div>
							</c:when>
						</c:choose>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../global/NavbarFooterFixed.jsp" />
</body>
</html>