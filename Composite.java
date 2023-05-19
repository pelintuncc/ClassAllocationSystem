import java.time.DayOfWeek;
import java.util.*;

// Component interface
interface CampusComponent {
    void reserve(DayOfWeek testDay);

    int getAvailableRoomsCount();

    List<CampusComponent> getChildren();

    String getName();

    void add(CampusComponent component);

    void accept(CampusVisitor visitor);

}

// Leaf class
class Classroom implements CampusComponent, ClassroomSubject {
    private final String name;
    private boolean available;
    private final List<ClassroomObserver> observers;

    private final int capacity;

    private List<DayOfWeek> unavailableTestDays;

    private String department;

    private Map<DayOfWeek, Boolean> availability = new HashMap<>();


    public Classroom(String name, int capacity, String departmentName) {
        this.name = name;
        this.available = true;
        this.observers = new ArrayList<>();
        this.capacity = capacity;
        this.unavailableTestDays = new ArrayList<>();
        this.department = departmentName;
    }

    public String getDepartment() {
        return department;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public void accept(CampusVisitor visitor) {
        visitor.visit(this);
        notifyObservers();
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

    public void setUnavailableTestDays(List<DayOfWeek> testDays) {
        this.unavailableTestDays = testDays;
    }

    public boolean isAvailableForTestDays(List<DayOfWeek> testDays) {
        for (DayOfWeek testDay : testDays) {
            if (unavailableTestDays.contains(testDay)) {
                return false; // Classroom is unavailable for at least one of the test days
            }
        }
        return true; // Classroom is available for all test days
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

    public void reserve(DayOfWeek testDay) {
        if (isAvailable(testDay)) {
            System.out.println("Classroom " + getName() + " with the capacity " + getCapacity() + " reserved for " + testDay + ".");
            setAvailable(testDay, false);
        } else {
            System.out.println("Classroom " + getName() + " already reserved for " + testDay + " for that time.");
        }
    }

    public boolean isAvailable(DayOfWeek dayOfWeek) {
        return availability.getOrDefault(dayOfWeek, true);
    }

    public void setAvailable(DayOfWeek dayOfWeek, boolean available) {
        availability.put(dayOfWeek, available);
        notifyObservers();
    }

    public void cancelReservation(DayOfWeek testDay) {
        if (!isAvailable()) {
            System.out.println("Classroom " + name + " is not reserved anymore.");
            setAvailable(testDay,true);
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

    @Override
    public void accept(CampusVisitor visitor) {
        visitor.visit(this);
        for (CampusComponent department : departments) {
            department.accept(visitor);
        }
    }

    public void reserve(DayOfWeek testDay) {
        System.out.println("Reserving all classrooms in building " + name + "...");
        for (CampusComponent department : departments) {
            department.reserve(testDay);
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

    private final List<Classroom> classrooms;


    public Department(String name) {
        this.name = name;
        this.classrooms = new ArrayList<>();

    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void add(CampusComponent floor) {
        floors.add(floor);
    }

    @Override
    public void accept(CampusVisitor visitor) {
        visitor.visit(this);
        for (CampusComponent floor : floors) {
            floor.accept(visitor);
        }
    }

    public void reserve(DayOfWeek testDay) {
        System.out.println("Reserving all classrooms in department " + name + "...");
        for (CampusComponent floor : floors) {
            floor.reserve(testDay);
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

    @Override
    public void accept(CampusVisitor visitor) {
        visitor.visit(this);
        for (CampusComponent classroom : classrooms) {
            classroom.accept(visitor);
        }
    }

    public void reserve(DayOfWeek testDay) {
        System.out.println("Reserving all classrooms on floor " + number + "...");
        for (CampusComponent classroom : classrooms) {
            classroom.reserve(testDay);
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