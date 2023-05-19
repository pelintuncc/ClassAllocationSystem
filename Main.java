import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Building building1 = new Building("Main Building");
        Department department1 = new Department("Computer Science");
        Department department2 = new Department("Industrial Engineering");
        Floor floor1 = new Floor(1);
        Floor floor2 = new Floor(2);
        Floor floor3 = new Floor(3);
        Classroom classroom1 = new Classroom("M101", 105, department1.getName());
        Classroom classroom2 = new Classroom("M102", 45, department1.getName());
        Classroom classroom3 = new Classroom("M103", 60, department1.getName());
        Classroom classroom4 = new Classroom("M201", 80, department2.getName());
        Classroom classroom5 = new Classroom("M301", 30, department2.getName());

        // Add classrooms to floor
        floor1.add(classroom1);
        floor1.add(classroom2);
        floor1.add(classroom3);
        floor2.add(classroom4);
        floor3.add(classroom5);

        // Add floor to department
        department1.add(floor1);
        department2.add(floor2);
        department2.add(floor3);

        // Add department to building
        building1.add(department1);
        building1.add(department2);

        QueryAvailableRoomsClassroomCommand queryCommand = new QueryAvailableRoomsClassroomCommand(building1);

        // Execute the command
        ClassroomAdmin classroomAdmin = new ClassroomAdmin();
        ClassroomScheduler scheduler = new ClassroomScheduler(classroomAdmin);


        UnlockVisitor unlockVisitor1 = new UnlockVisitor(department1.getName(), "Ecem Konca");

        UnlockVisitor unlockVisitor2 = new UnlockVisitor(department2.getName(), "Melis Baran");

        classroom1.attach(scheduler);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Classroom Allocation System!");

        boolean flag = false;
        while (!flag) {
            System.out.println("1. Reserve a classroom");
            System.out.println("2. Cancel a classroom");
            System.out.println("3. Mark all classrooms unavailable");
            System.out.println("4. Display all classrooms");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter the day of the week for the lesson:");
                    String testDay = scanner.next();

                    DayOfWeek lessonDay;
                    try {
                        lessonDay = DayOfWeek.valueOf(testDay.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid day of the week. Lesson scheduling failed.");
                        return;
                    }

                    System.out.println("Enter the start hour for the lesson (24-hour format):");
                    int startHour = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Enter the start minute for the lesson:");
                    int startMinute = scanner.nextInt();
                    scanner.nextLine();

                    LocalDateTime lessonStartDateTime = LocalDateTime.now()
                            .with(TemporalAdjusters.nextOrSame(lessonDay))
                            .withHour(startHour)
                            .withMinute(startMinute)
                            .withSecond(0)
                            .withNano(0);

                    LocalDateTime lessonEndDateTime = lessonStartDateTime.plusHours(1); // Assuming 1-hour duration for the exam

                    List<DayOfWeek> lessonDays = Collections.singletonList(lessonDay);
                    System.out.println("Enter the student capacity");
                    int capacity = scanner.nextInt();

                    classroomAdmin.executeCommand(queryCommand, lessonDay, capacity);

                    // Check if the selected classrooms are available for the lesson days
                    System.out.println("Enter the number of classrooms: ");
                    int numberOfClassroomsToReserve = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    List<Classroom> selectedClassrooms = new ArrayList<>();
                    for (int i = 0; i < numberOfClassroomsToReserve; i++) {
                        System.out.print("Enter the classroom number to reserve: ");
                        int classroomNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        Classroom selectedClassroom;
                        switch (classroomNumber) {
                            case 1:
                                selectedClassroom = classroom5;
                                break;
                            case 2:
                                selectedClassroom = classroom4;
                                break;
                            case 3:
                                selectedClassroom = classroom3;
                                break;
                            case 4:
                                selectedClassroom = classroom2;
                                break;
                            case 5:
                                selectedClassroom = classroom1;
                                break;
                            default:
                                System.out.println("Invalid classroom number. Reservation failed.");
                                return;
                        }
                        selectedClassrooms.add(selectedClassroom);
                    }
                    // Perform classroom reservations
                    for (Classroom classroom : selectedClassrooms) {
                        if (classroom.isAvailableForTestDays(lessonDays)) {
                            // Check if the department has an assigned security personnel
                            String departmentName = classroom.getDepartment();
                            UnlockVisitor unlockVisitor;
                            if (departmentName.equals(department1.getName())) {
                                unlockVisitor = unlockVisitor1;
                            } else if (departmentName.equals(department2.getName())) {
                                unlockVisitor = unlockVisitor2;
                            } else {
                                System.out.println("Department not found. Unable to unlock classrooms.");
                                return;
                            }
                            classroom.accept(unlockVisitor);
                            classroom.reserve(lessonDay);
                        }
                    }
                    break;
                case 2:
                    System.out.println("Enter the day of the week for the exam:");
                    String testDay21 = scanner.next();

                    DayOfWeek lessonDay21;
                    try {
                        lessonDay21 = DayOfWeek.valueOf(testDay21.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid day of the week. Exam scheduling failed.");
                        return;
                    }
                    System.out.println("Reserved classrooms for: " + lessonDay21);
                    ClassroomIterator classroomIterator = new ConcreteClassroomIterator(building1);
                    boolean hasReservedClassrooms = false;
                    while (classroomIterator.hasNext()) {
                        Classroom classroom = classroomIterator.next();
                        if (!classroom.isAvailable(lessonDay21)) {
                            System.out.println("Classroom: " + classroom.getName());
                            hasReservedClassrooms = true;
                        }
                    }

                    if (!hasReservedClassrooms) {
                        System.out.println("No classrooms are reserved for " + lessonDay21);
                    } else {
                        System.out.print("Enter the classroom name to cancel the reservation: ");
                        String classroomName = scanner.next();

                        // Find the selected classroom and cancel the reservation
                        Classroom selectedClassroom = null;
                        classroomIterator = new ConcreteClassroomIterator(building1);
                        while (classroomIterator.hasNext()) {
                            Classroom classroom = classroomIterator.next();
                            if (classroom.getName().equals(classroomName) && !classroom.isAvailable()) {
                                selectedClassroom = classroom;
                                break;
                            }
                        }

                        if (selectedClassroom != null) {
                            selectedClassroom.cancelReservation(lessonDay21);
                            System.out.println("Reservation for Classroom " + selectedClassroom.getName() + " has been canceled.");
                        } else {
                            System.out.println("Invalid classroom name or the classroom is not reserved.");
                        }
                    }
                    break;
                case 3:
                    System.out.println("Enter the day of the week for the exam:");
                    String testDay31 = scanner.next();

                    DayOfWeek lessonDay31;
                    try {
                        lessonDay31 = DayOfWeek.valueOf(testDay31.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid day of the week. Exam scheduling failed.");
                        return;
                    }
                    scheduler.markClassroomsAsUnavailable(building1, lessonDay31);
                    ClassroomIterator classroomIterator3 = new ConcreteClassroomIterator(building1);
                    while (classroomIterator3.hasNext()) {
                        Classroom classroom = classroomIterator3.next();
                        System.out.println("Classroom: " + classroom.getName() + " - Capacity: " + classroom.getCapacity() + " - Availability: " + (classroom.isAvailable(lessonDay31) ? "Available" : "Reserved"));
                    }
                    break;
                case 4:
                    System.out.println("Enter the day of the week for the exam:");
                    String testDay41 = scanner.next();

                    DayOfWeek lessonDay41;
                    try {
                        lessonDay41 = DayOfWeek.valueOf(testDay41.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid day of the week. Exam scheduling failed.");
                        return;
                    }
                    System.out.println("Classroom availability for :" + lessonDay41);
                    ClassroomIterator classroomIterator2 = new ConcreteClassroomIterator(building1);
                    while (classroomIterator2.hasNext()) {
                        Classroom classroom = classroomIterator2.next();
                        System.out.println("Classroom: " + classroom.getName() + " - Capacity: " + classroom.getCapacity() + " - Availability: " + (classroom.isAvailable(lessonDay41) ? "Available" : "Reserved"));
                    }
                    break;
                case 5:
                    System.out.println("Thank you for using Class Allocation System...");
                    flag = true;
                    break;
                default:
                    break;
            }
        }
    }
}