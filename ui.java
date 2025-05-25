package SeatAllocator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

public class ui {
    private JFrame mainFrame;
    private JTabbedPane tabbedPane;
    private jdbc db;

    public ui() {
        db = new jdbc();
        createUI();
    }
    
    private void createUI() {
        mainFrame = new JFrame("Exam Seating Arrangement System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Generate Seating", createGeneratorPanel());
        tabbedPane.addTab("Student Management", createStudentPanel());
        tabbedPane.addTab("Hall Management", createHallPanel());
        
        mainFrame.add(tabbedPane);
        mainFrame.setVisible(true);
    }
    
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable studentTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Student");
        JButton editButton = new JButton("Edit Student");
        JButton deleteButton = new JButton("Delete Student");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        refreshStudentTable(studentTable); // load students on init

        addButton.addActionListener(_ -> {
            showStudentDialog("Add", null);
            refreshStudentTable(studentTable);
        });

        editButton.addActionListener(_ -> {
            int selected = studentTable.getSelectedRow();
            if (selected != -1) {
                String roll = studentTable.getValueAt(selected, 0).toString();
                String name = studentTable.getValueAt(selected, 1).toString();
                String dept = studentTable.getValueAt(selected, 2).toString();
                String year = studentTable.getValueAt(selected, 3).toString();              
                showStudentDialog("Edit", new student(roll, name, dept , year));
                refreshStudentTable(studentTable);
            }
        });

        deleteButton.addActionListener(_ -> {
            int selected = studentTable.getSelectedRow();
            if (selected != -1) {
                String roll = studentTable.getValueAt(selected, 0).toString();
                db.deleteStudent(roll);
                refreshStudentTable(studentTable);
            }
        });

        return panel;
    }

    private JPanel createHallPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Similar structure to student panel
        // Implement hall management UI here
        
        return panel;
    }
    
    private JPanel createGeneratorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        // Department selection
        JComboBox<String> deptCombo = new JComboBox<>();
		
		  db.getDepartments().forEach(deptCombo::addItem);
		 
        // Bench count
        JSpinner benchSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        
        // Max per bench
        JSpinner maxPerBenchSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 3, 1));
        
        // Results area
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        
        // Generate button
        JButton generateBtn = new JButton("Generate Seating");
        generateBtn.addActionListener(_ -> {
            String department = (String) deptCombo.getSelectedItem();
			int benchCount = (int) benchSpinner.getValue();
			int maxPerBench = (int) maxPerBenchSpinner.getValue();
			
			List<student> students = db.getStudentsByDepartment(department);
			String[][] seating = logic.generateSeatingArrangement(students, benchCount, maxPerBench);
            
			// Display results
			StringBuilder sb = new StringBuilder();
			for (String[] row : seating) {
			    for (String seat : row) {
			        sb.append(seat).append("\t");
			    }
			    sb.append("\n");
			}
			resultArea.setText(sb.toString());
        });
        
        // Add components to input panel
        inputPanel.add(new JLabel("Department:"));
        inputPanel.add(deptCombo);
        inputPanel.add(new JLabel("Bench Count:"));
        inputPanel.add(benchSpinner);
        inputPanel.add(new JLabel("Max per Bench:"));
        inputPanel.add(maxPerBenchSpinner);
        inputPanel.add(new JLabel(""));
        inputPanel.add(generateBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void showStudentDialog(String type, student s) {
        JTextField rollField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField yearField = new JTextField();

        if (s != null) {
            rollField.setText(s.getRollNo());
            nameField.setText(s.getName());
            deptField.setText(s.getDepartment());
            yearField.setText(s.getYear());
            if (type.equals("Edit")) rollField.setEditable(false); // prevent editing roll number
        }

        Object[] fields = {
            "Roll No:", rollField,
            "Name:", nameField,
            "Department:", deptField,
            "Year:",yearField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, type + " Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            student newStudent = new student(
                rollField.getText().trim(),
                nameField.getText().trim(),
                deptField.getText().trim(),
                yearField.getText().trim()
            );

            if (type.equals("Add")) {
                db.addStudent(newStudent);
            } else if (type.equals("Edit")) {
                db.updateStudent(newStudent);
            }
        }
    }

    private void refreshStudentTable(JTable table) {
        List<student> students = db.getAllStudents();
        String[] columns = {"Roll No", "Name", "Department"};
        String[][] data = new String[students.size()][3];

        for (int i = 0; i < students.size(); i++) {
            student s = students.get(i);
            data[i][0] = s.getRollNo();
            data[i][1] = s.getName();
            data[i][2] = s.getDepartment();
            data[i][3] = s.getYear();
        }

        table.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }


    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ui());
    }
}