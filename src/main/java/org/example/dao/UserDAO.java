package org.example.dao;

import org.example.model.User;

public interface UserDAO {
    public void addUser(User user);
    public User getUserByName(String username);
}
