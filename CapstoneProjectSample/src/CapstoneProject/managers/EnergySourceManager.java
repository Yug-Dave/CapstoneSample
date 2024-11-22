package CapstoneProject.managers;

import CapstoneProject.models.Battery;
import CapstoneProject.models.EnergySource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnergySourceManager {
    private static final Map<String, EnergySource> energySources = new HashMap<>();

    public static void initialize() {
        energySources.put("summer", new EnergySource("Solar"));
        energySources.put("windy", new EnergySource("Windyy"));
        energySources.put("rainy", new EnergySource("Electricity"));
    }

    public static void chargeBatteries(String weather) {
        EnergySource source = energySources.get(weather.toLowerCase());
        if (source == null) {
            System.out.println("Invalid weather. Please try again.");
            return;
        }
        System.out.println("Using " + source.getName() + " energy to charge batteries...");
        List<Battery> batteries = BatteryManager.getBatteries();

        // Create a thread for each battery to charge concurrently
        List<Thread> threads = new ArrayList<>();
        for (Battery battery : batteries) {
            Thread chargingThread = new Thread(() -> {
                while (battery.getCharge() < 100) {
                    synchronized (battery) { // Ensure thread safety
                        battery.recharge(10); // Increment battery charge
                        System.out.println(battery.getName() + " recharging... Current: " + battery.getCharge() + "%");
                    }
                    try {
                        Thread.sleep(1000); // Simulate time taken to recharge
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Recharging interrupted for " + battery.getName());
                    }
                }
                System.out.println(battery.getName() + " is now fully charged.");
            });
            threads.add(chargingThread);
            chargingThread.start(); // Start the charging thread
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Charging process interrupted.");
            }
        }

        System.out.println("All batteries are fully charged.");
    }
}    

