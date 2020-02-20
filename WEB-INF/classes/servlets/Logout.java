package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/Logout")
public class Logout extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException{
        doGet(req, res);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();

        PrintWriter out = res.getWriter();
        Cookie[] cookies = req.getCookies();

        if(session != null) {
            if(session.getAttribute("username") != null)
                session.removeAttribute("username");
            if(session.getAttribute("admin") != null)
                session.removeAttribute("admin");
            session.invalidate();
            for(Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                res.addCookie(cookie);
            }
            res.sendRedirect("./index.jsp");
        }
    }

}