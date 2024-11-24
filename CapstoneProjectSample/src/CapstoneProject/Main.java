package CapstoneProject;

import CapstoneProject.managers.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BatteryManager.initialize();
        EnergySourceManager.initialize();

        while (true) {
            try {
                System.out.println("\nMenu:");
                System.out.println("1. Charging");
                System.out.println("2. Add new smart object");
                System.out.println("3. ON/OFF smart objects");
                System.out.println("4. Show logs");
                System.out.println("5. Batteries");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter weather (sunny, windy, rainy): ");
                        String weather = scanner.nextLine();
                        EnergySourceManager.chargeBatteries(weather);
                        break;
                    case 2:
                        System.out.print("Enter object name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter energy required: ");
                        int energyRequired = scanner.nextInt();
                        SmartObjectManager.addSmartObject(name, energyRequired);
                        break;
                    case 3:
                        System.out.print("Enter object names to toggle (comma-separated): ");
                        String names = scanner.nextLine();
                        SmartObjectManager.toggleSmartObjects(names);
                        break;
                    case 4:
                        accessLogs(scanner);
                        break;
                    case 5:
                        BatteryManager.showBatteryStatus();
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }
    
    private static void accessLogs(Scanner scanner) {
        while (true) {
            try {
                System.out.println("\n===== Log Management =====");
                System.out.println("1. Access Energy Source Logs");
                System.out.println("2. Access Smart Object Logs");
                System.out.println("3. Back to Main Menu");
                System.out.print("Enter your choice: ");

                int logChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (logChoice) {
                    case 1 -> accessEnergySourceLogs(scanner);
                    case 2 -> accessSmartObjectLogs(scanner);
                    case 3 -> {
                        return; // Return to main menu
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void accessEnergySourceLogs(Scanner scanner) {
        while (true) {
            try {
                System.out.println("\n===== Energy Source Log Management =====");
                System.out.println("1. View All Logs");
                System.out.println("2. View Logs by Filter");
                System.out.println("3. Delete Log by ID");
                System.out.println("4. Export Logs to File");
                System.out.println("5. Back to Log Management Menu");
                System.out.print("Enter your choice: ");

                int logChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (logChoice) {
                    case 1 -> {
                        System.out.println("\nViewing All Logs:");
                        ESLogManager.viewESLogs(); // Changed to ESLogManager
                    }
                    case 2 -> filterLogs(scanner); // Assuming filterLogs works with both SmartObject and ES logs
                    case 3 -> {
                        System.out.print("Enter Log ID to delete (or -1 to delete all logs): ");
                        int logId = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        if (logId == -1) {
                            System.out.print("Are you sure you want to delete all logs? (yes/no): ");
                            String confirm = scanner.nextLine();
                            if (confirm.equalsIgnoreCase("yes")) {
                                while (ESLogManager.getESLogs().size() > 0) { // Changed to ESLogManager
                                    ESLogManager.deleteESLog(0); // Continuously delete logs until empty
                                }
                                System.out.println("All logs deleted.");
                            } else {
                                System.out.println("Delete operation canceled.");
                            }
                        } else {
                            ESLogManager.deleteESLog(logId); // Changed to ESLogManager
                        }
                    }
                    case 4 -> {
                        System.out.print("Enter file path to export logs (e.g., logs.csv): ");
                        String filePath = scanner.nextLine();
                        try {
                            ESLogManager.exportESLogs(filePath); // Changed to ESLogManager
                            System.out.println("Logs successfully exported to " + filePath);
                        } catch (Exception e) {
                            System.out.println("Failed to export logs: " + e.getMessage());
                        }
                    }
                    case 5 -> {
                        return; // Back to Log Management Menu
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    
    
    
    private static void accessSmartObjectLogs(Scanner scanner) {
        while (true) {
            try {
                System.out.println("\n===== Smart Object Log Management =====");
                System.out.println("1. View All Logs");
                System.out.println("2. View Logs by Filter");
                System.out.println("3. Delete Log by ID");
                System.out.println("4. Export Logs to File");
                System.out.println("5. Back to Main Menu");
                System.out.print("Enter your choice: ");

                int logChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (logChoice) {
                    case 1 -> {
                        System.out.println("\nViewing All Logs:");
                        LogManager.viewLogs();
                    }
                    case 2 -> {
                        filterLogs(scanner);
                    }
                    case 3 -> {
                        System.out.print("Enter Log ID to delete (or -1 to delete all logs): ");
                        int logId = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        if (logId == -1) {
                            System.out.print("Are you sure you want to delete all logs? (yes/no): ");
                            String confirm = scanner.nextLine();
                            if (confirm.equalsIgnoreCase("yes")) {
                                while (LogManager.getLogs().size() > 0) {
                                    LogManager.deleteLog(0); // Continuously delete logs until empty
                                }
                                System.out.println("All logs deleted.");
                            } else {
                                System.out.println("Delete operation canceled.");
                            }
                        } else {
                            LogManager.deleteLog(logId);
                        }
                    }
                    case 4 -> {
                        System.out.print("Enter file path to export logs (e.g., logs.csv): ");
                        String filePath = scanner.nextLine();
                        try {
                            LogManager.exportLogs(filePath);
                            System.out.println("Logs successfully exported to " + filePath);
                        } catch (Exception e) {
                            System.out.println("Failed to export logs: " + e.getMessage());
                        }
                    }
                    case 5 -> {
                        return; // Exit to main menu
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (NullPointerException e) {
                System.out.println("An error occurred due to missing data: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace(); // Print stack trace for debugging
            }
        }
    }

    
    private static void filterLogs(Scanner scanner) {
        while (true) { // Keep the user in the filter menu
            try {
                System.out.println("\n===== Filter Logs =====");
                System.out.println("1. Filter by Smart Object Name");
                System.out.println("2. Filter by Battery Name");
                System.out.println("3. Filter by Date");
                System.out.println("4. Back to Log Management Menu");
                System.out.print("Enter your choice: ");
                int filterChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (filterChoice) {
                    case 1 -> {
                        System.out.print("Enter Smart Object Name: ");
                        String objectName = scanner.nextLine();
                        LogManager.viewLogsByFilter("object", objectName);
                    }
                    case 2 -> {
                        System.out.print("Enter Battery Name: ");
                        String batteryName = scanner.nextLine();
                        LogManager.viewLogsByFilter("battery", batteryName);
                    }
                    case 3 -> {
                        System.out.print("Enter Date (yyyy-MM-dd): ");
                        String dateString = scanner.nextLine();
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                            LogManager.viewLogsByDate(date);
                        } catch (ParseException e) {
                            System.out.println("Invalid date format. Please use yyyy-MM-dd. Example: 2024-01-01.");
                        }
                    }
                    case 4 -> {
                        return; // Exit back to Log Management Menu
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.nextLine(); // Clear invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace(); // Optional for debugging
            }
        }
    }

        
        
    private static void filterESLogs(Scanner scanner) {
        try {
            System.out.println("\n===== Filter Logs =====");
            System.out.println("1. Filter by Energy Resource Name");
            System.out.println("2. Filter by Battery Name");
            System.out.println("3. Filter by Date");
            System.out.print("Enter your choice: ");

            int filterChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (filterChoice) {
                case 1 -> {
                    System.out.print("Enter Energy Source Name: ");
                    String objectName = scanner.nextLine();
                    ESLogManager.viewESLogsByFilter("object", objectName);
                }
                case 2 -> {
                    System.out.print("Enter Battery Name: ");
                    String batteryName = scanner.nextLine();
                    ESLogManager.viewESLogsByFilter("battery", batteryName);
                }
                case 3 -> {
                    System.out.print("Enter Date (yyyy-MM-dd): ");
                    String dateString = scanner.nextLine();
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                        ESLogManager.viewESLogsByDate(date);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                    }
                }
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace(); // Optional: For debugging purposes
        }
    }

    }

