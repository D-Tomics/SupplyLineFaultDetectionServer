<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<!DOCTYPE html>
<html>
    <head>
        <title>ABC</title>
        <meta charset="utf-8" />
        <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>

        <script src="./scripts/chart.js"></script>
        <script src="./scripts/HomeScript.js"></script>
        <script>
            window.addEventListener("beforeprint",function() {
                for (var id in Chart.instances) {
                Chart.instances[id].resize();
                }
            });
        </script>
        <link rel="stylesheet" type="text/css" href="./css/Home.css">
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.6.0/dist/leaflet.css"
        integrity="sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ=="
        crossorigin=""/>

        <style>
            .popupCustom .leaflet-popup-tip,
            .popupCustom .leaflet-popup-content-wrapper {
                background: #191A1A;
                color: #3b515a;
            }
        </style>
        
        <script src="https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"
        integrity="sha512-gZwIG9x3wUXg2hdXF6+rVkLF/0Vi9U8D2Ntg4Ga5I5BZpVkVxlJWbSQtXPSiUTtC0TjtGOmxa1AJPuV0CPthew=="
        crossorigin=""></script>
        <link rel="stylesheet" href="https://unpkg.com/leaflet.markercluster@1.4.1/dist/MarkerCluster.Default.css">
        <script src="https://unpkg.com/leaflet.markercluster@1.4.1/dist/leaflet.markercluster.js"></script>
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
            <div id="map" class="dataPanelElements"></div>
            <div id="trData" class="dataPanelElements" style="opacity: 0;">
                <div id="table_box">
                    <table id="trDataTable">
                        <form id="trCustomQuery" action="Home" method="POST">
                            <input type="hidden" value="1" name="action">
                            <tr id="form_row">
                                <td rowspan="2">  <input type="number"  id="input_id"       name="id"       placeholder="id"        value="${id}"         > </td>
                                <td rowspan="2">  <input type="text"    id="input_loc"      name="location" placeholder="location"  value="${location}"   > </td>  
                                <td            >  <input type="number"  id="input_current"  name="current"  placeholder="current"   value="${current}"    > </td>
                                <td            >  <input type="number"  id="input_voltage"  name="voltage"  placeholder="voltage"   value="${voltage}"    > </td>
                                <td            >  <input type="number"  id="input_freq"     name="freq"     placeholder="freq(HZ)"  value="${freq}"       > </td>
                                <td rowspan="2">
                                    <select id="input_status" name="status">
                                        <option value="ok">ok</option>
                                        <option value="ERROR">error</option>
                                    </select>
                                </td>

                                <tr>
                                    <td>
                                        <select id="input_current_operation" name="current_operation">
                                            <option value="="   ${currOp == '='  ? 'selected' : ''}> =       </option>
                                            <option value="!="  ${currOp == '!=' ? 'selected' : ''}> !=      </option>
                                            <option value=">"   ${currOp == '>'  ? 'selected' : ''}> &#62;   </option>
                                            <option value=">="  ${currOp == '>=' ? 'selected' : ''}> &#62;=  </option>
                                            <option value="<"   ${currOp == '<'  ? 'selected' : ''}> &#60;   </option>
                                            <option value="<="  ${currOp == '<=' ? 'selected' : ''}> &#60;=  </option>
                                        </select>
                                    </td>
                                    <td>
                                        <select id="input_voltage_operation" name="voltage_operation">
                                            <option value="="   ${voltOp == '='  ? 'selected' : ''}> =       </option>
                                            <option value="!="  ${voltOp == '!=' ? 'selected' : ''}> !=      </option>
                                            <option value=">"   ${voltOp == '>'  ? 'selected' : ''}> &#62;   </option>
                                            <option value=">="  ${voltOp == '>=' ? 'selected' : ''}> &#62;=  </option>
                                            <option value="<"   ${voltOp == '<'  ? 'selected' : ''}> &#60;   </option>
                                            <option value="<="  ${voltOp == '<=' ? 'selected' : ''}> &#60;=  </option>
                                        </select>
                                    </td>
                                    <td>
                                        <select id="input_freq_operation" name="freq_operation">
                                            <option value="="   ${freqOp == '='  ? 'selected' : ''}> =       </option>
                                            <option value="!="  ${freqOp == '!=' ? 'selected' : ''}> !=      </option>
                                            <option value=">"   ${freqOp == '>'  ? 'selected' : ''}> &#62;   </option>
                                            <option value=">="  ${freqOp == '>=' ? 'selected' : ''}> &#62;=  </option>
                                            <option value="<"   ${freqOp == '<'  ? 'selected' : ''}> &#60;   </option>
                                            <option value="<="  ${freqOp == '<=' ? 'selected' : ''}> &#60;=  </option>
                                        </select>
                                    </td>
                                </tr>
                            </tr>
                            <tr id="form_row"> 
                                <td colspan="6"> <input type="submit" id="form_submit" value="SUBMIT"> </td>
                            </tr>
                        </form>
                        <tr id="header_row">
                            <th class="trData_table_header">id               </th>
                            <th class="trData_table_header">location(lat,lng)</th>
                            <th class="trData_table_header">current          </th>
                            <th class="trData_table_header">voltage          </th>
                            <th class="trData_table_header">freq             </th>
                            <th class="trData_table_header">status           </th>
                        </tr>
                        <c:forEach var="row" items="${trData}">
                            <tr class="tr_data_row">
                                <td class="trData_table_data"><c:out value="${row.getId()}"/>       </td>
                                <td class="trData_table_data"><c:out value="${row.getLoc()}"/>      </td>
                                <td class="trData_table_data"><c:out value="${row.getCurrent()}"/>  </td>
                                <td class="trData_table_data"><c:out value="${row.getVoltage()}"/>  </td>
                                <td class="trData_table_data"><c:out value="${row.getFrequency()}"/></td>
                                <td class="trData_table_data"><c:out value="${row.getStatus()}"/>
                                    <form action="Home" style="display: inline;" id="tr-controll-form-${row.getId()}" method="POST">
                                        
                                        <input type="hidden"   name="action"             id="a${row.getId()}"      value="1">
                                        <input type="hidden"   name="id"                 id="id${row.getId()}"              >
                                        <input type="hidden"   name="location"           id="loc${row.getId()}"             >
                                        <input type="hidden"   name="current"            id="curr${row.getId()}"            >
                                        <input type="hidden"   name="voltage"            id="volt${row.getId()}"            >
                                        <input type="hidden"   name="freq"               id="freq${row.getId()}"            >
                                        <input type="hidden"   name="current_operation"  id="curr_op${row.getId()}"         >
                                        <input type="hidden"   name="voltage_operation"  id="volt_op${row.getId()}"         >
                                        <input type="hidden"   name="freq_operation"     id="freq_op${row.getId()}"         >

                                        <input type="hidden"   name="update-id"                                      value="${row.getId()}">
                                        <input type="checkbox" name="onOff-check" id="${row.getId()}"  value="onoff-check" class="tr-controll"  ${row.isOn()?'checked':''}>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>

            <div id="analytics" class="dataPanelElements" style="opacity: 0;">
                <div id="chart-main">
                    <div id="analytics-form">
                        <form action="Home" method="POST">
                            <input type="hidden" value ="2" name="action">
                            <table id="analytics_form_table">
                                <tr>
                                    <td rowspan="2" >   <input type="number" id="analytics_id"      name="id"          value="${id}"        placeholder="id"       >  </td>
                                    <td             >   <input type="date"   id="analytics_fDate"   name="date_from"   value="${date_from}"                        >  </td>
                                    <td rowspan="2" >   <input type="submit" id="analytics_submit"  value="submit"                                                               >  </td>
                                </tr>
                                <tr><td             >   <input type="date"   id="analytics_tDate"   name="date_to"     value="${date_to}"                          >  </td></tr>
                            </table>
                        </form>
                    </div>
                    <div id="chart-container">
                        <canvas id="chart"></canvas>
                    </div>
                    <div id="chart-controller">
                        <input type="checkbox" id="checkbox-voltage"   class="chart-control" checked> <label for="checkbox-voltage"  > voltage  </label>
                        <input type="checkbox" id="checkbox-current"   class="chart-control"        > <label for="checkbox-current"  > current  </label>
                        <input type="checkbox" id="checkbox-frequency" class="chart-control"        > <label for="checkbox-frequency"> frequency</label>
                        <input type="checkbox" id="checkbox-power"     class="chart-control"        > <label for="checkbox-power"    > power    </label>
                    </div>
                </div>
            </div>

            <div id="about" class="dataPanelElements" style="opacity: 0;">
                <H1>
                    About
                </H1>
                <br>
                <p>
                    &nbsp&nbsp This website is a part of our final year project at Mar Athanasius College of Engineering, Kothamangalam.<br>
                    This website acts as a platform for data analysis and as a notifier of faults when ever a fault occurs on the grid.<br>
                    Group members:<B>Abdul Kareem, chriswin George, Samuel J George, Deleena Thomas</B><br>
                    Our guide : <B>Leya George</B>
                </p>

                <br>
                <div id="contacts" style="text-align: left;">
                    <h3>
                        API's
                    </h3>
                    <ul>
                        <li><a href="https://en.wikipedia.org/wiki/Java_servlet"                        target="_blank">    JAVA Servlets   </a></li>
                        <li><a href="https://leafletjs.com/"                                            target="_blank">    Leaflet API     </a></li>
                        <li><a href="https://www.chartjs.org/"                                          target="_blank">    Chart.js        </a></li>
                        <li><a href="https://docs.oracle.com/javase/tutorial/jdbc/overview/index.html"  target="_blank">    JDBC            </a></li>
                        <li><a href="https://www.mapbox.com/"                                           target="_blank">    MapBox          </a></li>
                    </ul>
                </div>
                    
            </div>

        </div>
        <div id="actions">
            <a href="Home?action=1"> <div class = "btn" id="trDataButton"   > LIVE DATA   </div></a>
            <a href="Home?action=2"> <div class = "btn" id="analyticsButton"> ANALYTICS   </div></a>
            <a href="Home">          <div class="btn" id="homepage"         > HOME        </div></a>
            <a href="Home?action=4"> <div class = "btn" id="aboutButton"    > ABOUT       </div></a>
            <a href="Logout">        <div class = "btn" id="logout"         > LOGOUT      </div></a>

            <div id="cName">A B C  <br> P o w e r  <br> G r i d </div>
        </div>
        <script>
            var prevVisible = getCookie("prevVisible");
            var visible = '${visible}';
            console.log(visible);
            var datapanel = document.getElementById("DataPanel");
            var trData =document.getElementById("trData");
            var analytics = document.getElementById("analytics");
            var map = document.getElementById("map");
            var about = document.getElementById("about");

            switch(visible) {
                case "trData":
                    
                    let on_off = document.getElementsByClassName("tr-controll");
                    for(let i = 0; i < on_off.length; i++) {
                        on_off[i].addEventListener('click',() => {
                            let id = on_off[i].id;
                            document.getElementById("id"+id).value      = document.getElementById("input_id").value;
                            document.getElementById("loc"+id).value     = document.getElementById("input_loc").value;
                            document.getElementById("curr"+id).value    = document.getElementById("input_current").value;
                            document.getElementById("volt"+id).value    = document.getElementById("input_voltage").value;
                            document.getElementById("freq"+id).value    = document.getElementById("input_freq").value;
                            document.getElementById("curr_op"+id).value = getSeletctedValue(document.getElementById("input_current_operation"));
                            document.getElementById("volt_op"+id).value = getSeletctedValue(document.getElementById("input_voltage_operation"));
                            document.getElementById("freq_op"+id).value = getSeletctedValue(document.getElementById("input_freq_operation"));

                            document.getElementById("tr-controll-form-"+id).submit();
                        });
                    }
                    
                    if(prevVisible == "analytics") 
                        analytics.style.opacity = 1;
                    else if(prevVisible == "about")
                        about.style.opacity = 1;

                    analytics.style.zIndex  = 0;
                    about.style.zIndex      = 0;
                    trData.style.zIndex     = 2;

                    trData.style.opacity = 1;
                    if(prevVisible != "trData")
                        trData.classList.add("fadeIn");
                    setCookie("prevVisible","trData");
                    break;
                case "analytics":
                    let today = new Date(); 
                    let dd = String(today.getDate()).padStart(2, '0');
                    let mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
                    let yyyy = today.getFullYear();
                    today =yyyy+'-'+mm+'-'+dd;
                    
                    var fDate = document.getElementById("analytics_fDate");
                    var tDate = document.getElementById("analytics_tDate");
                    if(fDate.value == "") fDate.defaultValue = today;
                    if(tDate.value == "") tDate.defaultValue = today;

                    var trDataLog = '${trDataLog}';
                    if(trDataLog != "") {
                        drawChart('${trDataLog}')
                        var checkButtons = document.getElementsByClassName('chart-control');
                        for(var i = 0; i < checkButtons.length; i++)
                        checkButtons[i].addEventListener('click',() => drawChart('${trDataLog}'));
                    }

                    if(prevVisible == "trData")
                        trData.style.opacity = 1;
                    else if(prevVisible == "about")
                        about.style.opacity = 1;

                    analytics.style.zIndex = 2;
                    trData.style.zIndex    = 0;
                    about.style.zIndex     = 0;

                    analytics.style.opacity = 1;
                    if(prevVisible != "analytics")
                        analytics.classList.add("fadeIn");
                    setCookie("prevVisible","analytics");
                    break;
                case "Home":
                    map.style.zIndex = 1;
                    
                    if(prevVisible == "trData") {
                        trData.style.opacity = 1;
                        trData.style.zIndex = 0;
                        trData.classList.add("fadeOut");
                        trData.style.opacity = 0;
                    } else if(prevVisible == "analytics") {
                        analytics.style.opacity = 1;
                        analytics.style.zIndex = 0;
                        analytics.classList.add("fadeOut");
                        analytics.style.opacity = 0;
                    } else {
                        about.style.opacity = 1;
                        about.style.zIndex = 0;
                        about.classList.add("fadeOut");
                        about.style.opacity = 0;
                    }
                    setCookie("prevVisible","");
                    drawMap('${trDataLoc}');
                    
                    break;
                case "About":
                    if(prevVisible == "trData") {
                        trData.style.opacity = 1;
                        trData.style.zIndex = 0;
                        trData.classList.add("fadeOut");
                        trData.style.opacity = 0;
                    } else if(prevVisible == "analytics") {
                        analytics.style.opacity = 1;
                        analytics.style.zIndex = 0;
                        analytics.classList.add("fadeOut");
                        analytics.style.opacity = 0;
                    }

                    analytics.style.zIndex = 0;
                    trData.style.zIndex    = 0;
                    about.style.zIndex     = 2;

                    about.style.opacity = 1;
                    if(prevVisible != "about")
                        about.classList.add("fadeIn");
                    setCookie("prevVisible","about");
                    break;
            }

            function getSeletctedValue(e) {
                return e.options[e.selectedIndex].value;
            }

            function getLocation(tr) {
                var locationString = tr.location.split(",");
                return [parseFloat(locationString[0]), parseFloat(locationString[1])]
            }
        </script>
    </body>
</html>