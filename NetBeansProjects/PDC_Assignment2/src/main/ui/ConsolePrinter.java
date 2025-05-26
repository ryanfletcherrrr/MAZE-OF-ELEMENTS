/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.ui;

/**
 *
 * @author ryanfletcher
 */
public class ConsolePrinter {

    // ----- methods -----
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
