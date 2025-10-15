package org.example;

import org.example.Service.UserService;
import org.example.dao.ProductDAOImpl;
import org.example.exception.InvalidProductException;
import org.example.model.Product;
import org.example.Service.InventoryService;
import org.example.model.User;
import org.example.util.CSVHelper;
import org.example.util.EmailUtil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        Scanner sc = new Scanner(System.in);

        System.out.println("==== Welcome to Inventory Management System ====");
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        User user = userService.login(username, password);

        if (user == null) {
            System.out.println("Invalid credentials!");
            return;
        }

        if (user.getRole().equalsIgnoreCase("admin")) {
            AdminMenu(sc);
        } else {
            UserMenu(sc);
        }
    }

    public static void AdminMenu(Scanner sc) {
        InventoryService service = new InventoryService();
        ProductDAOImpl dao= new ProductDAOImpl();
        while (true) {
            System.out.println("\n--- \uD83D\uDCE6Inventory Menu ---");
            System.out.println("1.✚ Add Product");
            System.out.println("2.\uD83D\uDC41️View All Products");
            System.out.println("3.\uD83D\uDD0ESearch Product");
            System.out.println("4.⬆️ Update Product");
            System.out.println("5.❌ Delete Product");
            System.out.println("6.\uD83D\uDCC1 CSV Report ");
            System.out.println("7.\uD83D\uDEAA Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Category: ");
                    String category = sc.nextLine();
                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();
                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble();
                    try {
                        Product p = new Product(id, name, category, qty, price);
                        service.addProduct(p);
                    } catch (InvalidProductException e) {
                        System.out.println("⚠️ Invalid Product: " + e.getMessage());
                    }
                    break;

                case 2:
                    service.getAllProducts();
                    break;

                case 3:
                    System.out.println("🔍 Search Product Options:");
                    System.out.println("1. By ID");
                    System.out.println("2. By Name");
                    System.out.println("3. By Category");
                    System.out.println("4. By Quantity");
                    System.out.print("Enter choice: ");
                    int searchChoice = sc.nextInt();
                    sc.nextLine();
                    switch (searchChoice) {
                        case 1:
                            System.out.print("Enter Product ID: ");
                            int sid;
                            try {
                                sid = sc.nextInt();
                                service.getProductById(sid);
                            } catch (InvalidProductException e) {
                                System.out.println(e.getMessage());
                            }

                            break;
                        case 2:
                            System.out.print("Enter Product Name: ");
                            String nm = sc.nextLine();
                            service.getProductByName(nm);
                            break;
                        case 3:
                            System.out.print("Enter Product Category: ");
                            String cat = sc.nextLine();
                            service.getProductByCategory(cat);
                            break;
                        case 4:
                            System.out.print("Enter Product minimum Quantity: ");
                            double min = sc.nextDouble();
                            System.out.print("Enter Product maximum Quantity: ");
                            double max = sc.nextDouble();
                            service.getProductByPriceRange(min, max);
                        default:
                            System.out.println("❌ Invalid choice!");
                    }
                    break;

                case 4:
                    System.out.print("Enter ID to Update: ");
                    int uid = sc.nextInt();
                    sc.nextLine();
                    System.out.println("❗️Just leave it blank if you don't wanna update anything ❗️");
                    System.out.print("Enter New Name: ");
                    String uname = sc.nextLine();
                    System.out.print("Enter New Category: ");
                    String ucat = sc.nextLine();
                    System.out.print("Enter New Quantity: ");
                    String uqty = sc.nextLine();
                    System.out.print("Enter New Price: ");
                    String uprice = sc.nextLine();

                    service.updateProduct(uid, uname, ucat, uqty, uprice);
                    break;

                case 5:
                    System.out.print("Enter ID to Delete: ");
                    int did = sc.nextInt();
                    service.deleteProduct(did);
                    break;

                case 6:
                    var products=dao.getAllProducts();
                    String filepath = CSVHelper.generateProductsReport(products,"Admin");
                    EmailUtil.sendReport("admin@gmail.com", "Inventory Management Report","Attached is your latest Inventory Report",(filepath));
                    break;

                case 7:
                    System.out.println("👋 Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    public static void UserMenu(Scanner sc) {
        InventoryService service = new InventoryService();
        while (true) {
            System.out.println("\n--- \uD83D\uDCE6Inventory Menu ---");
            System.out.println("1.\uD83D\uDC41️View All Products");
            System.out.println("2.\uD83D\uDD0ESearch Product");
            System.out.println("3.\uD83D\uDEAA Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            switch (choice) {

                case 1:
                    service.getAllProducts();
                    break;

                case 2:
                    System.out.println("🔍 Search Product Options:");
                    System.out.println("1. By ID");
                    System.out.println("2. By Name");
                    System.out.println("3. By Category");
                    System.out.println("4. By Quantity");
                    System.out.print("Enter choice: ");
                    int searchChoice = sc.nextInt();
                    sc.nextLine();
                    switch (searchChoice) {
                        case 1:
                            System.out.print("Enter Product ID: ");
                            int sid;
                            try {
                                sid = sc.nextInt();
                                service.getProductById(sid);
                            } catch (InvalidProductException e) {
                                System.out.println(e.getMessage());
                            }

                            break;
                        case 2:
                            System.out.print("Enter Product Name: ");
                            String nm = sc.nextLine();
                            service.getProductByName(nm);
                            break;
                        case 3:
                            System.out.print("Enter Product Category: ");
                            String cat = sc.nextLine();
                            service.getProductByCategory(cat);
                            break;
                        case 4:
                            System.out.print("Enter Product minimum Quantity: ");
                            double min = sc.nextDouble();
                            System.out.print("Enter Product maximum Quantity: ");
                            double max = sc.nextDouble();
                            service.getProductByPriceRange(min, max);
                        default:
                            System.out.println("❌ Invalid choice!");
                    }
                    break;


                case 3:
                    System.out.println("👋 Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
}

