package servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import db.Database;
import db.Table;

@WebServlet("/Analytics")
public class Analytics extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = (String)req.getSession().getAttribute("username");
        if(username == null) {
            res.sendRedirect("index.jsp");
            return;
        }

        Database database = Database.getDatabase("supplyline");

        String id = req.getParameter("id");
        String date_from = req.getParameter("date_from");
        String date_to   = req.getParameter("date_to");
        
        req.setAttribute("id"       , id        );
        req.setAttribute("date_from", date_from );
        req.setAttribute("date_to"  , date_to   );

        if(id == null || id != null && id.isEmpty()) {
            req.setAttribute("visible", "analytics");
            req.getRequestDispatcher("./Home.jsp").forward(req, res);
            return;
        }
        
        Date t_date      = date_from != null && !date_from.isEmpty() ? Date.valueOf(date_to): null;
        Date f_date      = date_to != null && !date_to.isEmpty() ? Date.valueOf(date_from) : null;
        
        if(f_date == null || t_date == null || f_date.after(t_date)) {
            req.setAttribute("visible", "analytics");
            req.getRequestDispatcher("./Home.jsp").forward(req, res);
            return;
        }
        
        Table trDataLog = database.getTable("TrDataLog");
        ResultSet trDataLogResultSet = trDataLog.get("dataLog", "id=? AND date>=? && date<=?", Integer.parseInt(id),f_date,t_date);
        
        req.setAttribute("trDataLog",parseToJsonData(trDataLogResultSet));
        req.setAttribute("visible", "analytics");

        req.getRequestDispatcher("./Home.jsp").forward(req, res);
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