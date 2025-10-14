package org.example.dao;

import org.example.model.User;

public interface UserDAO {
    void addUser(User user);
     User  getUserByName(String username);
}
