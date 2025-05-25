package SeatAllocator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

public class ExamSeatingApp {
    private final DatabaseManager dbManager;
    private JFrame mainFrame;
    private JTabbedPane tabbedPane;
    private HallTableModel hallTableModel; 
    
    public ExamSeatingApp() {
        dbManager = new DatabaseManager();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        mainFrame = new JFrame("Exam Seating Arrangement System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Seating Generator", createSeatingPanel());
        tabbedPane.addTab("Student Management", createStudentPanel());
        try {
			tabbedPane.addTab("Hall Management", createHallPanel());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        mainFrame.add(tabbedPane);
        mainFrame.setVisible(true);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table setup
        StudentTableModel tableModel = new StudentTableModel(dbManager.getAllStudents());
        JTable studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(_ -> showStudentDialog(null));
        
        JButton editButton = new JButton("Edit Selected");
        editButton.addActionListener(_ -> {
            int row = studentTable.getSelectedRow();
            if (row >= 0) {
                showStudentDialog(tableModel.getStudentAt(row));
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a student to edit");
            }
        });
        
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(_ -> {
            int row = studentTable.getSelectedRow();
            if (row >= 0) {
                try {
                    dbManager.deleteStudent(tableModel.getStudentAt(row).getRollNo());
                    tableModel.removeStudent(row);
                } catch (Exception ex) {
                    showError("Error deleting student: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a student to delete");
            }
        });
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(_ -> tableModel.setStudents(dbManager.getAllStudents()));
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createHallPanel() throws SQLException {
    	JPanel panel = new JPanel(new BorderLayout());
        try {
            hallTableModel = new HallTableModel(dbManager.getAllHalls());
        } catch (SQLException e) {
            showError("Failed to load halls: " + e.getMessage());
            hallTableModel = new HallTableModel(new ArrayList<>());
        }
        
    	JTable hallTable = new JTable(hallTableModel);
        JScrollPane scrollPane = new JScrollPane(hallTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Add Hall");
        addButton.addActionListener(_ -> showHallDialog(null));
        
        JButton editButton = new JButton("Edit Selected");
        editButton.addActionListener(_ -> {
            int row = hallTable.getSelectedRow();
            if (row >= 0) {
            	showHallDialog(hallTableModel.getHallAt(row));
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a hall to edit");
            }
        });
        
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(_ -> {
            int row = hallTable.getSelectedRow();
            if (row >= 0) {
                    try {
						dbManager.deleteHall(hallTableModel.getHallAt(row).getHname());
	                    hallTableModel.setHalls(dbManager.getAllHalls());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

            } else {
                JOptionPane.showMessageDialog(panel, "Please select a hall to delete");
            }
        });
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(_ -> {
			try {
				hallTableModel.setHalls(dbManager.getAllHalls());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
        		);
      
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
   
    private void showStudentDialog(student student) {
        JDialog dialog = new JDialog(mainFrame, student == null ? "Add Student" : "Edit Student", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        
        JTextField nameField = new JTextField();
        JTextField rollNoField = new JTextField();
        JTextField deptCombo = new JTextField();
        JTextField yearCombo = new JTextField();

        
        if (student != null) {
            nameField.setText(student.getName());
            rollNoField.setText(student.getRollNo());
            rollNoField.setEditable(false);
            deptCombo.setText((student.getDepartment()));
            yearCombo.setText((student.getYear()));
        }
        
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Roll No:"));
        dialog.add(rollNoField);
        dialog.add(new JLabel("Department:"));
        dialog.add(deptCombo);
        dialog.add(new JLabel("Year:"));
        dialog.add(yearCombo);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> {
            try {
                student newStudent = new student(
                    nameField.getText(),
                    rollNoField.getText(),
                    deptCombo.getText(),
                    yearCombo.getText()
                );
                
                if (student == null) {
                    dbManager.addStudent(newStudent);
                } else {
                    dbManager.updateStudent(newStudent);
                }
                
                JPanel studentPanel = (JPanel) tabbedPane.getComponentAt(1);
                JScrollPane scrollPane = (JScrollPane) studentPanel.getComponent(0);
                JTable studentTable = (JTable) scrollPane.getViewport().getView();
                StudentTableModel model = (StudentTableModel) studentTable.getModel();
                model.setStudents(dbManager.getAllStudents());
                
                dialog.dispose();
            } catch (Exception ex) {
                showError("Error saving student: " + ex.getMessage());
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> dialog.dispose());
        
        dialog.add(saveButton);
        dialog.add(cancelButton);
        
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private void showHallDialog(hall hall) {
        JDialog dialog = new JDialog(mainFrame, hall == null ? "Add Hall" : "Edit Hall", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        
        JTextField nameField = new JTextField();
        JSpinner benchSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        
        if (hall != null) {
            nameField.setText(hall.getHname());
            benchSpinner.setValue(hall.getBenchCount());
        }
        
        dialog.add(new JLabel("Hall Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Bench Count:"));
        dialog.add(benchSpinner);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> {
            try {
                hall newHall = new hall(
                    nameField.getText(),
                    (int) benchSpinner.getValue()
                    );
                
                if (hall == null) {
                    dbManager.addHall(newHall);
                } else {
                    dbManager.updateHall(newHall);
                }
             // Refresh hall table
                JPanel hallPanel = (JPanel) tabbedPane.getComponentAt(2); // Assuming hall tab is index 1
                JScrollPane scrollPane = (JScrollPane) hallPanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                ((HallTableModel)table.getModel()).setHalls(dbManager.getAllHalls());
                
                dialog.dispose();
            } catch (Exception ex) {
                showError("Error saving hall: " + ex.getMessage());
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> dialog.dispose());
        
        dialog.add(saveButton);
        dialog.add(cancelButton);
        
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }
        
    private JPanel createSeatingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        panel.add(contentPanel, BorderLayout.CENTER);
        
        // Input panel with vertical arrangement
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Department selection panel
        JPanel deptPanel = new JPanel(new BorderLayout(5, 5));
        deptPanel.add(new JLabel("Select Departments and Year:"), BorderLayout.NORTH);
        
        JList<String> deptList = new JList<>();
        deptList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        deptList.setVisibleRowCount(4);
        JScrollPane deptScrollPane = new JScrollPane(deptList);
        refreshDepartmentList(deptList);
        
        // Year selection panel
        JPanel yearPanel = new JPanel(new BorderLayout(5, 5));
        yearPanel.add(new JLabel("Select Years:"), BorderLayout.NORTH);
        
        JList<String> yearList = new JList<>(new String[]{"1", "2", "3", "4"});
        yearList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        yearList.setVisibleRowCount(4);
        JScrollPane yearScrollPane = new JScrollPane(yearList);
        
        // Student selection control buttons
        JPanel studentControlPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        studentControlPanel.add(deptScrollPane);
        studentControlPanel.add(yearScrollPane);
        
        JPanel studentButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton refreshStudentsBtn = new JButton("Refresh Departments");
        refreshStudentsBtn.addActionListener(_ -> refreshDepartmentList(deptList));
        
        studentButtonPanel.add(refreshStudentsBtn);
        
        deptPanel.add(studentControlPanel, BorderLayout.CENTER);
        deptPanel.add(studentButtonPanel, BorderLayout.SOUTH);
        inputPanel.add(deptPanel);

        // Hall selection panel
        JPanel hallPanel = new JPanel(new BorderLayout(5, 5));
        hallPanel.add(new JLabel("Select Halls:"), BorderLayout.NORTH);
        
        JList<String> hallList = new JList<>();
        hallList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        hallList.setVisibleRowCount(5);
        JScrollPane hallScrollPane = new JScrollPane(hallList);
        refreshHallList(hallList);
        
        // Hall control buttons
        JPanel hallButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton selectAllHallsBtn = new JButton("Select All");
        JButton deselectAllHallsBtn = new JButton("Deselect All");
        JButton refreshHallsBtn = new JButton("Refresh Halls");
        
        selectAllHallsBtn.addActionListener(_ -> hallList.setSelectionInterval(0, hallList.getModel().getSize()-1));
        deselectAllHallsBtn.addActionListener(_ -> hallList.clearSelection());
        refreshHallsBtn.addActionListener(_ -> refreshHallList(hallList));
        
        hallButtonPanel.add(selectAllHallsBtn);
        hallButtonPanel.add(deselectAllHallsBtn);
        hallButtonPanel.add(refreshHallsBtn);
        
        hallPanel.add(hallScrollPane, BorderLayout.CENTER);
        hallPanel.add(hallButtonPanel, BorderLayout.SOUTH);
        inputPanel.add(hallPanel);
        
        // Bench configuration
        JPanel benchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JSpinner maxPerBenchSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        maxPerBenchSpinner.setPreferredSize(new Dimension(50, 25));
        
        benchPanel.add(new JLabel("Max per Bench:"));
        benchPanel.add(maxPerBenchSpinner);
        inputPanel.add(benchPanel);
        
        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton generateButton = new JButton("Generate Seating");
        JButton printButton = new JButton("Print Seating");
        generateButton.setPreferredSize(new Dimension(150, 30));
        printButton.setPreferredSize(new Dimension(150, 30));
        
        // Results area
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setPreferredSize(new Dimension(800, 400));
        
        generateButton.addActionListener(_ -> {
            try {
                List<String> selectedDepts = deptList.getSelectedValuesList();
                List<String> selectedYears = yearList.getSelectedValuesList();
                List<String> selectedHalls = hallList.getSelectedValuesList().stream()
                    .map(s -> s.split(" ")[0]) // Extract hall names
                    .collect(Collectors.toList());
                    
                int maxPerBench = (int) maxPerBenchSpinner.getValue();
                
                if (selectedDepts.isEmpty()) {
                    showError("Please select at least one department");
                    return;
                }
                
                if (selectedYears.isEmpty()) {
                    showError("Please select at least one year");
                    return;
                }
                
                if (selectedHalls.isEmpty()) {
                    showError("Please select at least one hall");
                    return;
                }
                
                // Get students by selected departments and years
                List<student> students = new ArrayList<>();
                for (String dept : selectedDepts) {
                    for (String year : selectedYears) {
                        students.addAll(dbManager.getStudentsByDeptAndYear(dept, year));
                    }
                }
                
                // Get selected halls
                List<hall> halls = new ArrayList<>();
                int totalBenches = 0;
                Map<String, Integer> hallBenchMap = new LinkedHashMap<>();
                
                for (String hallName : selectedHalls) {
                    hall h = dbManager.getHallByName(hallName);
                    if (h != null) {
                        halls.add(h);
                        totalBenches += h.getBenchCount();
                        hallBenchMap.put(h.getHname(), h.getBenchCount());
                    }
                }
                
                // Generate seating
                String[][] seating = SeatingLogic.generateSeatingArrangement(
                    students, 
                    totalBenches, 
                    maxPerBench
                );
                
                displayMultiHallSeating(resultArea, seating, hallBenchMap, students, selectedDepts);
                
            } catch (Exception ex) {
                showError("Error generating seating: " + ex.getMessage());
            }
        });
        
        printButton.addActionListener(_ -> {
            try {
                resultArea.print();
            } catch (Exception ex) {
                showError("Error printing: " + ex.getMessage());
            }
        });
        
        actionPanel.add(generateButton);
        actionPanel.add(printButton);
        inputPanel.add(actionPanel);
        
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(resultScrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void refreshDepartmentList(JList<String> deptList) {
        try {
            DefaultListModel<String> model = new DefaultListModel<>();
            for (String dept : dbManager.getDepartments()) {
                model.addElement(dept);
            }
            deptList.setModel(model);
        } catch (Exception e) {
            showError("Error loading departments: " + e.getMessage());
        }
    }

    private void refreshHallList(JList<String> hallList) {
        try {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (hall h : dbManager.getAllHalls()) {
                listModel.addElement(h.getHname() + " (" + h.getBenchCount() + " benches)");
            }
            hallList.setModel(listModel);
        } catch (SQLException e) {
            showError("Error loading halls: " + e.getMessage());
        }
    }

	    private void displayMultiHallSeating(JTextArea area, String[][] seating, 
	            Map<String, Integer> hallBenchMap,
	            List<student> students, 
	            List<String> selectedDepts) {
	StringBuilder sb = new StringBuilder();
	
	// Report Header
	sb.append("SEATING ARRANGEMENT REPORT\n");
	sb.append("=========================\n");
	sb.append("Departments: ").append(String.join(", ", selectedDepts)).append("\n");
	sb.append("Total students: ").append(students.size()).append("\n");
	sb.append("Total Benches: ").append(seating.length).append("\n\n");
	
	int benchCounter = 0;
	for (Map.Entry<String, Integer> entry : hallBenchMap.entrySet()) {
	String hallName = entry.getKey();
	int benchesInHall = entry.getValue();
	
	sb.append("HALL: ").append(hallName);
	sb.append("------------------------------------------------\n");
	
	for (int i = 0; i < benchesInHall && benchCounter < seating.length; i++) {
	sb.append("Bench ").append(benchCounter + 1).append(": ");
	for (String seat : seating[benchCounter]) {
	sb.append(String.format("%-25s", seat));
	}
	sb.append("\n");
	benchCounter++;
	}
	sb.append("\n");
	}
	
	area.setText(sb.toString());
	area.setCaretPosition(0);
	}
    private void showError(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExamSeatingApp());
    }
}

class StudentTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<student> students;
    private final String[] columnNames = {"Roll No", "Name", "Department", "Year"};

    public StudentTableModel(List<student> students) {
        this.students = students;
    }

    public void setStudents(List<student> students) {
        this.students = students;
        fireTableDataChanged();
    }

    public student getStudentAt(int row) {
        return students.get(row);
    }

    public void removeStudent(int row) {
        students.remove(row);
        fireTableRowsDeleted(row, row);
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        student student = students.get(rowIndex);
        switch (columnIndex) {
            case 0: return student.getRollNo();
            case 1: return student.getName();
            case 2: return student.getDepartment();
            case 3: return student.getYear();
            default: return null;
        }
    }
}