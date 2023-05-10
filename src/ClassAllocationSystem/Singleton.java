package ClassAllocationSystem;

class ResourceAllocationDepartment extends Department {
    private static ResourceAllocationDepartment instance = null;

    private ResourceAllocationDepartment(String name) {
        super(name);
    }

    public static synchronized ResourceAllocationDepartment getInstance() {
        if (instance == null) {
            instance = new ResourceAllocationDepartment("Resource Allocation Department");
        }
        return instance;
    }

}