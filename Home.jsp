<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<!DOCTYPE html>
<html>
    <meta charset="utf-8" />
    <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <link rel="stylesheet" type="text/css" href="./css/Home.css">
<head>
</head>

    <body>
        <%
            response.setHeader("Cache-control","no-store,no-cache,must-revalidate"); // Http 1.1
            response.setHeader("Pragma","no-cahce"); //Http 1.0
            response.setHeader("Expires","0"); //proxies

            String usrName = (String)session.getAttribute("username");
            if(usrName == null) response.sendRedirect("./index.jsp");
        %>

        <div id="container">
            <header id="header">
                <div id="header-icons">
                    <a href="Home.jsp">Home</a>
                    <a href="Logout">Logout</a>
                </div>
                <h1>ABC POWER GRID</h1>
            </header>

            <div id="main">
                <sql:setDataSource var = "employee"
                driver = "com.mysql.jdbc.Driver"
                url = "jdbc:mysql://localhost/employee"
                user = "root"  password = "root"/>

                <sql:query dataSource="${employee}" var="trData"> SELECT * FROM trData; </sql:query>
                
                <table id="table">    
                    <tr>
                        <th>location</th>
                        <th>current</th>
                        <th>voltage</th>
                        <th>status</th>
                    </tr>
                    <c:forEach var="row" items="${trData.rows}">
                        <tr>
                            <td><c:out value="${row.location}"/></td>
                            <td><c:out value="${row.current}"/></td>
                            <td><c:out value="${row.voltage}"/></td>
                            <td><c:out value="${row.status}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>

        <footer id="footer">
            Copyright &copy 2019 D_tomics,All rights Reserved.
        </footer>
    </body>
</html>