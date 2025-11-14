package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.model.Product;
import org.example.service.InventoryService;

public class AddProductController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private TextField thresholdField;
    @FXML private Label messageLabel;

    private final InventoryService service = ServiceLocator.getInventoryService();

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            int qty = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            int threshold = Integer.parseInt(thresholdField.getText().trim());

            Product p = new Product(id, name, category, qty, price, threshold);
            service.addProduct(p); // calls existing backend

            messageLabel.setText("Product added.");
            // close dialog
            Stage st = (Stage) idField.getScene().getWindow();
            st.close();
        } catch (NumberFormatException nfe) {
            messageLabel.setText("ID, quantity, threshold must be integers; price numeric.");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error adding product: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage st = (Stage) idField.getScene().getWindow();
        st.close();
    }
}
