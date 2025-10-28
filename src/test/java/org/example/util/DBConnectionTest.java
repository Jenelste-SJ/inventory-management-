package org.example.util;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DBConnectionTest {

    private MockedStatic<DriverManager> mockedDriverManager;

    @BeforeEach
    void setUp() {
        mockedDriverManager = mockStatic(DriverManager.class);
    }

    @AfterEach
    void tearDown() {
        if (mockedDriverManager != null) mockedDriverManager.close();
    }

    // ✅ Test 1: Successful DB Connection
    @Test
    void testGetConnectionSuccess() throws Exception {
        Connection mockConnection = mock(Connection.class);

        // Mock the actual call, which may use nulls
        mockedDriverManager.when(() ->
                        DriverManager.getConnection(any(), any(), any()))
                .thenReturn(mockConnection);

        Connection result = DBConnection.getConnection();

        assertNotNull(result, "Connection should not be null on success");

        mockedDriverManager.verify(() ->
                DriverManager.getConnection(any(), any(), any()), times(1));
    }


    // ✅ Test 2: Connection Fails
    @Test
    void testGetConnectionFailure() throws Exception {
        mockedDriverManager.when(() ->
                        DriverManager.getConnection(any(), any(), any()))
                .thenThrow(new RuntimeException("DB connection error"));

        Connection result = DBConnection.getConnection();

        assertNull(result, "Should return null on connection failure");

        mockedDriverManager.verify(() ->
                DriverManager.getConnection(any(), any(), any()), times(1));
    }


    // ✅ Test 3: Missing environment variables
    @Test
    void testEnvironmentVariablesMissing() {
        mockedDriverManager.when(() ->
                        DriverManager.getConnection(any(), any(), any()))
                .thenThrow(new RuntimeException("Missing env vars"));

        assertDoesNotThrow(DBConnection::getConnection,
                "Should handle missing environment variables gracefully");
    }
}
