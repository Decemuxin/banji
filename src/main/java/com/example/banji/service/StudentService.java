package com.example.banji.service;
import com.example.banji.entity.Clazz;
import com.example.banji.entity.Student;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class StudentService {

    private static final String URL = "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PWD = "123456";

    private final ClazzService clazzService;

    public StudentService(ClazzService clazzService) {
        this.clazzService = clazzService;
    }

    /**
     * 分页查询学生列表
     */
    public Map<String, Object> pageQuery(int page, int pageSize, String keyword, Integer clazzId) {
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
                whereSql.append(" AND (s.student_no LIKE ? OR s.name LIKE ?)");
                String likeKeyword = "%" + keyword.trim() + "%";
                params.add(likeKeyword);
                params.add(likeKeyword);
            }

            if (clazzId != null && clazzId > 0) {
                whereSql.append(" AND s.clazz_id = ?");
                params.add(clazzId);
            }

            // 查询总数
            String countSql = "SELECT COUNT(*) FROM student s" + whereSql;
            ps = conn.prepareStatement(countSql);
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            long total = rs.next() ? rs.getLong(1) : 0;
            rs.close();
            ps.close();

            // 查询数据
            String dataSql = "SELECT s.*, c.class_name as clazz_name FROM student s " +
                    "LEFT JOIN clazz c ON s.clazz_id = c.id" +
                    whereSql +
                    " ORDER BY s.id DESC LIMIT ? OFFSET ?";
            ps = conn.prepareStatement(dataSql);
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ps.setInt(params.size() + 1, pageSize);
            ps.setInt(params.size() + 2, offset);
            rs = ps.executeQuery();

            List<Student> list = new ArrayList<>();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentNo(rs.getString("student_no"));
                student.setName(rs.getString("name"));
                student.setGender(rs.getString("gender"));
                student.setPhone(rs.getString("phone"));
                student.setEmail(rs.getString("email"));
                student.setClazzId(rs.getInt("clazz_id"));
                if (rs.wasNull()) {
                    student.setClazzId(null);
                }
                student.setStatus(rs.getString("status"));
                student.setCreateTime(rs.getTimestamp("create_time"));
                student.setUpdateTime(rs.getTimestamp("update_time"));
                student.setClazzName(rs.getString("clazz_name"));
                list.add(student);
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
     * 根据ID查询学生
     */
    public Student getById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "SELECT s.*, c.class_name as clazz_name FROM student s " +
                    "LEFT JOIN clazz c ON s.clazz_id = c.id WHERE s.id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentNo(rs.getString("student_no"));
                student.setName(rs.getString("name"));
                student.setGender(rs.getString("gender"));
                student.setPhone(rs.getString("phone"));
                student.setEmail(rs.getString("email"));
                student.setClazzId(rs.getInt("clazz_id"));
                if (rs.wasNull()) {
                    student.setClazzId(null);
                }
                student.setStatus(rs.getString("status"));
                student.setCreateTime(rs.getTimestamp("create_time"));
                student.setUpdateTime(rs.getTimestamp("update_time"));
                student.setClazzName(rs.getString("clazz_name"));
                return student;
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
     * 新增学生
     */
    public boolean add(Student student) {
        if (isStudentNoExists(student.getStudentNo())) {
            return false;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "INSERT INTO student(student_no, name, gender, phone, email, clazz_id, status) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, student.getStudentNo());
            ps.setString(2, student.getName());
            ps.setString(3, student.getGender());
            ps.setString(4, student.getPhone());
            ps.setString(5, student.getEmail());
            ps.setObject(6, student.getClazzId());
            ps.setString(7, student.getStatus());

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
     * 更新学生
     */
    public boolean update(Student student) {
        if (isStudentNoExists(student.getStudentNo(), student.getId())) {
            return false;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "UPDATE student SET student_no=?, name=?, gender=?, phone=?, email=?, clazz_id=?, status=? " +
                    "WHERE id=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, student.getStudentNo());
            ps.setString(2, student.getName());
            ps.setString(3, student.getGender());
            ps.setString(4, student.getPhone());
            ps.setString(5, student.getEmail());
            ps.setObject(6, student.getClazzId());
            ps.setString(7, student.getStatus());
            ps.setInt(8, student.getId());

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
     * 删除学生
     */
    public boolean delete(int id) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "DELETE FROM student WHERE id = ?";
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
     * 批量删除学生
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
            String sql = "DELETE FROM student WHERE id IN (" + placeholders + ")";
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
     * 检查学号是否已存在
     */
    private boolean isStudentNoExists(String studentNo) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "SELECT COUNT(*) FROM student WHERE student_no = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, studentNo);
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
     * 检查学号是否已存在（排除自己）
     */
    private boolean isStudentNoExists(String studentNo, int excludeId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "SELECT COUNT(*) FROM student WHERE student_no = ? AND id != ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, studentNo);
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
     * 获取所有班级（用于下拉选择）
     */
    public List<Clazz> getAllClazz() {
        return clazzService.getAll();
    }
}