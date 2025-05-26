/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.ui;

/**
 *
 * @author ryanfletcher
 */
public class MenuPrinter {

    // ----- methods -----
    private final ConsolePrinter printer;

    public MenuPrinter(ConsolePrinter printer) {
        this.printer = printer;
    }

    public void printMenu() {
        printer.printMessage("Student Information Management System:");
        printer.printMessage("1 - View Students");
        printer.printMessage("2 - Find Student");
        printer.printMessage("3 - Add Student");
        printer.printMessage("4 - Update Student Information");
        printer.printMessage("5 - Remove Student");
        printer.printMessage("0 - Exit");
    }
}
