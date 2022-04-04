<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head>
	 	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<style type="text/css">
  			.policy-content{
  				border: 1px solid #CCCCCC;
  				margin-bottom: 25px;
  				margin-top: 5px;
  				padding: 15px;
  			}
  			.policy-title *{
  				display: block;
  				float: left;
  			}
  			.policy-agreement{
  				clear: both;
  				padding-top: 15px;
  			}
  		</style>
	</head>
	<body>
		<div>
			<c:forEach items="${groupRoles}" var="dataset">
				<div class="thumbnail">
					<c:choose>
						<c:when test="${not empty dataset.label}">
							<span class="title">Dataset: ${dataset.label}</span> <br/>	
						</c:when>
						<c:otherwise>
							<span class="title">Dataset: ${dataset.roleName}</span> <br/>
						</c:otherwise>
					</c:choose>
					<span class="description">Description: ${dataset.description}</span> <br/>
					<span class="code">Code: ${dataset.roleName}</span> <br/>  
					<c:if test="${not empty dataset.datasetURL}">
						<span class="description">Link: <a target="_blank" href="${dataset.datasetURL}">${dataset.datasetURL}</a></span>
					</c:if>
				</div>		
				<c:if test="${empty rolePoliciesMap[dataset.roleName]}">
	  				<p>This dataset does not have any policy to accept.</p>
				</c:if>
				<c:forEach items="${rolePoliciesMap[dataset.roleName]}" var="policy">
					<div class="policy-content">
						<div class="policy-title">
							<img src="../resources/icons/agreement_32px.png" alt="agreement_image" />
	  						${policy.policyName} agreement
	  					</div>
	  					<div class="policy-agreement"> ${policy.agreement} </div>
	  				</div>
  				</c:forEach>
			</c:forEach>
		</div>
	</body>
</html>