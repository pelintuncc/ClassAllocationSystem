import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Component interface
interface CampusComponent {
    void reserve();
    int getAvailableRoomsCount();
    List<CampusComponent> getChildren();
}

// Leaf class
class Classroom implements CampusComponent {
    private final String name;
    private final boolean available;

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
    public int getAvailableRoomsCount() {
        return available ? 1 : 0;
    }

    public List<CampusComponent> getChildren() {
        return Collections.emptyList();
    }

    public void reserve() {
        // Reserve this classroom
        System.out.println("Classroom " + name + " reserved.");
    }
}

// Composite class
class Building implements CampusComponent {
    private final String name;
    private final List<CampusComponent> departments = new ArrayList<>();

    public Building(String name) {
        this.name = name;
    }

    public void addDepartment(CampusComponent department) {
        departments.add(department);
    }

    public void reserve() {
        // Reserve all classrooms in this building
        System.out.println("Reserving all classrooms in building " + name + "...");
        for (CampusComponent department : departments) {
            department.reserve();
        }
    }
    public int getAvailableRoomsCount() {
        int count = 0;
        for (CampusComponent department : departments) {
            count += department.getAvailableRoomsCount();
        }
        return count;
    }

    public List<CampusComponent> getChildren() {
        return departments;
    }
}

// Composite class
class Department implements CampusComponent {
    private final String name;
    private final List<CampusComponent> floors = new ArrayList<>();

    public Department(String name) {
        this.name = name;
    }

    public void addFloor(CampusComponent floor) {
        floors.add(floor);
    }

    public void reserve() {
        // Reserve all classrooms in this department
        System.out.println("Reserving all classrooms in department " + name + "...");
        for (CampusComponent floor : floors) {
            floor.reserve();
        }
    }
    public int getAvailableRoomsCount() {
        int count = 0;
        for (CampusComponent floor : floors) {
            count += floor.getAvailableRoomsCount();
        }
        return count;
    }

    public List<CampusComponent> getChildren() {
        return floors;
    }
}

// Composite class
class Floor implements CampusComponent {
    private final int number;
    private final List<CampusComponent> classrooms = new ArrayList<>();

    public Floor(int number) {
        this.number = number;
    }

    public void addClassroom(CampusComponent classroom) {
        classrooms.add(classroom);
    }

    public void reserve() {
        System.out.println("Reserving all classrooms on floor " + number + "...");
        for (CampusComponent classroom : classrooms) {
            classroom.reserve();
        }
    }
    public int getAvailableRoomsCount() {
        int count = 0;
        for (CampusComponent classroom : classrooms) {
            count += classroom.getAvailableRoomsCount();
        }
        return count;
    }

    public List<CampusComponent> getChildren() {
        return classrooms;
    }
}
