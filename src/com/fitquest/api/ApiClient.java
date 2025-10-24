package com.fitquest.api;

import java.net.URI;
import java.io.IOException;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import org.json.*; // add org.json library to project
import java.nio.charset.StandardCharsets;

public class ApiClient {
    private final String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private JSONObject postJson(String path, JSONObject body) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        String responseBody = resp.body();
        
        try {
            return new JSONObject(responseBody);
        } catch (JSONException e) {
            // This is critical for debugging. If the server returns an error (like a PHP warning) or
            // anything other than valid JSON, this will help identify the problem by showing the raw response.
            String errorMessage = "Failed to parse JSON from server. Response: " + responseBody;
            System.err.println(errorMessage); // Also log to console for visibility
            throw new JSONException(errorMessage, e);
        }
    }

    public JSONObject register(String name, String email, String password, Integer age, String gender, Double weight, Double height, String fitnessLevel) throws IOException, InterruptedException {
        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("email", email);
        body.put("password", password);
        // Add optional profile data
        if (age != null) body.put("age", age);
        if (gender != null) body.put("gender", gender);
        if (weight != null) body.put("weight", weight);
        if (height != null) body.put("height", height);
        if (fitnessLevel != null) body.put("fitness_level", fitnessLevel);

        return postJson("/register.php", body);
    }

    public JSONObject login(String email, String password) throws IOException, InterruptedException {
        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("password", password);
        return postJson("/login.php", body);
    }

    public JSONObject getDailyChallenges(int userId) throws IOException, InterruptedException {
        JSONObject body = new JSONObject();
        body.put("user_id", userId);
        return postJson("/getDailyChallenges.php", body);
    }

    public JSONObject submitProgress(int userId, int udcId, double progress, int complete) throws IOException, InterruptedException {
        JSONObject body = new JSONObject();
        body.put("user_id", userId);
        body.put("udc_id", udcId);
        body.put("progress", progress);
        body.put("complete", complete);
        return postJson("/submitProgress.php", body);
    }

    public JSONObject getUserProfile(int userId) throws IOException, InterruptedException {
        JSONObject body = new JSONObject();
        body.put("user_id", userId);
        return postJson("/getUserProfile.php", body);
    }
}
