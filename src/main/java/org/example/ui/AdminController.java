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
import org.example.service.StockAlertService;
import org.example.util.EmailUtil;
import org.example.util.CSVHelper;


import java.util.List;

public class AdminController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> thresholdColumn;


    private final StockAlertService alertService = new StockAlertService();


    private final InventoryService service = ServiceLocator.getInventoryService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        categoryColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategory()));
        quantityColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantity()).asObject());
        priceColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()).asObject());
        thresholdColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getThreshold()).asObject());
        alertService.checkStockAlerts();
        refreshList();
    }

    public void refreshList() {
        try {
            List<Product> products = service.getAllProducts();
            if (products == null) products = List.of(); // safe guard
            productTable.getItems().setAll(products);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load products: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleRefresh(ActionEvent e) { refreshList(); }

    @FXML
    private void openAddProductPopup(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ui/add_product.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            // showAndWait so we can refresh after dialog closes
            stage.showAndWait();
            refreshList();

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Unable to open Add Product dialog: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
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

            EditProductController controller = loader.getController();
            controller.setProduct(selected);

            Stage stage = new Stage();
            stage.setTitle("Edit Product");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refreshList();

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Unable to open Edit Product dialog: " + ex.getMessage(), Alert.AlertType.ERROR);
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
    private void handleExportCsv() {
        try {
            var products = service.getAllProducts();

            if (products == null || products.isEmpty()) {
                showAlert("No Data", "No products to export!", Alert.AlertType.WARNING);
                return;
            }

            // Generate CSV
            String email = ServiceLocator.getLoggedInEmail();
            String path = CSVHelper.generateProductsReport(products, email);

            if (path == null) {
                showAlert("Error", "Failed to generate CSV file!", Alert.AlertType.ERROR);
                return;
            }

            // Email the CSV
            EmailUtil.sendReport(
                    email,
                    "📦 Inventory CSV Report",
                    "Your latest inventory report is attached.",
                    path
            );

            showAlert("Exported",
                    "CSV generated and emailed to:\n" + email,
                    Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to export and email CSV: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ui/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inventory - Login");
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
