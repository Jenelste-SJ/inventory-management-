package org.example;

import org.example.service.InventoryService;
import org.example.service.UserService;
import org.example.dao.ProductDAOImpl;
import org.example.dao.UserDAOImpl;
import org.example.exception.InvalidProductException;
import org.example.model.Product;
import org.example.model.User;
import org.example.util.CSVHelper;
import org.example.util.EmailUtil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        UserDAOImpl userDAO = new UserDAOImpl();

        System.out.println("==========================================");
        System.out.println("🛒  WELCOME TO INVENTORY MANAGEMENT SYSTEM");
        System.out.println("==========================================");
        while (true) {
            System.out.println("\n🌐 MAIN MENU");
            System.out.println("1️⃣  Login");
            System.out.println("2️⃣  Register");
            System.out.println("3️⃣  Exit");
            System.out.print("➡️  Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("\n🔐 LOGIN");
                    System.out.print("👤 Username: ");
                    String username = sc.nextLine();
                    System.out.print("🔑 Password: ");
                    String password = sc.nextLine();

                    User user = userService.login(username, password);
                    if (user == null) {
                        System.out.println("❌ Invalid credentials! Please try again.");
                    } else {
                        if (user.getRole().equalsIgnoreCase("admin")) {
                            AdminMenu(sc);
                        } else {
                            UserMenu(sc);
                        }
                    }
                }

                case 2 -> {
                    System.out.println("\n📝 REGISTER NEW ACCOUNT");
                    System.out.print("👤 Choose a Username: ");
                    String username = sc.nextLine();
                    System.out.print("🔑 Choose a Password: ");
                    String password = sc.nextLine();

                    User newUser = new User(username, password, "User");
                    userDAO.addUser(newUser);

                    System.out.println("🎉 Registration successful! You can now log in.");
                }

                case 3 -> {
                    System.out.println("\n👋 Thank you for using the Inventory Management System!");
                    System.out.println("💡 Have a great day!");
                    sc.close();
                    return;
                }

                default -> System.out.println("⚠️  Invalid choice! Please enter 1, 2, or 3.");
            }
        }
    }

    // ==================== ADMIN MENU ====================
    public static void AdminMenu(Scanner sc) {
        InventoryService service = new InventoryService();
        ProductDAOImpl dao = new ProductDAOImpl();
        while (true) {
            System.out.println("\n========================");
            System.out.println("🧑‍💼 ADMIN DASHBOARD");
            System.out.println("========================");
            System.out.println("1️⃣  ➕ Add Product");
            System.out.println("2️⃣  👁️ View All Products");
            System.out.println("3️⃣  🔍 Search Product");
            System.out.println("4️⃣  ✏️ Update Product");
            System.out.println("5️⃣  🗑️ Delete Product");
            System.out.println("6️⃣  📊 Generate CSV Report");
            System.out.println("7️⃣  🚪 Logout");
            System.out.print("➡️  Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    try {
                        System.out.print("🪪Product ID: ");
                        int id = sc.nextInt();
                        System.out.print("📦 Product Name: ");
                        String name = sc.next();
                        System.out.print("🏷️ Category: ");
                        String category = sc.next();
                        System.out.print("📦 Quantity: ");
                        int qty = sc.nextInt();
                        System.out.print("💰 Price: ");
                        double price = sc.nextDouble();
                        System.out.print("𝍌 Threshold: ");
                        int threshold = sc.nextInt();
                        Product p = new Product(id, name, category, qty, price,threshold);
                        service.addProduct(p);
                    } catch (InvalidProductException e) {
                        System.out.println("⚠️ Invalid Product: " + e.getMessage());
                    }
                }

                case 2 -> service.getAllProducts();

                case 3 -> {
                    System.out.println("\n🔎 SEARCH OPTIONS");
                    System.out.println("1️⃣ By ID");
                    System.out.println("2️⃣ By Name");
                    System.out.println("3️⃣ By Category");
                    System.out.println("4️⃣ By Price Range");
                    System.out.print("➡️ Enter choice: ");
                    int sChoice = sc.nextInt();
                    sc.nextLine();

                    switch (sChoice) {
                        case 1 -> {
                            System.out.print("Enter Product ID: ");
                            int id = sc.nextInt();
                            try {
                                service.getProductById(id);
                            } catch (InvalidProductException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        case 2 -> {
                            System.out.print("Enter Product Name: ");
                            service.getProductByName(sc.nextLine());
                        }
                        case 3 -> {
                            System.out.print("Enter Category: ");
                            service.getProductByCategory(sc.nextLine());
                        }
                        case 4 -> {
                            System.out.print("Enter Min Price: ");
                            double min = sc.nextDouble();
                            System.out.print("Enter Max Price: ");
                            double max = sc.nextDouble();
                            service.getProductByPriceRange(min, max);
                        }
                        default -> System.out.println("❌ Invalid search option!");
                    }
                }

                case 4 -> {
                    System.out.print("Enter Product ID to Update: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("📝 New Name (or press Enter to skip): ");
                    String name = sc.nextLine();
                    System.out.print("🏷️ New Category (or press Enter to skip): ");
                    String cat = sc.nextLine();
                    System.out.print("📦 New Quantity (or press Enter to skip): ");
                    String qty = sc.nextLine();
                    System.out.print("💰 New Price (or press Enter to skip): ");
                    String price = sc.nextLine();
                    System.out.print("New Threshold (or press Enter to skip): ");
                    String threshold = sc.nextLine();
                    service.updateProduct(id, name, cat, qty, price,threshold);
                }

                case 5 -> {
                    System.out.print("Enter Product ID to Delete: ");
                    int delId = sc.nextInt();
                    service.deleteProduct(delId);
                }

                case 6 -> {
                    var products = dao.getAllProducts();
                    String filepath = CSVHelper.generateProductsReport(products, "Admin");
                    EmailUtil.sendReport("admin@gmail.com", "Inventory Management Report",
                            "Attached is your latest Inventory Report", filepath);
                    System.out.println("📧 Report emailed successfully!");
                }

                case 7 -> {
                    System.out.println("👋 Logging out of Admin Dashboard...");
                    return;
                }

                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    // ==================== USER MENU ====================
    public static void UserMenu(Scanner sc) {
        InventoryService service = new InventoryService();
        while (true) {
            System.out.println("\n======================");
            System.out.println("🙋 USER DASHBOARD");
            System.out.println("======================");
            System.out.println("1️⃣  👁️ View All Products");
            System.out.println("2️⃣  🔍 Search Product");
            System.out.println("3️⃣  🚪 Logout");
            System.out.print("➡️  Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> service.getAllProducts();
                case 2 -> {
                    System.out.println("\n🔎 SEARCH OPTIONS");
                    System.out.println("1️⃣ By ID");
                    System.out.println("2️⃣ By Name");
                    System.out.println("3️⃣ By Category");
                    System.out.println("4️⃣ By Price Range");
                    System.out.print("➡️ Enter choice: ");
                    int sChoice = sc.nextInt();
                    sc.nextLine();
                    switch (sChoice) {
                        case 1 -> {
                            System.out.print("Enter Product ID: ");
                            int id = sc.nextInt();
                            try {
                                service.getProductById(id);
                            } catch (InvalidProductException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        case 2 -> {
                            System.out.print("Enter Product Name: ");
                            service.getProductByName(sc.nextLine());
                        }
                        case 3 -> {
                            System.out.print("Enter Product Category: ");
                            service.getProductByCategory(sc.nextLine());
                        }
                        case 4 -> {
                            System.out.print("Enter Min Price: ");
                            double min = sc.nextDouble();
                            System.out.print("Enter Max Price: ");
                            double max = sc.nextDouble();
                            service.getProductByPriceRange(min, max);
                        }
                        default -> System.out.println("❌ Invalid search option!");
                    }
                }
                case 3 -> {
                    System.out.println("👋 Logging out...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }
}
