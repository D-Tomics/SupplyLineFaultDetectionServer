package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.User;

@WebServlet("/Login")
public class Login extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String userName = req.getParameter("username");
        String password = req.getParameter("password");

        HttpSession session = req.getSession();
        if(session.getAttribute("username") != null){
             res.sendRedirect("Home");
            return;
        }

        User user = new User(userName);

        if(user.authenticate(password)){
            session.setAttribute("username", userName);
            res.sendRedirect("Home");
        } else{
            req.getRequestDispatcher("/index.jsp").forward(req, res);;
        }
        
    }
}