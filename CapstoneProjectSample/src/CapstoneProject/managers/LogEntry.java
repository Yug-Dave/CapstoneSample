package CapstoneProject.managers;

public class LogEntry {
	private final String objectName;
	private final String batteryName;
	private final String action;
	private final String timestamp;

	public LogEntry(String objectName, String batteryName, String action, String timestamp) {
		this.objectName = objectName;
		this.batteryName = batteryName;
		this.action = action;
		this.timestamp = timestamp;
	}

	public String getObjectName() {
		return objectName;
	}

	public String getBatteryName() {
		return batteryName;
	}

	public String getAction() {
		return action;
	}

	public String getTimestamp() {
		return timestamp;
	}
}
