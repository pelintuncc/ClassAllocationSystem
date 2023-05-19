// Singleton Pattern
class ResourceAllocationDepartment extends Department {
    private static ResourceAllocationDepartment instance = null;

    private ResourceAllocationDepartment(String name) {
        super(name);
    }

    public static synchronized ResourceAllocationDepartment getInstance() {
        if (instance == null) {
            instance = new ResourceAllocationDepartment("You are directed to Resource Allocation Department");
        }
        return instance;
    }
}