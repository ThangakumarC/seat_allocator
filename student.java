// File: SeatAllocator/Student.java
package SeatAllocator;

public class student {
    private String name;
    private String rollNo;
    private String department;
    private String year;

    public student(String name,String rollNo , String department , String year) {
        this.name = name;
        this.rollNo=rollNo;
        this.department = department;
        this.year=year;
    }

    public String getRollNo() { return rollNo; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    
    @Override
    public String toString() {
    	return String.format("%s - %s (%s)", rollNo, name, department);
    }

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}


