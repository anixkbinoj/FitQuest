package com.fitquest.ui;
import javax.swing.*;
import java.awt.*;

import com.fitquest.api.ApiClient;
import org.json.JSONObject;

public class ProfileSetupPanel extends JPanel {
    private final AppFrame frame;
    private final ApiClient api;
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JTextField ageField = new JTextField();
    private final JComboBox<String> genderCombo = new JComboBox<>(new String[]{"male","female","other"});
    private final JTextField weightField = new JTextField();
    private final JTextField heightField = new JTextField();
    private final JComboBox<String> levelCombo = new JComboBox<>(new String[]{"beginner","advanced","elite"});

    public ProfileSetupPanel(AppFrame frame, ApiClient api) {
        this.frame = frame;
        this.api = api;
        setLayout(new GridLayout(9, 2, 8, 8));

        add(new JLabel("Name:")); add(nameField);
        add(new JLabel("Email:")); add(emailField);
        add(new JLabel("Password:")); add(passwordField);
        add(new JLabel("Age:")); add(ageField);
        add(new JLabel("Gender:")); add(genderCombo);
        add(new JLabel("Weight (kg):")); add(weightField);
        add(new JLabel("Height (cm):")); add(heightField);
        add(new JLabel("Fitness level:")); add(levelCombo);

        JButton registerButton = new JButton("Register & Continue");
        registerButton.addActionListener(e -> registerAndContinue());

        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(e -> frame.show("login"));

        add(backButton);
        add(registerButton);
    }

    private void registerAndContinue() {
        try {
            // TODO: Add input validation (e.g., check for empty fields, valid numbers)
            JSONObject resp = api.register(
                    nameField.getText(),
                    emailField.getText(),
                    new String(passwordField.getPassword())
                    // We can extend api.register to include other profile data later
            );

            if ("ok".equals(resp.optString("status"))) {
                int userId = resp.getInt("user_id");
                frame.onLoginSuccess(userId); // Use the same flow as login
            } else {
                JOptionPane.showMessageDialog(this, resp.optString("msg", "Registration failed."), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred during registration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
