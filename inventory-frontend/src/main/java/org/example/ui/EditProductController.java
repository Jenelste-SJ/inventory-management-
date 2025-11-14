package org.example.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.model.Product;
import org.example.service.InventoryService;

public class EditProductController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private TextField thresholdField;
    @FXML private Label messageLabel;

    private Product product;
    private final InventoryService service = new InventoryService();

    public void setProduct(Product p) {
        this.product = p;
        populateFields();
    }

    private void populateFields() {
        if (product == null) return;
        idField.setText(String.valueOf(product.getId()));
        idField.setDisable(true); // id not editable
        nameField.setText(product.getName());
        categoryField.setText(product.getCategory());
        quantityField.setText(String.valueOf(product.getQuantity()));
        priceField.setText(String.valueOf(product.getPrice()));
        thresholdField.setText(String.valueOf(product.getThreshold()));
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            int qty = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            int threshold = Integer.parseInt(thresholdField.getText().trim());

            // calling InventoryService.updateProduct(id, name, cat, qty, price, threshold)
            service.updateProduct(id, name, category, String.valueOf(qty), String.valueOf(price), String.valueOf(threshold));

            messageLabel.setText("Product updated.");
            closeWindow();
        } catch (NumberFormatException nfe) {
            messageLabel.setText("Quantity/threshold must be integers; price numeric.");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error updating: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage st = (Stage) idField.getScene().getWindow();
        st.close();
    }
}
