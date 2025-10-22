package com.fitquest.ui;
import javax.swing.*;
import java.awt.*;
import com.fitquest.api.ApiClient;

public class ProfileSetupPanel extends JPanel {
    public ProfileSetupPanel(AppFrame frame, ApiClient api) {
        setLayout(new GridLayout(7,2,8,8));
        JTextField name = new JTextField();
        JTextField age = new JTextField();
        JComboBox<String> gender = new JComboBox<>(new String[]{"male","female","other"});
        JTextField weight = new JTextField();
        JTextField height = new JTextField();
        JComboBox<String> level = new JComboBox<>(new String[]{"beginner","advanced","elite"});

        add(new JLabel("Name:")); add(name);
        add(new JLabel("Age:")); add(age);
        add(new JLabel("Gender:")); add(gender);
        add(new JLabel("Weight (kg):")); add(weight);
        add(new JLabel("Height (cm):")); add(height);
        add(new JLabel("Fitness level:")); add(level);

        JButton cont = new JButton("Continue");
        cont.addActionListener(e -> {
            // For demo, skip backend profile update
            DashboardPanel.setProfileData(name.getText(), age.getText(), weight.getText(), height.getText(), level.getSelectedItem().toString());
            frame.show("dashboard");
        });
        add(new JLabel()); add(cont);
    }
}
