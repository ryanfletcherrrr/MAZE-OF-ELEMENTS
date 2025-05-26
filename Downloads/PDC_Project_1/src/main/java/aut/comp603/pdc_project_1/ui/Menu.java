/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aut.comp603.pdc_project_1.ui;

import java.util.Scanner;

/**
 *
 * @author briancobcroft
 */
public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private InfoPrinter printer = new InfoPrinter();

    // ----- methods -----
    public int getMenuOption() {
        while (true) {
            printer.printPrompt("Select option: ");
            String input = scanner.nextLine().trim();
            try {
                int option = Integer.parseInt(input);
                if (option >= 1 && option <= 5) {
                    return option;
                } else {
                    printer.printMessage("Invalid option. Enter 1-5.");
                }
            } catch (NumberFormatException e) {
                printer.printMessage("Invalid input. Enter a number.");
            }
        }
    }
}
