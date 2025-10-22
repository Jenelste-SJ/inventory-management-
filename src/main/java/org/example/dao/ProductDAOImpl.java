package org.example.dao;

import org.example.exception.DatabaseException;
import org.example.exception.InvalidProductException;
import org.example.model.Product;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (id, name, category, quantity, price, threshold) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setInt(4, product.getQuantity());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getThreshold());

            stmt.executeUpdate();
            System.out.println("✅ Product added successfully.");
        } catch (Exception e) {
            System.out.println("❌ Error adding product:\n REASON:You can't add product with an already existing ID.❗️❗️USE A DIFFERENT ID ❗️❗️  " );
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getInt("threshold")
                ));
            }
        } catch (Exception e) {
            System.out.println("❌ Error fetching products: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getInt("threshold")
                );
            }
        } catch (Exception e) {
            System.out.println("❌ Error searching product: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Product> getProductByName(String name) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getInt("threshold")
                ));
            }
        } catch (Exception e) {
            System.out.println("❌ Error searching by name: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + category + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getInt("threshold")
                ));
            }
        } catch (Exception e) {
            System.out.println("❌ Error searching by category: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Product> getProductByPriceRange(double minPrice, double maxPrice) throws DatabaseException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM inventory WHERE price BETWEEN ? AND ? ORDER BY price ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getInt("threshold")
                ));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving products by price range", e);
        }
        return list;
    }

    @Override
    public void updateProduct(Product product) {
        String sql = "UPDATE products SET name=?, category=?, quantity=?, price=? ,threshold=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getThreshold());
            stmt.setInt(6, product.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Product updated successfully.");
            } else {
                System.out.println("⚠️ Product not found.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error updating product: " + e.getMessage());
        }
    }



    @Override
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Product deleted successfully.");
            } else {
                System.out.println("⚠️ Product not found.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error deleting product: " + e.getMessage());
        }
        return false;
    }
}
