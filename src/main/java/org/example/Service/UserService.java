package org.example.Service;

import org.example.dao.UserDAO;
import org.example.dao.UserDAOImpl;
import org.example.exception.DatabaseException;
import org.example.model.User;

public class UserService {
    private UserDAO userDAO=new UserDAOImpl();
    public UserService() {
        this.userDAO = userDAO;
    }


    public User login(String username, String password) {
        try {
            User user = userDAO.getUserByName(username);
            if(user==null){
                System.out.println("User not found");
                return null;
            }
            if (user.getPassword().equals(password)) {
                System.out.println("Login Successful!Welcome "+user.getUsername());
                System.out.println("User Role:"+user.getRole());
                return user;
            }
            else  {
                System.out.println("Enter a valid username and password");
                return null;
            }
        } catch (Exception e) {
            throw new DatabaseException("INVALID",e);
        }
    }
}
