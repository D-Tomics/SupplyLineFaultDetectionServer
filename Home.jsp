<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<!DOCTYPE html>
<html>	<meta charset="utf-8" />
    <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <link rel="stylesheet" type="text/css" href="./css/home/main.css">
    <link rel="stylesheet" type="text/css" href="./css/home/header.css">
    <link rel="stylesheet" type="text/css" href="./css/home/footer.css">
    <link rel="stylesheet" type="text/css" href="./css/home/section.css">
    <style>
    table {
            border : 1px solid #ddd;
            border-collapse: collapse;
            border-radius: 10px;
            background:#fff;
            margin:0px auto;
            width:25%;
            margin-top: 150px;
            margin-bottom: 150px;
          }

          th{
              padding:20px;
              border: 1px solid #ccc;
              background:#5d8ffc;
              color:#fff;
              text-align: left;
          }

          td {
              padding:20px;
              border: 1px solid #ccc;
              color : #5d8ffc;
              text-align:left;

              transition: 0.3s;        
              -webkit-transition: 0.3s;
              -moz-transition: 0.3s;
          }

          td:hover {
            background:#ccdcff;
            color : #5d8ffc;
          }

          tr:nth-child(even) {
              background: #dbf2ff;
          }
            </style>

    <script src="./scripts/homeScript.js"></script>
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

        <!-- HEADER  -->
        <div class="header" id="header">
            <div class="header-right">
                <a href="#blogs" class="menu">Home</a>
                <a href="Logout"  class="menu">Logout</a>
            </div>
            <h1>ABC POWER GRID</h1>
        </div>   

        <div class="main-header" id="main-header">
            <!-- <h3 style="display: inline;" class="user">${username}</h3> -->
            <div id="header-right">
                <a href="Home.jsp" class="item">Home</a>
                <a href="Logout"  class="item">Logout</a>
            </div>
        </div>


        <div id="blogs" style="width: 100%; margin: 0px;">
            <sql:setDataSource var = "employee"
            driver = "com.mysql.jdbc.Driver"
            url = "jdbc:mysql://localhost/employee"
            user = "root"  password = "root"/>


        <sql:query dataSource="${employee}" var="trData">
            SELECT * FROM trData;
        </sql:query>

        
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
        <!-- FOOTER -->
        <div class="footer">
            Copyright &copy 2019 D_tomics,All rights Reserved.
        </div>
    </body>
</html>