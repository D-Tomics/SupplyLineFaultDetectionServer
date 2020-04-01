package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.User;
import db.Database;

@WebServlet("/Login")
public class Login extends HttpServlet {

    private static final String ADMIN_UN = "admin";
    private static final String ADMIN_PW = "admin";

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String userName = req.getParameter("username");
        String password = req.getParameter("password");

        HttpSession session = req.getSession();
        if(session.getAttribute("username") != null){
             res.sendRedirect("./Home.jsp");
            return;
        }

        User user = new User(userName);

        if(user.authenticate(password)){
            session.setAttribute("username", userName);
            res.sendRedirect("./Home.jsp");
        } else{
            req.getRequestDispatcher("/index.jsp").forward(req, res);;
        }
        
    }
}