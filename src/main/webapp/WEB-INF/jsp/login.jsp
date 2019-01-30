<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>

<html>
<head>
    <title>Login</title>
</head>
<body>
<h1>Login</h1>
<form id="login" onsubmit="return checkUsernameLength()" method="POST" action="${pageContext.request.contextPath}/login">
    <label>
        Username: <input type="text" name="username" onchange="checkUserExists()" value=""/>
    </label><br/>
    <label>
        Password: <input type="password" name="password" value=""/>
    </label><br/>
    <button id="login-button" onclick="login()">Login</button>
    <button id="register-button" onclick="register()" disabled>Register</button>
</form>
</body>
<script>
  function checkUserExists() {
    var username = document.forms["login"]["username"].value;

    var request = new XMLHttpRequest();
    request.open('GET', '/clientExists/' + username, false);
    request.send(null);

    if (request.status === 404) {
      document.getElementById("register-button").disabled = false;
      return false;
    } else {
      document.getElementById("register-button").disabled = true;
      return true;
    }
  }

  function checkUsernameLength() {
    var username = document.forms["login"]["username"].value;

    if (username.length < 6) {
      alert("Username must be at least 6 characters in length");
      return false;
    }
  }

  function login() {
    var form = document.getElementById('login');
    form.action = '${pageContext.request.contextPath}/login';
  }

  function register() {
    var form = document.getElementById('login');
    form.action = '${pageContext.request.contextPath}/register';
  }
</script>
</html>
