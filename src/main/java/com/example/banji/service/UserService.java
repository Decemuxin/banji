package com.example.banji.service;

import com.example.banji.entity.User;
import com.example.banji.util.BCrypt;   // ← 用你自己的
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Date;

@Service
public class UserService {

    private static final String URL = "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PWD = "123456";

    public User login(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "SELECT * FROM user WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                String encryptedPwd = rs.getString("password");

                // 调试信息
                System.out.println("=== 登录调试 ===");
                System.out.println("用户名: " + username);
                System.out.println("数据库密码: " + encryptedPwd);
                System.out.println("验证结果: " + BCrypt.checkpw(password, encryptedPwd));

                if (BCrypt.checkpw(password, encryptedPwd)) {
                    updateLastLoginTime(conn, rs.getInt("id"));

                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role"));
                    user.setEmail(rs.getString("email"));
                    user.setCreateTime(rs.getTimestamp("create_time"));
                    user.setLastLoginTime(rs.getTimestamp("last_login_time"));
                    return user;
                }
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    public boolean register(String username, String password, String email, String role) {
        if (isUsernameExists(username)) {
            return false;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String encryptedPwd = BCrypt.hashpw(password, BCrypt.gensalt());
            System.out.println("注册加密结果: " + encryptedPwd);

            String sql = "INSERT INTO user(username, password, role, email, create_time) VALUES(?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, encryptedPwd);
            ps.setString(3, role != null && !role.isEmpty() ? role : "user");
            ps.setString(4, email);
            ps.setTimestamp(5, new Timestamp(new Date().getTime()));

            int result = ps.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    // ===== 以下方法保持不变 =====
    private boolean isUsernameExists(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);
            String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    private void updateLastLoginTime(Connection conn, int userId) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE user SET last_login_time = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(new Date().getTime()));
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
        }
    }

    public User getUserById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);
            String sql = "SELECT * FROM user WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                user.setEmail(rs.getString("email"));
                user.setCreateTime(rs.getTimestamp("create_time"));
                user.setLastLoginTime(rs.getTimestamp("last_login_time"));
                return user;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}