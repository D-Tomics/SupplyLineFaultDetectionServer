<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<!DOCTYPE html>
<html>
    <meta charset="utf-8" />
    <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <script src="./scripts/HomeScript.js"></script>
    <link rel="stylesheet" type="text/css" href="./css/Home.css">
<head>
</head>

    <body onload="onLoad()">
        <%
            response.setHeader("Cache-control","no-store,no-cache,must-revalidate"); // Http 1.1
            response.setHeader("Pragma","no-cahce"); //Http 1.0
            response.setHeader("Expires","0"); //proxies

            String usrName = (String)session.getAttribute("username");
            if(usrName == null) response.sendRedirect("./index.jsp");
        %>

        <div class="col" id="logo">
            <div id="cName">
                A B C &nbsp&nbsp P O W E R 
                G R I D
            </div>
        </div>

        <div class="col" id="actions">
            <div>TRANSFORMER DATA</div>
            <div>ANALYTICS</div>
            <div>REMOTE</div>
            <div>ABOUT</div>
            <a href="Logout"><div>LOGOUT</div></a>
        </div>
    </body>
</html>