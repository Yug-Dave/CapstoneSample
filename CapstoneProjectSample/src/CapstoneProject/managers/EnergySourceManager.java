package CapstoneProject.managers;

import CapstoneProject.models.Battery;
import CapstoneProject.models.EnergySource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EnergySourceManager {
	private static final Map<String, EnergySource> energySources = new HashMap<>();

	public static void initialize() {
		energySources.put("sunny", new EnergySource("Solar"));
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

	    // Initialize an array to track percentages for progress bars
	    int[] percentages = new int[batteries.size()];
	    for (int i = 0; i < batteries.size(); i++) {
	        percentages[i] = batteries.get(i).getCharge(); // Start with each battery's initial charge
	    }

	    // Create a thread for each battery to charge concurrently
	    List<Thread> threads = new ArrayList<>();
	    for (int i = 0; i < batteries.size(); i++) {
	        int index = i; // Required for use in lambda expression
	        Battery battery = batteries.get(i);

	        Thread chargingThread = new Thread(() -> {
	            while (battery.getCharge() < 100) {
	                synchronized (battery) { // Ensure thread safety
	                    battery.recharge(5); // Increment battery charge
	                    percentages[index] = battery.getCharge(); // Update the percentage array
	                    ESLogManager.addESLog(source.getName() , battery.getName(), String.valueOf(battery.getCharge()));
	                }

	                // Simulate charging time
	                try {
	                    Thread.sleep(500); // Adjust the speed of charging
	                } catch (InterruptedException e) {
	                    Thread.currentThread().interrupt();
	                    System.out.println("Recharging interrupted for " + battery.getName());
	                    return; // Exit the thread
	                }
	            }
//	            System.out.println(battery.getName() + " is now fully charged.");
	        });

	        threads.add(chargingThread);
	        chargingThread.start();
	    }

	    // Display progress bars in the main thread
	    Thread progressBarThread = new Thread(() -> {
	        try {
	            while (true) {
	                // Check if all batteries are fully charged
	                boolean allCharged = true;
	                for (int percentage : percentages) {
	                    if (percentage < 100) {
	                        allCharged = false;
	                        break;
	                    }
	                }

	                // Display progress bars
	                ProgressBar(percentages);

	                // Exit when all batteries are fully charged
	                if (allCharged) break;

	                Thread.sleep(200); // Update progress bars every 200ms
	            }
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            System.out.println("Progress bar display interrupted.");
	        }
	    });

	    progressBarThread.start();

	    // Wait for all threads to complete
	    for (Thread thread : threads) {
	        try {
	            thread.join();
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            System.out.println("Charging process interrupted.");
	        }
	    }

	    // Wait for the progress bar thread to complete
	    try {
	        progressBarThread.join();
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt();
	        System.out.println("Progress bar process interrupted.");
	    }

	    System.out.println("All batteries are fully charged.");
	}
	



	public static void ProgressBar(int[] percentages) throws InterruptedException {
		int pbLength = 20; // Length of each progress bar

	    // Build progress output for all batteries
	    StringBuilder output = new StringBuilder("\nCharging Progress:\n");
	    for (int i = 0; i < percentages.length; i++) {
	        int completed = (int) (percentages[i] / (100.0 / pbLength)); // Completed part
	        int remained = pbLength - completed; // Remaining part

	        // Build the progress bar for the current battery
	        output.append("Battery ").append(i + 1).append(": [");
	        for (int j = 0; j < completed; j++) {
	            output.append("=");
	        }
	        for (int j = 0; j < remained; j++) {
	            output.append(" ");
	        }
	        output.append(String.format("] %3d%%\n", percentages[i])); // Append percentage
	    }

	    // Clear the console by printing empty lines (Eclipse-specific workaround)
	    for (int i = 0; i < 30; i++) {
	        System.out.println();
	    }

	    // Print the updated progress
	    System.out.println(output);
	}

}
