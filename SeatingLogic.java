////package SeatAllocator;
////
////import java.util.*;
////
////public class SeatingLogic {
////    
////    public static String[][] generateSeatingArrangement(List<student> students, int benchCount, int maxPerBench) {
////        // Validate input
////        if (students == null || students.isEmpty() || benchCount <= 0 || maxPerBench <= 0) {
////            throw new IllegalArgumentException("Invalid input parameters");
////        }
////
////        // Group students by department
////        Map<String, Queue<student>> deptGroups = groupStudentsByDepartment(students);
////        
////        // Sort departments by student count (descending)
////        List<String> sortedDepts = sortDepartmentsBySize(deptGroups);
////        
////        // Calculate seating dimensions
////        int seatsPerBench = calculateSeatsPerBench(students.size(), benchCount, maxPerBench);
////        String[][] seating = new String[benchCount][seatsPerBench];
////        
////        // Assign departments to columns
////        String[] colAssignments = assignDepartmentsToColumns(sortedDepts, seatsPerBench);
////        
////        // Fill the seating arrangement
////        fillSeatingArrangement(seating, deptGroups, colAssignments);
////        
////        return seating;
////    }
////
////    private static Map<String, Queue<student>> groupStudentsByDepartment(List<student> students) {
////        Map<String, Queue<student>> deptGroups = new HashMap<>();
////        for (student s : students) {
////            deptGroups.computeIfAbsent(s.getDepartment(), _ -> new LinkedList<>()).add(s);
////        }
////        return deptGroups;
////    }
////
////    private static List<String> sortDepartmentsBySize(Map<String, Queue<student>> deptGroups) {
////        List<String> sortedDepts = new ArrayList<>(deptGroups.keySet());
////        sortedDepts.sort((d1, d2) -> 
////            Integer.compare(deptGroups.get(d2).size(), deptGroups.get(d1).size()));
////        return sortedDepts;
////    }
////
////    private static int calculateSeatsPerBench(int studentCount, int benchCount, int maxPerBench) {
////        return Math.min(maxPerBench, (int) Math.ceil((double) studentCount / benchCount));
////    }
////
////    private static String[] assignDepartmentsToColumns(List<String> departments, int seatsPerBench) {
////        String[] assignments = new String[seatsPerBench];
////        
////        if (departments.size() == 1) {
////            Arrays.fill(assignments, departments.get(0));
////            return assignments;
////        }
////        
////        if (departments.size() == 2 && seatsPerBench == 3) {
////            assignments[0] = departments.get(0); // First column - largest dept
////            assignments[1] = departments.get(1); // Middle column - smaller dept
////            assignments[2] = departments.get(0); // Third column - largest dept
////            return assignments;
////        }
////        
////        // General case - rotate through departments
////        for (int i = 0; i < seatsPerBench; i++) {
////            assignments[i] = departments.get(i % departments.size());
////        }
////        return assignments;
////    }
////
////    private static void fillSeatingArrangement(String[][] seating, 
////                                             Map<String, Queue<student>> deptGroups,
////                                             String[] colAssignments) {
////        for (int col = 0; col < seating[0].length; col++) {
////            String dept = colAssignments[col];
////            Queue<student> queue = deptGroups.get(dept);
////            
////            for (int row = 0; row < seating.length; row++) {
////                if (queue != null && !queue.isEmpty()) {
////                    student s = queue.poll();
////                    seating[row][col] = formatStudentSeat(s);
////                } else {
////                    seating[row][col] = "Empty(---)";
////                }
////            }
////        }
////    }
////
////    private static String formatStudentSeat(student s) {
////        return String.format("%s (%s - %s)", s.getRollNo(), s.getName() , s.getYear());
////    }
////}
////
////
//////2nd 
////package SeatAllocator;
////
////import java.util.*;
////
////public class SeatingLogic {
////    
////    public static String[][] generateSeatingArrangement(List<student> students, int benchCount, int maxPerBench) {
////        // Validate input
////        if (students == null || students.isEmpty() || benchCount <= 0 || maxPerBench <= 0) {
////            throw new IllegalArgumentException("Invalid input parameters");
////        }
////
////        // First group by department
////        Map<String, List<student>> deptGroups = new HashMap<>();
////        for (student s : students) {
////           deptGroups.computeIfAbsent(s.getDepartment(), _ -> new ArrayList<>()).add(s);
////        }
////        // Within each department, sort students by year
////        for (List<student> deptStudents : deptGroups.values()) {
////            deptStudents.sort(Comparator.comparing(student::getYear));
////        }
////
////        // Convert to queues after sorting
////        Map<String, Queue<student>> deptQueues = new HashMap<>();
////        deptGroups.forEach((dept, list) -> 
////            deptQueues.put(dept, new LinkedList<>(list)));
////
////        // Sort departments by student count (descending)
////        List<String> sortedDepts = new ArrayList<>(deptQueues.keySet());
////        sortedDepts.sort((d1, d2) -> 
////            Integer.compare(deptQueues.get(d2).size(), deptQueues.get(d1).size()));
////
////        // Calculate seating dimensions
////        int seatsPerBench = Math.min(maxPerBench, 
////            (int) Math.ceil((double) students.size() / benchCount));
////        String[][] seating = new String[benchCount][seatsPerBench];
////
////        // Assign departments to columns
////        String[] colAssignments = assignDepartmentsToColumns(sortedDepts, seatsPerBench);
////
////        // Fill the seating arrangement
////        for (int col = 0; col < seating[0].length; col++) {
////            String dept = colAssignments[col];
////           Queue<student> queue = deptQueues.get(dept);
////            
////            for (int row = 0; row < seating.length; row++) {
////                if (queue != null && !queue.isEmpty()) {
////                    student s = queue.poll();
////                   String position = getPositionIndicator(col, seating[0].length);
////                   seating[row][col] = String.format("%s-Y%s %s", 
////                       s.getRollNo(), s.getYear(), position);
////               } else {
////                   String position = getPositionIndicator(col, seating[0].length);
////                   seating[row][col] = String.format("Empty(%s)", position);
////               }
////            }
////        }
////
////        return seating;
////    }
////
////    private static String[] assignDepartmentsToColumns(List<String> departments, int seatsPerBench) {
////        String[] assignments = new String[seatsPerBench];
////        
////        if (departments.size() == 1) {
////            Arrays.fill(assignments, departments.get(0));
////            return assignments;
////        }
////       
////        if (departments.size() == 2 && seatsPerBench == 3) {
////            assignments[0] = departments.get(0);
////          assignments[1] = departments.get(1);
////           assignments[2] = departments.get(0);
////            return assignments;
////       }
////        
////       // General case - rotate through departments
////       for (int i = 0; i < seatsPerBench; i++) {
////           assignments[i] = departments.get(i % departments.size());
////       }
////        return assignments;
////    }
////    private static String getPositionIndicator(int colIndex, int totalColumns) {
////        if (totalColumns == 1) return "M";
////        if (totalColumns == 2) return (colIndex == 0) ? "L" : "R";
////       if (colIndex == 0) return "L";
////       if (colIndex == totalColumns - 1) return "R";
////        return "M";
////    }
////}
//
////3
//package SeatAllocator;
//
//import java.util.*;
//
//public class SeatingLogic {
//    
////    public static String[][] generateSeatingArrangement(List<student> students, int benchCount, int maxPerBench) {
////        // Validate input
////        if (students == null || students.isEmpty() || benchCount <= 0 || maxPerBench <= 0) {
////            throw new IllegalArgumentException("Invalid input parameters");
////        }
////
////        // Group by department and year
////        Map<String, Map<String, Queue<student>>> deptYearGroups = new HashMap<>();
////        for (student s : students) {
////            deptYearGroups
////                .computeIfAbsent(s.getDepartment(), _ -> new HashMap<>())
////                .computeIfAbsent(s.getYear(), _ -> new LinkedList<>())
////                .add(s);
////        }
////
////        // Sort departments by total students (descending)
////        List<String> sortedDepts = new ArrayList<>(deptYearGroups.keySet());
////        sortedDepts.sort((d1, d2) -> 
////            Integer.compare(
////                deptYearGroups.get(d2).values().stream().mapToInt(Queue::size).sum(),
////                deptYearGroups.get(d1).values().stream().mapToInt(Queue::size).sum()
////            ));
////
////        // Calculate seating
////        int seatsPerBench = Math.min(maxPerBench, 
////            (int) Math.ceil((double) students.size() / benchCount));
////        String[][] seating = new String[benchCount][seatsPerBench];
////
////        // Assign departments to columns
////        String[] colAssignments = assignDepartmentsToColumns(sortedDepts, seatsPerBench);
////
////        // Fill seats with year separation
////        fillSeatsWithSeparation(seating, deptYearGroups, colAssignments);
////
////        return seating;
////    }
////
////    private static String[] assignDepartmentsToColumns(List<String> departments, int seatsPerBench) {
////        String[] assignments = new String[seatsPerBench];
////        
////        if (departments.size() == 1) {
////            Arrays.fill(assignments, departments.get(0));
////            return assignments;
////        }
////        
////        if (departments.size() == 2 && seatsPerBench == 3) {
////            assignments[0] = departments.get(0);
////            assignments[1] = departments.get(1);
////            assignments[2] = departments.get(0);
////            return assignments;
////        }
////        
////        // Rotate departments with separation
////        for (int i = 0; i < seatsPerBench; i++) {
////            int deptIndex = i % departments.size();
////            if (i > 0 && assignments[i-1].equals(departments.get(deptIndex))) {
////                deptIndex = (deptIndex + 1) % departments.size();
////            }
////            assignments[i] = departments.get(deptIndex);
////        }
////        return assignments;
////    }
////
////    private static void fillSeatsWithSeparation(String[][] seating, 
////                                              Map<String, Map<String, Queue<student>>> deptYearGroups,
////                                              String[] colAssignments) {
////        // Track last used year per department
////        Map<String, String> lastYearUsed = new HashMap<>();
////        
////        for (int col = 0; col < seating[0].length; col++) {
////            String dept = colAssignments[col];
////            Map<String, Queue<student>> yearQueues = deptYearGroups.get(dept);
////            
////            if (yearQueues == null || yearQueues.isEmpty()) {
////                fillColumnWithEmpty(seating, col);
////                continue;
////            }
////            
////            // Get years sorted by student count
////            List<String> years = new ArrayList<>(yearQueues.keySet());
////            years.sort((y1, y2) -> 
////                Integer.compare(yearQueues.get(y2).size(), yearQueues.get(y1).size()));
////            
////            // Avoid consecutive same years
////            String lastYear = lastYearUsed.get(dept);
////            if (lastYear != null && years.size() > 1 && years.get(0).equals(lastYear)) {
////                Collections.swap(years, 0, 1);
////            }
////            
////            int yearIndex = 0;
////            
////            for (int row = 0; row < seating.length; row++) {
////                if (yearQueues.isEmpty()) {
////                    seating[row][col] = "Empty(---)";
////                    continue;
////                }
////                
////                String currentYear = years.get(yearIndex % years.size());
////                Queue<student> queue = yearQueues.get(currentYear);
////                
////                if (!queue.isEmpty()) {
////                    student s = queue.poll();
////                    seating[row][col] = formatStudentSeat(s, col, seating[0].length);
////                    lastYearUsed.put(dept, currentYear);
////                    yearIndex++;
////                } else {
////                    yearQueues.remove(currentYear);
////                    years.remove(currentYear);
////                    if (!years.isEmpty()) {
////                        row--; // Retry with next year
////                    } else {
////                        seating[row][col] = "Empty(---)";
////                    }
////                }
////            }
////        }
////    }
//	public static String[][] generateSeatingArrangement(List<student> students, int benchCount, int maxPerBench) {
//        // Validate input
//        if (students == null || students.isEmpty() || benchCount <= 0 || maxPerBench <= 0) {
//            throw new IllegalArgumentException("Invalid input parameters");
//        }
//
//        // First sort students by department and year
//        students.sort((s1, s2) -> {
//            int deptCompare = s1.getDepartment().compareTo(s2.getDepartment());
//            if (deptCompare != 0) return deptCompare;
//            return s1.getYear().compareTo(s2.getYear());
//        });
//
//        // Group by department and year while maintaining sort order
//        Map<String, Map<String, Queue<student>>> deptYearGroups = new LinkedHashMap<>();
//        for (student s : students) {
//            deptYearGroups
//                .computeIfAbsent(s.getDepartment(), k -> new LinkedHashMap<>())
//                .computeIfAbsent(s.getYear(), k -> new LinkedList<>())
//                .add(s);
//        }
//
//        // Sort departments by total student count (descending)
//        List<String> sortedDepts = new ArrayList<>(deptYearGroups.keySet());
//        sortedDepts.sort((d1, d2) -> 
//            Integer.compare(
//                deptYearGroups.get(d2).values().stream().mapToInt(Queue::size).sum(),
//                deptYearGroups.get(d1).values().stream().mapToInt(Queue::size).sum()
//            ));
//
//        // Calculate seating dimensions
//        int seatsPerBench = Math.min(maxPerBench, 
//            (int) Math.ceil((double) students.size() / benchCount));
//        String[][] seating = new String[benchCount][seatsPerBench];
//
//        // Assign departments to columns with separation
//        String[] colAssignments = assignDepartmentsToColumns(sortedDepts, seatsPerBench);
//
//        // Fill seats with proper formatting
//        fillSeatsWithFormatting(seating, deptYearGroups, colAssignments);
//
//        return seating;
//    }
//
//    private static String[] assignDepartmentsToColumns(List<String> departments, int seatsPerBench) {
//        String[] assignments = new String[seatsPerBench];
//        
//        if (departments.size() == 1) {
//            Arrays.fill(assignments, departments.get(0));
//            return assignments;
//        }
//        
//        if (departments.size() == 2 && seatsPerBench == 3) {
//            assignments[0] = departments.get(0);
//            assignments[1] = departments.get(1);
//            assignments[2] = departments.get(0);
//            return assignments;
//        }
//        
//        // Smart rotation to prevent same departments in adjacent columns
//        for (int i = 0; i < seatsPerBench; i++) {
//            int deptIndex = i % departments.size();
//            if (i > 0 && assignments[i-1].equals(departments.get(deptIndex))) {
//                deptIndex = (deptIndex + 1) % departments.size();
//            }
//            assignments[i] = departments.get(deptIndex);
//        }
//        return assignments;
//    }
//
//    private static void fillSeatsWithFormatting(String[][] seating, 
//                                             Map<String, Map<String, Queue<student>>> deptYearGroups,
//                                             String[] colAssignments) {
//        for (int col = 0; col < seating[0].length; col++) {
//            String dept = colAssignments[col];
//            Map<String, Queue<student>> yearQueues = deptYearGroups.get(dept);
//            
//            if (yearQueues == null || yearQueues.isEmpty()) {
//                fillColumnWithEmpty(seating, col);
//                continue;
//            }
//            
//            // Get years in sorted order
//            List<String> years = new ArrayList<>(yearQueues.keySet());
//            Collections.sort(years);
//            
//            int yearIndex = 0;
//            
//            for (int row = 0; row < seating.length; row++) {
//                if (yearQueues.isEmpty()) {
//                    seating[row][col] = formatEmptySeat(col, seating[0].length);
//                    continue;
//                }
//                
//                String currentYear = years.get(yearIndex % years.size());
//                Queue<student> queue = yearQueues.get(currentYear);
//                
//                if (!queue.isEmpty()) {
//                    student s = queue.poll();
//                    seating[row][col] = formatStudentSeat(s, col, seating[0].length);
//                    yearIndex++;
//                } else {
//                    yearQueues.remove(currentYear);
//                    years.remove(currentYear);
//                    if (!years.isEmpty()) {
//                        row--; // Retry with next year
//                        yearIndex = 0;
//                    } else {
//                        seating[row][col] = formatEmptySeat(col, seating[0].length);
//                    }
//                }
//            }
//        }
//    }
//    private static void fillColumnWithEmpty(String[][] seating, int col) {
//        for (int row = 0; row < seating.length; row++) {
//            seating[row][col] = "Empty(---)";
//        }
//    }
//
//    private static String formatStudentSeat(student s, int colIndex, int totalColumns) {
//        String position = getPositionIndicator(colIndex, totalColumns);
//        return String.format("%s %s", s.getRollNo(), position);
//    }
//    
//    private static String formatEmptySeat(int colIndex, int totalColumns) {
//        return String.format("Empty(---) %s", getPositionIndicator(colIndex, totalColumns));
//    }
//    private static String getPositionIndicator(int colIndex, int totalColumns) {
//        if (totalColumns == 1) return "M";
//        if (totalColumns == 2) return (colIndex == 0) ? "L" : "R";
//        if (colIndex == 0) return "L";
//        if (colIndex == totalColumns - 1) return "R";
//        return "M";
//    }
//
//    public static String generateReportHeader(List<student> students, List<String> departments) {
//        return String.format(
//            "SEATING ARRANGEMENT REPORT\n" +
//            "=========================\n" +
//            "Departments: %s\n" +
//            "Total students: %d\n",
//            String.join(", ", departments),
//            students.size()
//        );
//    }
//}


//Great
package SeatAllocator;

import java.util.*;

public class SeatingLogic {
    
    public static String[][] generateSeatingArrangement(List<student> students, int benchCount, int maxPerBench) {
        // Validate input
        if (students == null || students.isEmpty() || benchCount <= 0 || maxPerBench <= 0) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        // Sort students by year first, then roll number
        students.sort((s1, s2) -> {
            int yearCompare = s1.getYear().compareTo(s2.getYear());
            if (yearCompare != 0) return yearCompare;
            return s1.getRollNo().compareTo(s2.getRollNo());
        });

        // Group students by year
        Map<String, Queue<student>> yearGroups = new LinkedHashMap<>();
        for (student s : students) {
            yearGroups.computeIfAbsent(s.getYear(), _ -> new LinkedList<>()).add(s);
        }

        // Calculate seating dimensions
        int seatsPerBench = Math.min(maxPerBench, 3); // Max 3 columns (L/M/R)
        String[][] seating = new String[benchCount][seatsPerBench];

        // Fill columns with year separation
        fillSeatsWithYearSeparation(seating, yearGroups);

        return seating;
    }

    private static void fillSeatsWithYearSeparation(String[][] seating, 
                                                  Map<String, Queue<student>> yearGroups) {
        List<String> years = new ArrayList<>(yearGroups.keySet());
        if (years.size() == 1) {
            // Single year - fill sequentially
            Queue<student> students = yearGroups.get(years.get(0));
            fillColumnWise(seating, students);
        } else {
            // Multiple years - alternate between columns
            for (int col = 0; col < seating[0].length; col++) {
                String year = years.get(col % years.size());
                Queue<student> queue = yearGroups.get(year);
                
                for (int row = 0; row < seating.length; row++) {
                    if (queue != null && !queue.isEmpty()) {
                        student s = queue.poll();
                        seating[row][col] = formatStudentSeat(s, col, seating[0].length);
                    } else {
                        seating[row][col] = formatEmptySeat(col, seating[0].length);
                    }
                }
            }
        }
    }

    private static void fillColumnWise(String[][] seating, Queue<student> students) {
        for (int col = 0; col < seating[0].length; col++) {
            for (int row = 0; row < seating.length; row++) {
                if (!students.isEmpty()) {
                    student s = students.poll();
                    seating[row][col] = formatStudentSeat(s, col, seating[0].length);
                } else {
                    seating[row][col] = formatEmptySeat(col, seating[0].length);
                }
            }
        }
    }

    private static String formatStudentSeat(student s, int colIndex, int totalColumns) {
        return String.format("%s %s", s.getRollNo(), getPositionIndicator(colIndex, totalColumns));
    }

    private static String formatEmptySeat(int colIndex, int totalColumns) {
        return String.format("Empty %s", getPositionIndicator(colIndex, totalColumns));
    }

    private static String getPositionIndicator(int colIndex, int totalColumns) {
        if (totalColumns == 1) return "M";
        if (totalColumns == 2) return (colIndex == 0) ? "L" : "R";
        if (colIndex == 0) return "L";
        if (colIndex == totalColumns - 1) return "R";
        return "M";
    }
}