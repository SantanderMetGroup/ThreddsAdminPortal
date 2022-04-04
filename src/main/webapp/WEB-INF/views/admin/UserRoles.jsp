<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<title><spring:message code="app.role.title" /></title>
	<script type="text/javascript">
		<c:set var="message" value="${user.username}"/> 
		var username = '<c:out value="${message}"/>';
	</script>
	<script type="text/javascript" src="../resources/libraries/js-url-2.0.2/url.min.js"></script>
	<script type="text/javascript" src="../resources/js/admin/userRoles.js"></script>
</head>
<body>

	<div class="container" style="width:700px !important;">
		<div class="row">
			<div class="col-xs-12 col-md-6" style="padding: 0 0 35px 15px;" id="groupFilter">
				<h3><spring:message code="app.role.select"/></h3>
				<hr class="style-one"></hr>
				<p></p>
				<label for="selectRole">Select role(s)</label>
				<select id="selectRole" class="form-control custom">
					<c:forEach items="${availableRolesList}" var="role">
						<option value="${role.roleName}">${role.roleName}</option>
					</c:forEach>
				</select>
			</div>	
		</div>
		<div class="row">		
			<div class="col-xs-12 col-md-12">
				<div id="selectedRoles">
				</div>
			</div>
		</div>
		<div class="row" style="padding-top: 10px;">
			<div class="col-xs-12 col-md-12">
			<button id="buttonSaveSelected" type="button" class="btn btn-success btn-pull-left btn-save"><spring:message code="app.action.submit"/></button> 
			</div>
		</div>
		
		<div class="row">
			<div id="acceptPendingRoles" class="col-xs-12 col-md-12">
			<c:if test="${fn:length(pendingOfAcceptRolesList) ne 0}">
				<h3><spring:message code="app.role.pending.accept"/></h3>
				<hr class="style-one"></hr>
				<c:forEach items="${pendingOfAcceptRolesList}" var="role">
					<div class="col-xs-6 col-sm-4 col-md-4 group-thumbnail" data-role-name="${role.roleName}">
					    <div class="thumbnail thumbnail-mosaic">
					      <i class="fa fa-cog fa-3x" style="display:block;text-align:center"></i>
					      <div class="caption">
					        <h4>${role.roleName}</h4>
					        <p>${role.description}</p>
					        <p><button class="btn btn-primary btn-xs btn-remove-auth"><i class="glyphicon glyphicon-remove"></i><spring:message code="app.action.remove"/></button></p>
					      </div>
					    </div>
					  </div>
				</c:forEach>
			</c:if>
			</div>
		</div>
		<div class="row">
			<div id="authPendingRoles" class="col-xs-12 col-md-12">
			<c:if test="${fn:length(pendingOfAuthRolesList) ne 0}">
				<h3><spring:message code="app.role.pending.auth"/></h3>
				<hr class="style-one"></hr>
				<c:forEach items="${pendingOfAuthRolesList}" var="role">
					<div class="col-xs-6 col-sm-6 col-md-6 group-thumbnail" data-role-name="${role.roleName}">
						<div class="thumbnail thumbnail-mosaic">
							<p><i class="fa fa-cog fa-3x" style="display:block;text-align:center"></i></p>
							<hr>
							<div class="caption">
								<h4>${role.roleName}</h4>
								<p>${role.description}</p>
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
			<div id="authorizedRoles" class="col-xs-12 col-md-12">
			<c:if test="${fn:length(userRolesList) ne 0}">
				<h3><spring:message code="app.role.current"/></h3>
				<hr class="style-one"></hr>
				<c:forEach items="${userRolesList}" var="role">
					<div class="col-xs-6 col-sm-4 col-md-4 group-thumbnail" data-role-name="${role.roleName}">
					    <div class="thumbnail thumbnail-mosaic">
					      <i class="fa fa-cog fa-3x" style="display:block;text-align:center"></i>
					      <div class="caption">
					        <h4>${role.roleName}</h4>
					        <p>${role.description}</p>
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