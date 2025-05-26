/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aut.comp603.pdc_project_1.logic;

import aut.comp603.pdc_project_1.model.Student;
import aut.comp603.pdc_project_1.ui.InfoPrinter;
import java.util.Scanner;

/**
 *
 * @author briancobcroft
 */
public class StudentHandler {

    private Scanner scanner = new Scanner(System.in);
    private InfoPrinter printer = new InfoPrinter();

    // ----- methods -----
    public Student createStudent() {
        String firstName = inputString("Enter First Name: ");
        if (firstName == null) {
            return null;
        }

        String lastName = inputString("Enter Last Name: ");
        if (lastName == null) {
            return null;
        }

        int age = inputInt("Enter Age: ");
        if (age == -1) {
            return null;
        }

        String address = inputString("Enter Address: ");
        if (address == null) {
            return null;
        }

        String course = inputString("Enter Course: ");
        if (course == null) {
            return null;
        }

        int yearOfStudy = inputInt("Enter Year of Study: ");
        if (yearOfStudy == -1) {
            return null;
        }

        boolean graduated = inputBoolean("Has Graduated? (yes/no): ");
        // no null return for boolean

        String major = inputString("Enter Major: ");
        if (major == null) {
            return null;
        }

        int yearOfEnrollment = inputInt("Enter Year of Enrollment: ");
        if (yearOfEnrollment == -1) {
            return null;
        }

        int yearOfGraduation = inputInt("Enter Year of Graduation (0 if not graduated): ");
        if (yearOfGraduation == -1) {
            return null;
        }

        int id = generateUniqueId(yearOfEnrollment);

        return new Student(firstName, lastName, age, id, address, course, yearOfStudy, graduated, major, yearOfEnrollment, yearOfGraduation == 0 ? null : yearOfGraduation);
    }

    public Student editStudent(Student student) {
        String firstName = inputString("Enter New First Name (leave empty to keep current): ");
        if (firstName == null) {
            return null;
        }
        if (!firstName.isEmpty()) {
            student.setFirstName(firstName);
        }

        String lastName = inputString("Enter New Last Name (leave empty to keep current): ");
        if (lastName == null) {
            return null;
        }
        if (!lastName.isEmpty()) {
            student.setLastName(lastName);
        }

        int age = inputInt("Enter New Age (0 to keep current): ");
        if (age == -1) {
            return null;
        }
        if (age != 0) {
            student.setAge(age);
        }

        String address = inputString("Enter New Address (leave empty to keep current): ");
        if (address == null) {
            return null;
        }
        if (!address.isEmpty()) {
            student.setAddress(address);
        }

        String course = inputString("Enter New Course (leave empty to keep current): ");
        if (course == null) {
            return null;
        }
        if (!course.isEmpty()) {
            student.setCourse(course);
        }

        int yearOfStudy = inputInt("Enter New Year of Study (0 to keep current): ");
        if (yearOfStudy == -1) {
            return null;
        }
        if (yearOfStudy != 0) {
            student.setYearOfStudy(yearOfStudy);
        }

        boolean graduated = inputBoolean("Graduated? (yes/no): ");
        student.setGraduated(graduated);

        String major = inputString("Enter New Major (leave empty to keep current): ");
        if (major == null) {
            return null;
        }
        if (!major.isEmpty()) {
            student.setMajor(major);
        }

        int yearOfEnrollment = inputInt("Enter New Year of Enrollment (0 to keep current): ");
        if (yearOfEnrollment == -1) {
            return null;
        }
        if (yearOfEnrollment != 0) {
            student.setYearOfEnrollment(yearOfEnrollment);
        }

        int yearOfGraduation = inputInt("Enter New Year of Graduation (0 to keep current): ");
        if (yearOfGraduation == -1) {
            return null;
        }
        if (yearOfGraduation != 0) {
            student.setYearOfGraduation(yearOfGraduation);
        }

        return student;
    }

    public int selectStudentId() {
        return inputInt("Enter Student ID: ");
    }

    // ----- helper methods -----
    private String inputString(String prompt) {
        printer.printPrompt(prompt);
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("back")) {
            return null;
        }
        return input;
    }

    private int inputInt(String prompt) {
        while (true) {
            printer.printPrompt(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                return -1;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printer.printMessage("Invalid number. Try again.");
            }
        }
    }

    private boolean inputBoolean(String prompt) {
        while (true) {
            printer.printPrompt(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("back")) {
                return false; // assume default false on cancel
            } else if (input.equals("yes")) {
                return true;
            } else if (input.equals("no")) {
                return false;
            } else {
                printer.printMessage("Invalid input. Please type 'yes' or 'no'.");
            }
        }
    }

    private int generateUniqueId(int enrollmentYear) {
        int random = (int) (Math.random() * 10000);
        return enrollmentYear * 10000 + random;
    }
}
