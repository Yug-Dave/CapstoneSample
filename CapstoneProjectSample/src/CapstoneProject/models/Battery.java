package CapstoneProject.models;

public class Battery {
    private final String name;
    private int charge; // Percentage (0-100)

    public Battery(String name, int charge) {
        this.name = name;
        this.charge = charge; // Fully charged
    }


	public String getName() {
        return name;
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
