package org.example.ui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.example.model.Product;
import org.example.service.InventoryService;

import java.util.*;

public class UserController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, Double> priceColumn;

    @FXML private ComboBox<String> searchTypeBox;
    @FXML private TextField searchField;

    private final InventoryService service = new InventoryService();

    @FXML
    public void initialize() {

        // Fill search options
        searchTypeBox.getItems().addAll("ID", "Name", "Category", "Price Range");
        searchTypeBox.setValue("Name");

        // Table column binding
        idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        categoryColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategory()));
        quantityColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantity()).asObject());
        priceColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrice()).asObject());

        refreshList();
    }

    public void refreshList() {
        List<Product> products = service.getAllProducts();
        if (products == null) products = List.of();
        productTable.getItems().setAll(products);
    }

    @FXML
    private void handleSearch(ActionEvent e) {
        String type = searchTypeBox.getValue();
        String val = searchField.getText().trim();

        if (val.isEmpty()) {
            show("Enter search value");
            return;
        }

        switch (type) {
            case "ID" -> {
                try {
                    Product p = service.getProductById(Integer.parseInt(val));
                    productTable.getItems().setAll(p != null ? List.of(p) : List.of());
                } catch (Exception ex) {
                    show("Invalid ID");
                }
            }

            case "Name" -> {
                var results = service.searchProductsByName(val);
                productTable.getItems().setAll(results);
            }

            case "Category" -> {
                var results = service.getProductByCategory(val);
                productTable.getItems().setAll(results);
            }

            case "Price Range" -> {
                try {
                    String[] parts = val.split("-");
                    double min = Double.parseDouble(parts[0]);
                    double max = Double.parseDouble(parts[1]);
                    var results = service.getProductByPriceRange(min, max);
                    productTable.getItems().setAll(results);
                } catch (Exception ex) {
                    show("Enter valid price range: 100-500");
                }
            }
        }
    }

    @FXML
    private void handleReset(ActionEvent e) {
        searchField.clear();
        refreshList();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ui/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (Exception e) {
            show("Logout failed");
        }
    }

    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.show();
    }
}
