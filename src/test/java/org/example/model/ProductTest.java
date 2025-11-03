package org.example.model;

import org.example.dao.ProductDAOImpl;
import org.junit.jupiter.api.*;

public class ProductTest {
    private static ProductDAOImpl dao;

    @BeforeAll
    public static void setup() {
        dao = new ProductDAOImpl();
    }

    @Test
    public void testAddAndRetrieveProduct() {
        Product p = new Product(11, "Laptop", "Electronics", 10, 50000.0, 3);
        dao.addProduct(p);

        Product fetched = dao.getProductById(9);
        Assertions.assertNotNull(fetched);
        Assertions.assertEquals("Laptop", fetched.getName());
    }

    @Test
    public void testInvalidProductThrowsException() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            new Product(-1, "", "Electronics", -5, 0, 2);
        });
    }

    @AfterAll
    public static void cleanup() {
        dao.deleteProduct(1);
    }
}
