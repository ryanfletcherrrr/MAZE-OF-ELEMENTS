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
    private double grade;

    // ----- constructors -----
    // Primary Use: ALL    
    public Student(int id, String lastName, String firstName, int age, String course,
            String major, String address, int yearOfStudy, int yearOfEnrollment,
            boolean graduated, int yearOfGraduation, double grade) {
        super(firstName, lastName, age, address);
        this.id = id;
        this.course = course;
        this.yearOfStudy = yearOfStudy;
        this.graduated = graduated;
        this.major = major;
        this.yearOfEnrollment = yearOfEnrollment;
        this.yearOfGraduation = yearOfGraduation;
        this.grade = grade;
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

    public double getGrade() {
        return grade;
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

    public void setGrade(double grade) {
        this.grade = grade;
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
                + "\nGraduated: " + graduated
                + "\nYear of Graduation: " + yearOfGraduation
                + "\nGrade: " + grade;
    }
}
