<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<c:set var="baseURL" value="${pageContext.servletContext.contextPath}" />
   	<script type="text/javascript">
		var roleName = "${roleName}";
	</script>
	<script type="text/javascript" src="${baseURL}/resources/js/admin/rolePolicies.js"></script>
</head>
<body>
	<div class="container" style="width:700px !important;">
		<div class="row">
			<div class="col-xs-12 col-md-6" style="padding: 0 0 35px 15px;">
				<h3><spring:message code="app.policy.select"/></h3>
				<hr class="style-one"></hr>
				<p></p>
				<label for="selectPolicy"><spring:message code="app.policy.select"/></label>
				<select id="selectPolicy" class="form-control custom">
					<c:forEach items="${availablePoliciesList}" var="policy">
						<option value="${policy.policyName}">${policy.policyName}</option>
					</c:forEach>
				</select>
			</div>	
		</div>
		<div class="row">		
			<div class="col-xs-12 col-md-12">
				<div id="selectedPolicies">
				</div>
			</div>
		</div>
		<div class="row" style="padding-top: 10px;">
			<div class="col-xs-12 col-md-12">
			<button id="buttonSaveSelected" type="button" class="btn btn-success btn-pull-left btn-save"><spring:message code="app.action.submit"/></button> 
			</div>
		</div>
		
		<div class="row">
			<div id="rolePolicies" class="col-xs-12 col-md-12">
			<c:if test="${fn:length(rolePoliciesList) ne 0}">
				<h3><spring:message code="app.role.policies"/></h3>
				<hr class="style-one"></hr>
				<c:forEach items="${rolePoliciesList}" var="policy">
					<div class="col-xs-6 col-sm-4 col-md-4 group-thumbnail" data-policy-name="${policy.policyName}">
					    <div class="thumbnail">
					      <i class="fa fa-cog fa-3x" style="display:block;text-align:center"></i>
					      <div class="caption">
					        <h4>${policy.policyName}</h4>
					        <p>${policy.description}</p>
					        <p><button class="btn btn-primary btn-xs btn-remove"><i class="glyphicon glyphicon-remove"></i><spring:message code="app.action.remove"/></button></p>
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