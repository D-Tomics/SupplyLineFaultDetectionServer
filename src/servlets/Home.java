package servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import data.TransformerData;
import db.Database;
import db.Table;

@WebServlet("/Home")
public class Home extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TR_DATA = "1";
    private static final String ANALYTICS = "2";

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    
        String username = (String)req.getSession().getAttribute("username");
        if(username == null) {
            res.sendRedirect("index.jsp");
            return;
        }

        String action = req.getParameter("action");
        Database database = Database.getDatabase("supplyline");

        switch (action) {
            case TR_DATA:
                Table trData = database.getTable("TrLiveData");
                String sqlCondition = getSqlCondition(req);
                String update_id = req.getParameter("update-id");
                String onOffCheckBox = req.getParameter("onOff-check");
                req.setAttribute("test", onOffCheckBox);

                if(update_id != null && !update_id.isEmpty()) {
                    trData.updateColumns("id=?", "isOn", onOffCheckBox != null && onOffCheckBox.equals("onoff-check"),Integer.parseInt(update_id));
                }
                
                req.setAttribute("trData", getTrData(trData.get("*", sqlCondition.toString()),req));
                req.setAttribute("visible", "trData");
                break;
            case ANALYTICS:
                String id = req.getParameter("id");
                String date_from = req.getParameter("date_from");
                String date_to   = req.getParameter("date_to");
                
                req.setAttribute("id"       , id        );
                req.setAttribute("date_from", date_from );
                req.setAttribute("date_to"  , date_to   );

                if(id == null || id != null && id.isEmpty()) {
                    req.setAttribute("visible", "analytics");
                    break;
                }
                
                Date t_date      = date_from != null && !date_from.isEmpty() ? Date.valueOf(date_to): null;
                Date f_date      = date_to != null && !date_to.isEmpty() ? Date.valueOf(date_from) : null;
                
                if(f_date == null || t_date == null || f_date.after(t_date)) {
                    req.setAttribute("visible", "analytics");
                    break;
                }
                
                Table trDataLog = database.getTable("TrDataLog");
                ResultSet trDataLogResultSet = trDataLog.get("dataLog", "id=? AND date>=? && date<=?", Integer.parseInt(id),f_date,t_date);
                
                req.setAttribute("trDataLog",parseToJsonData(trDataLogResultSet));
                req.setAttribute("visible", "analytics");

                break;
            default:
                req.setAttribute("visible", null);
        }

       req.getRequestDispatcher("./Home.jsp").forward(req, res);
    }

    private String getSqlCondition(HttpServletRequest req) {
        String id       = req.getParameter("id"         );
        String location = req.getParameter("location"   );
        String current  = req.getParameter("current"    );
        String voltage  = req.getParameter("voltage"    );
        String freq     = req.getParameter("freq"       );
        String status   = req.getParameter("status"     );

        String currOp = req.getParameter("current_operation");
        String voltOp = req.getParameter("voltage_operation");
        String freqOp = req.getParameter("freq_operation");

        req.setAttribute("id"       , id        );
        req.setAttribute("location" , location  );
        req.setAttribute("current"  , current   );
        req.setAttribute("voltage"  , voltage   );
        req.setAttribute("freq"     , freq      );
        req.setAttribute("status"   , status    );
        
        req.setAttribute("currOp"   , currOp    );
        req.setAttribute("voltOp"   , voltOp    );
        req.setAttribute("freqOp"   , freqOp    );


        StringBuilder sqlCondition = new StringBuilder();
        if(id != null && !id.isEmpty())
            sqlCondition.append("id=").append(id);
        if(location != null && !location.isEmpty()) {
            if(!sqlCondition.toString().isEmpty())
                sqlCondition.append(" AND ");
            sqlCondition.append("location=").append("\"").append(location).append("\"");
        }
        if(current != null && !current.isEmpty()){
            if(!sqlCondition.toString().isEmpty())
                    sqlCondition.append(" AND ");
            sqlCondition.append("current").append(currOp).append(current);
        }
        if(voltage != null && !voltage.isEmpty()) {
            if(!sqlCondition.toString().isEmpty())
                    sqlCondition.append(" AND ");
            sqlCondition.append("voltage").append(voltOp).append(voltage);
        }
        if(freq != null && !freq.isEmpty()) {
            if(!sqlCondition.toString().isEmpty())
                sqlCondition.append(" AND ");
            sqlCondition.append("frequency").append(freqOp).append(freq);
        }
        if(status != null && !status.isEmpty()) {
            if(!sqlCondition.toString().isEmpty())
                sqlCondition.append(" AND ");
            sqlCondition.append("status=").append("\"").append(status).append("\"");
        }

        return sqlCondition.toString();
    }

    private ArrayList<TransformerData> getTrData(ResultSet rs,HttpServletRequest req) {
        ArrayList<TransformerData> dest = new ArrayList<>();
        try {
            while (rs.next()) {
                dest.add(new TransformerData(
                            rs.getInt("id"),
                            rs.getFloat("current"),
                            rs.getFloat("voltage"),
                            rs.getFloat("frequency"),
                            rs.getString("location"),
                            rs.getString("status"),
                            rs.getBoolean("isOn")
                    ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dest;
    }

    private JSONObject parseToJsonData(ResultSet rs) {
        try {
            rs.last();
            if(rs.getRow() >= 1) {
                JSONParser parser = new JSONParser();
                JSONObject job = new JSONObject();
                rs.beforeFirst();
                int i = 0;
                while(rs.next()) {
                    String json = rs.getString("dataLog");
                    JSONObject job2 = (JSONObject) parser.parse(json);
                    job.put(i, job2);
                    i++;
                }
                job.put("max_keys", i);
                return job;
            } else 
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

}