package controllers;

import services.PaymentService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/payment_action")
public class PaymentActionController extends HttpServlet {
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Mise à jour du paiement
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("token") == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            Long paymentId = Long.valueOf(Double.valueOf(request.getParameter("paymentId")).longValue());
            Double amount = Double.parseDouble(request.getParameter("amount"));
            String token = (String) session.getAttribute("token");
            Long invoiceId = Long.valueOf(Double.valueOf(request.getParameter("invoice")).longValue());

            if (paymentService.updatePayment(paymentId, amount, token)) {
                request.getSession().setAttribute("success", "Payment updated successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to update payment");
            }

            response.sendRedirect("payments?invoice=" + invoiceId);
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error: " + e.getMessage());
            response.sendRedirect("payments");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Suppression du paiement
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("token") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Convertir le Double en Long en gérant le cas où l'ID est un nombre décimal
            Long paymentId = Long.valueOf(Double.valueOf(request.getParameter("paymentId")).longValue());
            String token = (String) session.getAttribute("token");
            Long invoiceId = Long.valueOf(Double.valueOf(request.getParameter("invoice")).longValue());

            if (paymentService.deletePayment(paymentId, token)) {
                request.getSession().setAttribute("success", "Payment deleted successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to delete payment");
            }

            response.sendRedirect("payments?invoice=" + invoiceId);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error: " + e.getMessage());
            response.sendRedirect("payments");
        }
    }
} 