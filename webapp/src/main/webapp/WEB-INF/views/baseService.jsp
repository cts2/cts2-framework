<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib uri="http://informatics.mayo.edu/cts2/framework#beans" prefix="beans"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<script type="text/javascript" src="resources/common/include/js/jquery-1.7.1.min.js"></script>
	<link rel="stylesheet" media="screen,projection" type="text/css" href="resources/beans/beans.css" />

<script type="text/javascript">
$(document).ready(function() {
    var url = $(location).attr('href');
    
    $("#xml").click(function() {
    	redirect(url,"xml");
    });
    $("#json").click(function() {
    	redirect(url,"json");
    });
});

function redirect(url,format){
	window.location.href = url + (url.indexOf("?") < 0 ? "?" : "&") + "format="+format;
}

</script>
</head>
<body>

<div id="navcontainer">
	
	<h1>${ baseService.getClass().getSimpleName() }</h1>
	Show in: 
	<button id="xml">XML</button>
	<button id="json">JSON</button>
	
	<c:set var="bean" value="${beans:inspect( baseService) }" scope="request"/>

	<c:set var="map" value="${ bean }" scope="request"/>
	<jsp:include page="node.jsp"/>
 
 </div>   
    
</body>   

</html>