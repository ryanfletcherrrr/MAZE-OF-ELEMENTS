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
    private int studentId;
    private int academicYearLevel;
    private boolean hasGraduated;
    private String course;
    private int enrollmentYear;
    private int graduationYear;
    private double grade;

    // ----- constructors -----
    // Primary Use: ALL    
    public Student(int studentId, String lastName, String firstName, int age,
            String course, String address, int academicYearLevel, int enrollmentYear,
            boolean hasGraduated, int graduationYear, double grade) {
        super(firstName, lastName, age, address);
        this.studentId = studentId;
        this.academicYearLevel = academicYearLevel;
        this.hasGraduated = hasGraduated;
        this.course = course;
        this.enrollmentYear = enrollmentYear;
        this.graduationYear = graduationYear;
        this.grade = grade;
    }

    // ----- getters -----
    public int getStudentId() {
        return studentId;
    }

    public int getAcademicYearLevel() {
        return academicYearLevel;
    }

    public boolean hasGraduated() {
        return hasGraduated;
    }

    public String getCourse() {
        return course;
    }

    public int getEnrollmentYear() {
        return enrollmentYear;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public double getGrade() {
        return grade;
    }

    // ----- setters -----
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setAcademicYearLevel(int academicYearLevel) {
        this.academicYearLevel = academicYearLevel;
    }

    public void setHasGraduated(boolean hasGraduated) {
        this.hasGraduated = hasGraduated;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setEnrollmentYear(int enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    // ----- toString -----
    @Override
    public String toString() {
        return "Student{"
                + "student Id=" + studentId
                + ", Academic Year Level=" + academicYearLevel
                + ", Has Graduated=" + hasGraduated
                + ", course='" + course + '\''
                + ", Enrollment Year=" + enrollmentYear
                + ", Graduation Year=" + graduationYear
                + ", Grade=" + grade
                + ", First Name='" + getFirstName() + '\''
                + ", last Name='" + getLastName() + '\''
                + ", Age=" + getAge()
                + ", Address='" + getAddress() + '\''
                + '}';
    }
}
