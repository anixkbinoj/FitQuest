package com.fitquest.ui;

import javax.swing.*;
import java.awt.*;
import com.fitquest.api.ApiClient;

public class AppFrame extends JFrame {
    private CardLayout cards = new CardLayout();
    private JPanel container = new JPanel(cards);

    private ApiClient api;

    public AppFrame(ApiClient api) {
        super("FitQuest - Your Daily Fitness Adventure");
        this.api = api;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // panels
        WelcomePanel welcome = new WelcomePanel(this);
        LoginPanel login = new LoginPanel(this, api);
        ProfileSetupPanel profile = new ProfileSetupPanel(this, api);
        DashboardPanel dashboard = new DashboardPanel(this, api);

        container.add(welcome, "welcome");
        container.add(login, "login");
        container.add(profile, "profile");
        container.add(dashboard, "dashboard");

        getContentPane().add(container);
        show("welcome");
    }

    public void show(String name) { cards.show(container, name); }

    public static void open(ApiClient api) {
        SwingUtilities.invokeLater(() -> new AppFrame(api).setVisible(true));
    }
}
