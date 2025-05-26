/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aut.comp603.pdc_project_1.file;

import aut.comp603.pdc_project_1.model.Student;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author briancobcroft
 */
public class FileManager implements FileHandler {

    private final String filename = "students.txt";

    @Override
    public Map<Integer, Student> load() {
        File file = new File(filename);

        if (!file.exists()) {
            try {
                file.createNewFile(); // Create empty file if missing
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new HashMap<>(); // No file originally, return fresh empty students
        }

        Map<Integer, Student> students = new HashMap<>();

        try ( BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 11) {
                    int id = Integer.parseInt(fields[3]);
                    String firstName = fields[0];
                    String lastName = fields[1];
                    int age = Integer.parseInt(fields[2]);
                    String address = fields[4];
                    String course = fields[5];
                    int yearOfStudy = Integer.parseInt(fields[6]);
                    boolean graduated = Boolean.parseBoolean(fields[7]);
                    String major = fields[8];
                    int yearOfEnrollment = Integer.parseInt(fields[9]);
                    Integer yearOfGraduation = fields[10].equals("null") ? null : Integer.parseInt(fields[10]);

                    Student student = new Student(firstName, lastName, age, id, address, course, yearOfStudy, graduated, major, yearOfEnrollment, yearOfGraduation);
                    students.put(id, student);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public void save(Map<Integer, Student> students) {
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Student student : students.values()) {               
                writer.write(student.getFirstName() + ","
                        + student.getLastName() + ","
                        + student.getAge() + ","
                        + student.getId() + ","
                        + student.getAddress() + ","
                        + student.getCourse() + ","
                        + student.getYearOfStudy() + ","
                        + student.isGraduated() + ","
                        + student.getMajor() + ","
                        + student.getYearOfEnrollment() + ","
                        + (student.getYearOfGraduation() == null ? "null" : student.getYearOfGraduation()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
