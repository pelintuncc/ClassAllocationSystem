package ClassAllocationSystem;

interface RoomCommand {
    void execute();
}
    class LockClassroomCommand implements RoomCommand {
private Classroom classroom;

public LockClassroomCommand(Classroom classroom) {
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

class UnlockClassroomCommand implements RoomCommand {
    private Classroom classroom;

    public UnlockClassroomCommand(Classroom classroom) {
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
class ClassroomAdmin2 {
    public int executeCommand(QueryCommand command) {
        return command.execute(ResourceAllocationDepartment.getInstance());
    }

    public void executeCommand(RoomCommand command) {
        command.execute();
    }
}
