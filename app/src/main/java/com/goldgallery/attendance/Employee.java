package com.goldgallery.attendance;

public class Employee {
    private int id;
    private String name;
    private boolean isActive;

    public Employee() {}

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
        this.isActive = true;
    }

    // Getter و Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return name;
    }
}
