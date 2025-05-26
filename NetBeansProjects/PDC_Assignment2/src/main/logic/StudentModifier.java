/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.logic;

import java.util.Map;
import main.model.Student;

/**
 *
 * @author ryanfletcher
 */
public class StudentModifier {
    // ----- fields -----
    private final IdGeneration idGen = null;

    // ----- constructor -----
   

    public StudentModifier() {
        
    }

    // ----- methods -----
    public Student findStudent(Map<Integer, Student> students, int id) {
        return students.get(id);
    }

    public boolean addStudent(Map<Integer, Student> students, Student student) {
        while (students.containsKey(student.getId())) {
            int newId = idGen.generateUniqueId(student.getYearOfEnrollment());
            student = new Student(
                    student.getFirstName(),
                    student.getLastName(),
                    student.getAge(),
                    newId,
                    student.getAddress(),
                    student.getCourse(),
                    student.getYearOfStudy(),
                    student.isGraduated(),
                    student.getMajor(),
                    student.getYearOfEnrollment(),
                    student.getYearOfGraduation() == null ? 0 : student.getYearOfGraduation()
            );
        }
        students.put(student.getId(), student);
        return true;
    }

    public boolean updateStudent(Map<Integer, Student> students, int id, Student updatedStudent) {
        if (students.containsKey(id)) {
            students.put(id, updatedStudent);
            return true;
        }
        return false;
    }

    public boolean removeStudent(Map<Integer, Student> students, int id) {
        return students.remove(id) != null;
    }
}
