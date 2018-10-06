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
		<h2>Place Order Process</h2>
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
				AliExpress Account: <input type="text" name="account">
				Password: <input type="password" name="password"><br>
				<span style="vertical-align:middle;margin-top:10px"> Credit Card Input: </span><input type="checkbox" id="cc" name="cc" style="vertical-align:middle; margin-top:10px"/>
			</div>
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
			
			
			<hr>
			
			<div id="ccinfor" class="containers" style="display: none!important">
				<div class="rTableRow">
					<div class="rTableCell col1"><span >Card number</span><input class="stCol" type="text" name="cardNo" id="cardNo"></div>
					<div class="rTableCell col2"></div>
					<div class="rTableCell"></div>
				</div>
				<div class="rTableRow">
					<div class="rTableCell col1">
						<span >Expiration MM/YY</span>
						<input style="width:70px;" type="text" name="expYear" id="expYear"><input type="text" style="width:70px;margin-right:29px" name="expMonth" id="expMonth">
					</div>
					<div class="rTableCell col2">
						<span>CVS</span >
						<input type="password" name="cvs" id="cvs" style="width:70px;margin-right:100px" maxlength="4">
					</div>
					<div class="rTableCell">
						
					</div>
				</div>
				<div class="rTableRow">
					<div class="rTableCell col1">
						<span >Cardholder name</span >
						<input class="stCol" type="text" name="cardHolderName1" id="cardHolderName1" >
					</div>
					<div class="rTableCell col2">
						<input type="text" name="cardHolderName2">
					</div>
					<div class="rTableCell"></div>
				</div>
				<div class="rTableRow">
					<div class="rTableCell col1">
						<span >Billing address</span >
						<select class="stCol" id="country" name="country">
							<option value="UK">United Kingdom</option>
							<option value="CA">Canada</option>
							<option value="US" selected="">United States</option>
						</select>
					</div>
					<div class="rTableCell col2">
						<span >Address 1</span ><input type="text" name="add1" id="add1">
					</div>
					<div class="rTableCell col3">
						<span >Address 2</span ><input type="text" name="add2">
					</div>
				</div>
				<div class="rTableRow">
					<div class="rTableCell col1">
						<span >City</span ><input type="text" name="city"  id="city">
					</div>
					<div class="rTableCell col2">
						<span >State</span >
						<select id="state" name="state" >
							<option value="Alabama">Alabama</option>
							<option value="Alaska">Alaska</option>
							<option value="Arizona">Arizona</option>
							<option value="Arkansas">Arkansas</option>
							<option value="California">California</option>
							<option value="Colorado">Colorado</option>
							<option value="Connecticut">Connecticut</option>
							<option value="Delaware">Delaware</option>
							<option value="District of Columbia">District of Columbia</option>
							<option value="Florida">Florida</option>
							<option value="Georgia">Georgia</option>
							<option value="Hawaii">Hawaii</option>
							<option value="Idaho">Idaho</option>
							<option value="Illinois">Illinois</option>
							<option value="Indiana">Indiana</option>
							<option value="Iowa">Iowa</option>
							<option value="Kansas">Kansas</option>
							<option value="Kentucky">Kentucky</option>
							<option value="Louisiana">Louisiana</option>
							<option value="Maine">Maine</option>
							<option value="Maryland">Maryland</option>
							<option value="Massachusetts">Massachusetts</option>
							<option value="Michigan">Michigan</option>
							<option value="Minnesota">Minnesota</option>
							<option value="Mississippi">Mississippi</option>
							<option value="Missouri">Missouri</option>
							<option value="Montana" selected="">Montana</option>
							<option value="Nebraska">Nebraska</option>
							<option value="Nevada">Nevada</option>
							<option value="New Hampshire">New Hampshire</option>
							<option value="New Jersey">New Jersey</option>
							<option value="New Mexico">New Mexico</option>
							<option value="New York">New York</option>
							<option value="North Carolina">North Carolina</option>
							<option value="North Dakota">North Dakota</option>
							<option value="Ohio">Ohio</option>
							<option value="Oklahoma">Oklahoma</option>
							<option value="Oregon">Oregon</option>
							<option value="Pennsylvania">Pennsylvania</option>
							<option value="Rhode Island">Rhode Island</option>
							<option value="South Carolina">South Carolina</option>
							<option value="South Dakota">South Dakota</option>
							<option value="Tennessee">Tennessee</option>
							<option value="Texas">Texas</option>
							<option value="Utah">Utah</option>
							<option value="Vermont">Vermont</option>
							<option value="Virginia">Virginia</option>
							<option value="Washington">Washington</option>
							<option value="West Virginia">West Virginia</option>
							<option value="Wisconsin">Wisconsin</option>
							<option value="Wyoming">Wyoming</option>
						</select>
					</div>
					<div class="rTableCell col3">
						<span >Post code</span><input type="text" name="postcode" id="postcode">
					</div>
				</div>
			</div>
			<input type="hidden" name="projectId" id="projectId" value="2">
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
		
		if (document.getElementById('cc').checked) {
			if($("#cardNo").val() == '')
			{
				alert("Input cardNo"); return;
			}
			if($("#expYear").val() == '') 
			{
				alert("Input expYear"); return;
			}
			if($("#expMonth").val() == '')
			{
				alert("Input expMonth"); return;
			}
			if($("#cvs").val() == '')
			{
				alert("Input cvs"); return;
			}
			if($("#cardHolderName1").val() == '')
			{
				alert("Input cardHolderName1"); return;
			}
			if($("#country").val() == '')
			{
				alert("Input country"); return;
			}
			if($("#add1").val() == '')
			{
				alert("Input add1"); return;
			}
			if($("#city").val() == '')
			{
				alert("Input city"); return;
			}
			if($("#state").val() == '')
			{
				alert("Input state"); return;
			}
			if($("#postcode").val() == '')
			{
				alert("Input postcode"); return;
			}
			
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
		jQuery("#fileUploadName").html(this.value);
		};
		$('#cc').click(function() {
		    $('#ccinfor').toggle();
		});
		
		$("#BGvendor").change(function() {
			  //var action = $(this).val() == "people" ? "user" : "content";
			  $("#uploadForm").attr("action", "${baseURL}/home/bgbulkorder");
			});
		
	});
 </script>
</body>
</html>