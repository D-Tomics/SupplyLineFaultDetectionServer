package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import data.TransformerData;
import db.Database;
import db.Table;

@WebServlet("/Home")
public class Home extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TR_DATA = "1";
    private static final String ANALYTICS = "2";
    private static final String HOME = "3";
    private static final String ABOUT = "4";

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
        action = action != null ? action : "";
        switch (action) {
            case TR_DATA:
                req.getRequestDispatcher("./TrData").forward(req, res);
                return;
            case ANALYTICS:
                req.getRequestDispatcher("./Analytics").forward(req, res);
                return;
            case HOME:
                Database database = Database.getDatabase("supplyline");
                Table trData = database.getTable("TrLiveData");
                req.setAttribute("trDataLoc", getJSonString(TrData.getTrData(trData.get("*"),req)));
            case ABOUT:
            default:
                req.setAttribute("visible", null);
        }

       req.getRequestDispatcher("./Home.jsp").forward(req, res);
    }

    private String getJSonString(final ArrayList<TransformerData> list) {
        JSONArray array = new JSONArray();

        for(TransformerData data : list) {
            JSONObject obj = new JSONObject();
            obj.put("id", data.getId());
            obj.put("v", data.getVoltage());
            obj.put("i", data.getCurrent());
            obj.put("f", data.getFrequency());
            obj.put("status", data.getStatus());

            String[] latLng = data.getLoc().split(",");
            JSONObject location = new JSONObject();
            location.put("lat", Float.parseFloat(latLng[0]));
            location.put("lng", Float.parseFloat(latLng[1]));
            obj.put("loc", location);
            array.add(obj);
        }
        return array.toJSONString();
    }

}