package controllers;

import services.DiscountService;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet("/discounts")
public class DiscountController extends HttpServlet {
    private final DiscountService discountService = new DiscountService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("token") == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            String token = (String) session.getAttribute("token");
            Map<String, Object> currentRate = discountService.getCurrentRate(token);
            
            request.setAttribute("currentRate", currentRate.get("rate"));
            request.getRequestDispatcher("discounts.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error retrieving current discount rate: " + e.getMessage());
            request.getRequestDispatcher("discounts.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("token") == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            String token = (String) session.getAttribute("token");
            double rate = Double.parseDouble(request.getParameter("rate"));
            
            Map<String, Object> result = discountService.addDiscountRate(rate, token);
            
            request.getSession().setAttribute("success", "New discount rate added successfully");
            response.sendRedirect("discounts");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid rate format");
            request.getRequestDispatcher("discounts.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error adding new discount rate: " + e.getMessage());
            request.getRequestDispatcher("discounts.jsp").forward(request, response);
        }
    }
} 