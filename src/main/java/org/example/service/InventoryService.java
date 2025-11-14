package org.example.service;

import org.example.dao.ProductDAOImpl;
import org.example.exception.DatabaseException;
import org.example.exception.ProductNotFoundException;
import org.example.model.Product;
import org.example.ui.ServiceLocator;
import org.example.util.CSVHelper;
import org.example.util.EmailUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {
    public ProductDAOImpl dao = new ProductDAOImpl();


    public void addProduct(Product p) {
        try {
            dao.addProduct(p);
        } catch (DatabaseException e) {
            System.err.println("❌ Failed to add product. Reason: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        try {
            List<Product> products = dao.getAllProducts();

            // Print for console use
            if (products.isEmpty()) {
                System.out.println("⚠ No products available.");
            } else {
                System.out.println("---- 📦 Product List ----");
                System.out.printf("%-5s %-15s %-15s %-10s %-10s %-10s%n",
                        "ID", "Name", "Category", "Quantity", "Price", "Threshold");
                System.out.println("--------------------------------------------------------------------------");
                products.forEach(p ->
                        System.out.printf("%-5d %-15s %-15s %-10d %-10.2f %-10d%n",
                                p.getId(), p.getName(), p.getCategory(), p.getQuantity(), p.getPrice(), p.getThreshold()));
            }

            return products;

        } catch (DatabaseException e) {
            System.err.println("❌ Failed to fetch products. Reason: " + e.getMessage());
            return List.of(); // return empty list instead of null
        }
    }



    public Product getProductById(int id) {
        try {
            Product p = dao.getProductById(id);
            if (p != null) {
                System.out.println("Product Found: " + p);
            } else {
                System.out.println("⚠️ Product not found!");
            }
        } catch (ProductNotFoundException e) {
            System.err.println("⚠ " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println("❌ Error searching product. Reason: " + e.getMessage());
        }
        return dao.getProductById(id);
    }

    public Product getProductByName(String name) {
        try {
            List<Product> p = dao.getProductByName(name);
            if (p != null) {
                System.out.println("Product Found: " + p );
            } else {
                System.out.println("⚠️ Product not found!");
            }
        } catch (ProductNotFoundException e) {
            System.err.println("⚠ " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println("❌ Error searching product. Reason: " + e.getMessage());
        }
        return (Product) dao.getProductByName(name);
    }

    public Product getProductByCategory(String category) {
        try {
            List<Product> p = dao.getProductByCategory(category);
            if (p != null) {
                System.out.println("Product Found: " + p);
            } else {
                System.out.println("⚠️ Product not found!");
            }
        } catch (ProductNotFoundException e) {
            System.err.println("⚠ " + e.getMessage());
        }
        return (Product) dao.getProductByCategory(category);
    }

    public Product getProductByPriceRange(double minPrice, double maxPrice) {
        try {
            List<Product> p = dao.getProductByPriceRange( minPrice,maxPrice);
            if (p != null || !p.isEmpty()) {
                System.out.println("Product Found: " + p);
            } else {
                System.out.println("⚠️ Product not found!");
            }
        } catch (ProductNotFoundException e) {
            System.err.println("⚠ " + e.getMessage());
        }
        return (Product) dao.getProductByPriceRange(minPrice,maxPrice);
    }

    public void updateProduct(int id, String name, String category, String quantity, String price, String threshold) {
        try {
            Product p = dao.getProductById(id);
            if (p == null) {
                System.err.println("❌ Product not found");
                return;
            }

            if (!name.trim().isEmpty()) {
                p.setName(name);
            }
            if (!category.trim().isEmpty()) {
                p.setCategory(category);
            }
            if (!quantity.trim().isEmpty()) {
                p.setQuantity(Integer.parseInt(quantity));
            }
            if (!price.trim().isEmpty()) {
                p.setPrice(Double.parseDouble(price));
            }
            if (!threshold.trim().isEmpty()) {
                p.setThreshold(Integer.parseInt(threshold));
            }

            dao.updateProduct(p);

        } catch (Exception e) {
            System.err.println("❌ Error Updating the product: " + e.getMessage());
        }
    }

    public void updateProduct(Product p) {
        dao.updateProduct(p);
    }


    public void deleteProduct(int id) {
        try {
            dao.deleteProduct(id);
        } catch (DatabaseException e) {
            System.err.println("❌ Failed to remove product. Reason: " + e.getMessage());
        }
    }

    public List<Product> searchProductsByName(String name) {
        try {
            return dao.searchProductsByName(name);
        } catch (Exception e) {
            System.out.println("❌ Error searching products: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public String exportCSV() {
        try {
            String email = ServiceLocator.getLoggedInEmail();
            if (email == null) email = "Unknown";

            // Step 1 – Generate CSV using your CSVHelper
            String path = CSVHelper.generateProductsReport(dao.getAllProducts(), email);

            if (path == null) {
                throw new RuntimeException("CSV generation failed!");
            }

            // Step 2 – Email CSV report to the logged-in email
            String receiver = ServiceLocator.getLoggedInEmail();
            if (receiver != null && !receiver.isEmpty()) {
                EmailUtil.sendReport(
                        receiver,
                        "📦 Inventory Report",
                        "Hi,\n\nYour inventory CSV report has been generated.\nPlease find the attachment.\n\nRegards,\nInventory System",
                        path
                );
            } else {
                System.out.println("⚠ No logged-in email found. Email not sent.");
            }

            return path;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("CSV export failed: " + e.getMessage());
        }
    }





}
