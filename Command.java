import java.time.DayOfWeek;
import java.util.List;

interface ClassroomCommand {
    void execute(ResourceAllocationDepartment resourceAllocationDepartment, DayOfWeek testDays, int desiredCapacity);

    void unExecute(ResourceAllocationDepartment resourceAllocationDepartment);

    void executeAvailable(ResourceAllocationDepartment resourceAllocationDepartment, DayOfWeek testDays);

}

class QueryAvailableRoomsClassroomCommand implements ClassroomCommand {
    private final CampusComponent rootComponent;

    public QueryAvailableRoomsClassroomCommand(CampusComponent rootComponent) {
        this.rootComponent = rootComponent;
    }

    // query with days, capacity, and hours
    @Override
    public void execute(ResourceAllocationDepartment resourceAllocationDepartment, DayOfWeek  testDays, int desiredCapacity) {
        int count = 0;
        System.out.println("Available classrooms:");
        ClassroomIterator classroomIterator = new ConcreteClassroomIterator(rootComponent);
        int index = 1;
        while (classroomIterator.hasNext()) {
            Classroom classroom = classroomIterator.next();
            if (classroom.getAvailableRoomsCount() > 0 && classroom.isAvailable(testDays) && classroom.getCapacity() >= desiredCapacity) {
                System.out.println(index + ". Classroom: " + classroom.getName() + " Capacity: " + classroom.getCapacity());
                count++;
            }
            index++;
        }
        System.out.println("Total number of rooms available for a test: " + count);
    }

    @Override
    public void unExecute(ResourceAllocationDepartment resourceAllocationDepartment) {
        // No need to implement unExecute for this command
    }

    @Override
    public void executeAvailable(ResourceAllocationDepartment resourceAllocationDepartment, DayOfWeek testDays) {
        // No need to implement unExecute for this command
    }
}

class MarkClassroomAvailabilityClassroomCommand implements ClassroomCommand {
    private final Classroom classroom;
    private final boolean available;

    private DayOfWeek testDay;

    public MarkClassroomAvailabilityClassroomCommand(Classroom classroom,DayOfWeek testDay, boolean available) {
        this.classroom = classroom;
        this.available = available;
        this.testDay=testDay;
    }

    @Override
    public void execute(ResourceAllocationDepartment resourceAllocationDepartment, DayOfWeek testDays, int desiredCapacity) {
        classroom.setAvailable(available);
    }

    @Override
    public void unExecute(ResourceAllocationDepartment resourceAllocationDepartment) {
        classroom.setAvailable(!available);
    }

    @Override
    public void executeAvailable(ResourceAllocationDepartment resourceAllocationDepartment, DayOfWeek testDays) {
        classroom.setAvailable(available);
    }
}

class ClassroomAdmin {
    public void executeCommand(ClassroomCommand classroomCommand, DayOfWeek testDays, int desiredCapacity) {
        classroomCommand.execute(ResourceAllocationDepartment.getInstance(), testDays, desiredCapacity);
    }
    public void executeAvailabilityCommand(ClassroomCommand classroomCommand, DayOfWeek testDays) {
        classroomCommand.executeAvailable(ResourceAllocationDepartment.getInstance(), testDays);
    }

    public boolean isSuccessful(ClassroomCommand classroomCommand, DayOfWeek testDays) {
        try {
            classroomCommand.executeAvailable(ResourceAllocationDepartment.getInstance(), testDays);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}