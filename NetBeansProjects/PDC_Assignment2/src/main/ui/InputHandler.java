/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.ui;

import java.util.Scanner;

/**
 *
 * @author ryanfletcher
 */
public class InputHandler {

    // ----- fields -----
    private final ConsolePrinter printer;
    private final Scanner in;

    // ----- constructor -----
    public InputHandler(ConsolePrinter printer) {
        this.printer = printer;
        this.in = new Scanner(System.in);
    }

    // ----- methods -----
    public String inputString(String prompt) {
        printer.printPrompt(prompt);
        String input = in.nextLine().trim();
        if (input.equalsIgnoreCase("back")) {
            printer.printLine();
            return null;
        }
        return input;
    }

    public int inputInt(String prompt) {
        while (true) {
            printer.printPrompt(prompt);
            String input = in.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                printer.printLine();
                return -1;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printer.printMessage("Invalid number. Try again.");
            }
        }
    }

    public boolean inputBoolean(String prompt) {
        while (true) {
            printer.printPrompt(prompt);
            String input = in.nextLine().trim().toLowerCase();
            if (input.equals("back")) {
                printer.printLine();
                return false;
            } else if (input.equals("yes")) {
                return true;
            } else if (input.equals("no")) {
                return false;
            } else {
                printer.printMessage("Invalid input. Please type 'yes' or 'no'.");
            }
        }
    }
}
