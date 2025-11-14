package org.example.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FrontendMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ui/login.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Inventory System - Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
