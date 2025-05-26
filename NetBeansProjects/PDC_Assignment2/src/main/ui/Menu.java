/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.ui;

/**
 *
 * @author ryanfletcher
 */
public class Menu {

    // ----- fields -----
    private final ConsolePrinter printer;
    private final InputHandler input;

    // ----- constructor -----
    public Menu(ConsolePrinter printer, InputHandler input) {
        this.printer = printer;
        this.input = input;
    }

    // ----- method -----
    public int getMenuOption() {
        while (true) {
            printer.printPrompt("Select option: ");
            String inputStr = input.inputString("");
            if (inputStr == null) {
                return -1;
            }

            try {
                int option = Integer.parseInt(inputStr);
                if (option >= 0 && option <= 5) {
                    return option;
                } else {
                    printer.printMessage("Invalid option. Enter 0-5.");
                }
            } catch (NumberFormatException e) {
                printer.printMessage("Invalid input. Enter a number.");
            }
        }
    }
}
