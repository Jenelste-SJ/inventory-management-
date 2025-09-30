package org.example.util;

import org.example.dao.ProductDAOImpl;
import org.example.model.Product;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class CSVHelp {
    public void exportProductsToCSV(String filePath) throws Exception {
        ProductDAOImpl dao = new ProductDAOImpl();
        List<Product> products = dao.getAllProducts();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("id,name,category,quantity,price");
            for (Product p : products) {
                writer.println(
                        p.getId() + "," +
                                p.getName() + ","  +
                                p.getCategory() + "," +
                                p.getQuantity()+"," +
                                p.getPrice()
                );
            }
            System.out.println("✅ Products exported to " + filePath);
        }
    }

}
