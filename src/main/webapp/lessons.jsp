<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>Available Lessons</title>
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
<h1>Available Lessons</h1>
<table>
    <tr>
        <td>Description</td>
        <td>Level</td>
        <td>Start Date</td>
        <td>End Date</td>
        <td>Select</td>
    </tr>
    <c:forEach items="${lessons}" var="lesson">
        <tr>
            <td>${lesson.description}</td>
            <td>${lesson.level}</td>
            <td>${lesson.startDateTime}</td>
            <td>${lesson.endDateTime}</td>
            <td>
                <form action="${pageContext.request.contextPath}/lessons/select" method="POST">
                    <input type="hidden" id="lessonId" name="lessonId" value="${lesson.lessonid}">
                    <input type="submit" value="Select">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>