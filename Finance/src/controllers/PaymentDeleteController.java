package controllers;

import services.PaymentService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/payment_delete")
public class PaymentDeleteController extends HttpServlet {
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("token") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            String pathInfo = request.getPathInfo();
            Long paymentId = Long.parseLong(pathInfo.substring(1));
            String token = (String) session.getAttribute("token");
            String invoiceId = request.getParameter("invoice");

            if (paymentService.deletePayment(paymentId, token)) {
                request.getSession().setAttribute("success", "Payment deleted successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to delete payment");
            }

            response.sendRedirect(request.getContextPath() + "/payments?invoice=" + invoiceId);
            
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/payments");
        }
    }
} 