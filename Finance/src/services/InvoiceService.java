package services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class InvoiceService {
    private static final String API_BASE_URL = "http://localhost:80/api";
    private static final Gson gson = new Gson();

    public Map<String, Object> getInvoiceStats(String token) throws IOException {
        URL url = new URL(API_BASE_URL + "/invoices/count-by-status");
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
            throw new IOException("Failed to get invoice statistics");
        } finally {
            conn.disconnect();
        }
    }

    public Map<String, Object> getInvoicesByStatus(String status, String token) throws IOException {
        URL url = new URL(API_BASE_URL + "/invoices/status/" + status);
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
                        JsonObject data = jsonResponse.getAsJsonObject("data");
                        Map<String, Object> result = new HashMap<>();
                        result.put("status_display", data.get("status_display").getAsString());
                        result.put("status_code", data.get("status_code").getAsString());
                        
                        // Convertir manuellement les invoices pour s'assurer que l'ID est un Long
                        List<Map<String, Object>> invoices = new ArrayList<>();
                        for (JsonElement element : data.getAsJsonArray("invoices")) {
                            JsonObject invoice = element.getAsJsonObject();
                            Map<String, Object> invoiceMap = gson.fromJson(invoice, Map.class);
                            // Forcer la conversion de l'ID en Long
                            invoiceMap.put("id", invoice.get("id").getAsLong());
                            invoices.add(invoiceMap);
                        }
                        result.put("invoices", invoices);
                        
                        return result;
                    }
                }
            }
            throw new IOException("Failed to get invoices for status: " + status);
        } finally {
            conn.disconnect();
        }
    }

    public Map<String, Object> getInvoicePayments(Long invoiceId, String token) throws IOException {
        URL url = new URL(API_BASE_URL + "/invoices/" + invoiceId + "/payments");
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
            throw new IOException("Failed to get payments for invoice: " + invoiceId);
        } finally {
            conn.disconnect();
        }
    }

    public Map<String, Object> getPaymentStats(String token) throws IOException {
        URL url = new URL(API_BASE_URL + "/invoices/payment-stats");
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
            throw new IOException("Failed to get payment statistics");
        } finally {
            conn.disconnect();
        }
    }

    public Map<String, Object> getInvoicesBySection(String section, String token) throws IOException {
        URL url = new URL(API_BASE_URL + "/invoices/section/" + section);
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
            throw new IOException("Failed to get invoices for section: " + section);
        } finally {
            conn.disconnect();
        }
    }
} 