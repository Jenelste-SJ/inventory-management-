package org.example.ui;

import javafx.event.ActionEvent;
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

    private final UserService userService = new UserService();

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            User u = userService.login(username, password);

            if (u == null) {
                messageLabel.setText("Invalid Credentials!");
                return;
            }

            if ("admin".equalsIgnoreCase(u.getRole())) {
                loadScreen("admin.fxml", "Admin Dashboard");
            } else {
                loadScreen("user.fxml", "User Dashboard");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading screen!");
        }
    }

    private void loadScreen(String file, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ui/" + file));
        Parent root = loader.load();
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
    }
}
