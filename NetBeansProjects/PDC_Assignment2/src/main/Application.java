/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.SwingUtilities;
import main.ui.GUI;
//import main.file.FileHandler;
//import main.file.FileManager;
//import main.logic.IdGeneration;
//import main.logic.InfoSystem;
//import main.logic.StudentHandler;
//import main.logic.StudentManager;
//import main.logic.StudentModifier;
//import main.ui.ConsolePrinter;
//import main.ui.InputHandler;
//import main.ui.Menu;
//import main.ui.MenuPrinter;
//import main.ui.StudentPrinter;

/**
 *
 * @author ryanfletcher
 */
public class Application {

    // ----- methods -----
    public static void main(String[] args) {
        /*ConsolePrinter consolePrinter = new ConsolePrinter();
        StudentPrinter studentPrinter = new StudentPrinter(consolePrinter);
        MenuPrinter menuPrinter = new MenuPrinter(consolePrinter);

        InputHandler input = new InputHandler(consolePrinter);

        FileHandler fileManager = new FileManager();
        StudentModifier modifier = new StudentModifier(new IdGeneration());
        StudentHandler handler = new StudentHandler(input);
        Menu menu = new Menu(consolePrinter, input);

        InfoSystem system = new InfoSystem(
                fileManager,
                consolePrinter,
                studentPrinter,
                modifier,
                handler,
                menu,
                menuPrinter
        );

        system.start();
         */
        SwingUtilities.invokeLater(() -> new GUI());
    }
}
