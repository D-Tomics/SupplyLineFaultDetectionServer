package servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.TransformerData;
import db.Database;
import db.Table;

@WebServlet("/TrData")
public class TrData extends HttpServlet{

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = (String)req.getSession().getAttribute("username");
        if(username == null) {
            res.sendRedirect("index.jsp");
            return;
        }

        Database database = Database.getDatabase("supplyline");
        Table trData = database.getTable("TrLiveData");

        String sqlCondition = getSqlCondition(req);
        String update_id = req.getParameter("update-id");
        String onOffCheckBox = req.getParameter("onOff-check");

        if(update_id != null && !update_id.isEmpty()) {
            trData.updateColumns("id=?", "isOn", onOffCheckBox != null && onOffCheckBox.equals("onoff-check"),Integer.parseInt(update_id));
        }
        
        req.setAttribute("trData", getTrData(trData.get("*", sqlCondition.toString()),req));
        req.setAttribute("visible", "trData");

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

    public static ArrayList<TransformerData> getTrData(ResultSet rs,HttpServletRequest req) {
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

}