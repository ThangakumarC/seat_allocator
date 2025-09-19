package SeatAllocator;

import java.util.*;

public class logic {

    // New method to be called from the UI
    public static String[][] generateSeatingArrangement(List<student> students, int benchCount, int maxPerBench) {
        // Group students by department
        Map<String, Queue<student>> deptQueues = new HashMap<>();
        for (student s : students) {
            deptQueues.putIfAbsent(s.getDepartment(), new LinkedList<>());
            deptQueues.get(s.getDepartment()).add(s);
        }

        // Sort departments by student count (descending)
        List<Map.Entry<String, Queue<student>>> sortedDepts = new ArrayList<>(deptQueues.entrySet());
        sortedDepts.sort((a, b) -> b.getValue().size() - a.getValue().size());

        int totalStudents = students.size();
        int seatsPerBench = Math.min(maxPerBench, (int) Math.ceil((double) totalStudents / benchCount));
        String[][] seating = new String[benchCount][seatsPerBench];

        // Assign each column to a department
        List<String> colAssignments = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, Queue<student>> entry : sortedDepts) {
            if (index >= seatsPerBench) break;
            colAssignments.add(entry.getKey());
            index++;
        }

        // Fill seating arrangement
        for (int col = 0; col < seatsPerBench; col++) {
            String dept = colAssignments.get(col);
            Queue<student> queue = deptQueues.get(dept);

            for (int row = 0; row < benchCount; row++) {
                if (queue != null && !queue.isEmpty()) {
                    student s = queue.remove();
                    seating[row][col] = s.getRollNo() + " (" + s.getName() + ")";
                } else {
                    seating[row][col] = "Empty(---)";
                }
            }
        }

        // Fill remaining empty spots (if any) with "Empty"
        for (int row = 0; row < benchCount; row++) {
            for (int col = 0; col < seatsPerBench; col++) {
                if (seating[row][col] == null) {
                    seating[row][col] = "Empty(---)";
                }
            }
        }

        return seating;
    }

    private static void printSeatingPlan(String[][] seating) {
        System.out.println("\n--- Exam Seat Allocation ---");
        System.out.println("(Optimized Department Separation)\n");

        for (int row = 0; row < seating.length; row++) {
            for (int col = 0; col < seating[row].length; col++) {
                System.out.print((seating[row][col] != null ? seating[row][col] : "Empty(---)") + "\t");
            }
            System.out.println();
        }
    }

    // Only used if running this file directly
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of students: ");
        int studentCount = sc.nextInt();
        sc.nextLine();

        List<student> students = new ArrayList<>();
        for (int i = 0; i < studentCount; i++) {
            System.out.print("Enter name of student " + (i + 1) + ": ");
            String name = sc.nextLine();
            System.out.print("Enter id number of " + name + ": ");
            String rollNo = sc.nextLine();
            System.out.print("Enter department of " + name + ": ");
            String department = sc.nextLine();
            System.out.print("Enter year of " + name + ": ");
            String year = sc.nextLine();
            students.add(new student(name,rollNo,department,year));
        }

        System.out.print("Enter number of benches: ");
        int benchCount = sc.nextInt();

        // Generate seating arrangement
        String[][] seating = generateSeatingArrangement(students, benchCount, 3);
        printSeatingPlan(seating);

        sc.close();
    }
}
