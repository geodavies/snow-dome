<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>Login</title>
</head>
<body>
<h1>Login</h1>
<form method="POST" action="${pageContext.request.contextPath}/login">
    <label>
        Username:
        <input type="text" name="username" value=""/>
    </label><br/>
    <label>
        Password:
        <input type="password" name="password" value=""/>
    </label><br/>
    <input type="submit" value="Login"/>
</form>
<p>Don't have a user yet? Register <a href="${pageContext.request.contextPath}/register">here</a>.</p>
</body>
</html>