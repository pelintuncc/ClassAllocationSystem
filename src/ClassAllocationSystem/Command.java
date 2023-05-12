interface ClassroomCommand {
    void execute(ResourceAllocationDepartment resourceAllocationDepartment);

    void unExecute(ResourceAllocationDepartment resourceAllocationDepartment);
}

class QueryAvailableRoomsClassroomCommand implements ClassroomCommand {
    private final CampusComponent rootComponent;

    public QueryAvailableRoomsClassroomCommand(CampusComponent rootComponent) {
        this.rootComponent = rootComponent;
    }

    @Override
    public void execute(ResourceAllocationDepartment resourceAllocationDepartment) {
        int count = 0;
        System.out.println("Available classrooms:");
        ClassroomIterator classroomIterator = new ConcreteClassroomIterator(rootComponent);
        while (classroomIterator.hasNext()) {
            Classroom classroom = classroomIterator.next();
            if (classroom.getAvailableRoomsCount() > 0) {
                System.out.println(classroom.getName());
                count++;
            }
        }
        System.out.println("Total number of rooms available for a test: " + count);
    }


    @Override
    public void unExecute(ResourceAllocationDepartment resourceAllocationDepartment) {
        // No need to implement unExecute for this command
    }
}

class MarkClassroomAvailabilityClassroomCommand implements ClassroomCommand {
    private final Classroom classroom;
    private final boolean available;

    public MarkClassroomAvailabilityClassroomCommand(Classroom classroom, boolean available) {
        this.classroom = classroom;
        this.available = available;
    }

    @Override
    public void execute(ResourceAllocationDepartment resourceAllocationDepartment) {
        classroom.setAvailable(available);
    }

    @Override
    public void unExecute(ResourceAllocationDepartment resourceAllocationDepartment) {
        classroom.setAvailable(!available);
    }
}

    class ClassroomAdmin {
    public void executeCommand(ClassroomCommand classroomCommand) {
        classroomCommand.execute(ResourceAllocationDepartment.getInstance());
    }
    public boolean isSuccessful(ClassroomCommand classroomCommand) {
        try {
            classroomCommand.execute(ResourceAllocationDepartment.getInstance());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}



