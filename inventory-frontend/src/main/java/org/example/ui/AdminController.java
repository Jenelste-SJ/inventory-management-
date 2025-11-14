package org.example.ui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.Product;
import org.example.service.InventoryService;

import java.util.List;

public class AdminController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> thresholdColumn;

    private final InventoryService service = new InventoryService();

    @FXML
    public void initialize() {
        // wire columns
        idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        categoryColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategory()));
        quantityColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantity()).asObject());
        priceColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()).asObject());
        thresholdColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getThreshold()).asObject());

        refreshList();
    }

    public void refreshList() {
        try {
            List<Product> products = service.getAllProducts(); // expects List<Product>
            productTable.getItems().setAll(products);
        } catch (Exception e) {
            showAlert("Error", "Failed to load products: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        refreshList();
    }

    @FXML
    private void openAddProductPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ui/add_product.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Unable to open Add Product dialog:\n" + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleEdit(ActionEvent event) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Select a product to edit", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ui/edit_product.fxml"));
            Parent root = loader.load();

            // pass selected product to controller
            EditProductController controller = loader.getController();
            controller.setProduct(selected);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Product");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshList();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open Edit Product dialog: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Select a product to delete", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete product: " + selected.getName() + " (ID: " + selected.getId() + ")?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    service.deleteProduct(selected.getId());
                    showAlert("Success", "Product deleted", Alert.AlertType.INFORMATION);
                    refreshList();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Error", "Delete failed: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        // simply go back to login screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ui/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inventory Login");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to logout: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }
}
