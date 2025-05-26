/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.ui;

import java.util.Map;
import main.model.Student;

/**
 *
 * @author ryanfletcher
 */
public class StudentPrinter {

    // ----- fields -----
    private final ConsolePrinter printer;

    // ----- constructor -----
    public StudentPrinter(ConsolePrinter printer) {
        this.printer = printer;
    }

    // ----- methods -----
    public void printStudent(Student student) {
        if (student == null) {
            printer.printMessage("Student not found.");
            return;
        }

        printer.printMessage("ID Number: " + student.getId());
        printer.printMessage("Last Name: " + student.getLastName());
        printer.printMessage("First Name: " + student.getFirstName());
        printer.printMessage("Age: " + student.getAge());
        printer.printMessage("Course: " + student.getCourse());
        printer.printMessage("Major: " + student.getMajor());
        printer.printMessage("Year of Study: " + student.getYearOfStudy());
        printer.printMessage("Year of Enrollment: " + student.getYearOfEnrollment());
        printer.printMessage("Year of Graduation: "
                + (student.getYearOfGraduation() == null ? "N/A" : student.getYearOfGraduation()));
        printer.printMessage("Graduated: " + (student.isGraduated() ? "Yes" : "No"));
        printer.printMessage("Address: " + student.getAddress());
    }

    public void printStudents(Map<Integer, Student> students) {
        if (students.isEmpty()) {
            printer.printMessage("No students found.");
            return;
        }

        printer.printMessage("ID, Full Name, Course, Major, Graduated?");
        for (Student s : students.values()) {
            printer.printMessage(
                    s.getId() + ", "
                    + s.getFirstName() + " " + s.getLastName() + ", "
                    + s.getCourse() + ", "
                    + s.getMajor() + ", "
                    + (s.isGraduated() ? "Y" : "N")
            );
        }
    }
}
