/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.model;

/**
 *
 * @author ryanfletcher
 */
public class Student extends Person {

    // ----- fields -----
    //private String firstName;
    //private String lastName;
    //private int age;
    //private String address;
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
        super(firstName, lastName, age, address);
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
        super(firstName, lastName, age, address);
        this.id = id;
        this.course = course;
        this.yearOfStudy = yearOfStudy;
        this.graduated = graduated;
        this.major = major;
        this.yearOfEnrollment = yearOfEnrollment;
        this.yearOfGraduation = (yearOfGraduation == 0) ? null : yearOfGraduation;
    }

    // ----- getters -----
    public int getId() {
        return id;
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
                + "\nAddress: " + getAddress()
                + "\nCourse: " + course
                + "\nMajor: " + major
                + "\nYear of Study: " + yearOfStudy
                + "\nYear of Enrollment: " + yearOfEnrollment
                + "\nYear of Graduation: " + yearOfGraduation
                + "\nGraduated: " + graduated;
    }
}