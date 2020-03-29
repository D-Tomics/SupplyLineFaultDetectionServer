<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<!DOCTYPE html>
<html>
    <meta charset="utf-8" />
    <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <script src="./scripts/HomeScript.js"></script>
    <!-- <script src="https://cdn.jsdelivr.net/npm/chart.js@2.8.0"></script> -->
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

        <div id="DataPanel">
            <div id="cName" class="dataPanelElements">
                A B C <br><br>
                P O W E R <br><br>
                G R I D
            </div>
            <div id="trData" class="dataPanelElements" style="opacity: 0;">
                <table id="trDataTable">
                    <tr id="form_row">
                        <form id="trCustomQuery">
                            <td rowspan="2">  <input type="number"  id="input_id"       name="id"       placeholder="id">       </td>
                            <td rowspan="2">  <input type="text"    id="input_loc"      name="location" placeholder="location"> </td>  
                            <td>              <input type="number"  id="input_current"  name="current"  placeholder="current">  </td>
                            <td>              <input type="number"  id="input_voltage"  name="voltage"  placeholder="voltage">  </td>
                            <td>              <input type="number"  id="input_freq"     name="freq"     placeholder="freq(HZ)"> </td>
                            <td rowspan="2">
                                <select id="input_status" name="status">
                                    <option value="ok">ok</option>
                                    <option value="ERROR">error</option>
                                </select>
                            </td>

                            <tr>
                                <td>
                                    <select id="input_current_operation" name="current_operation">
                                        <option value="=">  =       </option>
                                        <option value="!="> !=      </option>
                                        <option value=">">  &#62;   </option>
                                        <option value=">="> &#62;=  </option>
                                        <option value="<">  &#60;   </option>
                                        <option value="<="> &#60;=  </option>
                                    </select>
                                </td>
                                <td>
                                    <select id="input_voltage_operation" name="current_operation">
                                        <option value="=">  =       </option>
                                        <option value="!="> !=      </option>
                                        <option value=">">  &#62;   </option>
                                        <option value=">="> &#62;=  </option>
                                        <option value="<">  &#60;   </option>
                                        <option value="<="> &#60;=  </option>
                                    </select>
                                </td>
                                <td>
                                    <select id="input_freq_operation" name="current_operation">
                                        <option value="=">  =       </option>
                                        <option value="!="> !=      </option>
                                        <option value=">">  &#62;   </option>
                                        <option value=">="> &#62;=  </option>
                                        <option value="<">  &#60;   </option>
                                        <option value="<="> &#60;=  </option>
                                    </select>
                                </td>
                            </tr>
                        </form>
                    </tr>
                    <tr>
                        <th class="trData_table_header">id      </th>
                        <th class="trData_table_header">location</th>
                        <th class="trData_table_header">current </th>
                        <th class="trData_table_header">voltage </th>
                        <th class="trData_table_header">freq    </th>
                        <th class="trData_table_header">status  </th>
                    </tr>
                    <c:forEach var="row" items="${trData}">
                        <tr class="tr_data_row">
                            <td class="trData_table_data"><c:out value="${row.getId()}"/></td>
                            <td class="trData_table_data"><c:out value="${row.getLoc()}"/></td>
                            <td class="trData_table_data"><c:out value="${row.getCurrent()}"/></td>
                            <td class="trData_table_data"><c:out value="${row.getVoltage()}"/></td>
                            <td class="trData_table_data"><c:out value="60"/></td>
                            <td class="trData_table_data"><c:out value="${row.getStatus()}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

            <div id="analytics" class="dataPanelElements" style="opacity: 0;">
                <canvas id="chart"></canvas>
            </div>

            <div id="remote" class="dataPanelElements" style="opacity: 0;">
                Remote
            </div>

        </div>
        <div id="actions">
            <a href="Home?id=1"><div class = "btn" id="trDataButton">LIVE DATA</div></a>
            <a href="Home?id=2"><div class = "btn" id="analyticsButton">ANALYTICS</div></a>
            <a href="Home?id=3"><div class = "btn" id="remoteButton">REMOTE</div></a>
            <a href="Home?id=4"><div class = "btn" id="aboutButton">ABOUT</div></a>
            <a href="Logout"><div class = "btn" id="logout">LOGOUT</div></a>
        </div>
        <script>
            var visible = '${visible}';
            var trData =document.getElementById("trData");
            var analytics = document.getElementById("analytics");
            var remote = document.getElementById("remote");
            var cName = document.getElementById("cName");

            switch(visible) {
                case "trData":
                    trData.style.zIndex = 2;

                    analytics.style.opacity = 1;
                    remote.style.opacity = 1;
                    trData.style.opacity = 1;

                    trData.classList.add("fadeIn");
                    break;
                case "analytics":
                    analytics.style.zIndex = 2;

                    trData.style.opacity = 1;
                    remote.style.opacity = 1;
                    analytics.style.opacity = 1;

                    analytics.classList.add("fadeIn");

                    break;
                case "remote":
                    remote.style.zIndex = 2;

                    trData.style.opacity = 1;
                    analytics.style.opacity = 1;
                    remote.style.opacity = 1;
                    
                    remote.classList.add("fadeIn");
                    break;
                default:
                    trData.style.opacity = 0;
                    analytics.style.opacity = 0;
                    remote.style.opacity = 0;
            }
        </script>
    </body>
</html>