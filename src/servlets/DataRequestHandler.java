package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.Database;
import db.Table;

@WebServlet("/request")
public class DataRequestHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String id = req.getParameter("id");
        if(id.equals("")) return;

        String current = req.getParameter("I");
        String voltage = req.getParameter("V");
        String loc = req.getParameter("loc");
        String status = req.getParameter("status");

        Database db = Database.getDatabase("supplyline");
        Table trData = db.getTable("trLiveData");

        if(trData.contains("id", id)) {
            trData.updateColumns("id="+id, new String[] {"location","current","voltage","status"},loc,current,voltage,status);
        }

    }

}