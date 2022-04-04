<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
	 	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title><spring:message code="app.menu.title.home"/></title>
		<jsp:include page="../includes/css-bs-fa-main.jsp" />
		<jsp:include page="../includes/script-jq-bs.jsp" />
	</head>
	<body>
	
		  	<jsp:include page="../global/Navbar.jsp" />

			<div class="page-heading">
			      <div class="container">
					<h1><spring:message code="app.home"/></h1>
		   			<p> You will find here general information about our services and how to start to use them.</p>
			      </div>
			</div>		
			<div style="padding-bottom:65px;" class="container">
				<div class="row">
					<div id="elements">
						<article>
							<h3>Thredds Access Portal</h3>
							<hr class="style-one"></hr>
							<p>
								The <a class="link blue-link" href="http://meteo.unican.es/en/dataservices">data services</a> provided by <a class="link blue-link" href="http://www.meteo.unican.es">University of Cantabria Meteorology Group (UCMG)</a>
								built on a THREDDS Data Server (TDS) implement user authentication and data authorization protocol via THREDDS Administration Panel (<a class="link blue-link" href="http://www.meteo.unican.es/tap">TAP</a>).
								Thus, registration in TAP is required to access different datasets. 
							</p>
							<p>
								The authentication scheme is defined in terms of groups (e.g. VALUE, CORDEX, EUPORIAS, SPECS, NACLIM, etc.) which provide access to specific datasets 
								(e.g. ERA-Interim for downscaling, System4, WIFEDI, etc.). All public datasets are included under the PUBLIC group.
							</p>
							
							<h3>Group Membership</h3>
							<hr class="style-one"></hr>
							<p>
								The available datasets are listed in the <em>Groups</em> menu, which also shows the corresponding data policies. For instance, three public datasets are available in the PUBLIC group: WFDEI (WATCH with  ERA-Interim) gridded observations, NCEP-NCAR reanalysis and CFSv2 seasonal hindcast. VALUE and CORDEX-ESD groups provide access to a subset of predictors from ERA-Interim reanalysis commonly used for statistical downscaling. Finally, the three groups related to the  ECOMS projects (EUPORIAS, SPECS, NACLIM) provide access to different seasonal forecasting products.
							</p>
							
							<h3> Data Access </h3>
							<hr class="style-one"></hr>
							<p>
								Users can access the authorized datasets using the standard THREDDS services (e.g. OPeNDAP and NCSS). However, some interfaces have been also developed in order to remotely access subsets of the datasets from scientific packages (mainly in R; see the <a class="link" href="https://github.com/santanderMetGroup/downscaleR">loadeR</a> package).
							</p>
	
						</article>
	
					</div>
				</div>
			</div>
			<jsp:include page="../global/NavbarFooterFixed.jsp" />
	</body>
</html>