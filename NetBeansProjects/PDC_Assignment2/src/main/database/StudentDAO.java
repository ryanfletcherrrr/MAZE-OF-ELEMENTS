/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
public class StudentDAO {

    private static final String TABLE_NAME = "students";

    private Connection conn;

    public StudentDAO(Connection conn) {
        this.conn = conn;
        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // create student table
    private void createTable() throws SQLException {
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
        if (!tables.next()) {
            String createTableSQL = "CREATE TABLE students ("
                    + "studentId INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,"
                    + "lastName VARCHAR(255) NOT NULL,"
                    + "firstName VARCHAR(255) NOT NULL,"
                    + "age INTEGER,"
                    + "course VARCHAR(255),"
                    + "address VARCHAR(255),"
                    + "academicYearLevel INT,"
                    + "yearOfEnrollment INT,"
                    + "graduated BOOLEAN,"
                    + "yearOfGraduation INT,"
                    + "grade DOUBLE"
                    + ")";
            try ( Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }
        }
    }

    public Map<Integer, Student> loadStudents() {
        Map<Integer, Student> students = new HashMap<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        try ( Statement st = conn.createStatement();  ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("studentId"),
                        rs.getString("lastName"),
                        rs.getString("firstName"),
                        rs.getInt("age"),
                        rs.getString("course"),
                        rs.getString("address"),
                        rs.getInt("academicYearLevel"),
                        rs.getInt("yearOfEnrollment"),
                        rs.getBoolean("graduated"),
                        rs.getInt("yearOfGraduation"),
                        rs.getDouble("grade")
                );
                students.put(s.getStudentId(), s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // creation
    private void setInsertParams(PreparedStatement stmt, Student s) throws SQLException {
        stmt.setString(1, s.getLastName());
        stmt.setString(2, s.getFirstName());
        stmt.setInt(3, s.getAge());
        stmt.setString(4, s.getCourse());
        stmt.setString(5, s.getAddress());
        stmt.setInt(6, s.getAcademicYearLevel());
        stmt.setInt(7, s.getEnrollmentYear());
        stmt.setBoolean(8, s.hasGraduated());
        stmt.setInt(9, s.getGraduationYear());
        stmt.setDouble(10, s.getGrade());
    }

    // update
    private void setUpdateParams(PreparedStatement stmt, Student s) throws SQLException {
        stmt.setString(1, s.getLastName());
        stmt.setString(2, s.getFirstName());
        stmt.setInt(3, s.getAge());
        stmt.setString(4, s.getCourse());
        stmt.setString(5, s.getAddress());
        stmt.setInt(6, s.getAcademicYearLevel());
        stmt.setInt(7, s.getEnrollmentYear());
        stmt.setBoolean(8, s.hasGraduated());
        stmt.setInt(9, s.getGraduationYear());
        stmt.setDouble(10, s.getGrade());
        stmt.setInt(11, s.getStudentId());
    }

    //saveStudents method
    public boolean saveStudent(Student s) {
        String checkSQL = "SELECT studentId FROM " + TABLE_NAME + " WHERE studentId = ?";
        String insertSQL = "INSERT INTO " + TABLE_NAME
                + " (lastName, firstName, age, course, address, academicYearLevel, yearOfEnrollment, graduated, yearOfGraduation, grade) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String updateSQL = "UPDATE " + TABLE_NAME
                + " SET lastName=?, firstName=?, age=?, course=?, address=?, academicYearLevel=?, yearOfEnrollment=?, graduated=?, yearOfGraduation=?, grade=? "
                + "WHERE studentId=?";

        try ( PreparedStatement ck = conn.prepareStatement(checkSQL)) {
            ck.setInt(1, s.getStudentId());
            ResultSet rs = ck.executeQuery();
            if (rs.next()) {
                try ( PreparedStatement up = conn.prepareStatement(updateSQL)) {
                    setUpdateParams(up, s);
                    return up.executeUpdate() > 0;
                }
            } else {
                try ( PreparedStatement ins = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                    setInsertParams(ins, s);
                    int n = ins.executeUpdate();
                    if (n > 0) {
                        try ( ResultSet gk = ins.getGeneratedKeys()) {
                            if (gk.next()) {
                                s.setStudentId(gk.getInt(1));
                            }
                        }
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeStudent(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE studentId = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
