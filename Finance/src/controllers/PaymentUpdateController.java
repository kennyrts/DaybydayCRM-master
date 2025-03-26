package controllers;

import services.PaymentService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/payment_update")
public class PaymentUpdateController extends HttpServlet {
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("token") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            String pathInfo = request.getPathInfo();
            Long paymentId = Long.parseLong(pathInfo.substring(1));
            Double amount = Double.parseDouble(request.getParameter("amount"));
            String token = (String) session.getAttribute("token");
            String invoiceId = request.getParameter("invoice");

            if (paymentService.updatePayment(paymentId, amount, token)) {
                request.getSession().setAttribute("success", "Payment updated successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to update payment");
            }

            response.sendRedirect(request.getContextPath() + "/payments?invoice=" + invoiceId);
            
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/payments");
        }
    }
} 