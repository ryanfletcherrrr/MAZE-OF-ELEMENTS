/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.databse;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import main.model.Student;

/**
 *
 * @author ryanfletcher
 */
public class DBManager {
    private static final String DB_URL = "jdbc:derby:studentDB;create=true";
    private static final String TABLE_NAME = "students";
    
    private Connection conn;
    
    public DBManager() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void createTable() throws SQLException {
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
        
        if (!tables.next()) {
            String createTableSQL = 
                "CREATE TABLE + students ("
                + "id INTEGER PRIMARY KEY,"
                + "last_name STRING(20) NOT NULL,"
                + "first_name STRING(20) NOT NULL,"
                + "grade VARCHAR(10),"
                + "age INTEGER,"
                + "course VARCHAR(50),"
                + "major VARCHAR(50),"
                + "address VARCHAR(100),"
                + "year_of_study INTEGER,"
                + "year_of_enrolment INTEGER,"
                + "graduated BOOLEAN,"
                + "year_of_graduation INTEGER"
                + ")";
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }
        }
    }
    
    public Map<Integer, Student> loadStudents() {
        Map<Integer, Student> students = new HashMap<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        
        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                Student student = new Student(
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("age"),
                        rs.getInt("id"),
                        rs.getString("address"),
                        rs.getString("course"),
                        rs.getInt("yearOfStudy"),
                        rs.getBoolean("graduated"),
                        rs.getString("major"),
                        rs.getInt("yearOfEnrollment"),
                        rs.getInt("yearOfGraduation")
                );
                students.put(student.getId(), student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    //setStudentParameters method; new to this project
    private void setStudentParameters(PreparedStatement statement, Student student) throws SQLException {
        statement.setString(1, student.getFirstName());
        statement.setString(2, student.getLastName());
        statement.setInt(3, student.getAge());
        statement.setString(4, student.getAddress());
        statement.setString(5, student.getCourse());
        statement.setInt(6, student.getYearOfStudy());
        statement.setBoolean(7, student.isGraduated());
        statement.setString(8, student.getMajor());
        statement.setInt(9, student.getYearOfEnrollment());
        statement.setInt(10, student.getYearOfGraduation() == null ? 0 : student.getYearOfGraduation());
    }
    
    //saveStudents method
    public boolean saveStudent(Student student) {
        String checkSQL = "SELECT id FROM " + TABLE_NAME + " WHERE id = ?";
        String insertSQL = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String updateSQL = "UPDATE " + TABLE_NAME + " SET firstName=?, lastName=?, age=?, address=?, "
                + "course=?, yearOfStudy=?, graduated=?, major=?, yearOfEnrollment=?, yearOfGraduation=? "
                + "WHERE id=?";
        
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
            checkStmt.setInt(1, student.getId());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                    setStudentParameters(updateStmt, student);
                    updateStmt.setInt(11, student.getId());
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                    insertStmt.setInt(1, student.getId());
                    setStudentParameters(insertStmt, student);
                    return insertStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public boolean removeStudent(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
