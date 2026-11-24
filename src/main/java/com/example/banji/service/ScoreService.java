package com.example.banji.service;

import com.example.banji.entity.Score;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ScoreService {

    private static final String URL = "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PWD = "123456";

    /**
     * 分页查询成绩列表
     */
    public Map<String, Object> pageQuery(int page, int pageSize, Integer studentId, String courseName, String semester) {
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

            if (studentId != null && studentId > 0) {
                whereSql.append(" AND s.student_id = ?");
                params.add(studentId);
            }

            if (courseName != null && !courseName.trim().isEmpty()) {
                whereSql.append(" AND s.course_name LIKE ?");
                params.add("%" + courseName.trim() + "%");
            }

            if (semester != null && !semester.trim().isEmpty()) {
                whereSql.append(" AND s.semester = ?");
                params.add(semester.trim());
            }

            // 查询总数
            String countSql = "SELECT COUNT(*) FROM score s" + whereSql;
            ps = conn.prepareStatement(countSql);
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            long total = rs.next() ? rs.getLong(1) : 0;
            rs.close();
            ps.close();

            // 查询数据
            String dataSql = "SELECT s.*, " +
                    "st.name as student_name, st.student_no, " +
                    "c.class_name as clazz_name " +
                    "FROM score s " +
                    "LEFT JOIN student st ON s.student_id = st.id " +
                    "LEFT JOIN clazz c ON st.clazz_id = c.id" +
                    whereSql +
                    " ORDER BY s.id DESC LIMIT ? OFFSET ?";
            ps = conn.prepareStatement(dataSql);
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ps.setInt(params.size() + 1, pageSize);
            ps.setInt(params.size() + 2, offset);
            rs = ps.executeQuery();

            List<Score> list = new ArrayList<>();
            while (rs.next()) {
                Score score = new Score();
                score.setId(rs.getInt("id"));
                score.setStudentId(rs.getInt("student_id"));
                score.setCourseName(rs.getString("course_name"));
                score.setScore(rs.getDouble("score"));
                score.setExamType(rs.getString("exam_type"));
                score.setExamDate(rs.getDate("exam_date"));
                score.setSemester(rs.getString("semester"));
                score.setCreateTime(rs.getTimestamp("create_time"));
                score.setUpdateTime(rs.getTimestamp("update_time"));
                score.setStudentName(rs.getString("student_name"));
                score.setStudentNo(rs.getString("student_no"));
                score.setClazzName(rs.getString("clazz_name"));
                list.add(score);
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
     * 根据ID查询成绩
     */
    public Score getById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "SELECT s.*, st.name as student_name, st.student_no, c.class_name as clazz_name " +
                    "FROM score s " +
                    "LEFT JOIN student st ON s.student_id = st.id " +
                    "LEFT JOIN clazz c ON st.clazz_id = c.id " +
                    "WHERE s.id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Score score = new Score();
                score.setId(rs.getInt("id"));
                score.setStudentId(rs.getInt("student_id"));
                score.setCourseName(rs.getString("course_name"));
                score.setScore(rs.getDouble("score"));
                score.setExamType(rs.getString("exam_type"));
                score.setExamDate(rs.getDate("exam_date"));
                score.setSemester(rs.getString("semester"));
                score.setCreateTime(rs.getTimestamp("create_time"));
                score.setUpdateTime(rs.getTimestamp("update_time"));
                score.setStudentName(rs.getString("student_name"));
                score.setStudentNo(rs.getString("student_no"));
                score.setClazzName(rs.getString("clazz_name"));
                return score;
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
     * 新增成绩
     */
    public boolean add(Score score) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "INSERT INTO score(student_id, course_name, score, exam_type, exam_date, semester) " +
                    "VALUES(?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, score.getStudentId());
            ps.setString(2, score.getCourseName());
            ps.setDouble(3, score.getScore());
            ps.setString(4, score.getExamType());
            ps.setDate(5, score.getExamDate() != null ? new java.sql.Date(score.getExamDate().getTime()) : null);
            ps.setString(6, score.getSemester());

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
     * 更新成绩
     */
    public boolean update(Score score) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "UPDATE score SET student_id=?, course_name=?, score=?, exam_type=?, exam_date=?, semester=? " +
                    "WHERE id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, score.getStudentId());
            ps.setString(2, score.getCourseName());
            ps.setDouble(3, score.getScore());
            ps.setString(4, score.getExamType());
            ps.setDate(5, score.getExamDate() != null ? new java.sql.Date(score.getExamDate().getTime()) : null);
            ps.setString(6, score.getSemester());
            ps.setInt(7, score.getId());

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
     * 删除成绩
     */
    public boolean delete(int id) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "DELETE FROM score WHERE id = ?";
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
     * 批量删除成绩
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
            String sql = "DELETE FROM score WHERE id IN (" + placeholders + ")";
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
     * 获取所有学生（用于下拉选择）
     */
    public List<Map<String, Object>> getAllStudents() {
        List<Map<String, Object>> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "SELECT id, student_no, name, clazz_id FROM student ORDER BY id";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", rs.getInt("id"));
                item.put("studentNo", rs.getString("student_no"));
                item.put("name", rs.getString("name"));
                item.put("clazzId", rs.getInt("clazz_id"));
                list.add(item);
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
     * 获取所有学期（去重）
     */
    public List<String> getAllSemesters() {
        List<String> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PWD);

            String sql = "SELECT DISTINCT semester FROM score WHERE semester IS NOT NULL ORDER BY semester DESC";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("semester"));
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

            // 总成绩条数
            String countSql = "SELECT COUNT(*) FROM score";
            ps = conn.prepareStatement(countSql);
            rs = ps.executeQuery();
            int total = rs.next() ? rs.getInt(1) : 0;
            result.put("total", total);

            // 平均分
            String avgSql = "SELECT AVG(score) FROM score";
            ps = conn.prepareStatement(avgSql);
            rs = ps.executeQuery();
            double avg = rs.next() ? rs.getDouble(1) : 0;
            result.put("average", Math.round(avg * 100) / 100.0);

            // 最高分
            String maxSql = "SELECT MAX(score) FROM score";
            ps = conn.prepareStatement(maxSql);
            rs = ps.executeQuery();
            double max = rs.next() ? rs.getDouble(1) : 0;
            result.put("max", max);

            // 最低分
            String minSql = "SELECT MIN(score) FROM score";
            ps = conn.prepareStatement(minSql);
            rs = ps.executeQuery();
            double min = rs.next() ? rs.getDouble(1) : 0;
            result.put("min", min);

            // 各课程平均分
            String courseSql = "SELECT course_name, AVG(score) as avg_score, COUNT(*) as count " +
                    "FROM score GROUP BY course_name ORDER BY avg_score DESC";
            ps = conn.prepareStatement(courseSql);
            rs = ps.executeQuery();
            List<Map<String, Object>> courseStats = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("courseName", rs.getString("course_name"));
                item.put("avgScore", Math.round(rs.getDouble("avg_score") * 100) / 100.0);
                item.put("count", rs.getInt("count"));
                courseStats.add(item);
            }
            result.put("courseStats", courseStats);

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