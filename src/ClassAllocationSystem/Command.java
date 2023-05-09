// Command interface
interface QueryCommand {
    void execute(ResourceAllocationDepartment resourceAllocationDepartment);

}

// Concrete command class
class QueryAvailableRoomsCommand implements QueryCommand {
    private final CampusComponent rootComponent;

    public QueryAvailableRoomsCommand(CampusComponent rootComponent) {
        this.rootComponent = rootComponent;
    }

    public void execute(ResourceAllocationDepartment resourceAllocationDepartment) {
        int count = 0;
        System.out.println("Available classrooms:");
        for (CampusComponent department : rootComponent.getChildren()) {
            for (CampusComponent floor : department.getChildren()) {
                for (CampusComponent classroom : floor.getChildren()) {
                    if (classroom.getAvailableRoomsCount() > 0) {
                        System.out.println(classroom.getName());
                        count++;
                    }
                }
            }
        }
        System.out.println("Total number of rooms available for a test: " + count);
    }
}

// Invoker class
class ClassroomAdmin {
    public void executeCommand(QueryCommand command) {
        command.execute(ResourceAllocationDepartment.getInstance());
    }
    public boolean isSuccessful(ClassroomCommand command) {
        try {
            command.Execute(ResourceAllocationDepartment.getInstance());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void executeCommand2(ClassroomCommand command) {
        try {
            command.Execute(ResourceAllocationDepartment.getInstance());
        } catch (Exception e) {
            System.out.println("Failed to execute command: " + e.getMessage());
        }
    }
}