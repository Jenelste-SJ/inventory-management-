package org.example.Service;

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
                System.out.println("---- 📦Product List ----");
                products.forEach(System.out::println);
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
            if (p != null) {
                System.out.println("Product Found: " + p);
            } else {
                System.out.println("⚠️ Product not found!");
            }
        } catch (ProductNotFoundException e) {
            System.err.println("⚠ " + e.getMessage());
        }
    }

    public void updateProduct(int id, String name,String category, String quantity, String price) {
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
            dao.updateProduct(p);
            System.out.println("Product Updated successfully!");
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
