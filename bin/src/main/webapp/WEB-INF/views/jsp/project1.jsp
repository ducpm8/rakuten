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
	<div class="col-md-4">
		<h2>Master Inventory Sheet Process</h2>
		<form id="uploadForm" method="POST" action="${baseURL}/home/uploadFile" enctype="multipart/form-data">
			<div class="image-upload">
			    <label for="file-input">
			        <img src="<spring:url value="/resources/core/images/folder_web_upload.ico" />"/>
			    </label>
			    <input type="file" name="filePricing[0]" id="file-input"/>
			</div>
			<!-- 
			<div style="margin:20px 0;">
				<label>Sheet Name: </label><input type="text" name="sheetName"><p style="font-style: italic;">Leave blank to process first sheet</p>
			</div>
			 -->
			<fieldset>
			  <div class="vendor-select-class">
			    <input type="radio" class="radio" name="vendor" value="AE" id="AEvendor" checked="checked" />
			    <label for="AEvendor">AliExpress</label>
			    <input type="radio" class="radio" name="vendor" value="BG" id="BGvendor"/>
			    <label for="BGvendor">Banggood</label>
			    <input type="radio" class="radio" name="vendor" value="CW" id="CWvendor"/>
			    <label for="CWvendor">CableWholeSale</label>
			  </div>
			</fieldset>
			<br>
			
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
 
<spring:url value="/resources/core/js/hello.js" var="coreJs" />
<spring:url value="/resources/core/js/bootstrap.min.js" var="bootstrapJs" />
<spring:url value="/resources/core/js/jquery-3.1.1.min.js" var="jQuery" /> 

<script src="${jQuery}"></script>
<script src="${coreJs}"></script>
<script src="${bootstrapJs}"></script>

<script type="text/javascript">
	function uploadFile() {
		document.getElementById("uploadForm").submit();
		//$("#loading").css('display','block');
	}
	function alertFilename()
    {
        var thefile = document.getElementById('file-input');
        alert(thefile.value);
    }
	$(document).ready(function() {		
		$('.image-upload').on('change',':file', function() {
			var k = 0;
			$('.image-upload input').each(function(){
				if ($(this).val() == "") {
					$(this).remove();
				} else {
					k = k + 1;
					$(this).attr("id","fileInput_" + k);
					$(this).attr("name","filePricing[" + (k-1) + "]");
				}
			})
			$(".image-upload label").attr("for","fileInput_" + (k + 1));
			$(".image-upload").append('<input type="file" name="filePricing[' + k + ']" id="fileInput_' + (k + 1) + '"/>');
		});
		
	});
 </script>
</body>
</html>