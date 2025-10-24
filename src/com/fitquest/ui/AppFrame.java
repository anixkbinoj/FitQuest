package com.fitquest.ui;

import javax.swing.*;
import java.awt.*;
import com.fitquest.api.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class AppFrame extends JFrame {
    private CardLayout cards = new CardLayout();
    private JPanel container = new JPanel(cards);
    private ApiClient api;

    // Keep references to panels to manage their state
    private DashboardPanel dashboard;
    private LoginPanel login;
    private ProfileSetupPanel profile;
    private DailyChallengesPanel dailyChallenges;

    public AppFrame(ApiClient api) {
        super("FitQuest - Your Daily Fitness Adventure");
        this.api = api;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // panels
        WelcomePanel welcome = new WelcomePanel(this);
        login = new LoginPanel(this, api); // LoginPanel will call back to AppFrame
        profile = new ProfileSetupPanel(this, api);
        dashboard = new DashboardPanel(this, api);
        dailyChallenges = new DailyChallengesPanel(this, api);

        container.add(welcome, "welcome");
        container.add(login, "login");
        container.add(profile, "profile");
        container.add(dashboard, "dashboard");
        container.add(dailyChallenges, "daily"); // Add the daily challenges panel

        getContentPane().add(container);
        show("welcome");
    }

    public void onLoginSuccess(JSONObject profileData) {
        // This method is now called from the UI thread with the complete profile
        int userId = profileData.optInt("user_id", 0);
        dashboard.setCurrentUserId(userId);
        dashboard.updateXp(profileData.optInt("xp", 0), profileData.optInt("level", 1));
        show("dashboard");
    }

    public void onOpenDailyChallenges(JSONArray challenges, int userId) {
        dailyChallenges.showChallenges(challenges, userId);
        show("daily");
    }

    public void onChallengeCompleted(int newXp, int newLevel) {
        dashboard.updateXp(newXp, newLevel);
        show("dashboard");
    }

    public void onLogout() {
        // Reset user-specific data on panels
        dashboard.setCurrentUserId(0);
        dashboard.updateXp(0, 1); // Reset XP bar to level 1
        login.clearFields();
        show("login"); // Go back to the login screen
    }

    public void show(String name) { cards.show(container, name); }

    public static void open(ApiClient api) {
        SwingUtilities.invokeLater(() -> new AppFrame(api).setVisible(true));
    }
}
