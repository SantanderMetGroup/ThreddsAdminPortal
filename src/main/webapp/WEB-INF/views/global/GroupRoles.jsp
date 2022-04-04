<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<style type="text/css">
 			.title{
 				padding: 20px;
 			}
 			.description{
 				padding-left: 20px;
 				font-size: 0.875em;
 			}
 		</style>
	</head>
	<body>
		<div>
			<c:forEach items="${groupRoles}" var="dataset">
				<div class="thumbnail">
					<span class="title">Dataset ${dataset.roleName}</span> <br/>  
					<span class="description">${dataset.description}</span> <br/>
					<c:if test="${not empty dataset.datasetURL}">
						<span class="description"><a href="${dataset.datasetURL}">${dataset.datasetURL}</a></span>
					</c:if>
				</div>			
			</c:forEach>
		</div>
	</body>
</html>