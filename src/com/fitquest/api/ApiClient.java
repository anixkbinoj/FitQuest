package com.fitquest.api;

import java.net.URI;
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

    private JSONObject postJson(String path, JSONObject body) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(resp.body());
    }

    public JSONObject register(String name, String email, String password) throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("email", email);
        body.put("password", password);
        return postJson("/register.php", body);
    }

    public JSONObject login(String email, String password) throws Exception {
        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("password", password);
        return postJson("/login.php", body);
    }

    public JSONObject getDailyChallenges(int userId) throws Exception {
        JSONObject body = new JSONObject();
        body.put("user_id", userId);
        return postJson("/getDailyChallenges.php", body);
    }

    public JSONObject submitProgress(int userId, int udcId, double progress, int complete) throws Exception {
        JSONObject body = new JSONObject();
        body.put("user_id", userId);
        body.put("udc_id", udcId);
        body.put("progress", progress);
        body.put("complete", complete);
        return postJson("/submitProgress.php", body);
    }
}
