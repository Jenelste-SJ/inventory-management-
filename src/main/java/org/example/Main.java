package org.example;

import org.example.exception.InvalidProductException;
import org.example.model.Product;
import org.example.Service.InventoryService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InventoryService service = new InventoryService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- \uD83D\uDCE6Inventory Menu ---");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Search Product");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. CSV Report ");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt(); sc.nextLine();
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
                    System.out.print("Enter choice: ");
                    int searchChoice = sc.nextInt();
                    sc.nextLine();
                    switch (searchChoice) {
                        case 1:
                            System.out.print("Enter Product ID: ");
                            int sid = 0;
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
                        default:
                            System.out.println("❌ Invalid choice!");
                    }
                    break;

                case 4:
                    System.out.print("Enter ID to Update: ");
                    int uid = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter New Name: ");
                    String uname = sc.nextLine();
                    System.out.print("Enter New Category: ");
                    String ucat = sc.nextLine();
                    System.out.print("Enter New Quantity: ");
                    int uqty = sc.nextInt();
                    System.out.print("Enter New Price: ");
                    double uprice = sc.nextDouble();

                    service.updateProduct(new Product(uid, uname, ucat, uqty, uprice));
                    break;

                case 5:
                    System.out.print("Enter ID to Delete: ");
                    int did = sc.nextInt();
                    service.deleteProduct(did);
                    break;

                case 6:
                    service.exportInventoryToCSV("inventory.csv");
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
}
