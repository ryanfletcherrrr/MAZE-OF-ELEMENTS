/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author briancobcroft
 */
public class StudentCourseDAO {
    private final Connection conn;
    private static final String TABLE_NAME = "student_courses";
    
    public StudentCourseDAO(Connection conn) throws SQLException {
        this.conn = conn;
        createTable();
    }
    
    // create course collection table
    private void createTable() throws SQLException {
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
        if (!tables.next()) {
            String sql = "CREATE TABLE " + TABLE_NAME + " ("
                    + "student_id INTEGER NOT NULL, "
                    + "course_id  INTEGER NOT NULL, "
                    + "PRIMARY KEY (student_id, course_id))";
            try ( Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        }
    }
    
    // enrol a student into a course
    public boolean enrollStudentInCourse(int studentId, int courseId) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (student_id, course_id) VALUES (?, ?)";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // remove a student from a course
    public boolean unenrollStudentFromCourse(int studentId, int courseId) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE student_id = ? AND course_id = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // get all course IDs for a student
    public List<Integer> getCoursesForStudent(int studentId) throws SQLException {
        String sql = "SELECT course_id FROM " + TABLE_NAME + " WHERE student_id = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            List<Integer> courseIds = new ArrayList<>();
            while (rs.next()) {
                courseIds.add(rs.getInt(1));
            }
            return courseIds;
        }
    }
    
    public List<Integer> getStudentsForCourse(int courseId) throws SQLException {
        String sql = "SELECT student_id FROM " + TABLE_NAME + " WHERE course_id = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            List<Integer> studentIds = new ArrayList<>();
            while (rs.next()) {
                studentIds.add(rs.getInt(1));
            }
            return studentIds;
        }
    }
}