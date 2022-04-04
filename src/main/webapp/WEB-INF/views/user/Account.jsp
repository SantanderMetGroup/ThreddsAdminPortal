<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
 	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title><spring:message code="app.menu.title.account" /></title>
	
	<jsp:include page="../includes/script-jq-bs.jsp"/>
   	<jsp:include page="../includes/css-bs-fa-main.jsp" />


	<script type="text/javascript">
		$(document).ready(function(){
			$(document).ready(function(){
	 			var select_options = $('#countrySelect option');
	 			var option_selected = $('#countrySelect option:selected');
	 			select_options.sort(function(a,b){
	 				if(a.text > b.text) return 1;
	 				else if(a.text < b.text) return -1;
	 				else return 0;
	 			});
	 			$('#countrySelect').empty().append(select_options);
	 			$("#countrySelect").val(option_selected.val());
 			});
		});
	</script>
</head>
<body>
  	<jsp:include page="../global/Navbar.jsp" />
	
	<div id="dialog-wrapper"> 
	    <div id="dialog">
	    	<div id="dialog-content">
	    	</div>
	    </div>
	</div>
	
		<div class="page-heading">
	      <div class="container">
			<h1><spring:message code="app.menu.title.account"/></h1>
   			<p> Change your account details</p>
	      </div>
	</div>
	
    <div style="padding-top:20px;padding-bottom: 65px" class="container">
    		<div class="row">
    			<div class="col-xs-12 col-md-6 col-md-offset-3">
    				<div class="panel panel-default">
						<div class="panel-heading"> <strong class="">Change your email</strong>
						</div>
						<div class="panel-body">
							<div class="col-xs-12">
								<form action="account" method="post" role="form">
									<div class="form-group ${not empty errorsMap.get('email') ? 'has-error' : ''}">
										<input class="form-control input-md" type="hidden" name="action" id="action" value="change_email">
										<label class="control-label" for="email">Email</label>
										<input class="form-control input-md" type="email" name="email" id="email" placeholder="Email" required tabindex="2" value="${user.email}">
										<span class="help-block">
											${not empty errorsMap.get("email") ? errorsMap.get("email") : "Institutional email could be required to access some restricted datasets."}
										</span>
									</div>
									<div class="form-group">
										<button type="submit" class="btn btn-info btn-mg"><span class="glyphicon glyphicon-user"></span> Submit</button>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
    		</div>
    		
    		<div class="row">
    			<div class="col-xs-12 col-md-6 col-md-offset-3">
    				<div class="panel panel-default">
						<div class="panel-heading"> <strong class="">Change your password</strong>
						</div>
						<div class="panel-body">
							<div class="col-xs-12">
								<form action="account" method="post" role="form">
									<div class="row">
										<div class="col-xs-12 col-sm-6 col-md-6">
											<div class="form-group ${not empty errorsMap.get('password') ? 'has-error' : ''}">
												<input class="form-control input-md" type="hidden" name="action" id="action" value="change_password">
												<label for="password">Type your new Password</label>
												<input type="password" name="password" id="password" class="form-control input-md" placeholder="Password" required tabindex="3">
												<span class="help-block">
													${not empty errorsMap.get("password") ? errorsMap.get("password") : ""}
												</span>
											</div>
										</div>
										<div class="col-xs-12 col-sm-6 col-md-6">
											<div class="form-group ${not empty errorsMap.get('password') || not empty errorsMap.get('confirmPassword') ? 'has-error' : ''}">
												<label for="confirmPassword">Confirm Password</label>
												<input type="password" name="confirmPassword" id="confirmPassword" class="form-control input-md" placeholder="Confirm password" required tabindex="4">
												<span class="help-block">
													${not empty errorsMap.get("confirmPassword") ? errorsMap.get("confirmPassword") : ""}
												</span>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-xs-12 col-sm-6 col-md-6">
											<div class="form-group">
												<button type="submit" class="btn btn-info btn-mg"><span class="glyphicon glyphicon-user"></span> Submit</button>
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
    		</div>
    		
			<div class="row">
				<div class="col-xs-12 col-md-6 col-md-offset-3">
					<div class="panel panel-default">
						<div class="panel-heading"> <strong class="">Change personal and research details</strong>
						</div>
						<div class="panel-body">
							<div class="col-xs-12">
								<form action="account" method="post" role="form">
									<input class="form-control input-md" type="hidden" name="action" id="action" value="change_other">
									<c:if test="${success}">
										<div class="row">
											<div class="col-xs-12 col-sm-12 col-md-12">
												<div class="alert alert-success">
													<p><span class="glyphicon glyphicon-ok"></span> Successfully saved</p>
												</div>
											</div>
										</div>
									</c:if>
									<h5>Personal details</h5>
									<hr class="fieldset-header">
									<div class="row">
										<div class="col-xs-12 col-sm-6 col-md-6">
											<div class="form-group  ${not empty errorsMap.get('firstName')}">
												<label for="firstName">First Name</label>
												<input type="text" name="firstName" id="firstName" class="form-control input-md" placeholder="First Name" required tabindex="5" value="${user.firstName}">
												<span class="help-block">
													${not empty errorsMap.get("firstName") ? errorsMap.get("firstName") : ""}
												</span>
											</div>
										</div>
										<div class="col-xs-12 col-sm-6 col-md-6">
											<div class="form-group ${not empty errorsMap.get('lastName')}">
												<label for="lastName">Last name</label>
												<input type="text" name="LastName" id="lastName" class="form-control input-md" placeholder="Last Name" required tabindex="6" value="${user.lastName}">
												<span class="help-block">
													${not empty errorsMap.get("lastName") ? errorsMap.get("lastName") : ""}
												</span>
											</div>
										</div>
									</div>
									
									<div class="form-group">
										<label for="countrySelect">Country</label>
										<select id="countrySelect" class="form-control input-md" name="isoCode" required tabindex="7">
										<c:forEach var="entry" items="${countryList}">
  											<option value="${entry.key}" ${user.isoCode eq entry.key ? 'selected' : '' } />${entry.value}</option>
										</c:forEach>										
										</select>
									</div>
									
									<h5>Research details</h5>
									<hr class="fieldset-header">
									<div class="form-group ${not empty errorsMap.get('institution')}">
										<label for="institution">Institution</label>
										<input type="text" name="institution" id="institution" class="form-control input-md" placeholder="Institution" required tabindex="8" value="${user.institution}">
										<span class="help-block">
											${not empty errorsMap.get("institution") ? errorsMap.get("institution") : ""}
										</span>
									</div>
									<div class="form-group ${not empty errorsMap.get('motivation')}">
										<label for="motivation">Intended Usage</label>
										<textarea rows="2" name="motivation" class="form-control input-md" placeholder="Intended Usage" required tabindex="9">${user.motivation}</textarea>
										<span class="help-block">
											${not empty errorsMap.get("motivation") ? errorsMap.get("motivation") : ""}
										</span>
									</div>
									<hr></hr>
									<div class="form-group">
										<div class="checkbox" style="margin: 0 0 0 15px">
											<label><input type="checkbox" name="newsletter" ${user.newsletter ?  'checked' : '' } tabindex="12"> Receive newsletter</label>
										</div>
									</div>
									<div class="form-group">
										<button type="submit" class="btn btn-info btn-mg"><span class="glyphicon glyphicon-user"></span> Submit</button>
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