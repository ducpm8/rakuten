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
		<a class="navbar-brand" href="${baseURL}/home/trace">Trace Log</a>
	</div>
  </div>
</nav>
 
<div class="jumbotron" style="padding:27px -1px !important">
	<div class="container">
		<h1>${title}</h1>
		<p class="mess">
			Hi Andrew !
	    </p>
	</div>
</div>
<div class="container">
 
	<div class="col-md-4" style="overflow:auto;margin-top:-40px;">
		<h2>Banggood Order Placement</h2>
		<form id="uploadForm" method="POST" action="${baseURL}/home/banggoodorder" enctype="multipart/form-data">
			<div class="image-upload">
			    <label for="file-input">
			        <img src="<spring:url value="/resources/core/images/folder_web_upload.ico" />"/>
			    </label>
			    <input id="file-input" type="file" name="file"/>
			    <label id="fileUploadName" stype="vertical-align:middle;margin-left:30px"></label>
			</div>
			<div style="margin:20px 0 20px;">
				Sheet Name: <input type="text" name="sheetName"><p style="font-style: italic;">Leave blank to process first sheet</p><br>
				Store Name: <input type="text" name="store" id="store" value="AA">
				Banggood Account: <input type="text" name="account" id="account" value="patrickmagreevy@gmail.com">
				Password: <input type="password" name="password" id="password" value="Sandman79"><br>
				Paypal Account: <input type="text" name="accountPP" id="accountPP" value="amccall974@gmail.com">
				Password: <input type="password" name="passwordPP" id="passwordPP" value="Fticrms.0070"><br>
				Credit card last 4 digits: <input type="text" name="fourLastDigit" id="fourLastDigit" value="2301"><br>
			</div>
			<hr>
			
			<input type="hidden" name="projectId" id="projectId" value="3">
		</form>	
		
	</div>	
	<div class="col-md-4">
		<p>
			<a class="btn btn-primary btn-lg" href="#" id="upload" onclick="uploadFile();" role="button">Process</a>
		</p>
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
		
		if($("#account").val() == '')
		{
			alert("Input account"); return;
		}
		if($("#password").val() == '') 
		{
			alert("Input password"); return;
		}
		if($("#store").val() == '') 
		{
			alert("Input store name"); return;
		}
		if($("#file-input").val() == '')
		{
			alert("Select file"); return;
		}
		
		document.getElementById("uploadForm").submit();
		//jQuery("#loading").css('display','block');
	}
	function alertFilename()
    {
        var thefile = document.getElementById('file');
        alert(thefile.value);
    }
	$(document).ready(function() {
		document.getElementById('file-input').onchange = function () {
			    var ext = this.value.match(/\.(.+)$/)[1];
			    switch(ext)
			    {
			        case 'xls':
			        	jQuery("#fileUploadName").html(this.value);
			            break;
			        default:
			            alert('not allowed');
			            this.value='';
			    }
		};
	});
 </script>
</body>
</html>