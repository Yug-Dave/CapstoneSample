package CapstoneProject.managers;

import CapstoneProject.models.SmartObject;
import CapstoneProject.models.Battery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class SmartObjectManager {
    private static final List<SmartObject> smartObjects = new ArrayList<>();
    private static final ReentrantLock batteryLock = new ReentrantLock(); // Lock for battery access

    public static void addSmartObject(String name, int energyRequired) {
        smartObjects.add(new SmartObject(name, energyRequired));
        System.out.println("Added Smart Object: " + name);
    }

    public static void toggleSmartObjects(String names) {
        String[] objectNames = names.split(","); // Split user input by commas
        boolean anyFound = false;

        for (String name : objectNames) {
            String trimmedName = name.trim(); // Trim whitespace
            for (SmartObject object : smartObjects) {
                if (object.getName().equalsIgnoreCase(trimmedName)) {
                    object.toggle();
                    if (object.isActive()) {
                        startObjectConsumption(object); // Start consumption
                    } else {
                        System.out.println(object.getName() + " is now OFF");
                    }
                    anyFound = true;
                    break;
                }
            }
        }
        if (!anyFound) {
            System.out.println("None of the specified smart objects were found.");
        }
    }

    private static void startObjectConsumption(SmartObject object) {
        AtomicBoolean stopFlag = new AtomicBoolean(false);
        
        // Thread for key listener to interrupt consumption
        Thread keyListenerThread = new Thread(() -> {
            System.out.println("Press ENTER at any time to stop all operations and return to the main menu.");
            try {
                System.in.read(); // Wait for the user to press Enter
                stopFlag.set(true); // Signal all threads to stop
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        keyListenerThread.start();

        Thread consumptionThread = new Thread(() -> {
            while (object.isActive() || !stopFlag.get()) {
                Battery battery = getAvailableBattery();
                if (battery == null) {
                    displayStaticFrame(object.getName(), "No batteries available", "---", "---");
                    break;
                }

                boolean consumed = false;
                try {
                    // Lock access to the battery
                    batteryLock.lock();
                    if (battery.getCharge() >= object.getEnergyRequired() || !stopFlag.get()) {
                        battery.discharge(object.getEnergyRequired());
                        consumed = true;
                        displayStaticFrame(object.getName(), "Consuming power", battery.getName(), battery.getCharge() + "%");
                    }
                } finally {
                    batteryLock.unlock(); // Ensure lock is released
                }

                if (!consumed && !stopFlag.get()) {
                    displayStaticFrame(object.getName(), battery.getName() + " is low. Switching...", "---", "---");
                    battery = getAvailableBattery(); // Find another battery
                    if (battery == null || battery.getCharge() < object.getEnergyRequired() || !stopFlag.get()) {
                        displayStaticFrame(object.getName(), "No suitable battery found", "---", "---");
                        break;
                    } else {
                        displayStaticFrame(object.getName(), "Switched to " + battery.getName(), battery.getName(), battery.getCharge() + "%");
                    }
                }

                try {
                    Thread.sleep(1000); // Simulate time taken for energy consumption
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    displayStaticFrame(object.getName(), "Consumption interrupted!", "---", "---");
                    object.toggle();
                    System.out.println(object.getName() + " is now OFF");
                    break;
                }
            }
            // Turn off the object when consumption ends
            if (!stopFlag.get()) {
                object.toggle();
                System.out.println(object.getName() + " is now OFF");
            }
        });

        // Start the thread
        consumptionThread.start();

        // Ensure the key listener thread interrupts the consumption thread
        new Thread(() -> {
            try {
                keyListenerThread.join(); // Wait for key listener to finish
                if (stopFlag.get()) {
                    consumptionThread.interrupt(); // Interrupt the consumption thread
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private static void displayStaticFrame(String name, String status, String batteryName, String chargeLevel) {
        // Print the static frame (only once)
        System.out.println("\033[2J"); // Clear the screen
        System.out.println("---------------------------------------------------");
        System.out.printf("| Smart Object: %-33s |\n", name);
        System.out.printf("| Status: %-40s |\n", status);
        System.out.printf("| Battery: %-15s | Charge Level: %-10s |\n", batteryName, chargeLevel);
        System.out.println("---------------------------------------------------");
    }

    private static Battery getAvailableBattery() {
        return BatteryManager.getBatteries().stream()
                .filter(b -> b.getCharge() > 0)
                .max(Comparator.comparingInt(Battery::getCharge))
                .orElse(null);
    }
}
