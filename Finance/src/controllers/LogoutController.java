package controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer la session si elle existe
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Invalider la session
            session.invalidate();
        }
        
        // Rediriger vers la page de login
        response.sendRedirect("login");
    }
} 