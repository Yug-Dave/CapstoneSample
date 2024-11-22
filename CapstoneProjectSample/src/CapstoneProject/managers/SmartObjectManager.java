package CapstoneProject.managers;

import CapstoneProject.models.SmartObject;
import CapstoneProject.models.Battery;

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
        new Thread(() -> {
            while (object.isActive()) {
                Battery battery = getAvailableBattery();
                if (battery == null) {
                    System.out.println("No batteries available for " + object.getName());
                    break;
                }
                while (object.isActive() && battery.getCharge() >= object.getEnergyRequired()) {
                    battery.discharge(object.getEnergyRequired());
                    System.out.println(object.getName() + " consuming " + object.getEnergyRequired() + "% from " + battery.getName());
                    try {
                        Thread.sleep(1000); // Simulate time taken for energy consumption
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Consumption interrupted for " + object.getName());
                    }
                }
                if (battery.getCharge() < object.getEnergyRequired()) {
                	Battery battery1 = getAvailableBattery();
                    System.out.println(battery.getName() + " is low. Switching battery to" + battery1.getName());
                }
            }
        }).start();
    }

    private static Battery getAvailableBattery() {
        return BatteryManager.getBatteries().stream()
                .filter(b -> b.getCharge() > 0)
                .max(Comparator.comparingInt(Battery::getCharge))
                .orElse(null);
    }
}
