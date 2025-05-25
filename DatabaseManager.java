package SeatAllocator;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private Connection con;

    public DatabaseManager() {
        String url = "jdbc:mysql://localhost:3306/seating_db";
        String user = "root";
        String password = "tk@2005";

        try {
            con = DriverManager.getConnection(url, user, password);
            createTablesIfNotExists();
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    private void createTablesIfNotExists() throws SQLException {
        String createStudentTable = "CREATE TABLE IF NOT EXISTS student (" +
            "rollno VARCHAR(20) PRIMARY KEY, " +
            "sname VARCHAR(100) NOT NULL, " +
            "dept VARCHAR(50) NOT NULL,"+
            "syear VARCHAR(5) NOT NULL)";
        
        String createHallTable = "CREATE TABLE IF NOT EXISTS hall (" +
           "hname VARCHAR(10) PRIMARY KEY ," +
        		"benchcount INT )";

        try (Statement stmt = con.createStatement()) {
            stmt.execute(createStudentTable);
            stmt.execute(createHallTable);
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
            System.err.println("Error fetching departments: " + e.getMessage());
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
                    rs.getString("sname"),
                    rs.getString("rollno"),
                    rs.getString("dept"),
                    rs.getString("syear")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching students: " + e.getMessage());
        }
        return students;
    }
    
    public  List<hall> getAllHalls() throws SQLException {
    	List<hall> halls = new ArrayList<>();
    	 String query = "SELECT * FROM hall";
    	    try (Statement stmt = con.createStatement();
    	         ResultSet rs = stmt.executeQuery(query)) {
    	        while (rs.next()) {
    	            halls.add(new hall(
    	                rs.getString("hname"),
    	                rs.getInt("benchcount")
    	                    ));
    	        }
    	    }
    	    return halls;
    	}

    public hall getHallByName(String name) throws SQLException {
        String query = "SELECT * FROM hall WHERE hname = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new hall(
                    rs.getString("hname"),
                    rs.getInt("benchcount")
                );
            }
        }
        return null;
    }
    
    public void addStudent(student s) throws SQLException {
        String query = "INSERT INTO student VALUES (?, ?, ?, ?)"; 
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, s.getRollNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getDepartment());
            ps.setString(4, s.getYear()); // Added year
            ps.executeUpdate();
        }
    }

    public void updateStudent(student s) throws SQLException {
        String query = "UPDATE student SET sname = ?, dept = ?, syear = ? WHERE rollno = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getDepartment());
            ps.setString(3, s.getYear());
            ps.setString(4, s.getRollNo());
            ps.executeUpdate();
        }
    }

    public void deleteStudent(String rollNo) throws SQLException {
        String query = "DELETE FROM student WHERE rollno = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, rollNo);
            ps.executeUpdate();
        }
    }
    
    // Add to DatabaseManager
    public List<student> getStudentsByDeptAndYear(String department, String year) throws SQLException {
        List<student> students = new ArrayList<>();
        String query = "SELECT * FROM student WHERE dept = ? AND syear = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, department);
            ps.setString(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                students.add(new student(
                    rs.getString("sname"),
                    rs.getString("rollno"),
                    rs.getString("dept"),
                    rs.getString("syear")
                ));
            }
        }
        return students;
    }
    public void updateHall(hall h) throws SQLException {
        String query = "UPDATE hall SET benchcount = ?  WHERE hname = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, h.getBenchCount());
            ps.setString(2, h.getHname());
            ps.executeUpdate();
        }
    }
    
    public void addHall(hall h) throws SQLException {
        String query = "INSERT INTO hall VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, h.getHname());
            ps.setInt(2, h.getBenchCount());
            ps.executeUpdate();
        }
    }
    

    public void deleteHall(String hname) throws SQLException {
        String query = "DELETE FROM hall WHERE hname = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, hname);
            ps.executeUpdate();
        }
    }
    
    public List<student> getAllStudents() {
        List<student> students = new ArrayList<>();
        String query = "SELECT * FROM student";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                students.add(new student(
                    rs.getString("sname"),
                    rs.getString("rollno"),
                    rs.getString("dept"),
                    rs.getString("syear")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all students: " + e.getMessage());
        }
        return students;
    }

    public void close() {
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}