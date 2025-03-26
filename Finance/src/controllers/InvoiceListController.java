package controllers;

import services.InvoiceService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet("/invoices")
public class InvoiceListController extends HttpServlet {
    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("token") == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            String section = request.getParameter("section");
            if (section == null || section.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Section parameter is missing");
                return;
            }

            String token = (String) session.getAttribute("token");
            Map<String, Object> invoiceData = invoiceService.getInvoicesBySection(section, token);
            
            request.setAttribute("invoiceData", invoiceData);
            request.getRequestDispatcher("invoice-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error retrieving invoices: " + e.getMessage());
            request.getRequestDispatcher("invoice-list.jsp").forward(request, response);
        }
    }
} 