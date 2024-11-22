package CapstoneProject;

import CapstoneProject.managers.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BatteryManager.initialize();
        EnergySourceManager.initialize();

        while (true) {
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
                    System.out.print("Enter weather (summer, windy, rainy): ");
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
                	    String names = scanner.nextLine(); // Allow multiple object names
                	    SmartObjectManager.toggleSmartObjects(names);
                	    break;
                case 4:
                    LogManager.showLogs();
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
        }
    }
}

