<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="./css/signUpStyle.css"/>
        <script>
        function validate() {
            var name = document.form.name.value;
            var password = document.form.password.value;
            var email=document.form.email.value;

            if(name=="") {
                alert("name cant be null");
                return false;
            }
            else if(password==""){
                 alert("password cant be null");
                return false;
            }
            else if(email=="") {
                alert("email cant be null");
                return false;
            }
            return true;
        }
        </script>
    </head>
    
    <body>
        <div class="main-div">
            <h3>SignUp page</h3>
            <form name="form" action="SignUp" method = "post" onsubmit="return validate()">
                User name : <input type="text" name="name"><br>
                password : <input type="password" name="password"><br>
                email  : <input type="text" name="email"><br>
                
                <input type="submit" value="signUp" id="submit_button"><br>
                <a href="index.jsp" style="text-decoration:none">
                    <input type="button" value = "cancel" id="submit_button">
                </a>
            </form>
        </div>
    </body>
</html>