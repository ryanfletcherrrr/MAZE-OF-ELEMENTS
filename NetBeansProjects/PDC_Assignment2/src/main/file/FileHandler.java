/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.file;

import java.util.Map;
import main.model.Student;

/**
 *
 * @author briancobcroft
 */
public interface FileHandler {
    Map<Integer, Student> load();
    void save(Map<Integer, Student> student);
}
