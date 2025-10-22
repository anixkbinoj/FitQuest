package com.fitquest.ui;
import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {
    public WelcomePanel(AppFrame frame) {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("FitQuest", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        JLabel subtitle = new JLabel("Your Daily Fitness Adventure", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        JTextArea desc = new JTextArea("Simple daily gamified physical challenges.\nSuitable for all ages. Click Start to continue.");
        desc.setEditable(false);
        desc.setBackground(getBackground());
        JPanel center = new JPanel(new GridLayout(3,1));
        center.add(title); center.add(subtitle); center.add(desc);
        add(center, BorderLayout.CENTER);
        JButton start = new JButton("Start");
        start.addActionListener(e -> frame.show("login"));
        add(start, BorderLayout.SOUTH);
    }
}
