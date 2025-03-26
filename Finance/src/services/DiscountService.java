package services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class DiscountService {
    private static final String API_BASE_URL = "http://localhost:80/api";
    private static final Gson gson = new Gson();

    public Map<String, Object> getCurrentRate(String token) throws IOException {
        URL url = new URL(API_BASE_URL + "/discounts/current");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                    
                    if ("success".equals(jsonResponse.get("status").getAsString())) {
                        return gson.fromJson(jsonResponse.get("data"), Map.class);
                    }
                }
            }
            throw new IOException("Failed to get current discount rate");
        } finally {
            conn.disconnect();
        }
    }

    public Map<String, Object> addDiscountRate(double rate, String token) throws IOException {
        URL url = new URL(API_BASE_URL + "/discounts");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setDoOutput(true);

            // Créer le corps de la requête
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("rate", rate);

            // Envoyer la requête
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                    
                    if ("success".equals(jsonResponse.get("status").getAsString())) {
                        return gson.fromJson(jsonResponse.get("data"), Map.class);
                    }
                }
            }
            throw new IOException("Failed to add discount rate");
        } finally {
            conn.disconnect();
        }
    }
} 