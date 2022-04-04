<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	 	<!-- Global variables -->
	 	<c:set var="baseURL" value="${pageContext.servletContext.contextPath}" />
		<title><spring:message code="app.menu.title.groups" /></title>
		<jsp:include page="../includes/css-bs-fa-main.jsp" />
		<jsp:include page="../includes/script-jq-bs.jsp" />
		<jsp:include page="../includes/jtable.jsp" />
		<script src="${baseURL}/resources/js/common/functions.js" type="text/javascript"></script>
		<script src="${baseURL}/resources/js/tables/groupsTable.js" type="text/javascript"></script>
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
				<p><spring:message code="app.lorem.sentence"/></p>
	 		</div>
		</div>	
		
	    <div style="padding-top:20px;padding-bottom: 65px" class="container">
			 <div id="searchContainer" class="row" style="padding: 15px 0;">    
				<div class="col-xs-8 col-md-6 col-lg-5">
					<div class="input-group">
		          		<div class="input-group-btn search-panel">
		              		<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
			              		<span id="search_concept">Filter by</span> <span class="caret"></span>
			              	</button>
							<ul class="dropdown-menu" role="menu">
							  <li><a href="#username">Group name</a></li>
							</ul>
			         	</div>
						<input type="hidden" name="search_param" value="all" id="search_param">         
						<input id="inputSearch" type="text" class="form-control" name="x" placeholder="Search term..." disabled>
						<span class="input-group-btn">
						<button type="button" class="btn btn-default btn-search">
    						<span class="glyphicon glyphicon-search"></span> Search
						</button>
			          </span>
			      </div>
		  		</div>
			</div>
	    	<div style="padding-left:15px" class="row" id="GroupTableContainer">
	    	</div>
	    </div>
		<jsp:include page="../global/NavbarFooterFixed.jsp" />	
	</body>
</html>