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

    private final JButton dailyChallengesButton = new JButton("Open Daily Challenges");
    private final JLabel xpLabel = new JLabel("XP: 0");
    private final JProgressBar xpBar = new JProgressBar(0, 100); // Level 1 XP requirement

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
        xpBar.setStringPainted(true);
        add(top, BorderLayout.NORTH);

        dailyChallengesButton.addActionListener(e -> openDailyChallenges());
        dailyChallengesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> frame.onLogout());

        JPanel centerPanel = new JPanel(); // Use a panel to hold multiple buttons
        centerPanel.add(dailyChallengesButton);
        centerPanel.add(logoutButton);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void openDailyChallenges() {
        if (currentUserId == 0) {
            JOptionPane.showMessageDialog(this, "No user logged in. Please log in to see challenges.", "Not Logged In", JOptionPane.WARNING_MESSAGE);
            return;
        }

        dailyChallengesButton.setEnabled(false);
        dailyChallengesButton.setText("Loading...");

        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                return api.getDailyChallenges(currentUserId);
            }

            @Override
            protected void done() {
                try {
                    JSONObject resp = get();
                    if (API_OK_STATUS.equals(resp.optString(API_STATUS_KEY))) {
                        JSONArray challengesArray = resp.getJSONArray(API_CHALLENGES_KEY);
                        frame.onOpenDailyChallenges(challengesArray, currentUserId);
                    } else {
                        String message = resp.optString("message", "Could not load challenges. Please try again.");
                        JOptionPane.showMessageDialog(DashboardPanel.this, message, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    if (cause instanceof IOException) {
                        JOptionPane.showMessageDialog(DashboardPanel.this, "A network error occurred. Please check your connection.", "Network Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(DashboardPanel.this, "An error occurred: " + cause.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    cause.printStackTrace();
                } finally {
                    dailyChallengesButton.setEnabled(true);
                    dailyChallengesButton.setText("Open Daily Challenges");
                }
            }
        }.execute();
    }
    public void setCurrentUserId(int id) {
        this.currentUserId = id;
    }

    public void updateXp(int newXp, int newLevel) {
        // Assuming 100 XP per level for simplicity
        int xpForCurrentLevel = newXp % 100;
        xpLabel.setText(String.format("Level: %d | XP: %d / 100", newLevel, xpForCurrentLevel));
        xpBar.setValue(xpForCurrentLevel);
    }
}
