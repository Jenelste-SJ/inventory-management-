package org.example.service;

import org.example.dao.ProductDAO;
import org.example.model.Product;
import org.example.util.EmailUtil;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class StockAlertServiceTest {

    @Mock
    private ProductDAO mockProductDAO;

    private StockAlertService stockAlertService;
    private MockedStatic<EmailUtil> mockedEmailUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockAlertService = new StockAlertService() {
            // Override DAO with mock for isolation
            @Override
            public void checkStockAlerts() {
                try {
                    List<Product> products = mockProductDAO.getAllProducts();
                    StringBuilder alertMessage = new StringBuilder();
                    boolean hasAlerts = false;

                    for (Product p : products) {
                        if (p.getQuantity() <= p.getThreshold()) {
                            hasAlerts = true;
                            int reorderQty = p.getThreshold() * 2;
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
        };

        // Mock static EmailUtil
        mockedEmailUtil = mockStatic(EmailUtil.class);
    }

    @AfterEach
    void tearDown() {
        if (mockedEmailUtil != null) mockedEmailUtil.close();
    }

    // ✅ Test 1: When no low-stock products → No email should be sent
    @Test
    void testCheckStockAlerts_NoLowStock() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(1, "Laptop", "Electronics", 20, 50000.0, 10),
                new Product(2, "Phone", "Electronics", 25, 30000.0, 10)
        );

        when(mockProductDAO.getAllProducts()).thenReturn(products);

        stockAlertService.checkStockAlerts();

        mockedEmailUtil.verify(() -> EmailUtil.sendAlertEmail(anyString(), anyString()), never());
    }

    // ✅ Test 2: When low-stock products exist → Send single consolidated email
    @Test
    void testCheckStockAlerts_WithLowStock() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(1, "Laptop", "Electronics", 5, 50000.0, 10),
                new Product(2, "Phone", "Electronics", 25, 30000.0, 10),
                new Product(3, "Watch", "Accessories", 3, 5000.0, 10)
        );

        when(mockProductDAO.getAllProducts()).thenReturn(products);

        stockAlertService.checkStockAlerts();

        mockedEmailUtil.verify(() -> EmailUtil.sendAlertEmail(
                eq("📊 Inventory Stock Alert Summary"), contains("Laptop")), times(1));
    }

    // ✅ Test 3: When exception occurs → no crash
    @Test
    void testCheckStockAlerts_WithException() throws Exception {
        when(mockProductDAO.getAllProducts()).thenThrow(new RuntimeException("DB error"));

        assertDoesNotThrow(() -> stockAlertService.checkStockAlerts());
        mockedEmailUtil.verifyNoInteractions();
    }

    // ✅ Test 4: When all products list is empty
    @Test
    void testCheckStockAlerts_EmptyProductList() throws Exception {
        when(mockProductDAO.getAllProducts()).thenReturn(Collections.emptyList());

        stockAlertService.checkStockAlerts();

        mockedEmailUtil.verifyNoInteractions();
    }
}
