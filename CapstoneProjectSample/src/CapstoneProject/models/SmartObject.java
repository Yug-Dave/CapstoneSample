package CapstoneProject.models;

public class SmartObject {
    private String name;
    private int energyRequired; // Energy required per cycle
    private boolean isActive;

    public SmartObject(String name, int energyRequired) {
        this.name = name;
        this.energyRequired = energyRequired;
        this.isActive = false;
    }

    public String getName() {
        return name;
    }

    public int getEnergyRequired() {
        return energyRequired;
    }

    public boolean isActive() {
        return isActive;
    }

    public void toggle() {
        this.isActive = !this.isActive;
    }
}

