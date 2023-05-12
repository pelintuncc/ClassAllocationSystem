import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

interface ClassroomSubject{
    void attach(ClassroomObserver observer);
    void deAttach(ClassroomObserver observer);
    void notifyObservers();
}

interface ClassroomObserver {
    void update(Classroom classroom);
}

class ClassroomScheduler implements ClassroomObserver {
    private final ClassroomAdmin classroomAdmin;

    public ClassroomScheduler(ClassroomAdmin classroomAdmin) {
        this.classroomAdmin = classroomAdmin;
    }

    public void markClassroomsAsAvailable(CampusComponent rootComponent, List<DayOfWeek> testDays) {
        List<Classroom> classrooms = extractClassrooms(rootComponent);
        List<ClassroomCommand> classroomCommands = new ArrayList<>();
        for (Classroom classroom : classrooms) {
            if (!classroom.isAvailable()) {
                MarkClassroomAvailabilityClassroomCommand command = new MarkClassroomAvailabilityClassroomCommand(classroom, true);
                classroomAdmin.executeCommand(command);
                classroomCommands.add(command);
            }
        }
        executeCommands(classroomCommands, testDays, "available");
    }

    public void markClassroomsAsUnavailable(CampusComponent rootComponent, List<DayOfWeek> testDays) {
        List<Classroom> classrooms = extractClassrooms(rootComponent);
        List<ClassroomCommand> classroomCommands = new ArrayList<>();
        for (Classroom classroom : classrooms) {
            if (classroom.isAvailable()) {
                MarkClassroomAvailabilityClassroomCommand command = new MarkClassroomAvailabilityClassroomCommand(classroom, false);
                classroomAdmin.executeCommand(command);
                classroomCommands.add(command);
            }
        }
        executeCommands(classroomCommands, testDays, "unavailable");
    }

    private List<Classroom> extractClassrooms(CampusComponent rootComponent) {
        ClassroomIterator classroomIterator = new ConcreteClassroomIterator(rootComponent);
        List<Classroom> classrooms = new ArrayList<>();
        while (classroomIterator.hasNext()) {
            Classroom classroom = classroomIterator.next();
            classrooms.add(classroom);
        }
        return classrooms;
    }

    private void executeCommands(List<ClassroomCommand> classroomCommands, List<DayOfWeek> testDays, String availabilityStatus) {
        boolean success = true;
        for (ClassroomCommand classroomCommand : classroomCommands) {
            if (!classroomAdmin.isSuccessful(classroomCommand)) {
                success = false;
                break;
            }
        }
        System.out.println("For " + testDays.toString().substring(1, testDays.toString().length() - 1).toLowerCase() + " all classrooms are " + availabilityStatus);

        if (!success) {
            for (int i = classroomCommands.size() - 1; i >= 0; i--) {
                ClassroomCommand classroomCommand = classroomCommands.get(i);
                if (classroomAdmin.isSuccessful(classroomCommand)) {
                    classroomCommand.unExecute(ResourceAllocationDepartment.getInstance());
                }
            }
        }
    }

    @Override
    public void update(Classroom classroom) {
        if (classroom.isAvailable()) {
            System.out.println("Classroom " + classroom.getName() + " has been updated. Availability: " + classroom.isAvailable());
        }
    }
}