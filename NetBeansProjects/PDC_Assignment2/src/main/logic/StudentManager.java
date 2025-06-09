/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.logic;

import java.util.Map;
import main.database.DBManager;
import main.model.Student;

/**
 *
 * @author ryanfletcher
 */
public class StudentManager {
    // This class replaces InfoSystem clasa

    // This is the CONTROLLER between the GUI and BUSINESS LOGIC
    // Here are the methods required (minimum):
    // - Map<Integer, Student> getAllStudents()
    // - Student findStudentById(int id)
    // - boolean addStudent(Student student)
    // - boolean updateStudent(int id, student updatedStudent)
    // - boolean removeStudent(int id)
    // Here are some optional methods for filtering
    // - List<Student> findStudentByName(String name)
    // - List<Student> findStudentByCourse(String course)
    // First, initialize the DBManager so we can use data from the database
    private DBManager dbManager;
    private Map<Integer, Student> students;

    // Initialise on start-up
    public StudentManager() {
        dbManager = new DBManager();
        students = dbManager.loadStudents();
    }

    // Send Student Map (DB information used by business logic)
    public Map<Integer, Student> getAllStudents() {
        return students;
    }

    // Find student among all students
    public Student findStudentById(int id) {
        return students.get(id);
    }

    // Add a new student
    public boolean addStudent(Student student) {
        boolean result = dbManager.saveStudent(student);
        if (result) {
            students.put(student.getId(), student);
        }
        return result;
    }
    
    // Update an existing student
    public boolean updateStudent(int id, Student updatedStudent) {
        boolean result = dbManager.saveStudent(updatedStudent);
        if (result) {
            students.put(id, updatedStudent);
        }
        return result;
    }

    // Remove an existing student
    public boolean removeStudent(int id) {
        boolean result = dbManager.removeStudent(id);
        if (result) {
            students.remove(id);
        }
        return result;
    }

    // Close the database; should be proceeeded by exiting the program
    public void close() {
        dbManager.close();
    }
}
