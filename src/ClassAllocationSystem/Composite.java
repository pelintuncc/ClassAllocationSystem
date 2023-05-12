import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Component interface
interface CampusComponent {
    void reserve();

    int getAvailableRoomsCount();

    List<CampusComponent> getChildren();

    String getName();

    void add(CampusComponent component);

}

// Leaf class
class Classroom implements CampusComponent, ClassroomSubject {
    private final String name;
    private boolean available;
    private final List<ClassroomObserver> observers;


    public Classroom(String name) {
        this.name = name;
        this.available = true;
        this.observers = new ArrayList<>();
    }

    @Override
    public void attach(ClassroomObserver observer) {
        observers.add(observer);
    }

    @Override
    public void deAttach(ClassroomObserver observer) {
        observers.remove(observer);
    }

    public String getName() {
        return name;
    }

    @Override
    public void add(CampusComponent component) {
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

    public void setAvailable(boolean available) {
        this.available = available;
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (ClassroomObserver observer : observers) {
            observer.update(this);
        }
    }

    public void reserve() {
        if (isAvailable()) {
            setAvailable(false);
            System.out.println("Classroom " + name + " reserved.");
        } else {
            System.out.println("Classroom " + name + " reserved already.");
        }
    }

    public void cancelReservation() {
        if (!isAvailable()) {
            System.out.println("Classroom " + name + " is not reserved anymore.");
            setAvailable(true);
        } else {
            System.out.println("Classroom " + name + " is already available. ");
        }
    }
}


// Composite class
class Building implements CampusComponent {
    private final String name;
    private final List<CampusComponent> departments = new ArrayList<>();

    public Building(String name) {
        this.name = name;
    }

    public void add(CampusComponent department) {
        departments.add(department);
    }

    public void reserve() {
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

    @Override
    public String getName() {
        return name;
    }
}

// Composite class
class Department implements CampusComponent {
    @Override
    public String getName() {
        return name;
    }

    private final String name;
    private final List<CampusComponent> floors = new ArrayList<>();

    public Department(String name) {
        this.name = name;
    }

    public void add(CampusComponent floor) {
        floors.add(floor);
    }

    public void reserve() {
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
    private String name;
    private final List<CampusComponent> classrooms = new ArrayList<>();

    public Floor(int number) {
        this.number = number;
    }

    @Override
    public String getName() {
        return name;
    }

    public void add(CampusComponent classroom) {
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