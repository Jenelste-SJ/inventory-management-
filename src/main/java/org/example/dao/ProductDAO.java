package org.example.dao;

import org.example.model.Product;

import java.util.List;

public interface ProductDAO {
    void addProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(int id);
    List<Product> getProductByName(String name);
    List<Product> getProductByCategory(String category);
    void updateProduct(Product product);
    boolean deleteProduct(int id);
}
