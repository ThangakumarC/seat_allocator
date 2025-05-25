// File: SeatAllocator/DatabaseManager.java
package SeatAllocator;

import java.sql.*;
import java.util.*;

public class jdbc {

    private Connection con;

    public jdbc() {
        String url = "jdbc:mysql://localhost:3306/seating_db";
        String user = "root";
        String password = "tk@2005";

        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getDepartments() {
        List<String> departments = new ArrayList<>();
        String query = "SELECT DISTINCT dept FROM student";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                departments.add(rs.getString("dept"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }

    public List<student> getStudentsByDepartment(String department) {
        List<student> students = new ArrayList<>();
        String query = "SELECT * FROM student WHERE dept = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, department);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(new student(
                    rs.getString("rollno"),
                    rs.getString("sname"),
                    rs.getString("dept"),
                    rs.getString("syear")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
    public void addStudent(student s) {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO student VALUES (?, ?, ?)");
            ps.setString(1, s.getRollNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getDepartment());
            ps.setString(4, s.getYear());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(student s) {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE student SET sname = ?, dept = ? , syear= ? WHERE rollno = ?");
            ps.setString(1, s.getName());
            ps.setString(2, s.getDepartment());
            ps.setString(3, s.getRollNo());
            ps.setString(4, s.getYear());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(String rollNo) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM student WHERE rollno = ?");
            ps.setString(1, rollNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<student> getAllStudents() {
        List<student> students = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM student");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                students.add(new student(rs.getString("rollno"), rs.getString("sname"), rs.getString("dept") , rs.getString("syear")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    // Add more methods as needed (e.g., addStudent, deleteStudent, etc.)
}
