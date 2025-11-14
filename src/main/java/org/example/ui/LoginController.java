package org.example.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.model.User;
import org.example.service.UserService;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final UserService userService = ServiceLocator.getUserService();

    @FXML
    private void handleLogin() {
        try {
            String uname = usernameField.getText();
            String pass = passwordField.getText();

            User user = userService.login(uname, pass);

            if (user == null) {
                messageLabel.setText("Invalid username or password!");
                return;
            }

            // Store email BEFORE loading UI
            ServiceLocator.setLoggedInEmail(user.getEmail());

            String next = user.getRole().equalsIgnoreCase("admin") ?
                    "admin.fxml" : "user.fxml";

            load(next, user.getRole() + " Dashboard");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error: " + e.getMessage());
        }
    }

    private void load(String fxml, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ui/" + fxml));
        Parent root = loader.load();
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
    }
}
