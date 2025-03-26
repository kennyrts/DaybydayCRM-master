package controllers;

import services.PaymentService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/payments/*")
public class PaymentController extends HttpServlet {
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Vérifier l'authentification
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("token") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Non authentifié");
            return;
        }

        try {
            // Extraire l'ID du paiement de l'URL
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de paiement manquant");
                return;
            }
            
            Long paymentId = Long.parseLong(pathInfo.substring(1));
            Double amount = Double.parseDouble(request.getParameter("amount"));
            String token = (String) session.getAttribute("token");

            boolean success = paymentService.updatePayment(paymentId, amount, token);
            
            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"status\": \"success\"}");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Échec de la mise à jour");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de paiement ou montant invalide");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Vérifier l'authentification
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("token") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Non authentifié");
            return;
        }

        try {
            // Extraire l'ID du paiement de l'URL
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de paiement manquant");
                return;
            }
            
            Long paymentId = Long.parseLong(pathInfo.substring(1));
            String token = (String) session.getAttribute("token");

            boolean success = paymentService.deletePayment(paymentId, token);
            
            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"status\": \"success\"}");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Échec de la suppression");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de paiement invalide");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
} 