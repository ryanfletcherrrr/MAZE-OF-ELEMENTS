/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aut.comp603.pdc_project_1.logic;

import aut.comp603.pdc_project_1.model.Student;
import java.util.Map;

/**
 *
 * @author briancobcroft
 */
public class StudentModifier {

    // ----- methods -----
    public void viewAllStudents(Map<Integer, Student> students) {
        new aut.comp603.pdc_project_1.ui.InfoPrinter().printStudents(students);
    }

    public Student findStudent(Map<Integer, Student> students, int id) {
        return students.get(id);
    }

    public boolean addStudent(Map<Integer, Student> students, Student student) {
        while (students.containsKey(student.getId())) {
            int newId = generateUniqueId(student.getYearOfEnrollment());
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
        } else {
            return false;
        }
    }

    public boolean removeStudent(Map<Integer, Student> students, int id) {
        if (students.containsKey(id)) {
            students.remove(id);
            return true;
        } else {
            return false;
        }
    }

    // ----- helper methods -----
    private int generateUniqueId(int enrollmentYear) {
        int random = (int) (Math.random() * 10000);
        return enrollmentYear * 10000 + random;
    }
}
