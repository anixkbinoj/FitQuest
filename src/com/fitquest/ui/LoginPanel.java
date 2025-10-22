package com.fitquest.ui;

import javax.swing.*;
import java.awt.*;
import com.fitquest.api.ApiClient;
import org.json.JSONObject;

public class LoginPanel extends JPanel {
    public LoginPanel(AppFrame frame, ApiClient api) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        JTextField emailField = new JTextField(20);
        JPasswordField passField = new JPasswordField(20);
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.addActionListener(ev -> {
            try {
                JSONObject resp = api.login(emailField.getText(), new String(passField.getPassword()));
                if ("ok".equals(resp.getString("status"))) {
                    JSONObject user = resp.getJSONObject("user");
                    int uid = user.getInt("id");
                    // Save user id somewhere (in DashboardPanel or App state). For simplicity, we will use DashboardPanel static field or better use shared context.
                    DashboardPanel.setCurrentUserId(uid);
                    frame.show("profile"); // ask physical details next
                } else {
                    JOptionPane.showMessageDialog(this, resp.optString("msg","Login failed"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage());
            }
        });

        registerBtn.addActionListener(ev -> frame.show("profile")); // or create register panel
        // layout
        c.gridx=0; c.gridy=0; add(new JLabel("Email:"), c);
        c.gridx=1; add(emailField, c);
        c.gridx=0; c.gridy=1; add(new JLabel("Password:"), c);
        c.gridx=1; add(passField, c);
        c.gridy=2; c.gridx=0; add(loginBtn, c);
        c.gridx=1; add(registerBtn, c);
    }
}
