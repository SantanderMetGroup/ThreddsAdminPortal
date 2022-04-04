<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1-transitional.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<title><spring:message code="app.role.title" /></title>
</head>
<body>
		<c:choose>
			<c:when test="${not empty  errors}">
			    <div class="error">
			    <c:forEach items="${errors}" var="err">
			        ${err.defaultMessage}
			        <br/>
			    </c:forEach>
			    </div>
			</c:when>
			</c:choose>
		<form:form method="POST" commandName="role" action="recordRole">
			<form:errors path="*" cssClass="errorblock" element="div" />
			<spring:message code="app.role.name"/><form:input path="roleName" disabled="${not empty role.roleName}"/><form:errors path="roleName" cssClass="error" /><br/>
			<spring:message code="app.role.description"/> <form:input type="text" path="description" /> <form:errors path="description" cssClass="error"/><br/>
			<spring:message code="app.role.needadmin"/> <form:checkbox path="needAdmin" /><form:errors path="needAdmin" cssClass="error"/><br/>
			<spring:message code="app.action.save" var="saveMessage"/>
			<input type="submit" value="${saveMessage}"/>
		</form:form>
</body>
</html>