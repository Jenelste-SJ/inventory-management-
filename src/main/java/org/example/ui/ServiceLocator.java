package org.example.ui;

import org.example.service.InventoryService;
import org.example.service.UserService;

public class ServiceLocator {

    private static InventoryService inventoryService = new InventoryService();
    private static UserService userService = new UserService();

    private static String loggedInEmail;

    public static InventoryService getInventoryService() {
        return inventoryService;
    }

    public static UserService getUserService() {
        return userService;
    }

    public static void setLoggedInEmail(String email) {
        loggedInEmail = email;
    }

    public static String getLoggedInEmail() {
        return loggedInEmail;
    }
}
