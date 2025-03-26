package controllers;

import services.InvoiceService;
import services.TaskService;
import services.OfferService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {
    private final InvoiceService invoiceService = new InvoiceService();
    private final TaskService taskService = new TaskService();
    private final OfferService offerService = new OfferService();
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
            
            // Récupérer les statistiques
            Map<String, Object> stats = invoiceService.getInvoiceStats(token);
            Map<String, Object> taskStats = taskService.getTaskStats(token);
            Map<String, Object> offerStats = offerService.getOfferStats(token);
            Map<String, Object> paymentStats = invoiceService.getPaymentStats(token);
            
            // Convertir en JSON pour les graphiques
            request.setAttribute("taskStats", gson.toJson(taskStats));
            request.setAttribute("invoiceStats", gson.toJson(stats));
            request.setAttribute("offerStats", gson.toJson(offerStats));
            
            // Transmettre directement les données de paiement
            request.setAttribute("paymentStats", paymentStats);
            
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors de la récupération des statistiques: " + e.getMessage());
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        }
    }
} 