package com.fitquest.ui;
import javax.swing.*;
import java.awt.*;
import com.fitquest.api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class DashboardPanel extends JPanel {
    private static int currentUserId = 0;
    private ApiClient api;
    private JLabel xpLabel = new JLabel("XP: 0");
    private JProgressBar xpBar = new JProgressBar(0,100);

    public DashboardPanel(AppFrame frame, ApiClient api) {
        this.api = api;
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout());
        top.add(new JLabel("Welcome to FitQuest!"));
        top.add(xpLabel);
        top.add(xpBar);
        add(top, BorderLayout.NORTH);

        JButton daily = new JButton("Open Daily Challenges");
        daily.addActionListener(e -> {
            if (currentUserId != 0) {
                try {
                    JSONObject resp = api.getDailyChallenges(currentUserId);
                    if ("ok".equals(resp.getString("status"))) {
                        JSONArray arr = resp.getJSONArray("challenges");
                        // Show daily challenges panel with data
                        DailyChallengesPanel.showChallenges(arr, api, currentUserId);
                        frame.show("daily"); // you must add it to AppFrame container as "daily"
                    } else {
                        JOptionPane.showMessageDialog(this, "Could not load challenges");
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No user logged in");
            }
        });

        add(daily, BorderLayout.CENTER);
    }

    public static void setCurrentUserId(int id) { currentUserId = id; }
    public static void setProfileData(String name, String age, String weight, String height, String level) {
        // optional: store to static fields or pass to API
    }
}
