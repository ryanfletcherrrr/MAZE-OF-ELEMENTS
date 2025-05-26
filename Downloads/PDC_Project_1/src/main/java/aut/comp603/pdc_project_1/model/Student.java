/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aut.comp603.pdc_project_1.model;
import aut.comp603.pdc_project_1.logic.Person;

/**
 *
 * @author ryanfletcher
 */
public class Student {

    // ----- fields -----
    
    private final Person person;
    private final int id;
    private String course;
    private int yearOfStudy;
    private boolean graduated;
    private String major;
    private int yearOfEnrollment;
    private Integer yearOfGraduation;

    // ----- constructors -----
    // Primary Use: Existing Students, Saving and Loading to HashMap or File
    public Student(String firstName, String lastName, int age, int id, String address,
            String course, int yearOfStudy, boolean graduated, String major,
            int yearOfEnrollment, Integer yearOfGraduation) {
        
        this.person = new Person(firstName, lastName, age, address);
        this.id = id;
        this.course = course;
        this.yearOfStudy = yearOfStudy;
        this.graduated = graduated;
        this.major = major;
        this.yearOfEnrollment = yearOfEnrollment;
        this.yearOfGraduation = yearOfGraduation;
    }

    // Primary Use: New Students
    public Student(String firstName, String lastName, int age, int id, String address,
            String course, int yearOfStudy, boolean graduated, String major,
            int yearOfEnrollment, int yearOfGraduation) {
        
        this.id = id;
        this.course = course;
        this.yearOfStudy = yearOfStudy;
        this.graduated = graduated;
        this.major = major;
        this.yearOfEnrollment = yearOfEnrollment;
        this.yearOfGraduation = (yearOfGraduation == 0) ? null : yearOfGraduation;
        this.person = null;
    }

    // ----- methods -----
    // Primary Use: Generate random Id for new student.
    // NOTE: A new student must have a UNIQUE Id. 
    // Therefore, generating a new student should be iterated through.
    // This is to be handled by StudentHandler
    private int generateId(int enrollmentYear) {
        int random = (int) (Math.random() * 10000);
        return enrollmentYear * 10000 + random;
    }

    // ----- getters -----
    public String getFirstName() {
        return person.getFirstName();
    }


    public String getLastName() {
        return person.getLastName();
    }

    public int getAge() {
        return person.getAge();
    }


    public int getId() {
        return id;
    }

    
    public String getAddress() {
        return person.getAddress();
    }


    public String getCourse() {
        return course;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public boolean isGraduated() {
        return graduated;
    }

    public String getMajor() {
        return major;
    }

    public int getYearOfEnrollment() {
        return yearOfEnrollment;
    }

    public Integer getYearOfGraduation() {
        return yearOfGraduation;
    }

    // ----- setters -----
    public void setFirstName(String firstName) {
        person.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        person.setLastName(lastName);
    }
 
    public void setAge(int age) {
        if (age >= 16 && age <= 65) {
            person.setAge(age);
        }
    }
    
    public void setAddress(String address) {
        person.setAddress(address);
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public void setGraduated(boolean graduated) {
        this.graduated = graduated;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setYearOfEnrollment(int yearOfEnrollment) {
        this.yearOfEnrollment = yearOfEnrollment;
    }

    public void setYearOfGraduation(int yearOfGraduation) {
        this.yearOfGraduation = yearOfGraduation;
    }

    // ----- toString -----
    @Override
    public String toString() {
        return "ID Number: " + id
                + "\nLast Name: " + getLastName()
                + "\nFirst Name: " + getFirstName()
                + "\nAge: " + getAge()
                + "\nCourse: " + course
                + "\nMajor: " + major
                + "\nYear of Study: " + yearOfStudy
                + "\nYear of Enrollment: " + yearOfEnrollment
                + "\nYear of Graduation: " + yearOfGraduation
                + "\nGraduated: " + graduated
                + "\nAddress: " + getAddress();
    }
}
