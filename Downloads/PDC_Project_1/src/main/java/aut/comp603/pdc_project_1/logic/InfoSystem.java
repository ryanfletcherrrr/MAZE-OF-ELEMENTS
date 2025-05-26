/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aut.comp603.pdc_project_1.logic;

import aut.comp603.pdc_project_1.file.FileManager;
import aut.comp603.pdc_project_1.model.Student;
import aut.comp603.pdc_project_1.ui.InfoPrinter;
import aut.comp603.pdc_project_1.ui.Menu;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ryanfletcher
 */
public class InfoSystem {

    /**
     * @param args the command line arguments
     */
    private Map<Integer, Student> students = new HashMap<>();
    private FileManager fileHandler = new FileManager();
    private InfoPrinter printer = new InfoPrinter();
    private StudentModifier modifier = new StudentModifier();
    private StudentHandler handler = new StudentHandler();
    private Menu menu = new Menu();

    public void start() {
        students = fileHandler.load();

        boolean running = true;
        while (running) {
            printer.printMenu();
            int option = menu.getMenuOption();
            running = handleMenuOption(option);
        }

        fileHandler.save(students);
    }

    private boolean handleMenuOption(int option) {
        switch (option) {
            case 1:
                findStudent();
                break;
            case 2:
                addStudent();
                break;
            case 3:
                updateStudent();
                break;
            case 4:
                removeStudent();
                break;
            case 5:
                return false;
            default:
                printer.printMessage("Invalid option.");
        }
        return true;
    }

    private void findStudent() {
        printer.printSection("Find Student");
        printer.printMessage("Type 'back' anytime to cancel.");

        int id = handler.selectStudentId();
        if (id == -1) {
            printer.printLine();
            return;
        }
        Student student = modifier.findStudent(students, id);
        printer.printStudent(student);
        printer.printLine();
    }

    private void addStudent() {
        printer.printSection("Add New Student");
        printer.printMessage("Type 'back' anytime to cancel.");

        Student newStudent = handler.createStudent();
        if (newStudent == null) {
            printer.printLine();
            return;
        }

        boolean success = modifier.addStudent(students, newStudent);
        if (success) {
            fileHandler.save(students);
            printer.printMessage("Student added successfully.");
        } else {
            printer.printMessage("Failed to add student.");
        }
        printer.printLine();
    }

    private void updateStudent() {
        printer.printSection("Update Existing Student");
        printer.printMessage("Type 'back' anytime to cancel.");

        int id = handler.selectStudentId();
        if (id == -1) {
            printer.printLine();
            return;
        }

        Student existing = modifier.findStudent(students, id);
        if (existing == null) {
            printer.printMessage("Student not found.");
            printer.printLine();
            return;
        }

        Student updated = handler.editStudent(existing);
        if (updated == null) {
            printer.printLine();
            return;
        }

        boolean success = modifier.updateStudent(students, id, updated);
        if (success) {
            fileHandler.save(students);
            printer.printMessage("Student updated successfully.");
        } else {
            printer.printMessage("Student not found. Update failed.");
        }
        printer.printLine();
    }

    private void removeStudent() {
        printer.printSection("Remove Existing Student");
        printer.printMessage("Type 'back' anytime to cancel.");

        int id = handler.selectStudentId();
        if (id == -1) {
            printer.printLine();
            return;
        }

        boolean success = modifier.removeStudent(students, id);
        if (success) {
            fileHandler.save(students);
            printer.printMessage("Student removed successfully.");
        } else {
            printer.printMessage("Student not found. Removal failed.");
        }
        printer.printLine();
    }
}
