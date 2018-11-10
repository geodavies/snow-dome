<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>Register</title>
</head>
<body>
<h1>Register</h1>
<form method="POST" action="${pageContext.request.contextPath}/register">
    <label>
        Username:
        <input type="text" name="username" value=""/>
    </label><br/>
    <label>
        Password:
        <input type="password" name="password" value=""/>
    </label><br/>
    <input type="submit" value="Register"/>
</form>
<p>Already have a user? Login <a href="${pageContext.request.contextPath}/login">here</a>.</p>
</body>
</html>
