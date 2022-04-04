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
		<section>
			<article class="element">
				<c:forEach items="${rolePolicies}" var="policy">
					<div class="policy-content">
						<div class="policy-title">
							<img src="../resources/icons/agreement_32px.png" alt="agreement_image" />
	  						${policy.policyName} agreement
	  					</div>
	  					<div class="policy-agreement"> ${policy.agreement} </div>
	  				</div>
				</c:forEach>
			</article>
		</section>
	</body>
</html>