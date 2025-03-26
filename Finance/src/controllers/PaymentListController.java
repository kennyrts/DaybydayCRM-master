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

@WebServlet("/payments")
public class PaymentListController extends HttpServlet {
    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Vérifier l'authentification
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("token") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Récupérer l'ID de la facture depuis le paramètre de requête
            String invoiceId = request.getParameter("invoice");
            if (invoiceId == null || invoiceId.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invoice ID is missing");
                return;
            }
            
            String token = (String) session.getAttribute("token");
            Map<String, Object> paymentData = invoiceService.getInvoicePayments(Long.parseLong(invoiceId), token);
            
            request.setAttribute("paymentData", paymentData);
            request.getRequestDispatcher("/payment-list.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid invoice ID format");
            request.getRequestDispatcher("/payment-list.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error retrieving payments: " + e.getMessage());
            request.getRequestDispatcher("/payment-list.jsp").forward(request, response);
        }
    }
} 