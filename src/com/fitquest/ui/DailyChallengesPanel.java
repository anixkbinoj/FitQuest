package com.fitquest.ui;

import com.fitquest.api.ApiClient;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;

public class DailyChallengesPanel extends JPanel {

    private final JPanel challengesContainer;

    public DailyChallengesPanel(AppFrame frame, ApiClient api) {
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
        // TODO: Implement the logic to display challenges from the JSONArray
        challengesContainer.removeAll(); // Clear previous challenges
        challengesContainer.add(new JLabel(challenges.length() + " challenges loaded. (UI not implemented yet)"));
        challengesContainer.revalidate();
        challengesContainer.repaint();
    }
}