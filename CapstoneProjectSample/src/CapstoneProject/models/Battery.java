package CapstoneProject.models;

public class Battery {
    private final String name;
    private int charge; // Percentage (0-100)
    private int capacity; // KW

    public Battery(String name, int capacity, int charge) {
        this.name = name;
        this.capacity = capacity;
        this.charge = charge;
    }


	public String getName() {
        return name;
    }

    public int getCapacity() {
		return capacity;
	}


	public int getCharge() {
        return charge;
    }

    public void discharge(int amount) {
        charge = Math.max(0, charge - amount);
    }

    public void recharge(int amount) {
        charge = Math.min(100, charge + amount);
    }

    public boolean isEmpty() {
        return charge == 0;
    }
}
