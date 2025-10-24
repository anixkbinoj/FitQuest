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
    private final JButton registerButton = new JButton("Register & Continue");

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

        registerButton.addActionListener(e -> registerAndContinue());
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(e -> frame.show("login"));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
        final Integer[] age = {null};
        final Double[] weight = {null};
        final Double[] height = {null};
        try {
            if (!ageField.getText().isBlank()) age[0] = Integer.parseInt(ageField.getText());
            if (!weightField.getText().isBlank()) weight[0] = Double.parseDouble(weightField.getText());
            if (!heightField.getText().isBlank()) height[0] = Double.parseDouble(heightField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age, Weight, and Height must be valid numbers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String gender = genderCombo.getSelectedIndex() > 0 ? (String) genderCombo.getSelectedItem() : null;
        String fitnessLevel = levelCombo.getSelectedIndex() > 0 ? (String) levelCombo.getSelectedItem() : null;

        // Disable the button during the network request
        registerButton.setEnabled(false);
        registerButton.setText("Registering...");

        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                return api.register(
                        nameField.getText(),
                        emailField.getText(),
                        new String(passwordField.getPassword()),
                        age[0], gender, weight[0], height[0], fitnessLevel
                );
            }

            @Override
            protected void done() {
                try {
                    JSONObject resp = get();
                    if ("ok".equals(resp.optString("status"))) {
                        // Use optInt for safer parsing. It returns 0 if the key is not found.
                        frame.onLoginSuccess(resp);
                    } else {
                        JOptionPane.showMessageDialog(ProfileSetupPanel.this, resp.optString("message", "Registration failed."), "Registration Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(ProfileSetupPanel.this, "An error occurred during registration: " + cause.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    cause.printStackTrace();
                } finally {
                    registerButton.setEnabled(true);
                    registerButton.setText("Register & Continue");
                }
            }
        }.execute();
    }
}
