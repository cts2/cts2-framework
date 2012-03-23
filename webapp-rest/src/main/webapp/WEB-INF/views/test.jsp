<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib uri="http://informatics.mayo.edu/cts2/framework#beans" prefix="beans"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<body>
 

 
<%-- <c:out value="${beans:inspect( testBean) }"></c:out>    --%>


<c:set var="bean" value="${beans:inspect( testBean) }" scope="request"/>

	<c:set var="map" value="${ bean }" scope="request"/>
	<jsp:include page="node.jsp"/>
    
    
</body>   

</html>