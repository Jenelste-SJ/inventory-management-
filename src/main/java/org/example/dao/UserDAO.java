package org.example.dao;

import org.example.model.User;

public interface UserDAO {
    void addUser(User user);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    void updateUserVerificationStatus(String username, boolean status);
}
