<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript">
	$(document).ready(function(){
		var url = document.documentURI;
		var value = url.substring(url.lastIndexOf("/") + 1);
	});
</script>
<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="container col-md-8 col-md-offset-2">
		 <div class="navbar-header">
			  <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#topNavbar">
				  <span class="icon-bar"></span>
				  <span class="icon-bar"></span>
				  <span class="icon-bar"></span> 
			  </button>
			  <a class="navbar-brand" href="https://meteo.unican.es/udg-tap">
			  	<img style="max-height: 35px; max-width:100px; margin-top: -7px;" src="${pageContext.servletContext.contextPath}/resources/img/tap-logo.png">
			  </a>
		 </div>
		 <c:choose>
		
			<c:when test="${fn:contains(pageContext.request.requestURL, 'public')}">
				<div class="collapse navbar-collapse" id="topNavbar">
				  <ul class="nav navbar-nav">
					  <li><a class="scroll" href="#header">Home</a></li>
					  <li><a class="scroll" href="#services">Services</a></li>
					  <li><a class="scroll" href="#features">Features</a></li>
					  <li><a class="scroll" href="#contact">Contact</a></li>
				  </ul>
				  <ul class="nav navbar-nav navbar-right">
					<li><div><a href="signup" style="margin-left:10px" class="btn btn-info navbar-btn"><span class="glyphicon glyphicon-user"></span> Sign up</a></div></li>
					<li><div><a href="signin" style="margin-left:10px" class="btn btn-success navbar-btn"><span class="glyphicon glyphicon-log-in"></span> Sign in</a></div></li>
				  </ul>
		 		</div>
		 	</c:when>
		 	<c:otherwise>
			 	<div class="collapse navbar-collapse" id="topNavbar">
					<ul class="nav navbar-nav">
						<li>
							<a id="#home" class="scroll" href="../user/home"><span><spring:message code="app.menu.title.home"/></span></a>
						</li>
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="app.menu.title.groups"/> <b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li>
									<a id="#select" class="scroll" href="../user/groups#select"><span>Select</span></a>
								</li>
								<li>
									<a id="#pending" class="scroll" href="../user/groups#pending"><span>Authorization Pending</span></a>
								</li>
								<li>
									<a id="#authorized" class="scroll" href="../user/groups#authorized"><span>Authorized</span></a>
								</li>
							</ul>	
						</li>
						<li>
							<a id="#dataset" class="scroll" href="../user/datasets"><span><spring:message code="app.menu.title.datasets"/></span></a>
						</li>

						<c:if test="${not empty role && role == 'ROLE_ADMIN'}">
							<li class="dropdown">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown">Administration <b class="caret"></b></a>
								<ul class="dropdown-menu">
									<li>
										<a id="#userPanel" class="scroll" href="../admin/users"><span><spring:message code="app.admin.panel.user.title"/></span></a>
									</li>
									<li>
										<a id="#groupPanel" class="scroll" href="../admin/groups"><span><spring:message code="app.admin.panel.group.title"/></span></a>
									</li>
									<li>
										<a id="#rolePanel" class="scroll" href="../admin/roles"><span><spring:message code="app.admin.panel.role.title"/></span></a>
									</li>
									<li>
										<a id="#policyPanel" class="scroll" href="../admin/policies"><span><spring:message code="app.admin.panel.policy.title"/></span></a>
									</li>	
									<li>
										<a id="#messagePanel" class="scroll" href="../admin/message"><span><spring:message code="app.menu.title.message"/></span></a>
									</li>
								</ul>	
							</li>
						</c:if>
					</ul>
 					<ul class="nav navbar-nav navbar-right">
 						<li class="dropdown">
 							<a href="#" class="dropdown-toggle" data-toggle="dropdown">
 								<span class="glyphicon glyphicon-user"></span> ${user.username} <b class="caret"></b>
 							</a>
 							<ul class="dropdown-menu">
 								<li><a href="../user/account">Account</a></li>
 								<li><a href="<c:url value='../j_spring_security_logout' />"> Sign out</a></li>
 							</ul>
 						</li>
					</ul>
		 		</div>
		 	</c:otherwise>
		</c:choose>	
	</div>
</nav>