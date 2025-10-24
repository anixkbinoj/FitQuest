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
    private final JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Select...", "Male", "Female", "Other"});
    private final JTextField weightField = new JTextField();
    private final JTextField heightField = new JTextField();
    private final JComboBox<String> levelCombo = new JComboBox<>(new String[]{"Select...", "Beginner", "Advanced", "Elite"});

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
        // Input validation
        if (nameField.getText().isBlank() || emailField.getText().isBlank() || passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Name, Email, and Password cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Parse optional fields, handling potential blank inputs
        Integer age = null;
        Double weight = null;
        Double height = null;
        try {
            if (!ageField.getText().isBlank()) age = Integer.parseInt(ageField.getText());
            if (!weightField.getText().isBlank()) weight = Double.parseDouble(weightField.getText());
            if (!heightField.getText().isBlank()) height = Double.parseDouble(heightField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age, Weight, and Height must be valid numbers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String gender = genderCombo.getSelectedIndex() > 0 ? (String) genderCombo.getSelectedItem() : null;
        String fitnessLevel = levelCombo.getSelectedIndex() > 0 ? (String) levelCombo.getSelectedItem() : null;

        try {
            // Call the full register method with all profile data
            JSONObject resp = api.register(
                    nameField.getText(),
                    emailField.getText(),
                    new String(passwordField.getPassword()),
                    age, gender, weight, height, fitnessLevel
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
