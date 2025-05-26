/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.logic;

/**
 *
 * @author ryanfletcher
 */
public class IdGeneration {
    // ----- methods -----
    public int generateUniqueId(int enrollmentYear) {
        int random = (int) (Math.random() * 10000);
        return enrollmentYear * 10000 + random;
    }
}
