<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<table>
    <tr>
        <td><a href="${pageContext.request.contextPath}/lessons/available">Available Lessons</a></td>
        <td><a href="${pageContext.request.contextPath}/lessons/selected">Selected Lessons</a></td>
        <td><a href="${pageContext.request.contextPath}/logout">Logout</a></td>
    </tr>
</table>
<p>Hello ${client.username}!</p>
