<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>Register</title>
</head>
<body>
<h1>Register</h1>
<form id="register" onsubmit="return validateForm()" method="POST" action="${pageContext.request.contextPath}/register">
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
<script>
    function validateForm() {
        var username = document.forms["register"]["username"].value;

        if (username.length < 6) {
            alert("Username must be at least 6 characters in length");
            return false;
        }

        var request = new XMLHttpRequest();
        request.open('GET', '/clientExists/' + username, false);
        request.send(null);

        if (request.status === 204) {
            alert("A client with that username already exists");
            return false;
        }
    }
</script>
</html>
