package CapstoneProject.managers;

public class LogESEntry {

	private final String batteryName;
	private final String timestamp;
	private final String energySource;
	private final String currentCharge;

	public LogESEntry(String energySource, String batteryName, String currentCharge, String timestamp) {
		this.batteryName = batteryName;
		this.timestamp = timestamp;
		this.energySource = energySource;
		this.currentCharge = currentCharge;

	}

	public String getBatteryName() {
		return batteryName;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getEnergySource() {
		return energySource;
	}

	public String getCurrentCharge() {
		return currentCharge;
	}

}
