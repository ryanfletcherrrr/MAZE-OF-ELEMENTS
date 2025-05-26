/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.logic;

import main.model.Student;
import main.ui.InputHandler;

/**
 *
 * @author ryanfletcher
 */
public class StudentHandler {

    // ----- fields -----
    private final InputHandler input = null;
    private final IdGeneration idGen = null;

    // ----- constructor -----
    public StudentHandler() {
        //this.input = input;
        //this.idGen = new IdGeneration();
    }

    // ----- methods -----
    public Student createStudent() {
        String firstName = input.inputString("Enter First Name: ");
        if (firstName == null) {
            return null;
        }

        String lastName = input.inputString("Enter Last Name: ");
        if (lastName == null) {
            return null;
        }

        int age = input.inputInt("Enter Age: ");
        if (age == -1) {
            return null;
        }

        String address = input.inputString("Enter Address: ");
        if (address == null) {
            return null;
        }

        String course = input.inputString("Enter Course: ");
        if (course == null) {
            return null;
        }

        int yearOfStudy = input.inputInt("Enter Year of Study: ");
        if (yearOfStudy == -1) {
            return null;
        }

        boolean graduated = input.inputBoolean("Has Graduated? (yes/no): ");

        String major = input.inputString("Enter Major: ");
        if (major == null) {
            return null;
        }

        int yearOfEnrollment = input.inputInt("Enter Year of Enrollment: ");
        if (yearOfEnrollment == -1) {
            return null;
        }

        int yearOfGraduation = input.inputInt("Enter Year of Graduation (0 if not graduated): ");
        if (yearOfGraduation == -1) {
            return null;
        }

        int id = idGen.generateUniqueId(yearOfEnrollment);

        return new Student(
                firstName, lastName, age, id, address, course,
                yearOfStudy, graduated, major, yearOfEnrollment,
                yearOfGraduation == 0 ? null : yearOfGraduation
        );
    }

    public Student editStudent(Student student) {
        String firstName = input.inputString("Enter New First Name (leave empty to keep current): ");
        if (firstName == null) {
            return null;
        }
        if (!firstName.isEmpty()) {
            student.setFirstName(firstName);
        }

        String lastName = input.inputString("Enter New Last Name (leave empty to keep current): ");
        if (lastName == null) {
            return null;
        }
        if (!lastName.isEmpty()) {
            student.setLastName(lastName);
        }

        int age = input.inputInt("Enter New Age (0 to keep current): ");
        if (age == -1) {
            return null;
        }
        if (age != 0) {
            student.setAge(age);
        }

        String address = input.inputString("Enter New Address (leave empty to keep current): ");
        if (address == null) {
            return null;
        }
        if (!address.isEmpty()) {
            student.setAddress(address);
        }

        String course = input.inputString("Enter New Course (leave empty to keep current): ");
        if (course == null) {
            return null;
        }
        if (!course.isEmpty()) {
            student.setCourse(course);
        }

        int yearOfStudy = input.inputInt("Enter New Year of Study (0 to keep current): ");
        if (yearOfStudy == -1) {
            return null;
        }
        if (yearOfStudy != 0) {
            student.setYearOfStudy(yearOfStudy);
        }

        boolean graduated = input.inputBoolean("Graduated? (yes/no): ");
        student.setGraduated(graduated);

        String major = input.inputString("Enter New Major (leave empty to keep current): ");
        if (major == null) {
            return null;
        }
        if (!major.isEmpty()) {
            student.setMajor(major);
        }

        int yearOfEnrollment = input.inputInt("Enter New Year of Enrollment (0 to keep current): ");
        if (yearOfEnrollment == -1) {
            return null;
        }
        if (yearOfEnrollment != 0) {
            student.setYearOfEnrollment(yearOfEnrollment);
        }

        int yearOfGraduation = input.inputInt("Enter New Year of Graduation (0 to keep current): ");
        if (yearOfGraduation == -1) {
            return null;
        }
        if (yearOfGraduation != 0) {
            student.setYearOfGraduation(yearOfGraduation);
        }

        return student;
    }

    public int selectStudentId() {
        return input.inputInt("Enter Student ID: ");
    }
}
