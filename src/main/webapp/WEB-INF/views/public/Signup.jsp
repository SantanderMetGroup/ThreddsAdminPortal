<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
  	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<title>UDG-TAP Sign up</title>
  	<jsp:include page="../includes/script-jq-bs.jsp" />
  	<link rel="stylesheet" href="http://getbootstrap.com/examples/sticky-footer-navbar/sticky-footer-navbar.css">
  	<jsp:include page="../includes/css-bs-fa-main.jsp" />
	<script type="text/javascript">
		var RecaptchaOptions = {
    			theme : 'clean'
 		};
		$(document).ready(function() {
			$("#input-username").focus();
			$("#topNavbar ul:first li").each(function(){
				var href = $(this).children().attr('href');
				$(this).children().attr('href', "home" + href); 
			});
 			var select_options = $('#countrySelect option');
 			select_options.sort(function(a,b){
 				if(a.text > b.text) return 1;
 				else if(a.text < b.text) return -1;
 				else return 0;
 			});
 			$('#countrySelect').empty().append(select_options);
 			$('#countrySelect').prepend("<option value='' selected='selected'>Select your country</option>");
 			
		});
    </script>
    
  </head>
  <body class="body-background">
		
		<jsp:include page="../global/Navbar.jsp" />
		
		<div class="container">
			<div class="row">
				<div class="col-xs-12 col-md-6 col-md-offset-3">
					<div class="panel panel-default">
						<div class="panel-heading"> <strong class="">UDG-TAP Sign up</strong>
						</div>
						<div class="panel-body">
							<div class="col-xs-12">
								<form action="signup" method="post" role="form">
									<h5>Account details</h5>
									<hr class="fieldset-header">
									<div class="form-group">
										<label for="username">Username</label>
										<input class="form-control input-md" type="text" name="username" id="username" placeholder="Username" required tabindex="1">
									</div>
									<div class="form-group">
										<label for="email">Email</label>
										<input class="form-control input-md" type="email" name="email" id="email" placeholder="Email" required tabindex="2">
										<span class="help-block">Use your institutional email to access restricted datasets</span>
									</div>
									<div class="row">
										<div class="col-xs-12 col-sm-6 col-md-6">
											<div class="form-group">
												<label for="password">Password</label>
												<input type="password" name="password" id="password" class="form-control input-md" placeholder="Password" required tabindex="3">
											</div>
										</div>
										<div class="col-xs-12 col-sm-6 col-md-6">
											<div class="form-group">
												<label for="confirmPassword">Confirm Password</label>
												<input type="password" name="confirmPassword" id="confirmPassword" class="form-control input-md" placeholder="Confirm password" required tabindex="4">
											</div>
										</div>
									</div>
									<h5>Personal details</h5>
									<hr class="fieldset-header">
									<div class="row">
										<div class="col-xs-12 col-sm-6 col-md-6">
											<div class="form-group">
												<label for="firstName">First Name</label>
												<input type="text" name="firstName" id="firstName" class="form-control input-md" placeholder="First Name" required tabindex="5">
											</div>
										</div>
										<div class="col-xs-12 col-sm-6 col-md-6">
											<div class="form-group">
												<label for="lastName">Last name</label>
												<input type="text" name="LastName" id="lastName" class="form-control input-md" placeholder="Last Name" required tabindex="6">
											</div>
										</div>
									</div>
									<div class="form-group">
										<label for="countrySelect">Country</label>
										<select id="countrySelect" class="form-control input-md" name="isoCode" required tabindex="7">
										<c:forEach var="entry" items="${countryList}">
  										<option value='<c:out value="${entry.key}"/>'><c:out value="${entry.value}"/></option>
										</c:forEach>
										
										</select>
									</div>
									<h5>Research details</h5>
									<hr class="fieldset-header">
									<div class="form-group">
										<label for="institution">Institution</label>
										<input type="text" name="institution" id="institution" class="form-control input-md" placeholder="Institution" required tabindex="8">
									</div>
									<div class="form-group">
										<label for="motivation">Intended Usage</label>
										<textarea rows="2" name="motivation" class="form-control input-md" placeholder="Intended Usage" required tabindex="9"></textarea>
									</div>
									<hr>
									<div class="form-group">
										<div class="g-recaptcha" data-sitekey="${recaptchaKey}" tabindex="10"></div>
										<div class="error">${invalidCaptcha}</div> 
									</div>
									<div class="form-group">
										<div class="checkbox">
											<label><input type="checkbox" name="termsofuse" tabindex="11"> I accept <a href="terms" target="_blank">the Terms of use</a></input></label>
										</div>
									</div>

									<div class="form-group">
										<div class="checkbox">
											<label><input type="checkbox" name="newsletter" checked tabindex="12"> Sign up for our newsletter</label>
										</div>
									</div>
									<div class="form-group">
										<button type="submit" class="btn btn-info btn-mg"><span class="glyphicon glyphicon-user"></span> Sign up</button>
									</div>
								</form>		
							</div>
						</div>
					</div>
				</div>
			</div>
		</div> <!-- end container -->
		<jsp:include page="../global/NavbarFooter.jsp" />
		<script src="https://www.google.com/recaptcha/api.js" async defer></script>
  </body>
</html>