package com.fitquest.ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import org.json.JSONException;
import com.fitquest.api.ApiClient;
import org.json.JSONObject;

public class LoginPanel extends JPanel {
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passField = new JPasswordField(20);

    public LoginPanel(AppFrame frame, ApiClient api) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        // emailField and passField are now class members

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        loginBtn.addActionListener(ev -> {
            // Disable button to prevent multiple clicks
            loginBtn.setEnabled(false);
            loginBtn.setText("Logging in...");

            // Run network operation on a background thread
            new SwingWorker<JSONObject, Void>() {
                @Override
                protected JSONObject doInBackground() throws Exception {
                    // The login API call should return the full user profile on success.
                    return api.login(emailField.getText(), new String(passField.getPassword()));
                }

                @Override
                protected void done() {
                    // This runs on the UI thread after doInBackground completes
                    try {
                        JSONObject resp = get();
                        if ("ok".equals(resp.optString("status"))) {
                            // Use optInt for safer parsing. It returns 0 if the key is not found.
                            frame.onLoginSuccess(resp);
                        } else {
                            JOptionPane.showMessageDialog(LoginPanel.this, resp.optString("message", "Login failed. Please check your credentials."), "Login Failed", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (Exception ex) {
                        // This will catch exceptions from doInBackground (IOException, InterruptedException, JSONException)
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                        JOptionPane.showMessageDialog(LoginPanel.this, "An error occurred: " + cause.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        cause.printStackTrace();
                    } finally {
                        // Always re-enable the button
                        loginBtn.setEnabled(true);
                        loginBtn.setText("Login");
                    }
                }
            }.execute();
        });

        registerBtn.addActionListener(ev -> frame.show("profile")); // Navigate to the registration/profile panel
        // layout
        c.gridx=0; c.gridy=0; add(new JLabel("Email:"), c);
        c.gridx=1; add(emailField, c);
        c.gridx=0; c.gridy=1; add(new JLabel("Password:"), c);
        c.gridx=1; add(passField, c);
        c.gridy=2; c.gridx=0; add(loginBtn, c);
        c.gridx=1; add(registerBtn, c);
    }

    public void clearFields() {
        emailField.setText("");
        passField.setText("");
    }
}
