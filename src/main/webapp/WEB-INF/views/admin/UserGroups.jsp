<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<title><spring:message code="app.menu.title.groups" /></title>
	<c:set var="baseURL" value="${pageContext.servletContext.contextPath}" />
	<script type="text/javascript">
		var username = "${user.username}";
		var role = "${role}";
		var availableProjects = new Array();
		var deletedGroups = new Array();
		<c:forEach items="${availableGroupsList}" var="group">
			availableProjects.push("${group.groupName}");
		</c:forEach>
		$('#dialog-content').find('#content').css({"padding-left":"0px", "padding-top":"0px"});
	</script>
	<script type="text/javascript" src="${baseURL}/resources/js/common/functions.js"></script>
	<script type="text/javascript" src="${baseURL}/resources/js/admin/userGroups.js"></script>
</head>
<body>
	<div class="container" style="width:700px !important;">
		<div class="row">
			<div class="col-xs-12 col-md-6" style="padding: 0 0 35px 15px;" id="groupFilter">
				<h3><spring:message code="app.group.select"/></h3>
				<hr class="style-one"></hr>
				<p></p>
				<label for="selectGroup">Select group(s)</label>
				<select id="selectGroup" class="form-control custom">
					<c:forEach items="${availableGroupsList}" var="group">
						<option value="${group.groupName}">${group.groupName}</option>
					</c:forEach>
				</select>
			</div>	
		</div>
		<div class="row">		
			<div class="col-xs-12 col-md-12">
				<div id="selectedGroups">
				</div>
			</div>
		</div>
		<div class="row" style="padding-top: 10px;">
			<div class="col-xs-12 col-md-12">
			<button id="buttonSaveSelected" type="button" class="btn btn-success btn-pull-left btn-save"><spring:message code="app.action.submit"/></button> 
			</div>
		</div>

		<div class="row">
			<div id="authPendingGroups" class="col-xs-12 col-md-12">
			<c:if test="${fn:length(pendingOfAuthGroupsList) ne 0}">
				<h3><spring:message code="app.group.auth.pending"/></h3>
				<hr class="style-one"></hr>
				<c:forEach items="${pendingOfAuthGroupsList}" var="group">
					<div class="col-xs-6 col-sm-6 col-md-6 group-thumbnail" data-group-name="${group.groupName}">
						<div class="thumbnail">
							<p><i class="fa fa-users fa-3x" style="display:block;text-align:center"></i></p>
							<hr>
							<div class="caption">
								<h4>${group.groupName}</h4>
								<p>${group.description}</p>
								<input type="radio" name="decision" value="authorized">Authorize</input>
								<input type="radio" name="decision" value="rejected">Reject</input>
							</div>
						</div>
					</div>								
				</c:forEach>
				
				<div class="row" style="padding-top: 10px;">
					<div class="col-xs-12 col-md-12">
						<button id="buttonSavePending" type="button" class="btn btn-success btn-pull-left btn-save"><spring:message code="app.action.submit"/></button> 
					</div>
				</div>
			</c:if>
			</div>
		</div>
		
		<div class="row">
			<div id="authorizedGroups" class="col-xs-12 col-md-12">
			<c:if test="${fn:length(userGroupsList) ne 0}">
				<h3><spring:message code="app.group.auth.current"/></h3>
				<hr class="style-one"></hr>
				<c:forEach items="${userGroupsList}" var="group">
					<div class="col-xs-6 col-sm-4 col-md-4 group-thumbnail" data-group-name="${group.groupName}">
					    <div class="thumbnail">
					      <i class="fa fa-users fa-3x" style="display:block;text-align:center"></i>
					      <div class="caption">
					        <h4>${group.groupName}</h4>
					        <p>${group.groupName}</p>
					        <p><button class="btn btn-primary btn-xs btn-remove-auth"><i class="glyphicon glyphicon-remove"></i><spring:message code="app.action.remove"/></button></p>
					      </div>
					    </div>
					  </div>
				</c:forEach>
			</c:if>
			</div>	
		</div>
	</div>
</body>
</html> 