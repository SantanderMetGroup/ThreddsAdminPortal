<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="baseURL" value="${pageContext.servletContext.contextPath}"/>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
  	<meta name="viewport" content="width=device-width, initial-scale=1">
    <title>UDG-TAP Home</title>
    <jsp:include page="../includes/script-jq-bs.jsp" />
	<jsp:include page="../includes/css-bs-fa-main.jsp" />
	<link rel="stylesheet" type="text/css" href="${baseURL}/resources/styles/style-home.css">
 </head>
  <body class="body-background">
	  
	  <jsp:include page="../global/Navbar.jsp" />
	  
	  <header id="header" class="container-image">
		  <div class="header-content">
			  <div class="header-content-inner">
				<img src="${baseURL}/resources/img/tap-logo.png" style="margin-top: -7px;">
				  <h1 style="font-size: 65px;">UDG-TAP</h1>
				  <hr>
				  <p>The User Data Getaway - Thredds Access Portal (UDG-TAP) is the user authorization web application which controls user access to scientific data resources exposed in a Thredds Data Server. </p>
				  <a href="#services" class="btn btn-inverse btn-xl scroll">GET STARTED!</a>
			  </div>
		  </div>
	  </header>
	  
  	  <section id="services" class="inverse">
		  <div class="container">
			  <div class="row">
				  <div class="col-lg-12 section-heading text-center ">
					  <h2>
						  UDG Services
					  </h2>
					  <hr class="light">
			  </div>
  			  <div class="row">
				  <div class="col-lg-3 col-lg-offset-2">
					  <div class="service-box text-center">
							<i class="fa fa-4x fa-database" style="visibility: visible; animation-name: bounceIn; color: #F8F8F8"></i>
							<h3>UDG-TDS</h3>
							<p>Thredds Data Server is the tool to explore and access datasets in UDG. <a class="light-blue" target="_blank" href="https://meteo.unican.es/trac/wiki/udg/download">(More info...)</a></p>
					  </div>
				  </div>
				  
  				  <div class="col-lg-3 col-lg-offset-2">
					  <div class="service-box text-center">
							<i class="fa fa-4x fa-user" style="visibility: visible; animation-name: bounceIn; color: #F8F8F8"></i>
							<h3>UDG-TAP</h3>
							<p>Thredds Access Portal manages the interaction among users, datasets and its restrictions. <a class="light-blue" target="_blank" href="https://meteo.unican.es/trac/wiki/tap">(More info...)</a></p>
					  </div>
				  </div>
				  
				  <div class="col-lg-3 col-lg-offset-2">
					  <div class="service-box text-center">
							<i class="fa fa-4x fa-cogs" style="visibility: visible; animation-name: bounceIn; color: #F8F8F8"></i>
							<h3>UDG-APIs</h3>
							<p>APIs based in R packages are provided for remote data access. <a class="light-blue" target="_blank" href="https://meteo.unican.es/trac/wiki/udg/interfaces">(More info...)</a></p>
					  </div>
				  </div>
				  
  				  <div class="col-lg-3 col-lg-offset-2">
					  <div class="service-box text-center">
							<i class="fa fa-4x fa-book" style="visibility: visible; animation-name: bounceIn; color: #F8F8F8"></i>
							<h3>UDG-WIKI</h3>
							<p>The UDG Wiki contains all the knowledge needed to use UDG. <a class="light-blue" target="_blank" href="https://meteo.unican.es/trac/wiki/udg">(More info...)</a></p>
					  </div>
				  </div>
			  </div>
		  </div>
		  </div>	  
	  </section>
	  
	  <section id="features" >
		  <div class="container">
			  <div class="row">
				  <div class="section-heading text-center">
					  <h2 class>UDG-TAP Features</h2>
					  <hr class="primary">
				  </div>
			  </div>
			  <div class="row">
				  <div class="col-md-3">
					  <div class="service-box text-center">
							<i class="fa fa-4x fa-user" style="visibility: visible; animation-name: bounceIn; color: #262626"></i>
							<h3>User Registration</h3>
							<p class="text-muted">Anyone can register in UDG-TAP but you need your institutional email account to access to restricted datasets. You can also Log in directly using your OpenID.</p>
					  </div>
				  </div>
				  
				  <div class="col-md-3">
					  <div class="service-box text-center">
							<i class="fa fa-4x fa-users" style="visibility: visible; animation-name: bounceIn; color: #262626"></i>
							<h3>Groups</h3>
							<p class="text-muted">Datasets are organised in groups which corresponds to projects, interests, etc. There are two types: open and restricted access.</p>
					  </div>
				  </div>
				  <div class="col-md-3">
					  <div class="service-box text-center">
							<i class="fa fa-4x fa-lock" style="visibility: visible; animation-name: bounceIn; color: #262626"></i>
							<h3>Dataset restrictions</h3>
							<p class="text-muted">Some datasets have policy acceptance. That means, before applying for a group, you must accept the policies of the datasets in that group.</p>
					  </div>
				  </div>
				  <div class="col-md-3">
					  <div class="service-box text-center">
							<i class="fa fa-4x fa-key" style="visibility: visible; animation-name: bounceIn; color: #262626"></i>
							<h3>User Dataset Access</h3>
							<p class="text-muted">Restricted access groups are managed by a coordinator. The coordinator is usually a person who is involved in the group.</p>
					  </div>
				  </div>
			  </div>
		  </div>
	  </section>
	  
	  <footer id="contact" class="inverse">
	  	<div class="footer">
		  	<div class="container">
		  		<div class="row">
		  			<div class="col-md-12">
		  				<div class="footer-content">
		  					<div class="col-md-6">
			  					<div class="footer-brand">
									<a class="" rel="home" href="http://meteo.unican.es" title="University of Cantabria Meteo Group">
										<img style="padding: 10px 0; max-width:140px;" src="${baseURL}/resources/img/ucmg-logo.png">
									</a>
								</div>
								<div class="row">
									<div class="col-md-10">
										<p> University of Cantabria Meteorology Group.</p>
										<p> Group formed by professors and researchers from the University of Cantabria (UC, Dept. of Applied Mathematics and Computer Science) and the National Research Council (CSIC and IFCA)
										<ul class="icon-list">
											<li class="twitter">
												<a class="btn btn-default btn-circle btn-md" href="https://twitter.com/SantanderMeteo" target="_blank"><i class="fa fa-twitter"></i> Follow us on Twitter</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
							<div class="col-md-4 col-md-offset-2">
								<div class="row">
									<div class="headline">
										<h4>Contact</h4>
									</div>
									<address>
										<ul class="icon-list" class="text-clear">
											<li>
												<i class="fa fa-user"></i> University of Cantabria Meteorology Group
											</li>
											<li>
												<i class="fa fa-map-marker"></i> Avda. de los Castros, s/n.
											</li>
											<li>
												<i class="fa fa-globe"></i> Cantabria, Spain
											</li>
											<li>
												<i class="fa fa-envelope-o"></i> meteo@unican.es 
											</li>
											<li>
												<i class="fa fa-bug"></i> <a href="https://meteo.unican.es/trac/query">Meteo trac system</a>
											</li>
										</ul>
									</address>
								</div>
							</div>
		  				</div>
		  			</div>
		  		</div>
		  	</div>
	  	</div>
	  </footer>

	<script type="text/javascript">
			$('.scroll').click(function() {
				var href= $(this).attr('href');
				$('html,body').animate({
        			scrollTop: $(href).offset().top},'slow');
				$("#topNavbar ul:first li").each(function(){
					$(this).removeClass("active");
					if($(this).children().attr('href') === href){
						$(this).addClass("active");
					}
				});
			});  
	</script>
  </body>
</html>