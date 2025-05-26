/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.model;

/**
 *
 * @author ryanfletcher
 */
public class Person {

    // ----- fields -----
    private String firstName;
    private String lastName;
    private int age;
    private String address;

    // ----- constructor -----
    public Person(String firstName, String lastName, int age, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
    }

    // ----- getters -----
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    // ----- setters -----
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // There is no maximum age limit for university
    public void setAge(int age) {
        if (age >= 16 && age <= 130) {
            this.age = age;
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
