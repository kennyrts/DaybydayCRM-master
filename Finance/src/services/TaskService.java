package services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class TaskService {
    private static final String API_BASE_URL = "http://localhost:80/api";
    private static final Gson gson = new Gson();

    public Map<String, Object> getTaskStats(String token) throws IOException {
        System.out.println("getTaskStats");
        URL url = new URL(API_BASE_URL + "/tasks/stats");
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
                    
                    if (jsonResponse.get("success").getAsBoolean()) {
                        return gson.fromJson(jsonResponse.get("data"), Map.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new IOException("Error parsing task statistics response: " + e.getMessage());
                }
            }
            throw new IOException("Failed to get task statistics");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error getting task statistics: " + e.getMessage());
        } finally {
            conn.disconnect();
            System.out.println("getTaskStats fin");
        }
    }
}