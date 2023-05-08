package ClassAllocationSystem;

public class Main {
    public static void main(String[] args) {
        Building building1 = new Building("Building 1");
        Building building2 = new Building("Building 2");
        Department department1 = new Department("Department 1");
        Department department2 = new Department("Department 2");
        Floor floor1 = new Floor(1);
        Floor floor2 = new Floor(2);
        Classroom classroom1 = new Classroom("Classroom 1");
        Classroom classroom2 = new Classroom("Classroom 2");
        Classroom classroom3 = new Classroom("Classroom 3");
        Classroom classroom4 = new Classroom("Classroom 4");


        // Add classrooms to floor
        floor1.addClassroom(classroom1);
        floor1.addClassroom(classroom2);
        floor1.addClassroom(classroom4);
        floor2.addClassroom(classroom3);

        // Add floor to department
        department1.addFloor(floor1);
        department2.addFloor(floor2);

        // Add department to building
        building1.addDepartment(department1);
        building2.addDepartment(department2);

        ResourceAllocationDepartment.getInstance().reserve();
        int count = new ClassroomAdmin().executeCommand(new QueryAvailableRoomsCommand(building1));
        System.out.println("The total number of available rooms for a test in building 1: " +count);
        // Reserve all classrooms in building 1
        building1.reserve();
        //Cancel the reservation of classroom 1 ( reserved )
        classroom1.cancelReservation();
        // Try to cancel the reservation of an available class
        classroom3.cancelReservation();

        //QueryAvailableRoomsCommand queryCommand = new QueryAvailableRoomsCommand(building2);


        // Execute the command
        //ClassroomAdmin classroomAdmin = new ClassroomAdmin();
        //classroomAdmin.executeCommand(queryCommand);
    }
}

