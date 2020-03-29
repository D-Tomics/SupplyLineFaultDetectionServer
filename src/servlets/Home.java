package servlets;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.TransformerData;
import db.Database;
import db.Table;

@WebServlet("/Home")
public class Home extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TR_DATA = "1";
    private static final String ANALYTICS = "2";
    private static final String REMOTE = "3";

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");

        Database database = Database.getDatabase("employee");

        switch (action) {
            case TR_DATA:
                String sqlCondition = getSqlCondition(req);
                // if(sqlCondition.isEmpty() || sqlCondition.equalsIgnoreCase("status=ok"))
                //     sqlCondition = "id <= 5";
                    
                Table trData = database.getTable("trData");
                ResultSet rs = trData.get(sqlCondition,"*");
                ArrayList<TransformerData> list = new ArrayList<>();
                try {
                    while (rs.next()) {
                        list.add(getTrData(rs));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                req.setAttribute("trData",list);
                req.setAttribute("visible", "trData");
                break;
            case ANALYTICS:
                req.setAttribute("visible", "analytics");
                break;
            case REMOTE:
                req.setAttribute("visible", "remote");
                break;
            default:
                req.setAttribute("visible", null);
        }

       req.getRequestDispatcher("./Home.jsp").forward(req, res);
    }

    private TransformerData getTrData(ResultSet rs) throws NumberFormatException, SQLException {
        return new TransformerData(
            rs.getInt("id"),
            Float.parseFloat(rs.getString("current")),
            Float.parseFloat(rs.getString("voltage")),
            rs.getString("location"),
            rs.getString("status")
            );
    }

    private String getSqlCondition(HttpServletRequest req) {
        String id = req.getParameter("id");
        String location = req.getParameter("location");
        String current = req.getParameter("current");
        String voltage = req.getParameter("voltage");
        String freq = req.getParameter("freq");
        String status = req.getParameter("status");

        String currOp = req.getParameter("current_operation");
        String volOp = req.getParameter("voltage_operation");
        String freqOp = req.getParameter("freq_operation");

        StringBuilder sqlCondition = new StringBuilder();
        if(id != null && !id.isEmpty())
            sqlCondition.append("id=").append(id);
        if(location != null && !location.isEmpty()) {
            if(id != null && !id.isEmpty())
                sqlCondition.append(" AND ");
            sqlCondition.append("location=").append("\"").append(location).append("\"");
        }
        if(current != null && !current.isEmpty()){
            if(
                id != null && !id.isEmpty() ||
                location != null && !location.isEmpty()
                )
                    sqlCondition.append(" AND ");
            sqlCondition.append("current").append(currOp).append(current);
        }
        if(voltage != null && !voltage.isEmpty()) {
            if(
                id != null && !id.isEmpty() ||
                location != null && !location.isEmpty() ||
                current != null && !current.isEmpty()
                )
                    sqlCondition.append(" AND ");
            sqlCondition.append("voltage").append(volOp).append(voltage);
        }
        if(freq != null && !freq.isEmpty()) {
            if(
                id != null && !id.isEmpty() ||
                location != null && !location.isEmpty() ||
                current != null && !current.isEmpty() ||
                voltage != null && !voltage.isEmpty()
                )
                sqlCondition.append(" AND ");
            sqlCondition.append("frequency").append(freqOp).append(freq);
        }
        if(status != null && !status.isEmpty()) {
            if(
                id != null && !id.isEmpty() ||
                location != null && !location.isEmpty() ||
                current != null && !current.isEmpty() ||
                voltage != null && !voltage.isEmpty() ||
                freq != null && !freq.isEmpty()
                )
                sqlCondition.append(" AND ");
            sqlCondition.append("status=").append("\"").append(status).append("\"");
        }

        return sqlCondition.toString();
    }

}