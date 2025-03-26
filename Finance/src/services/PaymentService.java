package services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import models.Payment;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class PaymentService {
    private static final String API_BASE_URL = "http://localhost:80/api";
    private static final Gson gson = new Gson();

    public boolean updatePayment(Long paymentId, Double amount, String token) throws IOException {
        URL url = new URL(API_BASE_URL + "/payments/" + paymentId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setDoOutput(true);

            // Créer le corps de la requête
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("amount", amount);

            // Envoyer la requête
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Lire la réponse
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                    return "success".equals(jsonResponse.get("status").getAsString());
                }
            }
            return false;
        } finally {
            conn.disconnect();
        }
    }

    public boolean deletePayment(Long paymentId, String token) throws IOException {
        URL url = new URL(API_BASE_URL + "/payments/" + paymentId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                    return "success".equals(jsonResponse.get("status").getAsString());
                }
            }
            return false;
        } finally {
            conn.disconnect();
        }
    }
} 