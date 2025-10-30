package org.example.service;

import org.example.dao.ProductDAO;
import org.example.dao.ProductDAOImpl;
import org.example.model.Product;
import org.example.util.EmailUtil;

import java.util.List;

public class StockAlertService {
    private final ProductDAO dao = new ProductDAOImpl();

    public void checkStockAlerts() {
        try {
            List<Product> products = dao.getAllProducts();
            StringBuilder alertMessage = new StringBuilder();
            boolean hasAlerts = false;

            for (Product p : products) {
                if (p.getQuantity() <= p.getThreshold()) {
                    hasAlerts = true;

                    int reorderQty = p.getThreshold() * 2; // your original logic

                    alertMessage.append("⚠️ Low Stock Alert: ").append(p.getName()).append("\n")
                            .append("Current Qty: ").append(p.getQuantity()).append("\n")
                            .append("Threshold: ").append(p.getThreshold()).append("\n")
                            .append("Recommended Reorder Qty: ").append(reorderQty)
                            .append("\n\n");
                }
            }

            if (hasAlerts) {

                String subject = "📊 Inventory Stock Alert Summary";
                String body = "Dear Admin,\n\nThe following products are low in stock:\n\n" +
                        alertMessage +
                        "\nPlease restock accordingly.\n\n- Inventory Management System";

                EmailUtil.sendAlertEmail(subject, body);
                System.out.println("📩 Consolidated stock alert email sent successfully!");
            } else {
                System.out.println("✅ All products have sufficient stock levels.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error checking stock alerts: " + e.getMessage());
        }
    }
}
