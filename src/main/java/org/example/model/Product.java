package org.example.model;

import org.example.exception.InvalidProductException;

public class Product {
    private int id;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private int threshold;

    public Product(int id, String name, String category, int quantity, double price, int threshold) {
        if (id <= 0 )
            throw new InvalidProductException("❌ ID must be greater than 0.");
        if (name == null || name.trim().isEmpty())
            throw new InvalidProductException("❌ Name cannot be empty.");
        if (category == null || category.trim().isEmpty())
            throw new InvalidProductException("❌ Category cannot be empty.");
        if (quantity < 0)
            throw new InvalidProductException("❌ Quantity cannot be negative.");
        if (price <= 0)
            throw new InvalidProductException("❌ Price must be greater than 0.");

        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.threshold = threshold;
    }

    // Getters & Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return  "\n"+id + " | " + name + " | " + category + " | " + quantity + " | " + price + " | " + threshold;
    }
}
