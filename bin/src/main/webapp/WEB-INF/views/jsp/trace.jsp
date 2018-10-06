<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="req" value="${pageContext.request}" />
<c:set var="baseURL" value="${fn:replace(req.requestURL, fn:substring(req.requestURI, 1, fn:length(req.requestURI)), fn:substring(req.contextPath,1,fn:length(req.contextPath)))}" />
	
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Maven + Spring MVC</title>
	 
	<spring:url value="/resources/core/css/hello.css" var="coreCss" />
	<spring:url value="/resources/core/css/bootstrap.min.css" var="bootstrapCss" />
	<link href="${bootstrapCss}" rel="stylesheet" />
	<link href="${coreCss}" rel="stylesheet" />
</head>
 
<nav class="navbar navbar-inverse navbar-fixed-top">
  <div class="container">
	<div class="navbar-header">
		<a class="navbar-brand" href="${baseURL}/home/pj1">Project 1</a>
		<a class="navbar-brand" href="${baseURL}/home/pj2">Project 2</a>
		<a class="navbar-brand" href="${baseURL}/home/trace">Trace Log</a>
	</div>
  </div>
</nav>
 
<div class="jumbotron">
	<div class="container">
	    <p>
			<a class="btn btn-primary btn-lg" href="#" role="button">Learn more</a>
		</p>
		<c:forEach items="${fileList}" var="fileSub">
			<a href="${baseURL}/home/download/<c:out value="${fileSub}"/>"><c:out value="${fileSub}"/></a><br>
		</c:forEach>
	</div>
</div>
 
<spring:url value="/resources/core/css/hello.js" var="coreJs" />
<spring:url value="/resources/core/css/bootstrap.min.js" var="bootstrapJs" />
 
<script src="${coreJs}"></script>
<script src="${bootstrapJs}"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
 
</body>
</html>