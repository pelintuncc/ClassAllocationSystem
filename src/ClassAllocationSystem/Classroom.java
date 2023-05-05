package ClassAllocationSystem;

public class Classroom {
    private String name;
    private boolean available;

    public Classroom(String name) {
        this.name = name;
        this.available = true;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void reserve() {
        available = false;
    }

    public void cancelReservation() {
        available = true;
    }
}