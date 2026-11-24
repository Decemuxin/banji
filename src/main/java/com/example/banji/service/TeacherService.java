package com.example.banji.service;

import com.example.banji.entity.Teacher;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class TeacherService {

    private static final String URL = "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PWD = "123456";

    /**
     * 分页查询教师列表
     */
    public Map<String, Object> pageQuery(int page, int pageSize, String keyword) {
        if (pageSize > 100) pageSize = 100;
        if (page < 1) page = 1;

        int offset = (page - 1) * pageSize;
        Map<String, Object> result = new HashMap<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            StringBuilder whereSql = new StringBuilder(" WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                whereSql.append(" AND (teacher_no LIKE ? OR name LIKE ? OR department LIKE ? OR title LIKE ?)");
                String likeKeyword = "%" + keyword.trim() + "%";
                params.add(likeKeyword);
                params.add(likeKeyword);
                params.add(likeKeyword);
                params.add(likeKeyword);
            }

            // 查询总数
            String countSql = "SELECT COUNT(*) FROM teacher" + whereSql;
            ps = conn.prepareStatement(countSql);
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            long total = rs.next() ? rs.getLong(1) : 0;
            rs.close();
            ps.close();

            // 查询数据
            String dataSql = "SELECT * FROM teacher" + whereSql +
                    " ORDER BY id DESC LIMIT ? OFFSET ?";
            ps = conn.prepareStatement(dataSql);
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ps.setInt(params.size() + 1, pageSize);
            ps.setInt(params.size() + 2, offset);
            rs = ps.executeQuery();

            List<Teacher> list = new ArrayList<>();
            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(rs.getInt("id"));
                teacher.setTeacherNo(rs.getString("teacher_no"));
                teacher.setName(rs.getString("name"));
                teacher.setGender(rs.getString("gender"));
                teacher.setPhone(rs.getString("phone"));
                teacher.setEmail(rs.getString("email"));
                teacher.setTitle(rs.getString("title"));
                teacher.setDepartment(rs.getString("department"));
                teacher.setHireDate(rs.getDate("hire_date"));
                teacher.setCreateTime(rs.getTimestamp("create_time"));
                teacher.setUpdateTime(rs.getTimestamp("update_time"));
                list.add(teacher);
            }

            result.put("list", list);
            result.put("total", total);
            result.put("page", page);
            result.put("pageSize", pageSize);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return result;
    }

    /**
     * 根据ID查询教师
     */
    public Teacher getById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "SELECT * FROM teacher WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(rs.getInt("id"));
                teacher.setTeacherNo(rs.getString("teacher_no"));
                teacher.setName(rs.getString("name"));
                teacher.setGender(rs.getString("gender"));
                teacher.setPhone(rs.getString("phone"));
                teacher.setEmail(rs.getString("email"));
                teacher.setTitle(rs.getString("title"));
                teacher.setDepartment(rs.getString("department"));
                teacher.setHireDate(rs.getDate("hire_date"));
                teacher.setCreateTime(rs.getTimestamp("create_time"));
                teacher.setUpdateTime(rs.getTimestamp("update_time"));
                return teacher;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return null;
    }

    /**
     * 获取所有教师（用于下拉选择）
     */
    public List<Teacher> getAll() {
        List<Teacher> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "SELECT id, teacher_no, name, title FROM teacher ORDER BY id";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(rs.getInt("id"));
                teacher.setTeacherNo(rs.getString("teacher_no"));
                teacher.setName(rs.getString("name"));
                teacher.setTitle(rs.getString("title"));
                list.add(teacher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return list;
    }

    /**
     * 检查工号是否已存在
     */
    private boolean isTeacherNoExists(String teacherNo) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "SELECT COUNT(*) FROM teacher WHERE teacher_no = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, teacherNo);
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

    /**
     * 检查工号是否已存在（排除自己）
     */
    private boolean isTeacherNoExists(String teacherNo, int excludeId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "SELECT COUNT(*) FROM teacher WHERE teacher_no = ? AND id != ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, teacherNo);
            ps.setInt(2, excludeId);
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

    /**
     * 新增教师
     */
    public boolean add(Teacher teacher) {
        if (isTeacherNoExists(teacher.getTeacherNo())) {
            return false;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "INSERT INTO teacher(teacher_no, name, gender, phone, email, title, department, hire_date) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, teacher.getTeacherNo());
            ps.setString(2, teacher.getName());
            ps.setString(3, teacher.getGender());
            ps.setString(4, teacher.getPhone());
            ps.setString(5, teacher.getEmail());
            ps.setString(6, teacher.getTitle());
            ps.setString(7, teacher.getDepartment());
            ps.setDate(8, teacher.getHireDate() != null ? new java.sql.Date(teacher.getHireDate().getTime()) : null);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    /**
     * 更新教师
     */
    public boolean update(Teacher teacher) {
        if (isTeacherNoExists(teacher.getTeacherNo(), teacher.getId())) {
            return false;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "UPDATE teacher SET teacher_no=?, name=?, gender=?, phone=?, email=?, title=?, department=?, hire_date=? " +
                    "WHERE id=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, teacher.getTeacherNo());
            ps.setString(2, teacher.getName());
            ps.setString(3, teacher.getGender());
            ps.setString(4, teacher.getPhone());
            ps.setString(5, teacher.getEmail());
            ps.setString(6, teacher.getTitle());
            ps.setString(7, teacher.getDepartment());
            ps.setDate(8, teacher.getHireDate() != null ? new java.sql.Date(teacher.getHireDate().getTime()) : null);
            ps.setInt(9, teacher.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    /**
     * 删除教师
     */
    public boolean delete(int id) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "DELETE FROM teacher WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    /**
     * 批量删除教师
     */
    public boolean batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
            String sql = "DELETE FROM teacher WHERE id IN (" + placeholders + ")";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    /**
     * 获取统计信息
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> result = new HashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            // 总教师数
            String countSql = "SELECT COUNT(*) FROM teacher";
            ps = conn.prepareStatement(countSql);
            rs = ps.executeQuery();
            int total = rs.next() ? rs.getInt(1) : 0;
            result.put("total", total);

            // 各职称分布
            String titleSql = "SELECT title, COUNT(*) as count FROM teacher GROUP BY title ORDER BY count DESC";
            ps = conn.prepareStatement(titleSql);
            rs = ps.executeQuery();
            List<Map<String, Object>> titleStats = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("title", rs.getString("title"));
                item.put("count", rs.getInt("count"));
                titleStats.add(item);
            }
            result.put("titleStats", titleStats);

            // 各院系分布
            String deptSql = "SELECT department, COUNT(*) as count FROM teacher GROUP BY department ORDER BY count DESC";
            ps = conn.prepareStatement(deptSql);
            rs = ps.executeQuery();
            List<Map<String, Object>> deptStats = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("department", rs.getString("department"));
                item.put("count", rs.getInt("count"));
                deptStats.add(item);
            }
            result.put("deptStats", deptStats);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return result;
    }
}