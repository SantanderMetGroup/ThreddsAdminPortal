<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
 	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
 	<!-- Global variables -->
 	<c:set var="baseURL" value="${pageContext.servletContext.contextPath}" />
	<title><spring:message code="app.menu.title.groups" /></title>
   	<jsp:include page="../includes/script-jq-bs.jsp" />
   	<jsp:include page="../includes/css-bs-fa-main.jsp" />
    <script type="text/javascript" src="${baseURL}/resources/js/common/functions.js"></script>
    <script type="text/javascript" src="${baseURL}/resources/js/user/groups.js"></script>
  	<link rel="stylesheet" href="${baseURL}/resources/libraries/bootstrap-select-1.7.2/dist/css/bootstrap-select.css">
    <script type="text/javascript" src="${baseURL}/resources/libraries/bootstrap-select-1.7.2/dist/js/bootstrap-select.js"></script>
	<script type="text/javascript">
		var username = "${user.username}";
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
			<h1><spring:message code="app.menu.title.groups"/></h1>
   			<p> Dataset access is organized by groups. Some groups are moderated and require the acceptance of your request by a manager.</p>
	      </div>
	</div>		
	
    <div style="padding-bottom: 65px " class="container">
		<div id="select" style="display:none">
			<c:if test="${not empty availableDatasetsList}">
				<div id="filter" class="row">
					<div class="col-xs-12 col-md-6" style="padding: 0 0 0 15px;" id="groupFilter">
						<h3><spring:message code="app.group.selection"/></h3>
						<hr class="style-one"></hr>
						<p></p>
						<label for="selectDataset">Group filtering by dataset</label>
						<select id="selectDataset" class="form-control custom" data-live-search="true" multiple>
							<c:forEach items="${availableDatasetsList}" var="dataset">
								<c:if test="${dataset.isPrivate eq false }">
									<option data-icon="fa fa-file-code-o">${dataset.roleName}</option>
								</c:if>
							</c:forEach>
						</select>
						<span class="help-block">
							Filter the above groups selector by the selected dataset.
						</span>
					</div>	
				</div>
			</c:if>
			<div class="row">
				<div class="col-xs-12 col-md-6" style="padding: 35px 0 35px 15px;" id="groupSelection">
					<label for="selectGroup">Select the group(s) you want to join</label>
					<select id="selectGroup" class="form-control custom" data-live-search="true">
						<c:forEach items="${availableGroupsList}" var="group">
							<option data-icon="fa fa-users" value="${group.groupName}">${group.groupName}</option>
						</c:forEach>
					</select>
					<span class="help-block">
						Please, do not select groups randomly. Try to join groups accordingly to the projects you belong. 
					</span>
				</div>
			</div>	
			<div class="row">		
				<div class="col-xs-12 col-md-12">
					<p> You have <span id="groupCounter">0</span> group(s) selected</p>
					<div id="selectedGroups">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 col-md-12" style="padding-top: 10px;">
					<button id="buttonSave" type="button" class="btn btn-info btn-pull-left"><spring:message code="app.action.submit"/></button> 
				</div>
			</div>		
		</div>
		
		
		<div  style="padding: 0 0 35px 15px;display:none;" class="row" id="pending">
			<h3><spring:message code="app.group.auth.pending"/></h3>
			<hr class="style-one"></hr>
			<c:if test="${fn:length(pendingOfAuthGroupsList) eq 0}">
				<p>There are no pending of authorization groups.</p>
			</c:if>
			<c:if test="${fn:length(pendingOfAuthGroupsList) ne 0}">
				<p>  This groups are moderated access. Pending of Approval groups will show in this section until the moderator approves your request.</p>
				<div id="authPendingGroups">
					<c:forEach items="${pendingOfAuthGroupsList}" var="group">
						<c:if test="${group.isProject}">
							<div class="col-xs-6 col-sm-4 col-md-3 group-thumbnail" data-group-name="${group.groupName}">
								<div class="thumbnail thumbnail-mosaic">
									<img src="../resources/img/logos/logo_${fn:toLowerCase(group.groupName)}.png"  alt="Logo ${fn:toLowerCase(group.groupName)}">
									<hr>
									<div class="caption">
										<h4>${fn:toLowerCase(group.groupName)} group</h4>
										<p>${group.description}</p>
										<button class="btn btn-default btn-xs btn-show-datasets"><i class="fa fa-file-code-o"></i> <spring:message code="app.role.datasets"/></button> 
										<button class="btn btn-default btn-xs btn-show-policies"><i class="fa fa-bookmark-o"></i> <spring:message code="app.role.policies"/></button>
										<button class="btn btn-primary btn-xs btn-leave"><i class="fa fa-times"></i> <spring:message code="app.action.cancel"/></button> 
									</div>
								</div>
							</div>
						</c:if>
					</c:forEach>
				</div>
			</c:if>
		</div>
		<div  style="padding-left:15px; display:none" class="row" id="authorized">

			<h3><spring:message code="app.group.auth.current"/></h3>
			<hr class="style-one"></hr>
			<c:if test="${fn:length(userGroupsList) eq 0}">
				<p>There are no authorized groups.</p>
			</c:if>
			<c:if test="${fn:length(userGroupsList) ne 0}">
				<p> You have access to the datasets in these groups.</p>
				<div id="authorizedGroups">
					<c:forEach items="${userGroupsList}" var="group">
						<c:if test="${group.isProject}">
							<div class="col-xs-6 col-sm-4 col-md-3 group-thumbnail" data-group-name="${group.groupName}">
								<div class="thumbnail thumbnail-mosaic">
									<img src="../resources/img/logos/logo_${fn:toLowerCase(group.groupName)}.png"  alt="Logo ${fn:toLowerCase(group.groupName)}">
									<hr>
									<div class="caption">
										<h4>${fn:toLowerCase(group.groupName)} group</h4>
										<p>${group.description}</p>
										<button class="btn btn-default btn-xs btn-show-datasets"><i class="fa fa-file-code-o"></i> <spring:message code="app.role.datasets"/></button> 
										<button class="btn btn-default btn-xs btn-show-policies"><i class="fa fa-bookmark-o"></i> <spring:message code="app.role.policies"/></button>
										<button class="btn btn-primary btn-xs btn-leave"><i class="fa fa-times"></i> <spring:message code="app.action.leave"/></button> 
									</div>
								</div>
							</div>
						</c:if>
					</c:forEach>
				</div>
			</c:if>
		</div>
	</div>
	<jsp:include page="../global/NavbarFooterFixed.jsp" />		

</body>
</html> 