import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Building building1 = new Building("Building 1");
        Building building2 = new Building("Building 2");
        Department department1 = new Department("Department 1");
        Department department2 = new Department("Department 2");
        Floor floor1 = new Floor(1);
        Floor floor2 = new Floor(2);
        Floor floor3 = new Floor(3);
        Classroom classroom1 = new Classroom("M 101");
        Classroom classroom2 = new Classroom("M 102");
        Classroom classroom3 = new Classroom("M 103");
        Classroom classroom4 = new Classroom("M 201");
        Classroom classroom5 = new Classroom("M 301");

        // Add classrooms to floor
        floor1.addClassroom(classroom1);
        floor1.addClassroom(classroom2);
        floor1.addClassroom(classroom3);
        floor2.addClassroom(classroom4);
        floor3.addClassroom(classroom5);

        // Add floor to department
        department1.addFloor(floor1);
        department2.addFloor(floor2);
        department2.addFloor(floor3);

        // Add department to building
        building1.addDepartment(department1);
        building2.addDepartment(department2);

        QueryAvailableRoomsCommand queryCommand = new QueryAvailableRoomsCommand(building1);
        QueryAvailableRoomsCommand queryCommand2 = new QueryAvailableRoomsCommand(building2);

        // Execute the command
        ClassroomAdmin classroomAdmin = new ClassroomAdmin();
        classroomAdmin.executeCommand(queryCommand);
        classroomAdmin.executeCommand(queryCommand2);

        // Singleton
        ResourceAllocationDepartment.getInstance().reserve();

        building1.reserve();
        classroom1.cancelReservation();

        // Create a ClassroomScheduler with the ClassroomAdmin
        ClassroomScheduler scheduler = new ClassroomScheduler(classroomAdmin);

        // Define the test days
        List<DayOfWeek> testDays = Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY);

        // Mark classrooms as unavailable for test days
        scheduler.markClassroomsAsUnavailable(building1, testDays);
        classroomAdmin.executeCommand(queryCommand);
        // Mark classrooms as available for test days
        scheduler.markClassroomsAsAvailable(building1, testDays);
        classroomAdmin.executeCommand(queryCommand);
    }
}

