/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import main.logic.StudentManager;

/**
 *
 * @author ryanfletcher
 */
public class GUI {

    private JFrame frame;
    private StudentManager studentManager;
    private JPanel main;

    public GUI() {
        studentManager = new StudentManager();
        initializeFrame();
        showMainMenu();
    }

    // Creates the frame
    private void initializeFrame() {
        frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        
        main = new JPanel(new BorderLayout());
        frame.add(main);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    // Replaces initialise().
    private void showMainMenu() {
        main.removeAll();

        // Title
        JLabel titleLabel = new JLabel("Student Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        main.add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Buttons
        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");
        JButton removeButton = new JButton("Remove Student");
        JButton showButton = new JButton("Show Students");
        JButton findButton = new JButton("Find Student");
        JButton exitButton = new JButton("Exit");

        // Add buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(showButton);
        buttonPanel.add(findButton);
        buttonPanel.add(exitButton);

        main.add(buttonPanel, BorderLayout.CENTER);

        // Action listeners
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        removeButton.addActionListener(e -> removeStudent());
        showButton.addActionListener(e -> showStudents());
        findButton.addActionListener(e -> findStudent());
        exitButton.addActionListener(e -> {
            studentManager.close();
            frame.dispose();
        });

        main.revalidate();
        main.repaint();
    }

    // For adding a specific student
    private void addStudent() {
        main.removeAll();

        // Personal Information 
        JPanel personalPanel = new JPanel(new GridLayout(0, 2, 0, 6));
        personalPanel.setBorder(BorderFactory.createTitledBorder("Personal Info"));

        personalPanel.add(new JLabel("First Name:"));
        JTextField firstNameField = new JTextField(10);
        personalPanel.add(firstNameField);

        personalPanel.add(new JLabel("Last Name:"));
        JTextField lastNameField = new JTextField(10);
        personalPanel.add(lastNameField);

        personalPanel.add(new JLabel("Age:"));
        JTextField ageField = new JTextField(4);
        personalPanel.add(ageField);

        personalPanel.add(new JLabel("Address:"));
        JTextField addressField = new JTextField(12);
        personalPanel.add(addressField);

        // Academic Information 
        JPanel academicPanel = new JPanel(new GridLayout(0, 2, 0, 6));
        academicPanel.setBorder(BorderFactory.createTitledBorder("Academic Info"));

        academicPanel.add(new JLabel("Academic Year Level:"));
        javax.swing.JComboBox<String> yearLevelBox = new javax.swing.JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6"});
        academicPanel.add(yearLevelBox);

        academicPanel.add(new JLabel("Enrollment Year:"));
        String[] years = new String[12];
        int currentYear = java.time.LocalDate.now().getYear();
        for (int i = 0; i < years.length; i++) {
            years[i] = Integer.toString(currentYear - i);
        }
        javax.swing.JComboBox<String> enrollmentYearBox = new javax.swing.JComboBox<>(years);
        academicPanel.add(enrollmentYearBox);

        academicPanel.add(new JLabel("Graduated:"));
        JCheckBox graduatedBox = new JCheckBox();
        academicPanel.add(graduatedBox);

        academicPanel.add(new JLabel("Year of Graduation (Enter 0 for non-graduate):"));
        JTextField graduationYearField = new JTextField(4);
        academicPanel.add(graduationYearField);

        academicPanel.add(new JLabel("Grade Point Average (GPA):"));
        JTextField gradeField = new JTextField(4);
        academicPanel.add(gradeField);

        // Course Selection
        java.util.List<main.model.Course> courses = studentManager.getAllCourses();
        JPanel coursePanel = new JPanel(new GridLayout(0, 1));
        coursePanel.setBorder(BorderFactory.createTitledBorder("Select Courses"));
        java.util.List<JCheckBox> courseBoxes = new ArrayList<>();

        for (main.model.Course c : courses) {
            String label = c.getName();
            if (c.getDescription() != null && !c.getDescription().trim().isEmpty()) {
                label += " - " + c.getDescription();
            }
            JCheckBox box = new JCheckBox(label);
            box.putClientProperty("id", c.getCourseId());
            coursePanel.add(box);
            courseBoxes.add(box);
        }

        JScrollPane scrollCourses = new JScrollPane(coursePanel);
        scrollCourses.setPreferredSize(new java.awt.Dimension(400, 120));

        // --- Formatting ---
        JPanel form = new JPanel(new GridLayout(3, 1, 20, 20));
        form.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        form.add(personalPanel);
        form.add(academicPanel);
        form.add(scrollCourses);

        // --- Buttons ---
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);

        JPanel container = new JPanel(new BorderLayout());
        container.add(form, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        main.add(container, BorderLayout.CENTER);
        main.revalidate();
        main.repaint();

        submitButton.addActionListener(e -> {
            try {
                String lastName = lastNameField.getText().trim();
                String firstName = firstNameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String address = addressField.getText().trim();
                int yearLevel = Integer.parseInt((String) yearLevelBox.getSelectedItem());
                int enrollmentYear = Integer.parseInt((String) enrollmentYearBox.getSelectedItem());
                boolean graduated = graduatedBox.isSelected();
                int graduationYear = graduationYearField.getText().trim().isEmpty() ? 0
                        : Integer.parseInt(graduationYearField.getText().trim());
                double grade = Double.parseDouble(gradeField.getText().trim());

                // Get selected course IDs
                java.util.List<Integer> courseIds = new ArrayList<>();
                for (JCheckBox cb : courseBoxes) {
                    if (cb.isSelected()) {
                        courseIds.add((Integer) cb.getClientProperty("id"));
                    }
                }

                String Courses = "";
                if (!courseIds.isEmpty()) {
                    int firstId = courseIds.get(0);
                    for (main.model.Course c : courses) {
                        if (c.getCourseId() == firstId) {
                            Courses = c.getName();
                            break;
                        }
                    }
                }

                main.model.Student s = new main.model.Student(
                        0,
                        lastName,
                        firstName,
                        age,
                        Courses,
                        address,
                        yearLevel,
                        enrollmentYear,
                        graduated,
                        graduationYear,
                        grade
                );

                boolean success = studentManager.addStudent(s, courseIds);
                String msg = success ? "Student added successfully with " + courseIds.size() + " course(s)."
                        : "Failed to add student.";
                javax.swing.JOptionPane.showMessageDialog(frame, msg);
                showMainMenu();
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(frame, "Please enter valid numbers for numeric fields.");
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        backButton.addActionListener(e -> showMainMenu());
    }

    // For updating a specific student
// For updating a specific student
    private void updateStudent() {
        main.removeAll();

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Update Student"));

        inputPanel.add(new JLabel("Enter Student ID to Update:"));
        JTextField idField = new JTextField(8);
        inputPanel.add(idField);

        JButton searchButton = new JButton("Search");
        inputPanel.add(new JLabel());
        inputPanel.add(searchButton);

        main.add(inputPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        main.add(formPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainMenu());
        main.add(backButton, BorderLayout.SOUTH);

        main.revalidate();
        main.repaint();

        searchButton.addActionListener(e -> {
            formPanel.removeAll();
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(frame, "Enter a Student ID.");
                return;
            }
            int studentId;
            try {
                studentId = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(frame, "Invalid ID.");
                return;
            }
            main.model.Student s = studentManager.findStudentById(studentId);
            if (s == null) {
                javax.swing.JOptionPane.showMessageDialog(frame, "Student not found.");
                formPanel.revalidate();
                formPanel.repaint();
                return;
            }

            // --- Personal Info
            JPanel personalPanel = new JPanel(new GridLayout(0, 2, 0, 6));
            personalPanel.setBorder(BorderFactory.createTitledBorder("Personal Info"));

            personalPanel.add(new JLabel("First Name:"));
            JTextField firstNameField = new JTextField(s.getFirstName(), 10);
            personalPanel.add(firstNameField);

            personalPanel.add(new JLabel("Last Name:"));
            JTextField lastNameField = new JTextField(s.getLastName(), 10);
            personalPanel.add(lastNameField);

            personalPanel.add(new JLabel("Age:"));
            JTextField ageField = new JTextField(Integer.toString(s.getAge()), 4);
            personalPanel.add(ageField);

            personalPanel.add(new JLabel("Address:"));
            JTextField addressField = new JTextField(s.getAddress(), 12);
            personalPanel.add(addressField);

            // --- Academic Info
            JPanel academicPanel = new JPanel(new GridLayout(0, 2, 0, 6));
            academicPanel.setBorder(BorderFactory.createTitledBorder("Academic Info"));

            academicPanel.add(new JLabel("Academic Year Level:"));
            javax.swing.JComboBox<String> yearLevelBox = new javax.swing.JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6"});
            yearLevelBox.setSelectedItem(Integer.toString(s.getAcademicYearLevel()));
            academicPanel.add(yearLevelBox);

            academicPanel.add(new JLabel("Enrolment Year:"));
            String[] years = new String[12];
            int currentYear = java.time.LocalDate.now().getYear();
            for (int i = 0; i < years.length; i++) {
                years[i] = Integer.toString(currentYear - i);
            }
            javax.swing.JComboBox<String> enrollmentYearBox = new javax.swing.JComboBox<>(years);
            enrollmentYearBox.setSelectedItem(Integer.toString(s.getEnrollmentYear()));
            academicPanel.add(enrollmentYearBox);

            academicPanel.add(new JLabel("Graduated:"));
            JCheckBox graduatedBox = new JCheckBox();
            graduatedBox.setSelected(s.hasGraduated());
            academicPanel.add(graduatedBox);

            academicPanel.add(new JLabel("Year of Graduation (0 if not graduated):"));
            JTextField graduationYearField = new JTextField(Integer.toString(s.getGraduationYear()), 4);
            academicPanel.add(graduationYearField);

            academicPanel.add(new JLabel("Grade Point Average (GPA):"));
            JTextField gradeField = new JTextField(Double.toString(s.getGrade()), 4);
            academicPanel.add(gradeField);

            // --- Course Panel
            java.util.List<main.model.Course> courses = studentManager.getAllCourses();
            JPanel coursePanel = new JPanel(new GridLayout(0, 1));
            coursePanel.setBorder(BorderFactory.createTitledBorder("Select Courses"));
            java.util.List<JCheckBox> courseBoxes = new ArrayList<>();
            java.util.List<main.model.Course> enrolledCourses = studentManager.getCoursesForStudentObjects(s.getStudentId());
            java.util.Set<Integer> enrolledCourseIds = new java.util.HashSet<>();
            for (main.model.Course ec : enrolledCourses) {
                enrolledCourseIds.add(ec.getCourseId());
            }

            for (main.model.Course c : courses) {
                String label = c.getName();
                if (c.getDescription() != null && !c.getDescription().trim().isEmpty()) {
                    label += " - " + c.getDescription();
                }
                JCheckBox box = new JCheckBox(label);
                box.putClientProperty("id", c.getCourseId());
                if (enrolledCourseIds.contains(c.getCourseId())) {
                    box.setSelected(true);
                }
                coursePanel.add(box);
                courseBoxes.add(box);
            }
            JScrollPane scrollCourses = new JScrollPane(coursePanel);
            scrollCourses.setPreferredSize(new java.awt.Dimension(400, 120));

            // --- Formatting
            JPanel form = new JPanel(new GridLayout(3, 1, 20, 20));
            form.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
            form.add(personalPanel);
            form.add(academicPanel);
            form.add(scrollCourses);

            // --- Buttons
            JButton submitButton = new JButton("Submit");
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(submitButton);

            formPanel.setLayout(new BorderLayout());
            formPanel.add(form, BorderLayout.CENTER);
            formPanel.add(buttonPanel, BorderLayout.SOUTH);

            formPanel.revalidate();
            formPanel.repaint();

            submitButton.addActionListener(ev -> {
                try {
                    String lastName = lastNameField.getText().trim();
                    String firstName = firstNameField.getText().trim();
                    int age = Integer.parseInt(ageField.getText().trim());
                    String address = addressField.getText().trim();
                    int yearLevel = Integer.parseInt((String) yearLevelBox.getSelectedItem());
                    int enrollmentYear = Integer.parseInt((String) enrollmentYearBox.getSelectedItem());
                    boolean graduated = graduatedBox.isSelected();
                    int graduationYear = graduationYearField.getText().trim().isEmpty() ? 0
                            : Integer.parseInt(graduationYearField.getText().trim());
                    double grade = Double.parseDouble(gradeField.getText().trim());

                    java.util.List<Integer> courseIds = new ArrayList<>();
                    for (JCheckBox cb : courseBoxes) {
                        if (cb.isSelected()) {
                            courseIds.add((Integer) cb.getClientProperty("id"));
                        }
                    }

                    String primaryCourse = "";
                    if (!courseIds.isEmpty()) {
                        int firstId = courseIds.get(0);
                        for (main.model.Course c : courses) {
                            if (c.getCourseId() == firstId) {
                                primaryCourse = c.getName();
                                break;
                            }
                        }
                    }

                    main.model.Student updated = new main.model.Student(
                            s.getStudentId(),
                            lastName,
                            firstName,
                            age,
                            primaryCourse,
                            address,
                            yearLevel,
                            enrollmentYear,
                            graduated,
                            graduationYear,
                            grade
                    );

                    boolean success = studentManager.updateStudent(updated.getStudentId(), updated, courseIds);
                    if (success) {
                        javax.swing.JOptionPane.showMessageDialog(frame, "Student updated successfully.");
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(frame, "Failed to update student.");
                    }
                    showMainMenu();

                } catch (NumberFormatException ex) {
                    javax.swing.JOptionPane.showMessageDialog(frame, "Please enter valid numbers for numeric fields.");
                } catch (Exception ex) {
                    javax.swing.JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        });
    }

    // For finding a specific student
    // Should have three ways:
    // Find Student by Name - Hands a list of similarly named students in a selector, clicked student outputs textbox
    // Find Student by Course - Hands a list of all students in a course in a selector, clicked student outputs textbox.
    // Find Student by Id - Only outputs if the Id is correct
    private void findStudent() {
        main.removeAll();

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Find Student"));

        // Search By ID
        inputPanel.add(new JLabel("By ID:"));
        JPanel idPanel = new JPanel(new BorderLayout());
        JTextField idField = new JTextField(8);
        JButton idBtn = new JButton("Search");
        idPanel.add(idField, BorderLayout.CENTER);
        idPanel.add(idBtn, BorderLayout.EAST);
        inputPanel.add(idPanel);

        // Search By Name
        inputPanel.add(new JLabel("By Name:"));
        JPanel namePanel = new JPanel(new BorderLayout());
        JTextField nameField = new JTextField(12);
        JButton nameBtn = new JButton("Search");
        namePanel.add(nameField, BorderLayout.CENTER);
        namePanel.add(nameBtn, BorderLayout.EAST);
        inputPanel.add(namePanel);

        // Search By Course
        inputPanel.add(new JLabel("By Course:"));
        JPanel coursePanel = new JPanel(new BorderLayout());
        java.util.List<main.model.Course> courses = studentManager.getAllCourses();
        javax.swing.JComboBox<main.model.Course> courseBox = new javax.swing.JComboBox<>();
        for (main.model.Course c : courses) {
            courseBox.addItem(c);
        }
        JButton courseBtn = new JButton("Search");
        coursePanel.add(courseBox, BorderLayout.CENTER);
        coursePanel.add(courseBtn, BorderLayout.EAST);
        inputPanel.add(coursePanel);

        JTextArea results = new JTextArea(15, 40);
        results.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(results);

        JButton backButton = new JButton("Back");

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(inputPanel, BorderLayout.NORTH);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        wrapper.add(backButton, BorderLayout.SOUTH);

        main.add(wrapper, BorderLayout.CENTER);
        main.revalidate();
        main.repaint();

        // ID Search - detailed
        idBtn.addActionListener(e -> {
            results.setText("");
            try {
                int id = Integer.parseInt(idField.getText().trim());
                main.model.Student s = studentManager.findStudentById(id);
                if (s != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                    sb.append("Student ID: ").append(s.getStudentId()).append("\n");
                    sb.append("Name: ").append(s.getFirstName()).append(" ").append(s.getLastName()).append("\n");
                    sb.append("Age: ").append(s.getAge()).append("\n");
                    sb.append("Address: ").append(s.getAddress()).append("\n");
                    sb.append("Academic Year Level: ").append(s.getAcademicYearLevel()).append("\n");
                    sb.append("Enrollment Year: ").append(s.getEnrollmentYear()).append("\n");
                    sb.append("Graduated: ").append(s.hasGraduated() ? "Yes" : "No").append("\n");
                    if (s.hasGraduated()) {
                        sb.append("Graduation Year: ").append(s.getGraduationYear()).append("\n");
                    }
                    sb.append("Grade: ").append(s.getGrade()).append("\n");

                    java.util.List<main.model.Course> enrolledCourses = studentManager.getCoursesForStudentObjects(s.getStudentId());
                    sb.append("Enrolled Courses: ");
                    if (enrolledCourses.isEmpty()) {
                        sb.append("None");
                    } else {
                        sb.append("\n");
                        for (main.model.Course c : enrolledCourses) {
                            sb.append("  - ").append(c.getCode()).append(" - ").append(c.getName());
                            if (c.getDescription() != null && !c.getDescription().trim().isEmpty()) {
                                sb.append(" (").append(c.getDescription()).append(")");
                            }
                            sb.append("\n");
                        }
                    }
                    sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                    results.setText(sb.toString());
                } else {
                    results.setText("No student found with ID: " + id);
                }
            } catch (Exception ex) {
                results.setText("Invalid ID.");
            }
        });

        // Name Search - summary
        nameBtn.addActionListener(e -> {
            results.setText("");
            String name = nameField.getText().trim();
            java.util.Map<Integer, main.model.Student> matches = studentManager.findStudentByName(name);
            if (!matches.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (main.model.Student s : matches.values()) {
                    sb.append("ID: ").append(s.getStudentId())
                            .append(" | ").append(s.getFirstName()).append(" ").append(s.getLastName())
                            .append(" | ").append(s.getCourse())
                            .append("\n");
                }
                results.setText(sb.toString());
            } else {
                results.setText("No students found with name: " + name);
            }
        });

        // Course Search - summary
        courseBtn.addActionListener(e -> {
            results.setText("");
            main.model.Course selectedCourse = (main.model.Course) courseBox.getSelectedItem();
            if (selectedCourse == null) {
                results.setText("No course selected.");
                return;
            }
            int courseId = selectedCourse.getCourseId();
            java.util.List<main.model.Student> students;
            try {
                students = studentManager.findStudentsByCourse(courseId);
            } catch (Exception ex) {
                ex.printStackTrace();
                students = new java.util.ArrayList<>();
            }
            if (!students.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (main.model.Student s : students) {
                    sb.append("ID: ").append(s.getStudentId())
                            .append(" | ").append(s.getFirstName()).append(" ").append(s.getLastName())
                            .append(" | ").append(s.getCourse())
                            .append("\n");
                }
                results.setText(sb.toString());
            } else {
                results.setText("No students found for course.");
            }
        });

        backButton.addActionListener(e -> showMainMenu());
    }

    // For showing all students
    private void showStudents() {
        main.removeAll();

        // Get all students
        Map<Integer, main.model.Student> students = studentManager.getAllStudents();

        // Build display string
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL STUDENTS ===\n\n");

        if (students.isEmpty()) {
            sb.append("No students found.\n");
        } else {
            sb.append(String.format("%-10s %-15s %-15s %-20s\n", "ID", "First Name", "Last Name", "Courses"));
            sb.append("--------------------------------------------------------------\n");
            for (main.model.Student s : students.values()) {
                java.util.List<main.model.Course> enrolledCourses = studentManager.getCoursesForStudentObjects(s.getStudentId());
                StringBuilder courseCodes = new StringBuilder();
                for (int i = 0; i < enrolledCourses.size(); i++) {
                    courseCodes.append(enrolledCourses.get(i).getName());
                    if (i < enrolledCourses.size() - 1) {
                        courseCodes.append(", ");
                    }
                }
                sb.append(String.format("%-10d %-15s %-15s %-20s\n",
                        s.getStudentId(),
                        s.getFirstName(),
                        s.getLastName(),
                        courseCodes.toString()
                ));
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12)); // .txt text
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> showMainMenu());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        main.add(panel, BorderLayout.CENTER);
        main.revalidate();
        main.repaint();
    }

    // For removing a specific student
    private void removeStudent() {
        main.removeAll();

        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Remove Student"));
        inputPanel.setPreferredSize(new java.awt.Dimension(350, 80));

        // Input Student ID
        inputPanel.add(new JLabel("Enter Student ID:"));
        JTextField idField = new JTextField(8);
        inputPanel.add(idField);

        // Remove button
        inputPanel.add(new JLabel());
        JButton removeButton = new JButton("Remove");
        inputPanel.add(removeButton);

        // Wrap inputPanel so there's no stretching
        JPanel wrapper = new JPanel();
        wrapper.add(inputPanel);

        panel.add(wrapper, BorderLayout.CENTER);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainMenu());
        panel.add(backButton, BorderLayout.SOUTH);

        main.add(panel, BorderLayout.CENTER);
        main.revalidate();
        main.repaint();

        removeButton.addActionListener(e -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(frame, "Please enter a Student ID.");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(frame, "Invalid Student ID.");
                return;
            }

            int confirm = javax.swing.JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove student ID " + id + "?", "Confirm Removal", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }

            boolean success = studentManager.removeStudent(id);
            if (success) {
                javax.swing.JOptionPane.showMessageDialog(frame, "Student removed.");
            } else {
                javax.swing.JOptionPane.showMessageDialog(frame, "No student removed. Check ID.");
            }
            showMainMenu();
        });
    }
}
