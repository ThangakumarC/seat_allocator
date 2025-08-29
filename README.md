# Exam Seating Arrangement System

This is a Java Swing application for managing and generating exam seating arrangements for students. The app allows administrators to manage student and hall data, and automatically generate seating plans based on selected departments, years, halls, and bench configurations.

## Features

- **Student Management**: Add, edit, delete, and view students.
- **Hall Management**: Add, edit, delete, and view halls (with bench counts).
- **Seating Generator**: Select departments, years, halls, and max students per bench to generate seating arrangements automatically.
- **Printable Seating Plan**: View and print the generated seating plan.

## How to Run

1. **Prerequisites**:  
   - Java JDK 8 or above
   - Database setup (make sure `DatabaseManager` and required database are correctly configured)

2. **Build & Run**:  
   - Clone the repository:
     ```
     git clone https://github.com/ThangakumarC/seat_allocator.git
     ```
   - Compile and run:
     ```
     javac -cp . ExamSeatingApp.java
     java SeatAllocator.ExamSeatingApp
     ```

## Project Structure

- `ExamSeatingApp.java`: Main GUI application.
- `DatabaseManager.java`: Handles database operations (students, halls).
- `student.java`: Student entity class.
- `hall.java`: Hall entity class.
- `SeatingLogic.java`: Seating arrangement logic.

## Usage

- Start the app, use the tabs for managing students and halls.
- Use the "Seating Generator" tab to select criteria and generate seating arrangement.
- The result can be printed or copied.

## Notes

- This is a basic Swing desktop application.  
- Make sure the database connection is set up before running.
- For customization or improvements, see the code comments and modular structure.

## License

MIT
