<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
 	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title><spring:message code="app.menu.title.datasets" /></title>
	<jsp:include page="../includes/css-bs-fa-main.jsp" />
	<jsp:include page="../includes/script-jq-bs.jsp" />
    <script type="text/javascript" src="${pageContext.servletContext.contextPath}/resources/js/common/functions.js"></script>
	<script src="${pageContext.servletContext.contextPath}/resources/js/user/datasets.js"></script>
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
			<h1><spring:message code="app.menu.title.datasets"/></h1>
			<p> You can check your authorized datasets given by the groups you belong. Pending of acceptance datasets will be shown here.</p>
 		</div>
	</div>		
		
    <div class="container" style="padding-bottom: 65px" >    		
		<div class="row">
			<div id="acceptancePendingDatasets" class="col-xs-12 col-md-12">
			<c:if test="${fn:length(pendingOfAcceptRolesList) ne 0}">
				<h3><spring:message code="app.role.pending.accept"/></h3>
				<hr class="style-one"></hr>
				<c:forEach items="${pendingOfAcceptRolesList}" var="dataset">
					<div class="col-xs-6 col-sm-4 col-md-3 group-thumbnail" data-dataset-name="${dataset.roleName}">
						<div class="thumbnail thumbnail-mosaic">
							<p><i class="fa fa-file-code-o fa-3x" style="display:block;text-align:center"></i></p>
							<div class="caption">
								<c:choose>
									<c:when test="${not empty dataset.label}">
										<h4>${dataset.label}</h4>
									</c:when>
									<c:otherwise>
										<h4>${dataset.roleName}</h4>
									</c:otherwise>
								</c:choose>
								<p>Description: ${dataset.description}</p>
								<p>Code: ${dataset.roleName}</p>
								<p><a href="${dataset.datasetURL}">Dataset link</a></p>
								<input type="checkbox" name="policy-accepted">I accept the Terms of Use of this dataset.</input>
								<input type="checkbox" name="policy-declined" style="display:none;"></input>
							</div>
						</div>
					</div>								
				</c:forEach>
				
				<div class="row" style="padding-top: 10px;">
					<div class="col-xs-12 col-md-12">
						<button id="buttonSave" type="button" class="btn btn-success btn-pull-left"><spring:message code="app.action.submit"/></button> 
					</div>
				</div>
			</c:if>
			</div>
		</div>	
		<div class="row">
			<div id="authorizedDatasets" class="col-xs-12 col-md-12">
			<c:if test="${fn:length(userRolesList) ne 0}">
				<h3><spring:message code="app.dataset.current"/></h3>
				<hr class="style-one"></hr>
				<c:forEach items="${userRolesList}" var="dataset">
					<div class="col-xs-6 col-sm-4 col-md-3 group-thumbnail" data-dataset-name="${dataset.roleName}">
						<div class="thumbnail thumbnail-mosaic">
							<p><i class="fa fa-file-code-o fa-3x" style="display:block;text-align:center"></i></p>
							<div class="caption">
								<c:choose>
									<c:when test="${not empty dataset.label}">
										<h4>${dataset.label}</h4>	
									</c:when>
									<c:otherwise>
										<h4>${dataset.roleName}</h4>
									</c:otherwise>
								</c:choose>
								<p>Description: ${dataset.description}</p>
								<p>Code: ${dataset.roleName}</p>
								<p><a href="${dataset.datasetURL}">Go to this dataset</a></p>
								<button class="btn btn-default btn-xs btn-show-policies"><i class="fa fa-bookmark-o"></i> <spring:message code="app.role.policies"/></button>
							</div>
						</div>
					</div>								
				</c:forEach>
			</c:if>
		</div>
	</div>
	<jsp:include page="../global/NavbarFooterFixed.jsp" />	
</body>
</html> 