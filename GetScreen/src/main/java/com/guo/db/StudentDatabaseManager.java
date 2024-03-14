package com.guo.db;

import com.guo.entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDatabaseManager {
    private Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/student_enroll?useSSL=false";
        String user = "root";
        String password = "root";
        return DriverManager.getConnection(url, user, password);
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String SQL = "SELECT * FROM students";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            while (rs.next()) {
                students.add(new Student(rs.getInt("student_id"), rs.getString("name"), rs.getString("identity_number"), rs.getString("portrait_path")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return students;
    }

    public Student getStudentById(Integer studentId) {
        String SQL = "SELECT * FROM students WHERE student_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, studentId.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(rs.getInt("student_id"), rs.getString("name"), rs.getString("identity_number"), rs.getString("portrait_path"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void insertStudent(String name, String identityNumber, String portraitPath) {
        String SQL = "INSERT INTO students(name, identity_number, portrait_path) VALUES(?, ?, ?)";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, identityNumber);
            pstmt.setString(3, portraitPath);

            pstmt.executeUpdate();
            System.out.println("Inserted student: " + name);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void selectStudent(int studentId) {
        String SQL = "SELECT name, identity_number, portrait_path FROM students WHERE student_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, studentId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Name: " + rs.getString("name") + ", Identity Number: " + rs.getString("identity_number") + ", Portrait Path: " + rs.getString("portrait_path"));
            } else {
                System.out.println("Student not found with ID: " + studentId);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void updateStudent(int studentId, String name, String identityNumber, String portraitPath) {
        String SQL = "UPDATE students SET name = ?, identity_number = ?, portrait_path = ? WHERE student_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, identityNumber);
            pstmt.setString(3, portraitPath);
            pstmt.setInt(4, studentId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Updated student: " + name);
            } else {
                System.out.println("Student not found with ID: " + studentId);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteStudent(int studentId) {
        String SQL = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, studentId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Deleted student with ID: " + studentId);
            } else {
                System.out.println("Student not found with ID: " + studentId);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}