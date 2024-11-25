package CapstoneProject.models;

public class EnergySource {
    private String name;
    private int power; // KW/s

    public EnergySource(String name, int power) {
        this.name = name;
        this.power = power;
    }

    public String getName() {
        return name;
    }

	public int getPower() {
		return power;
	}
}
