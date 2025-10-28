package org.example.dao;

import org.example.model.User;
import org.example.util.DBConnection;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserDAOImplTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private UserDAOImpl userDAO;
    private MockedStatic<DBConnection> mockedDBConnection;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockedDBConnection = mockStatic(DBConnection.class);
        mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
        userDAO = new UserDAOImpl();
    }

    @AfterEach
    void tearDown() {
        if (mockedDBConnection != null) {
            mockedDBConnection.close();
        }
    }

    // ✅ Test addUser() - success
    @Test
    void testAddUserSuccess() throws Exception {
        User user = new User("ajit", "1234", "Admin", "ajit@gmail.com");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        userDAO.addUser(user);

        verify(mockPreparedStatement).setString(1, "ajit");
        verify(mockPreparedStatement).setString(2, "1234");
        verify(mockPreparedStatement).setString(3, "Admin");
        verify(mockPreparedStatement).setString(4, "ajit@gmail.com");
        verify(mockPreparedStatement).setBoolean(5, false);
        verify(mockPreparedStatement).executeUpdate();
    }

    // ✅ Test addUser() - duplicate username
    @Test
    void testAddUserDuplicateUsername() throws Exception {
        User user = new User("existingUser", "abcd", "User", "user@example.com");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        doThrow(new SQLIntegrityConstraintViolationException("Duplicate username"))
                .when(mockPreparedStatement).executeUpdate();

        assertDoesNotThrow(() -> userDAO.addUser(user));
        verify(mockPreparedStatement).executeUpdate();
    }

    // ✅ Test getUserByUsername() - success
    @Test
    void testGetUserByUsernameSuccess() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("username")).thenReturn("ajit");
        when(mockResultSet.getString("password")).thenReturn("1234");
        when(mockResultSet.getString("role")).thenReturn("Admin");
        when(mockResultSet.getString("email")).thenReturn("ajit@gmail.com");
        when(mockResultSet.getBoolean("isVerified")).thenReturn(true);

        User result = userDAO.getUserByUsername("ajit");

        assertNotNull(result);
        assertEquals("ajit", result.getUsername());
        assertEquals("Admin", result.getRole());
        assertEquals("ajit@gmail.com", result.getEmail());
        assertTrue(result.isVerified());
    }

    // ✅ Test getUserByUsername() - user not found
    @Test
    void testGetUserByUsernameNotFound() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        User result = userDAO.getUserByUsername("nonexistent");

        assertNull(result);
    }

    // ✅ Test updateUserVerificationStatus()
    @Test
    void testUpdateUserVerificationStatus() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        userDAO.updateUserVerificationStatus("ajit", true);

        verify(mockPreparedStatement).setBoolean(1, true);
        verify(mockPreparedStatement).setString(2, "ajit");
        verify(mockPreparedStatement).executeUpdate();
    }
}
