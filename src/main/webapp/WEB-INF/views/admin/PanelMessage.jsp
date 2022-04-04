<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
	 	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	 	<title><spring:message code="app.menu.title.message" /></title>
	 	<!-- Global variables -->
	    <c:set var="baseURL" value="${pageContext.servletContext.contextPath}" />
	   	<title><spring:message code="app.menu.title.message" /></title>
		<jsp:include page="../includes/css-bs-fa-main.jsp" />
		<jsp:include page="../includes/script-jq-bs.jsp" />
		<script type="text/javascript"src="../resources/libraries/jquery/select2-3.4.8/select2.js" type="text/javascript"></script>	
		<script type="text/javascript" src="../resources/libraries/jquery/ckeditor/ckeditor.js"></script>
		<script type="text/javascript" src="../resources/libraries/jquery/ckeditor/adapters/jquery.js"></script>
		<script type="text/javascript">
			var groupName = "${groupName}";
			var availableGroups = new Array();
			<c:forEach items="${availableGroupsList}" var="group">
			availableGroups.push("${group.groupName}");
			</c:forEach>
			$('.element-details').css({"padding-left":"0px", "padding-top":"0px"});
			$('#dialog-content').find('#content').css({"padding-left":"0px", "padding-top":"0px"});
		</script>
		<script type="text/javascript" src="../resources/js/admin/message.js"></script>
		<!-- GENERAL STYLING -->
		<link rel="stylesheet" type="text/css" href="${baseURL}/resources/libraries/jquery/select2-3.4.8/select2.css"/>
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
				<h1><spring:message code="app.menu.title.message"/></h1>
				<p>Send a message to your users by clicking a group.</p>
	 		</div>
		</div>	
		
			<div style="padding-top:20px;padding-bottom: 65px" class="row">
    			<div class="col-xs-12 col-md-6 col-md-offset-3">
    				<div class="panel panel-default">
						<div class="panel-heading"> <strong class="">Send a message to your groups</strong>
						</div>
						<div class="panel-body">
							<div class="col-xs-12">
								<form method="post" action="message" role="form"">
									<div class="form-group ${not empty errorsMap.get('email') ? 'has-error' : ''}">
										<label class="control-label" for="select-group"><spring:message code="app.mail.to"/> </label>
										<select id="select-groups" name="to[]" multiple class="multiple-select"></select>
									</div>
									<div class="form-group ${not empty errorsMap.get('email') ? 'has-error' : ''}">
										<label class="control-label" for="subject"><spring:message code="app.mail.subject"/> </label>
										<input id="subject" type="text" name="subject" class="form-control input-md"/>
									</div>
									
									<div class="form-group ${not empty errorsMap.get('email') ? 'has-error' : ''}">
										<label class="control-label" for="message"><spring:message code="app.mail.message"/> </label>
										<textarea id="message" name="message" class="form-control input-md html-editor"></textarea>
										<span class="help-block">
											Write your message without using Dear User.. and Best regards. ONLY THE MESSAGE YOU WANT TO COMUNICATE.
										</span>
									</div>
									<div class="form-group">
										<button type="submit" class="btn btn-info btn-mg"><span class="glyphicon glyphicon-user"></span> <spring:message code="app.action.send"/></button>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
    		</div>
		<jsp:include page="../global/NavbarFooterFixed.jsp" />	
	</body>
</html> 