<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<html>
<head>
    <title>Selected Lessons</title>
    <style>
        table {
            border-collapse: collapse;
        }

        table, th, td {
            border: 1px solid black;
        }
    </style>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<h1>Selected Lessons</h1>
<table>
    <tr>
        <td>Description</td>
        <td>Level</td>
        <td>Start Date</td>
        <td>End Date</td>
        <td>Cancel</td>
    </tr>
    <c:forEach items="${lessons}" var="lesson">
        <tr>
            <td>${lesson.description}</td>
            <td>${lesson.level}</td>
            <td>${lesson.startDateTime}</td>
            <td>${lesson.endDateTime}</td>
            <td>
                <form action="${pageContext.request.contextPath}/lessons/cancel" method="POST">
                    <input type="hidden" id="lessonId" name="lessonId" value="${lesson.lessonid}">
                    <input type="submit" value="Cancel">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<br/>
<form action="${pageContext.request.contextPath}/lessons/selected/finalise" method="POST">
    <input type="submit" value="Finalise Selections">
</form>
</body>
</html>
