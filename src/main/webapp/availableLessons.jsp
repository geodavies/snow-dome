<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

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
    <link rel='stylesheet' href='${pageContext.request.contextPath}/resources/css/fullcalendar.min.css' />
    <script src='${pageContext.request.contextPath}/resources/js/jquery.min.js'></script>
    <script src='${pageContext.request.contextPath}/resources/js/moment.min.js'></script>
    <script src='${pageContext.request.contextPath}/resources/js/fullcalendar.min.js'></script>
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
<br/>
<div id='calendar'></div>
</body>
<script>
  $(function() {
    $('#calendar').fullCalendar({
      defaultView: 'basicWeek',
      editable: false,
      defaultDate: '2010-12-03',
      events: [
        <c:forEach items="${lessons}" var="lesson">
          {
            title: '${lesson.description}',
            start: new Date('${lesson.startDateTime}'),
            end: new Date('${lesson.startDateTime}')
          },
        </c:forEach>
      ]
    })
  });
</script>
</html>
