//package SeatAllocator;
//
//import java.util.*;
//
//public class logic {
//
//    // New method to be called from the UI
//    public static String[][] generateSeatingArrangement(List<Department> departments, int benchCount, int maxPerBench) {
//        return createOptimalSeating(departments, benchCount, maxPerBench);
//    }
//
//    // Modified to accept maxPerBench from UI
////    public static String[][] createOptimalSeating(List<Department> departments, int benchCount, int maxPerBench) {
////        departments.sort((d1, d2) -> d2.studentCount - d1.studentCount);
////
////        int totalStudents = departments.stream().mapToInt(d -> d.studentCount).sum();
////        int seatsPerBench = Math.min(maxPerBench, (int) Math.ceil((double) totalStudents / benchCount));
////        String[][] seating = new String[benchCount][seatsPerBench];
////
////        Map<String, Queue<Integer>> deptQueues = new HashMap<>();
////        int studentCounter = 1;
////        for (Department dept : departments) {
////            Queue<Integer> queue = new LinkedList<>();
////            for (int i = 0; i < dept.studentCount; i++) {
////                queue.add(studentCounter++);
////            }
////            deptQueues.put(dept.name, queue);
////        }
////
////        String[] colAssignments = getColumnAssignments(departments, seatsPerBench);
////
////        for (int col = 0; col < seatsPerBench; col++) {
////            String assignedDept = colAssignments[col];
////            Queue<Integer> primaryQueue = deptQueues.get(assignedDept);
////
////            for (int row = 0; row < benchCount; row++) {
////                if (primaryQueue != null && !primaryQueue.isEmpty()) {
////                    seating[row][col] = "S" + primaryQueue.remove() + "(" + assignedDept + ")";
////                } else {
////                    seating[row][col] = "Empty(---)";
////                }
////            }
////        }
////
////        return seating;
////    }
//    public static String[][] generateSeatingArrangement(List<student> students, int benchCount, int maxPerBench) {
//        // Group students by department
//        Map<String, Queue<student>> deptQueues = new HashMap<>();
//        for (student s : students) {
//            deptQueues.putIfAbsent(s.getDepartment(), new LinkedList<>());
//            deptQueues.get(s.getDepartment()).add(s);
//        }
//
//        // Sort departments by student count (descending)
//        List<Map.Entry<String, Queue<student>>> sortedDepts = new ArrayList<>(deptQueues.entrySet());
//        sortedDepts.sort((a, b) -> b.getValue().size() - a.getValue().size());
//
//        int totalStudents = students.size();
//        int seatsPerBench = Math.min(maxPerBench, (int) Math.ceil((double) totalStudents / benchCount));
//        String[][] seating = new String[benchCount][seatsPerBench];
//
//        // Assign each column to a department
//        List<String> colAssignments = new ArrayList<>();
//        int index = 0;
//        for (Map.Entry<String, Queue<student>> entry : sortedDepts) {
//            if (index >= seatsPerBench) break;
//            colAssignments.add(entry.getKey());
//            index++;
//        }
//
//        for (int col = 0; col < seatsPerBench; col++) {
//            String dept = colAssignments.get(col);
//            Queue<student> queue = deptQueues.get(dept);
//
//            for (int row = 0; row < benchCount; row++) {
//                if (queue != null && !queue.isEmpty()) {
//                    student s = queue.remove();
//                    seating[row][col] = s.getRollNo() + " (" + s.getName() + ")";
//                } else {
//                    seating[row][col] = "Empty(---)";
//                }
//            }
//        }
//
//        return seating;
//    }
//
//    private static String[] getColumnAssignments(List<Department> departments, int seatsPerBench) {
//        String[] assignments = new String[seatsPerBench];
//
//        if (departments.size() == 1) {
//            Arrays.fill(assignments, departments.get(0).name);
//            return assignments;
//        }
//
//        if (departments.size() == 2) {
//            assignments[0] = departments.get(0).name;
//            if (seatsPerBench > 1) {
//                assignments[1] = departments.get(1).name;
//            }
//            if (seatsPerBench > 2) {
//                assignments[2] = departments.get(0).name;
//            }
//            return assignments;
//        }
//
//        for (int i = 0; i < seatsPerBench; i++) {
//            assignments[i] = departments.get(i % departments.size()).name;
//        }
//        return assignments;
//    }
//
//    private static void printSeatingPlan(String[][] seating) {
//        System.out.println("\n--- Exam Seat Allocation ---");
//        System.out.println("(Optimized Department Separation)\n");
//
//        for (int row = 0; row < seating.length; row++) {
//            for (int col = 0; col < seating[row].length; col++) {
//                System.out.print((seating[row][col] != null ? seating[row][col] : "Empty(---)") + "\t");
//            }
//            System.out.println();
//        }
//    }
//
//    // Only used if running this file directly
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        System.out.print("Enter number of departments: ");
//        int deptCount = sc.nextInt();
//        sc.nextLine();
//
//        List<Department> departments = new ArrayList<>();
//        for (int i = 0; i < deptCount; i++) {
//            System.out.print("Enter name of department " + (i + 1) + ": ");
//            String name = sc.nextLine();
//            System.out.print("Enter number of students in " + name + ": ");
//            int count = sc.nextInt();
//            sc.nextLine();
//            departments.add(new Department(name, count));
//        }
//
//        System.out.print("Enter number of benches: ");
//        int benchCount = sc.nextInt();
//
//        String[][] seating = createOptimalSeating(departments, benchCount, 3);
//        printSeatingPlan(seating);
//
//        sc.close();
//    }
//}
//
//class Department {
//    public String name;
//    public int studentCount;
//
//    public Department(String name, int studentCount) {
//        this.name = name;
//        this.studentCount = studentCount;
//    }
//}


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