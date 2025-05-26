/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aut.comp603.pdc_project_1.ui;

import aut.comp603.pdc_project_1.model.Student;
import java.util.Map;

/**
 *
 * @author briancobcroft
 */
public class InfoPrinter {

    // ----- methods -----
    public void printMenu() {
        System.out.println("Student Information Management System:");
        System.out.println("1 - Find Student");
        System.out.println("2 - Add Student");
        System.out.println("3 - Update Student Information");
        System.out.println("4 - Remove Student");
        System.out.println("5 - Exit");
    }

    public void printStudents(Map<Integer, Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student student : students.values()) {
            printStudent(student);
            printLine();
        }
    }

    public void printStudent(Student student) {
        if (student == null) {
            printMessage("Student not found.");
            return;
        }

        System.out.println("ID Number: " + student.getId());
        System.out.println("Last Name: " + student.getLastName());
        System.out.println("First Name: " + student.getFirstName());
        System.out.println("Age: " + student.getAge());
        System.out.println("Course: " + student.getCourse());
        System.out.println("Major: " + student.getMajor());
        System.out.println("Year of Study: " + student.getYearOfStudy());
        System.out.println("Year of Enrollment: " + student.getYearOfEnrollment());
        System.out.println("Year of Graduation: "
                + (student.getYearOfGraduation() == null ? "N/A" : student.getYearOfGraduation()));
        System.out.println("Graduated: " + (student.isGraduated() ? "Yes" : "No"));
        System.out.println("Address: " + student.getAddress());
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printPrompt(String prompt) {
        System.out.print(prompt);
    }

    public void printSection(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }

    public void printLine() {
        System.out.println("----------------------------");
    }

}
