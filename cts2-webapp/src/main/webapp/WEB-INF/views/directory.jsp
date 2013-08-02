<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://informatics.mayo.edu/cts2/framework#beans"
	prefix="beans"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:if test="${directory != null}">

	<span>${directory.numEntries} results returned (${ directory.complete }).</span>
	
	<c:if test="${ fn:length(directory.entry) > 0 }">
		<table>
			<thead>
				<tr>
					<c:forEach var="headerKey" items="${ beans:summary( directory.entry[0] ) }">
							<th scope="col">${headerKey.key}</th>
					</c:forEach>
				</tr>
			</thead>
			<tbody>
				<c:set var="count" value="0" scope="page" />

				<c:forEach var="j" items="${ directory.entry }">
					<tr class="parent" id="${ count }">
						<c:forEach var="entry" items="${ beans:summary( j ) }">
								<td>${entry.value}</td>
						</c:forEach>
					</tr>
					
					<tr class="child-${ count }">
						<td colspan="100%" class="tdContent">											
							<c:set var="map" value="${beans:inspect( j ) }"
								scope="request" />
							<jsp:include page="node.jsp" />			
						</td>
					</tr>
					<c:set var="count" value="${count + 1}" scope="page"/>	
				</c:forEach>	
			</tbody>
		</table>
		
		<c:if test="${ directory.prev ne null }">
			<a href="${ directory.prev }"><<< Previous</a>
		</c:if>
		<c:if test="${ directory.next ne null }">
			<a href="${ directory.next }">Next >>></a>
		</c:if>
	</c:if>

</c:if>