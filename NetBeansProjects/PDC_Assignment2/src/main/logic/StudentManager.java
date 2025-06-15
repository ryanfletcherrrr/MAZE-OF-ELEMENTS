/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.logic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.database.CourseDAO;
import main.database.DBManager;
import main.database.StudentCourseDAO;
import main.database.StudentDAO;
import main.model.Course;
import main.model.Student;

/**
 *
 * @author ryanfletcher
 */
public class StudentManager {
    // This class replaces InfoSystem class

    // This is the CONTROLLER between the GUI and BUSINESS LOGIC
    // Here are the methods required (minimum):
    // - Map<Integer, Student> getAllStudents()                                     DONE
    // - Student findStudentById(int id)                                            DONE
    // - boolean addStudent(Student student)                                        DONE
    // - boolean updateStudent(int id, student updatedStudent)                      DONE
    // - boolean removeStudent(int id)                                              DONE
    // Here are some optional methods for filtering     
    // - List<Student> findStudentByName(String name)
    // - List<Student> findStudentByCourse(String course)
    // First, initialize the StudentDAO so we can use data from the database
    private StudentDAO studentDAO;
    private StudentCourseDAO studentCourseDAO;
    private CourseDAO courseDAO;
    private Map<Integer, Student> students;

    // Initialise on start-up
    public StudentManager() {
        try {
            Connection conn = DBManager.getConnection();
            studentDAO = new StudentDAO(conn);
            studentCourseDAO = new StudentCourseDAO(conn);
            courseDAO = new CourseDAO(conn);
            students = studentDAO.loadStudents();
        } catch (SQLException e) {
            e.printStackTrace();
            students = new HashMap<>();
        }
    }

    // Get Course objects for a student
    public List<Course> getCoursesForStudentObjects(int studentId) {
        List<Integer> courseIds = getCoursesForStudent(studentId);
        List<Course> courses = new ArrayList<>();
        for (int id : courseIds) {
            Course c = courseDAO.findCourseById(id);
            if (c != null) {
                courses.add(c);
            }
        }
        return courses;
    }

    // Getter Methods
    public List<Integer> getCoursesForStudent(int studentId) {
        try {
            return studentCourseDAO.getCoursesForStudent(studentId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Integer> getStudentsForCourse(int courseId) {
        try {
            return studentCourseDAO.getStudentsForCourse(courseId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Send Student Map (DB information used by business logic)
    public Map<Integer, Student> getAllStudents() {
        return students;
    }

    // Send Course Map
    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    // Find student among all students
    public Student findStudentById(int id) {
        return students.get(id);
    }

    // Add a new student
    public boolean addStudent(Student student, List<Integer> courseIds) throws SQLException {
        boolean result = studentDAO.saveStudent(student);
        if (result) {
            students.put(student.getStudentId(), student);
            for (int courseId : courseIds) {
                studentCourseDAO.enrollStudentInCourse(student.getStudentId(), courseId);
            }
        }
        return result;
    }

    // Update an existing student
    public boolean updateStudent(int id, Student updatedStudent, List<Integer> newCourseIds) {
        boolean result = studentDAO.saveStudent(updatedStudent);
        if (result) {
            students.put(id, updatedStudent);
            updateStudentCourses(id, newCourseIds);
        }
        return result;
    }

    private void updateStudentCourses(int studentId, java.util.List<Integer> newCourseIds) {
        try {
            java.util.List<Integer> oldCourseIds = studentCourseDAO.getCoursesForStudent(studentId);
            for (int courseId : oldCourseIds) {
                if (!newCourseIds.contains(courseId)) {
                    studentCourseDAO.unenrollStudentFromCourse(studentId, courseId);
                }
            }
            for (int courseId : newCourseIds) {
                if (!oldCourseIds.contains(courseId)) {
                    studentCourseDAO.enrollStudentInCourse(studentId, courseId);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    // Remove an existing student
    public boolean removeStudent(int id) {
        List<Integer> courseIds;
        try {
            courseIds = studentCourseDAO.getCoursesForStudent(id);
            for (int courseId : courseIds) {
                studentCourseDAO.unenrollStudentFromCourse(id, courseId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        boolean result = studentDAO.removeStudent(id);
        if (result) {
            students.remove(id);
        }
        return result;
    }

    // Find any student by name
    public Map<Integer, Student> findStudentByName(String name) {
        Map<Integer, Student> foundStudents = new HashMap<>();

        for (Map.Entry<Integer, Student> entry : students.entrySet()) {
            Student student = entry.getValue();
            if (student.getFirstName().equalsIgnoreCase(name)) {
                foundStudents.put(entry.getKey(), student);
            }
        }

        return foundStudents;
    }

    // Find all students enrolled in a course
    public List<Student> findStudentsByCourse(int courseId) throws SQLException {
        List<Integer> studentIds = studentCourseDAO.getStudentsForCourse(courseId);
        List<Student> studentsInCourse = new ArrayList<>();
        for (int id : studentIds) {
            Student student = students.get(id);
            if (student != null) {
                studentsInCourse.add(student);
            }
        }
        return studentsInCourse;
    }

   
    
    // Close the database; should be proceeded by exiting the program
    public void close() {
        DBManager.closeConnection();
    }
}
