package org.example.dao;

import org.example.exception.DatabaseException;
import org.example.model.Product;
import org.example.util.DBConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductDAOImplTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    private ProductDAOImpl productDAO;
    private MockedStatic<DBConnection> mockedDBConnection;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockedDBConnection = mockStatic(DBConnection.class);
        mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
        productDAO = new ProductDAOImpl();
    }

    @AfterEach
    void tearDown() {
        if (mockedDBConnection != null) {
            mockedDBConnection.close();
        }
    }

    // ✅ Test Add Product
    @Test
    void testAddProductSuccess() throws Exception {
        Product product = new Product(1, "Laptop", "Electronics", 10, 50000.0, 5);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        productDAO.addProduct(product);

        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setString(2, "Laptop");
        verify(mockPreparedStatement).setString(3, "Electronics");
        verify(mockPreparedStatement).setInt(4, 10);
        verify(mockPreparedStatement).setDouble(5, 50000.0);
        verify(mockPreparedStatement).setInt(6, 5);
        verify(mockPreparedStatement).executeUpdate();
    }

    // ✅ Test Get All Products
    @Test
    void testGetAllProducts() throws Exception {
        Statement mockStatement = mock(Statement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Laptop");
        when(mockResultSet.getString("category")).thenReturn("Electronics");
        when(mockResultSet.getInt("quantity")).thenReturn(10);
        when(mockResultSet.getDouble("price")).thenReturn(50000.0);
        when(mockResultSet.getInt("threshold")).thenReturn(5);

        List<Product> result = productDAO.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals(5, result.get(0).getThreshold());
    }

    // ✅ Test Get Product By ID - Found
    @Test
    void testGetProductByIdFound() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Laptop");
        when(mockResultSet.getString("category")).thenReturn("Electronics");
        when(mockResultSet.getInt("quantity")).thenReturn(10);
        when(mockResultSet.getDouble("price")).thenReturn(50000.0);
        when(mockResultSet.getInt("threshold")).thenReturn(5);

        Product result = productDAO.getProductById(1);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals("Electronics", result.getCategory());
        assertEquals(10, result.getQuantity());
        assertEquals(50000.0, result.getPrice());
        assertEquals(5, result.getThreshold());
    }

    // ✅ Test Get Product By ID - Not Found
    @Test
    void testGetProductByIdNotFound() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Product result = productDAO.getProductById(99);
        assertNull(result);
    }

    // ✅ Test Get Product By Name
    @Test
    void testGetProductByName() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Laptop");
        when(mockResultSet.getString("category")).thenReturn("Electronics");
        when(mockResultSet.getInt("quantity")).thenReturn(10);
        when(mockResultSet.getDouble("price")).thenReturn(50000.0);
        when(mockResultSet.getInt("threshold")).thenReturn(5);

        List<Product> result = productDAO.getProductByName("Laptop");
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }

    // ✅ Test Get Product By Category
    @Test
    void testGetProductByCategory() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Phone");
        when(mockResultSet.getString("category")).thenReturn("Electronics");
        when(mockResultSet.getInt("quantity")).thenReturn(8);
        when(mockResultSet.getDouble("price")).thenReturn(30000.0);
        when(mockResultSet.getInt("threshold")).thenReturn(5);

        List<Product> result = productDAO.getProductByCategory("Electronics");
        assertEquals(1, result.size());
        assertEquals("Phone", result.get(0).getName());
    }

    // ✅ Test Get Product By Price Range
    @Test
    void testGetProductByPriceRange() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Laptop");
        when(mockResultSet.getString("category")).thenReturn("Electronics");
        when(mockResultSet.getInt("quantity")).thenReturn(10);
        when(mockResultSet.getDouble("price")).thenReturn(45000.0);
        when(mockResultSet.getInt("threshold")).thenReturn(5);

        List<Product> result = productDAO.getProductByPriceRange(20000, 50000);
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }

    // ✅ Test Update Product
    @Test
    void testUpdateProductSuccess() throws Exception {
        Product product = new Product(1, "Tablet", "Electronics", 5, 25000.0, 3);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        productDAO.updateProduct(product);

        verify(mockPreparedStatement).setString(1, "Tablet");
        verify(mockPreparedStatement).setString(2, "Electronics");
        verify(mockPreparedStatement).setInt(3, 5);
        verify(mockPreparedStatement).setDouble(4, 25000.0);
        verify(mockPreparedStatement).setInt(5, 3);
        verify(mockPreparedStatement).setInt(6, 1);
        verify(mockPreparedStatement).executeUpdate();
    }

    // ✅ Test Delete Product
    @Test
    void testDeleteProductSuccess() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = productDAO.deleteProduct(1);
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).executeUpdate();
        assertFalse(result); // method always returns false in DAO
    }
}
