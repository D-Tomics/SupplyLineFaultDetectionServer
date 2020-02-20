<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="./css/Login.css">
    <script>
        function validate() {
            var un = document.form.username.value;
            var pw = document.form.password.value;
            if(un == ""){
                alert("user name cannot be null");
                return false;
            }
            else if( pw == ""){
                alert("password cannot be null");
                return false;
            }
            return true;
        }

        function loop() {
            while(true) {
                
            }
        }
    </script>
</head>

<body>

    <%
        response.setHeader("Cache-control","no-store,no-cache,must-revalidate"); // Http 1.1
        response.setHeader("Pragma","no-cahce"); //Http 1.0
        response.setHeader("Expires","0"); //proxies

        String usrName = (String)session.getAttribute("username");
        if(usrName != null) response.sendRedirect("./Home.jsp");
    %>


    <div class="main-div">
        <h3>LOGIN PAGE</h3>
        <form name="form" action="Login" method = "post" onsubmit="return validate()">
            Username : <input type="text" name="username"><br>
            password : <input type="password" name="password"><br>
            <input type="submit" value="login to account" id="submit_button">
            <a href="signUpPage.jsp"> SignUp</a>
        </form>
    </div>
</body>

</html>