import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

interface ClassroomCommand {
    void Execute(ResourceAllocationDepartment resourceAllocationDepartment);

    void unExecute(ResourceAllocationDepartment resourceAllocationDepartment);
}

class MarkClassroomAvailableCommand implements ClassroomCommand {
    private final Classroom classroom;
    private boolean wasAvailable;

    public MarkClassroomAvailableCommand(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public void Execute(ResourceAllocationDepartment resourceAllocationDepartment) {
        wasAvailable = classroom.isAvailable();
        classroom.markAvailable();
    }

    @Override
    public void unExecute(ResourceAllocationDepartment resourceAllocationDepartment) {
        if (!wasAvailable) {
            classroom.markUnavailable();
        }
    }
}

class MarkClassroomUnavailableCommand implements ClassroomCommand {
    private final Classroom classroom;
    private boolean wasAvailable;

    public MarkClassroomUnavailableCommand(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public void Execute(ResourceAllocationDepartment resourceAllocationDepartment) {
        wasAvailable = classroom.isAvailable();
        classroom.markUnavailable();
    }

    @Override
    public void unExecute(ResourceAllocationDepartment resourceAllocationDepartment) {
        if (wasAvailable) {
            classroom.markAvailable();
        }
    }
}

class ClassroomScheduler {
    private final ClassroomAdmin classroomAdmin;

    public ClassroomScheduler(ClassroomAdmin classroomAdmin) {
        this.classroomAdmin = classroomAdmin;
    }

    public void markClassroomsAsAvailable(CampusComponent rootComponent, List<DayOfWeek> testDays) {
        ClassroomIterator classroomIterator = new ConcreteClassroomIterator(rootComponent);

        List<ClassroomCommand> commands = new ArrayList<>();
        while (classroomIterator.hasNext()) {
            Classroom classroom = classroomIterator.next();
            if (!classroom.isAvailable()) {
                MarkClassroomAvailableCommand command = new MarkClassroomAvailableCommand(classroom);
                classroomAdmin.executeCommand2(command);
                commands.add(command);
            }
        }

        // if any of the commands fail, unexecute all executed commands
        boolean success = true;
        for (ClassroomCommand command : commands) {
            if (!classroomAdmin.isSuccessful(command)) {
                success = false;
                break;
            }
        }
        System.out.println("For " + testDays.toString().substring(1, testDays.toString().length() - 1).toLowerCase() + " all classrooms are available now");

        if (!success) {
            for (int i = commands.size() - 1; i >= 0; i--) {
                ClassroomCommand command = commands.get(i);
                if (classroomAdmin.isSuccessful(command)) {
                    command.unExecute(ResourceAllocationDepartment.getInstance());
                }
            }
        }
    }


    public void markClassroomsAsUnavailable(CampusComponent rootComponent, List<DayOfWeek> testDays) {
        ClassroomIterator classroomIterator = new ConcreteClassroomIterator(rootComponent);

        List<ClassroomCommand> commands = new ArrayList<>();
        while (classroomIterator.hasNext()) {
            Classroom classroom = classroomIterator.next();
            if (testDays.contains(LocalDate.now().getDayOfWeek())) {
                MarkClassroomUnavailableCommand command = new MarkClassroomUnavailableCommand(classroom);
                classroomAdmin.executeCommand2(command);
                commands.add(command);
            }
        }

        // if any of the commands fail, unexecute all executed commands
        boolean success = true;
        for (ClassroomCommand command : commands) {
            if (!classroomAdmin.isSuccessful(command)) {
                success = false;
                break;
            }
        }
        System.out.println("For " + testDays.toString().substring(1, testDays.toString().length() - 1).toLowerCase() + " all classrooms are unavailable now");


        if (!success) {
            for (int i = commands.size() - 1; i >= 0; i--) {
                ClassroomCommand command = commands.get(i);
                if (classroomAdmin.isSuccessful(command)) {
                    command.unExecute(ResourceAllocationDepartment.getInstance());
                }
            }
        }
    }
}

interface ClassroomIterator {
    boolean hasNext();

    Classroom next();
}

class ConcreteClassroomIterator implements ClassroomIterator {
    private final Deque<CampusComponent> stack = new ArrayDeque<>();

    public ConcreteClassroomIterator(CampusComponent root) {
        stack.push(root);
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Classroom next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        CampusComponent node = stack.pop();
        if (node instanceof Classroom) {
            return (Classroom) node;
        } else {
            Iterator<CampusComponent> iter = node.getChildren().iterator();
            while (iter.hasNext()) {
                stack.push(iter.next());
            }
            return next();
        }
    }
}
