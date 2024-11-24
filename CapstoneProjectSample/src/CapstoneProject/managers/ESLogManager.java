package CapstoneProject.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ESLogManager {
	private static final List<LogESEntry> ESlogs = new ArrayList<>();
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static synchronized void addESLog(String energySource, String batteryName, String currentCharge) {
		String timestamp = DATE_FORMAT.format(new Date());
		ESlogs.add(new LogESEntry(energySource, batteryName, currentCharge, timestamp));
	}

	public static synchronized void viewESLogs() {
		if (ESlogs.isEmpty()) {
			System.out.println("No logs available.");
			return;
		}
		displayESLogs(ESlogs);
	}

	public static synchronized void viewESLogsByFilter(String filterType, String filterValue) {
		List<LogESEntry> filteredESLogs = new ArrayList<>();

		switch (filterType.toLowerCase()) {
		case "energy source name" -> filteredESLogs
				.addAll(ESlogs.stream().filter(log -> log.getEnergySource().equalsIgnoreCase(filterValue)).toList());
		case "battery name" -> filteredESLogs
				.addAll(ESlogs.stream().filter(log -> log.getBatteryName().equalsIgnoreCase(filterValue)).toList());
		default -> {
			System.out.println("Invalid filter type.");
			return;
		}
		}

		if (filteredESLogs.isEmpty()) {
			System.out.println("No logs found for the specified filter.");
		} else {
			displayESLogs(filteredESLogs);
		}
	}

	public static synchronized void viewESLogsByDate(Date date) {
		String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
		List<LogESEntry> filteredLogs = ESlogs.stream().filter(log -> log.getTimestamp().startsWith(dateString)).toList();

		if (filteredLogs.isEmpty()) {
			System.out.println("No logs found for the specified date.");
		} else {
			displayESLogs(filteredLogs);
		}
	}

	public static synchronized void deleteESLog(int id) {
		if (id < 0 || id >= ESlogs.size()) {
			System.out.println("Invalid log ID.");
			return;
		}
		ESlogs.remove(id);
		System.out.println("Log entry removed.");
	}

	private static void displayESLogs(List<LogESEntry> logs) {
		System.out.println("---------------------------------------------------");
		System.out.printf("| %-5s | %-15s | %-15s | %-20s | %-20s |\n", "ID", "Energy Source", "Battery",
				"Current Charge", "Timestamp");
		System.out.println("---------------------------------------------------");
		for (int i = 0; i < logs.size(); i++) {
			LogESEntry log = logs.get(i);
			System.out.printf("| %-5d | %-15s | %-15s | %-20s | %-20s |\n", i, log.getEnergySource(),
					log.getBatteryName(), log.getCurrentCharge(), log.getTimestamp());
		}
	}

	public static synchronized List<LogESEntry> getESLogs() {
		return ESlogs;
	}

	public static synchronized void exportESLogs(String filePath) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			writer.println("Object,Battery,Action,Timestamp");
			for (LogESEntry log : ESlogs) {
				writer.printf("%s,%s,%s,%s\n", log.getEnergySource(), log.getBatteryName(), log.getCurrentCharge(),
						log.getTimestamp());
			}
			System.out.println("Logs exported to " + filePath);
		} catch (IOException e) {
			System.out.println("Error exporting logs: " + e.getMessage());
		}
	}
}
