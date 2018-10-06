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
		<a class="navbar-brand" href="${baseURL}/home/pj3">Project 3</a>
		<a class="navbar-brand" href="${baseURL}/home/pj4">Project 4</a>
		<a class="navbar-brand" href="${baseURL}/home/pj5">ASIN</a>
		<!-- <a class="navbar-brand" href="${baseURL}/home/banggoodorder">Bang Goods Order</a> -->
		<a class="navbar-brand" href="${baseURL}/home/trace">Trace Log</a>
	</div>
  </div>
</nav>
 
<div class="jumbotron">
	<div class="container">
		<h1>${title}</h1>
		<p class="mess">
			Hi Andrew !
	    </p>
	</div>
</div>

	
 
<div class="container">
 
  <div class="row">
	<!-- 
	<div class="col-md-4">
		<h2>Heading</h2>
		<p>ABC</p>
		<p>
			<a class="btn btn-default" href="#" role="button">View details</a>
		</p>
	</div>
	<div class="col-md-4">
		<h2>Heading</h2>
		<p>ABC</p>
		<p>
			<a class="btn btn-default" href="#" role="button">View details</a>
		</p>
	</div>
	-->
	<div class="col-md-4">
		<h2>Master Inventory Sheet Process</h2>
		<form id="uploadForm" method="POST" action="${baseURL}/home/uploadFile" enctype="multipart/form-data">
			<div class="image-upload">
			    <label for="file-input">
			        <img src="<spring:url value="/resources/core/images/folder_web_upload.ico" />"/>
			    </label>
			    <input id="file-input" type="file" name="file"/>
			    <label id="fileUploadName" stype="vertical-align:middle;margin-left:30px"></label>
			</div>
			<div style="margin:20px 0 20px;">
				Sheet Name: <input type="text" name="sheetName"><p style="font-style: italic;">Leave blank to process first sheet</p><br>
			</div>
			<input type="hidden" name="projectId" id="projectId" value="1">
		</form>	
		
	</div>	
	<div class="col-md-4">
		<p>
			<a class="btn btn-primary btn-lg" href="#" id="upload" onclick="uploadFile();" role="button">Process</a>
		</p>
	</div>
  </div>
 
  <hr>
  <footer>
	<p>&copy; Andrew McCall 2015</p>
  </footer>
</div>
 
<spring:url value="/resources/core/css/hello.js" var="coreJs" />
<spring:url value="/resources/core/css/bootstrap.min.js" var="bootstrapJs" />
 
<script src="${coreJs}"></script>
<script src="${bootstrapJs}"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script type="text/javascript">
	function uploadFile() {
		document.getElementById("uploadForm").submit();
		//jQuery("#loading").css('display','block');
	}
	function alertFilename()
    {
        var thefile = document.getElementById('file-input');
        alert(thefile.value);
    }
	$(document).ready(function() {
		document.getElementById('file-input').onchange = function () {
		  jQuery("#fileUploadName").html(this.value);
		};
		
	});
 </script>
</body>
</html>