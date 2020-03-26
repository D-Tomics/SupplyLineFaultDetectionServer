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

@WebServlet("/SignUp")
public class SignUp extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String userName = req.getParameter("name");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        HttpSession session = req.getSession();

        User user = new User(userName, email);

        PrintWriter out = res.getWriter();

        if(user.registerUser(password)) {
            session.setAttribute("username", userName);
            res.sendRedirect("Login");
        } else {
            req.getRequestDispatcher("/signUpPage.jsp").include(req,res);
            out.println("<center style='color:red'>userName already exist</center>");
        }

    }

}
