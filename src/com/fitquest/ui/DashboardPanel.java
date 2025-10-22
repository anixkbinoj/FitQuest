package com.fitquest.ui;
import javax.swing.*;
import java.awt.*;
import com.fitquest.api.ApiClient;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardPanel extends JPanel {
    // Constants for keys and panel names
    private static final String DAILY_CHALLENGES_PANEL_KEY = "daily";
    private static final String API_STATUS_KEY = "status";
    private static final String API_CHALLENGES_KEY = "challenges";
    private static final String API_OK_STATUS = "ok";

    private final AppFrame frame;
    private final ApiClient api;
    private int currentUserId = 0; // Store user ID per instance

    private final JLabel xpLabel = new JLabel("XP: 0");
    private final JProgressBar xpBar = new JProgressBar(0, 100);

    public DashboardPanel(AppFrame frame, ApiClient api) {
        this.frame = frame;
        this.api = api;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JPanel top = new JPanel(new FlowLayout());
        top.add(new JLabel("Welcome to FitQuest!"));
        top.add(xpLabel);
        top.add(xpBar);
        add(top, BorderLayout.NORTH);

        JButton dailyChallengesButton = new JButton("Open Daily Challenges");
        dailyChallengesButton.addActionListener(e -> openDailyChallenges());

        add(dailyChallengesButton, BorderLayout.CENTER);
    }

    private void openDailyChallenges() {
        if (currentUserId == 0) {
            JOptionPane.showMessageDialog(this, "No user logged in. Please log in to see challenges.", "Not Logged In", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            JSONObject resp = api.getDailyChallenges(currentUserId);
            if (API_OK_STATUS.equals(resp.optString(API_STATUS_KEY))) {
                JSONArray challengesArray = resp.getJSONArray(API_CHALLENGES_KEY);
                // Assuming DailyChallengesPanel has a static method to show challenges.
                // A better design might be to get an instance of the panel from AppFrame and update it.
                DailyChallengesPanel.showChallenges(challengesArray, api, currentUserId);
                frame.show(DAILY_CHALLENGES_PANEL_KEY);
            } else {
                String message = resp.optString("message", "Could not load challenges. Please try again.");
                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "A network error occurred. Please check your connection.", "Network Error", JOptionPane.ERROR_MESSAGE);
        } catch (JSONException ex) {
            JOptionPane.showMessageDialog(this, "Error parsing data from the server.", "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setCurrentUserId(int id) { this.currentUserId = id; }
}
