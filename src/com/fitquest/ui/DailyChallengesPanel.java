package com.fitquest.ui;

import com.fitquest.api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class DailyChallengesPanel extends JPanel {

    private final JPanel challengesContainer;
    private final AppFrame frame;
    private ApiClient api;
    private int currentUserId;

    public DailyChallengesPanel(AppFrame frame, ApiClient api) {
        this.frame = frame;
        setLayout(new BorderLayout());
        add(new JLabel("Your Daily Challenges", SwingConstants.CENTER), BorderLayout.NORTH);

        challengesContainer = new JPanel();
        challengesContainer.setLayout(new BoxLayout(challengesContainer, BoxLayout.Y_AXIS));
        add(new JScrollPane(challengesContainer), BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> frame.show("dashboard"));
        add(backButton, BorderLayout.SOUTH);
    }

    public void showChallenges(JSONArray challenges, ApiClient api, int userId) {
        this.api = api;
        this.currentUserId = userId;
        challengesContainer.removeAll(); // Clear previous challenges

        if (challenges.length() == 0) {
            challengesContainer.add(new JLabel("No daily challenges available. Check back tomorrow!"));
        } else {
            for (int i = 0; i < challenges.length(); i++) {
                JSONObject challenge = challenges.getJSONObject(i);
                challengesContainer.add(createChallengePanel(challenge));
            }
        }

        challengesContainer.revalidate();
        challengesContainer.repaint();
    }

    private JPanel createChallengePanel(JSONObject challenge) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));

        String name = challenge.getString("name");
        String description = challenge.getString("description");
        int target = challenge.getInt("target");
        int udcId = challenge.getInt("udc_id");

        JLabel nameLabel = new JLabel(String.format("<html><b>%s</b></html>", name));
        JLabel descLabel = new JLabel(description);

        JProgressBar progressBar = new JProgressBar(0, target);
        progressBar.setValue(challenge.getInt("progress"));
        progressBar.setStringPainted(true);

        JButton completeButton = new JButton("Complete");
        completeButton.addActionListener(e -> completeChallenge(udcId));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(nameLabel);
        infoPanel.add(descLabel);

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);
        panel.add(completeButton, BorderLayout.EAST);

        return panel;
    }

    private void completeChallenge(int udcId) {
        try {
            JSONObject resp = api.submitProgress(currentUserId, udcId, 0, 1); // Assuming progress is handled server-side on completion
            frame.onChallengeCompleted(resp.getInt("new_xp"), resp.getInt("new_level"));
        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(this, "Network error completing challenge.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}