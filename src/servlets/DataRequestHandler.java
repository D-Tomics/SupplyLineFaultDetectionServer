package servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.TransformerData;
import db.Database;
import db.Table;

@WebServlet("/request")
public class DataRequestHandler extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter out = res.getWriter();

        String id = req.getParameter("id");
        if(id.equals("")) return;

        String current = req.getParameter("I");
        String voltage = req.getParameter("V");
        String loc = req.getParameter("loc");
        String status = req.getParameter("status");

        TransformerData data = new TransformerData(
            Integer.parseInt(id),
            Float.parseFloat(current),
            Float.parseFloat(voltage),
            loc,status);
        data.update();
    }

}