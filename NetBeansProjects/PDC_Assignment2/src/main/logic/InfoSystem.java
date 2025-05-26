/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.logic;


import java.util.Map;
import java.util.HashMap;
import main.file.FileHandler;
import main.model.Student;
import main.ui.ConsolePrinter;
import main.ui.Menu;
import main.ui.MenuPrinter;
import main.ui.StudentPrinter;

/**
 *
 * @author ryanfletcher
 */
public class InfoSystem {

    /**
     * @param args the command line arguments
     */
    // ----- fields -----
    private final FileHandler fileManager;
    private final ConsolePrinter consolePrinter;
    private final StudentPrinter studentPrinter;
    private final StudentModifier modifier;
    private final StudentHandler handler;
    private final Menu menu;
    private final MenuPrinter menuPrinter;
    private Map<Integer, Student> students = new HashMap<>();

    // ----- constructor -----
    public InfoSystem(
            FileHandler fileManager,
            ConsolePrinter consolePrinter,
            StudentPrinter studentPrinter,
            StudentModifier modifier,
            StudentHandler handler,
            Menu menu,
            MenuPrinter menuPrinter) {

        this.fileManager = fileManager;
        this.consolePrinter = consolePrinter;
        this.studentPrinter = studentPrinter;
        this.modifier = modifier;
        this.handler = handler;
        this.menu = menu;
        this.menuPrinter = menuPrinter;
    }

    // ----- methods -----
    public void start() {
        students = fileManager.load();

        boolean running = true;
        while (running) {
            menuPrinter.printMenu();
            int option = menu.getMenuOption();
            running = handleMenuOption(option);
        }

        fileManager.save(students);
    }

    private boolean handleMenuOption(int option) {
        switch (option) {
            case 1 ->
                viewStudents();
            case 2 ->
                findStudent();
            case 3 ->
                addStudent();
            case 4 ->
                updateStudent();
            case 5 ->
                removeStudent();
            case 0 -> {
                return false;
            }
            default ->
                consolePrinter.printMessage("Invalid option.");
        }
        return true;
    }

    private void viewStudents() {
        consolePrinter.printSection("View All Students");
        studentPrinter.printStudents(students);
        consolePrinter.printLine();
    }

    private void findStudent() {
        consolePrinter.printSection("Find Student");
        consolePrinter.printMessage("Type 'back' anytime to cancel.");

        int id = handler.selectStudentId();
        if (id == -1) {
            return;
        }

        Student student = modifier.findStudent(students, id);
        studentPrinter.printStudent(student);
        consolePrinter.printLine();
    }

    private void addStudent() {
        consolePrinter.printSection("Add New Student");
        consolePrinter.printMessage("Type 'back' anytime to cancel.");

        Student newStudent = handler.createStudent();
        if (newStudent == null) {
            return;
        }

        boolean success = modifier.addStudent(students, newStudent);
        if (success) {
            fileManager.save(students);
            consolePrinter.printMessage("Student added successfully.");
        } else {
            consolePrinter.printMessage("Failed to add student.");
        }
        consolePrinter.printLine();
    }

    private void updateStudent() {
        consolePrinter.printSection("Update Existing Student");
        consolePrinter.printMessage("Type 'back' anytime to cancel.");

        int id = handler.selectStudentId();
        if (id == -1) {
            return;
        }

        Student existing = modifier.findStudent(students, id);
        if (existing == null) {
            consolePrinter.printMessage("Student not found.");
            consolePrinter.printLine();
            return;
        }

        Student updated = handler.editStudent(existing);
        if (updated == null) {
            return;
        }

        boolean success = modifier.updateStudent(students, id, updated);
        if (success) {
            fileManager.save(students);
            consolePrinter.printMessage("Student updated successfully.");
        } else {
            consolePrinter.printMessage("Student not found. Update failed.");
        }
        consolePrinter.printLine();
    }

    private void removeStudent() {
        consolePrinter.printSection("Remove Existing Student");
        consolePrinter.printMessage("Type 'back' anytime to cancel.");

        int id = handler.selectStudentId();
        if (id == -1) {
            return;
        }

        boolean success = modifier.removeStudent(students, id);
        if (success) {
            fileManager.save(students);
            consolePrinter.printMessage("Student removed successfully.");
        } else {
            consolePrinter.printMessage("Student not found. Removal failed.");
        }
        consolePrinter.printLine();
    }
}