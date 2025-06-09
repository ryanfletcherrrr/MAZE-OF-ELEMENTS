/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import main.logic.StudentManager;

/**
 *
 * @author ryanfletcher
 */
public class GUI {
    private JFrame frame;
    private StudentManager studentManager;
    
    public GUI() {
        studentManager = new StudentManager();
        initialize();
    }
    
    private void initialize() {
        frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        
        // Panels
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Student Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Buttons
        JButton findButton = new JButton("Find Student");
        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");
        JButton removeButton = new JButton("Remove Student");
        JButton exitButton = new JButton("Exit");
        
        // Buttons for panel
        buttonPanel.add(findButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(exitButton);
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        frame.add(mainPanel);
        
        frame.setVisible(true);
        
        // Action listeners
        findButton.addActionListener(e -> findStudent());
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        removeButton.addActionListener(e -> removeStudent());
        exitButton.addActionListener(e -> {
            studentManager.close();
            frame.dispose();
        });
    }

    /* --------------- methods --------------- */
    // For adding a specific student
    private void addStudent() {
        // prompt once individually per requirement
        // show what has been done so far in a textbox (with scroll)
        // when successful, it should show the id for the user to memorise
    }

    // For updating a specific student
    private void updateStudent() {
        //
    }
    
    // For finding a specific student
    private void findStudent() {
        
    }
    
    // For showing all students
    private void showStudents() {
        
    }

    // For removing a specific student
    private void removeStudent() {
        
    }
    
    
    
}
