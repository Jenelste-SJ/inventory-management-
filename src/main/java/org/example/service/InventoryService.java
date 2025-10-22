package org.example.service;

import org.example.dao.ProductDAOImpl;
import org.example.exception.DatabaseException;
import org.example.exception.ProductNotFoundException;
import org.example.model.Product;

import java.util.List;

public class InventoryService {
    public ProductDAOImpl dao = new ProductDAOImpl();

//Products
    public void addProduct(Product p) {
        try {
            dao.addProduct(p);
        } catch (DatabaseException e) {
            System.err.println("❌ Failed to add product. Reason: " + e.getMessage());
        }
    }

    public void getAllProducts() {
        try {
            List<Product> products = dao.getAllProducts();
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

        } catch (DatabaseException e) {
            System.err.println("❌ Failed to fetch products. Reason: " + e.getMessage());
        }

    }


    public void getProductById(int id) {
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
    }

    public void getProductByName(String name) {
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
    }

    public void getProductByCategory(String category) {
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
    }

    public void getProductByPriceRange(double minPrice, double maxPrice) {
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
    }

    public void updateProduct(int id, String name,String category, String quantity, String price, String threshold) {
        try {
            Product p = dao.getProductById(id);
            if (p != null && !name.trim().isEmpty()) {
                p.setName(name);
            }
            if (p != null && !category.trim().isEmpty()) {
                p.setCategory(category);
            }
            if (p !=null && !quantity.trim().isEmpty()){
                int q=Integer.parseInt(quantity);
                p.setQuantity(q);
            }
            if (p !=null  && !price.trim().isEmpty()){
                int pp=Integer.parseInt(price);
                p.setPrice(pp);
            }
            if (p !=null  && !threshold.trim().isEmpty()){
                int t=Integer.parseInt(threshold);
                p.setThreshold(t);
            }
            dao.updateProduct(p);
        } catch (DatabaseException e) {
            System.err.println("❌ Error Updating the product.");
        }
    }


    public void deleteProduct(int id) {
        try {
            boolean deleted = dao.deleteProduct(id);
            if (deleted) {
                System.out.println(" Product removed successfully!");
            } else {
                System.out.println("No product found with that ID!");
            }
        } catch (DatabaseException e) {
            System.err.println("❌ Failed to remove product. Reason: " + e.getMessage());
        }
    }

}
