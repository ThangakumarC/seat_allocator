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
