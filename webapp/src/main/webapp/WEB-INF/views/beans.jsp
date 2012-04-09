<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib uri="http://informatics.mayo.edu/cts2/framework#beans" prefix="beans"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<script type="text/javascript" src="${urlBase}/resources/common/include/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="${urlBase}/resources/beans/include/js/jquery.ba-bbq.min.js"></script>
	<script type="text/javascript" src="${urlBase}/resources/beans/include/js/CollapsibleLists.compressed.js"></script>
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${urlBase}/resources/beans/beans.css" />

<script type="text/javascript">
$(document).ready(function() {
    var url = $(location).attr('href');
    
    $("#xml").click(function() {
    	redirect(url,"xml");
    });
    $("#json").click(function() {
    	redirect(url,"json");
    });
    
    CollapsibleLists.apply(true);
    
    $('tr.parent')
	.css("cursor","pointer")
	.attr("title","Click to expand/collapse")
	.click(function(){
		$(this).siblings('.child-'+this.id).toggle();
	});
    
	$('tr[class^=child-]').hide().children('td');
	
	if( "${isDirectory}" == 'true' ){
		$('#directorySearch').show();
	} else {
		$('#directorySearch').hide();
	}
	
	$('#directorySearch').submit(function() {
	  var matchValue = $('#matchtext').val();
	  
	  var searchUrl = getSearchUrl( $(location).attr('href') ,matchValue);
	  
	  window.location.href = searchUrl;
	  
	  return false;
	});
});

String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
};

function redirect(url,format){
	window.location.href = url + (url.indexOf("?") < 0 ? "?" : "&") + "format="+format;
}

function getSearchUrl(url,search){
	var url = $.param.querystring( url, 'matchvalue=' + search );

	return url;
}

</script>
</head>
<body>

<div id="navcontainer">
	
	<h1>${ bean['class'].simpleName }</h1>
	Show in: 
	<button id="xml">XML</button>
	<button id="json">JSON</button>
	
	<form id="directorySearch" 
		action=""
        method="get">
         Find <input type="text" id="matchtext">
         <input type="submit" value="search">
    </form>
	
	<c:if test="${isDirectory eq 'true'}">
		<c:set var="directory" value="${ bean }" scope="request"/>
		<jsp:include page="directory.jsp"/>
	</c:if>
	
	<c:if test="${isDirectory ne 'true'}">
		<c:set var="mappedBean" value="${beans:inspect( bean) }" scope="request"/>
		<c:set var="map" value="${ mappedBean }" scope="request"/>
		<jsp:include page="node.jsp"/>
	</c:if>

 
 </div>   
    
</body>   

</html>