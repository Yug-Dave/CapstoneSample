package CapstoneProject.managers;

import CapstoneProject.models.SmartObject;
import java.util.concurrent.atomic.AtomicBoolean;

import CapstoneProject.models.Battery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SmartObjectManager {
    private static final List<SmartObject> smartObjects = new ArrayList<>();

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
                        System.out.println(object.getName() + " is now ON");
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
        AtomicBoolean isInterrupted = new AtomicBoolean(false);
        Thread inputThread = new Thread(() -> {
            try {
                System.out.println("Press Enter to stop consumption and return to the main menu...");
                System.in.read(); // Waits for the user to press Enter
                isInterrupted.set(true); // Set interrupt flag
            } catch (IOException e) {
                System.out.println("Error reading input: " + e.getMessage());
            }
        });

        Thread consumptionThread = new Thread(() -> {
            while (object.isActive() && !isInterrupted.get()) {
                Battery battery = getAvailableBattery();
                if (battery == null) {
                    System.out.println("No batteries available for " + object.getName());
                    break;
                }

                while (object.isActive() && !isInterrupted.get() && battery.getCharge() >= object.getEnergyRequired()) {
                    battery.discharge(object.getEnergyRequired());
                    System.out.println(object.getName() + " consuming " + object.getEnergyRequired() + "% from " + battery.getName());
                    try {
                        Thread.sleep(1000); // Simulate time taken for energy consumption
                    } catch (InterruptedException e) {
                        System.out.println("Consumption interrupted for " + object.getName());
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                if (battery.getCharge() < object.getEnergyRequired()) {
                    System.out.println(battery.getName() + " is low. Trying to switch batteries...");
                    battery = getAvailableBattery();
                    if (battery == null || battery.getCharge() < object.getEnergyRequired()) {
                        System.out.println("No suitable battery found for " + object.getName() + ". Stopping consumption.");
                        break;
                    } else {
                        System.out.println("Switched to " + battery.getName());
                    }
                }
            }
            if (isInterrupted.get()) {
                System.out.println("Consumption interrupted for " + object.getName() + ". Returning to the main menu...");
            }
        });

        // Start both threads
        inputThread.start();
        consumptionThread.start();

        try {
            // Wait for the input thread to finish (Enter key press)
            inputThread.join();
            // Ensure the consumption thread stops when interrupted
            consumptionThread.join();
        } catch (InterruptedException e) {
            System.out.println("Error while waiting for threads to finish: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }


    private static Battery getAvailableBattery() {
        return BatteryManager.getBatteries().stream()
                .filter(b -> b.getCharge() > 0)
                .max(Comparator.comparingInt(Battery::getCharge))
                .orElse(null);
    }
}
