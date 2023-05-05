package ClassAllocationSystem;

// Command interface
interface Command {
    void execute();
}

// Concrete command classes
class ReserveClassroomCommand implements Command {
    private Classroom classroom;

    public ReserveClassroomCommand(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public void execute() {
        if (classroom.isAvailable()) {
            classroom.reserve();
            System.out.println("Classroom " + classroom.getName() + " reserved successfully.");
        } else {
            System.out.println("Classroom " + classroom.getName() + " is not available for reservation.");
        }
    }
}

class CancelClassroomCommand implements Command {
    private Classroom classroom;

    public CancelClassroomCommand(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public void execute() {
        if (!classroom.isAvailable()) {
            classroom.cancelReservation();
            System.out.println("Reservation for classroom " + classroom.getName() + " cancelled successfully.");
        } else {
            System.out.println("There is no reservation for classroom " + classroom.getName() + ".");
        }
    }
}


// Invoker class
class ClassroomAdmin {
    public void executeCommand(Command command) {
        command.execute();
    }
}

