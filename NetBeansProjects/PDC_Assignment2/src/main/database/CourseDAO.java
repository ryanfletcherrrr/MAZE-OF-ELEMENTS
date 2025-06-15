/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.database;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;
import main.model.Course;
/**
 *
 * @author briancobcroft
 */
public class CourseDAO {
    private static final String TABLE_NAME = "courses";
    private final Connection conn;
    
    public CourseDAO(Connection conn) throws SQLException {
        this.conn = conn;
        createTable();
    }
    
    private void createTable() throws SQLException {
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
        if (!tables.next()) {
            String sql = "CREATE TABLE " + TABLE_NAME + " ("
                    + "courseId INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,"
                    + "name VARCHAR(255) NOT NULL,"
                    + "description VARCHAR(1024))";
            conn.createStatement().execute(sql);
        }
    }
    
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("courseId"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    
    public Course findCourseById(int courseId) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE courseId = ?")) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Course(courseId, rs.getString("name"), rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}