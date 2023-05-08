package ClassAllocationSystem;

// Command interface
interface QueryCommand {
    int execute(ResourceAllocationDepartment resourceAllocationDepartment);
}

// Concrete command class
class QueryAvailableRoomsCommand implements QueryCommand {
    private final CampusComponent rootComponent;

    public QueryAvailableRoomsCommand(CampusComponent rootComponent) {
        this.rootComponent = rootComponent;
    }

    public int execute(ResourceAllocationDepartment resourceAllocationDepartment) {
        int count = 0;
        for (CampusComponent department : rootComponent.getChildren()) {
            count += department.getAvailableRoomsCount();
        }
        System.out.println("Total number of rooms available for a test: " + count);

        return count;
    }
}

// Invoker class
class ClassroomAdmin {

    public int executeCommand(QueryCommand command) {
        return command.execute(ResourceAllocationDepartment.getInstance());
    }
}