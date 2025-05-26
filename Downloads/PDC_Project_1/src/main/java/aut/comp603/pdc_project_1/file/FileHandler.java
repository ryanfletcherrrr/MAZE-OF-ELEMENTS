/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aut.comp603.pdc_project_1.file;

import aut.comp603.pdc_project_1.model.Student;
import java.util.Map;

/**
 *
 * @author briancobcroft
 */
public interface FileHandler {
    Map<Integer, Student> load();
    void save(Map<Integer, Student> student);
}
