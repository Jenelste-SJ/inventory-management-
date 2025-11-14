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
    private final InventoryService service = ServiceLocator.getInventoryService();

    // Called by AdminController when opening the edit popup
    public void setProduct(Product p) {
        this.product = p;

        // Populate form
        idField.setText(String.valueOf(p.getId()));
        idField.setEditable(false);

        nameField.setText(p.getName());
        categoryField.setText(p.getCategory());
        quantityField.setText(String.valueOf(p.getQuantity()));
        priceField.setText(String.valueOf(p.getPrice()));
        thresholdField.setText(String.valueOf(p.getThreshold()));
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            // update object fields
            product.setName(nameField.getText().trim());
            product.setCategory(categoryField.getText().trim());
            product.setQuantity(Integer.parseInt(quantityField.getText().trim()));
            product.setPrice(Double.parseDouble(priceField.getText().trim()));
            product.setThreshold(Integer.parseInt(thresholdField.getText().trim()));

            // call new method from InventoryService
            service.updateProduct(product);

            messageLabel.setText("✅ Product updated successfully!");

            closeWindow();

        } catch (NumberFormatException e) {
            messageLabel.setText("❌ Quantity/Price/Threshold must be numeric!");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("❌ Failed to update product: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }
}
