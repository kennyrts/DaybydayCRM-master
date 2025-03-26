package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    
    private static final String API_URL = "http://localhost:80/api/login";
    private static final Gson gson = new Gson();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Afficher la page de login
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Récupérer les paramètres du formulaire
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            // Créer l'objet JSON pour le corps de la requête
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("email", email);
            requestBody.addProperty("password", password);
            
            // Créer la connexion HTTP
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            // Écrire le corps de la requête
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // Lire la réponse
            int responseCode = conn.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                // Lire le corps de la réponse
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder responseBody = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        responseBody.append(responseLine.trim());
                    }
                    
                    // Parser la réponse JSON
                    JsonObject jsonResponse = gson.fromJson(responseBody.toString(), JsonObject.class);
                    
                    if (jsonResponse.has("status") && jsonResponse.get("status").getAsString().equals("success")) {
                        // Authentification réussie
                        HttpSession session = request.getSession();
                        session.setAttribute("token", jsonResponse.get("token").getAsString());
                        session.setAttribute("user", jsonResponse.get("user").toString());
                        
                        // Rediriger vers le dashboard
                        response.sendRedirect( "dashboard");
                    } else {
                        // Authentification échouée
                        request.setAttribute("error", "Identifiants invalides");
                        request.getRequestDispatcher("index.jsp").forward(request, response);
                    }
                }
            } else {
                // Lire le message d'erreur si présent
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorResponse.append(errorLine.trim());
                    }
                    request.setAttribute("error", "Erreur de connexion: " + errorResponse.toString());
                }
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
