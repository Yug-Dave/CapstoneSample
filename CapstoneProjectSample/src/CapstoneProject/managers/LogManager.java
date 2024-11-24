package CapstoneProject.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogManager {
	private static final List<LogEntry> logs = new ArrayList<>();
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static synchronized void addLog(String objectName, String batteryName, String action) {
		String timestamp = DATE_FORMAT.format(new Date());
		logs.add(new LogEntry(objectName, batteryName, action, timestamp));
	}


	public static synchronized void viewLogs() {
		if (logs.isEmpty()) {
			System.out.println("No logs available.");
			return;
		}
		displayLogs(logs);
	}
	

	public static synchronized void viewLogsByFilter(String filterType, String filterValue) {
		List<LogEntry> filteredLogs = new ArrayList<>();

		switch (filterType.toLowerCase()) {
		case "object" -> filteredLogs
				.addAll(logs.stream().filter(log -> log.getObjectName().equalsIgnoreCase(filterValue)).toList());
		case "battery" -> filteredLogs
				.addAll(logs.stream().filter(log -> log.getBatteryName().equalsIgnoreCase(filterValue)).toList());
		default -> {
			System.out.println("Invalid filter type.");
			return;
		}
		}

		if (filteredLogs.isEmpty()) {
			System.out.println("No logs found for the specified filter.");
		} else {
			displayLogs(filteredLogs);
		}
	}

	public static synchronized void viewLogsByDate(Date date) {
		String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
		List<LogEntry> filteredLogs = logs.stream().filter(log -> log.getTimestamp().startsWith(dateString)).toList();

		if (filteredLogs.isEmpty()) {
			System.out.println("No logs found for the specified date.");
		} else {
			displayLogs(filteredLogs);
		}
	}

	public static synchronized void deleteLog(int id) {
		if (id < 0 || id >= logs.size()) {
			System.out.println("Invalid log ID.");
			return;
		}
		logs.remove(id);
		System.out.println("Log entry removed.");
	}

	private static void displayLogs(List<LogEntry> logs) {
		System.out.println("---------------------------------------------------");
		System.out.printf("| %-5s | %-15s | %-15s | %-20s | %-20s |\n", "ID", "Object", "Battery", "Action",
				"Timestamp");
		System.out.println("---------------------------------------------------");
		for (int i = 0; i < logs.size(); i++) {
			LogEntry log = logs.get(i);
			System.out.printf("| %-5d | %-15s | %-15s | %-20s | %-20s |\n", i, log.getObjectName(),
					log.getBatteryName(), log.getAction(), log.getTimestamp());
		}
	}
	

	public static synchronized List<LogEntry> getLogs() {
		return logs;
	}

	public static synchronized void exportLogs(String filePath) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			writer.println("Object,Battery,Action,Timestamp");
			for (LogEntry log : logs) {
				writer.printf("%s,%s,%s,%s\n", log.getObjectName(), log.getBatteryName(), log.getAction(),
						log.getTimestamp());
			}
			System.out.println("Logs exported to " + filePath);
		} catch (IOException e) {
			System.out.println("Error exporting logs: " + e.getMessage());
		}
	}
}
