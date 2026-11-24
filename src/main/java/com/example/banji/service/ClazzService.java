package com.example.banji.service;

import com.example.banji.entity.Clazz;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClazzService {
    private static final String URL = "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PWD = "123456";

    // ========== 查询列表（支持搜索） ==========
    public List<Clazz> getClazzList(String keyword) {
        List<Clazz> list = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PWD);
            String sql = "SELECT * FROM clazz";
            if (keyword != null && !keyword.isEmpty()) {
                sql += " WHERE class_code LIKE ? OR class_name LIKE ? OR major LIKE ?";
            }
            PreparedStatement ps = conn.prepareStatement(sql);
            if (keyword != null && !keyword.isEmpty()) {
                ps.setString(1, "%" + keyword + "%");
                ps.setString(2, "%" + keyword + "%");
                ps.setString(3, "%" + keyword + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Clazz c = new Clazz();
                c.setId(rs.getInt("id"));
                c.setClassCode(rs.getString("class_code"));
                c.setClassName(rs.getString("class_name"));
                c.setGrade(rs.getInt("grade"));
                c.setMajor(rs.getString("major"));
                c.setHeadTeacher(rs.getString("head_teacher"));
                c.setEstablishTime(rs.getTimestamp("establish_time"));
                list.add(c);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ========== 【新增】获取所有班级（用于下拉选择） ==========
    public List<Clazz> getAll() {
        List<Clazz> list = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PWD);
            // 只需要 id 和 class_name，用于下拉框显示
            String sql = "SELECT id, class_name, grade, major FROM clazz ORDER BY id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Clazz c = new Clazz();
                c.setId(rs.getInt("id"));
                c.setClassName(rs.getString("class_name"));
                c.setGrade(rs.getInt("grade"));
                c.setMajor(rs.getString("major"));
                list.add(c);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ========== 根据ID查询单个 ==========
    public Clazz getClazzById(int id) {
        Clazz c = new Clazz();
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PWD);
            String sql = "SELECT * FROM clazz WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c.setId(rs.getInt("id"));
                c.setClassCode(rs.getString("class_code"));
                c.setClassName(rs.getString("class_name"));
                c.setGrade(rs.getInt("grade"));
                c.setMajor(rs.getString("major"));
                c.setHeadTeacher(rs.getString("head_teacher"));
                c.setEstablishTime(rs.getTimestamp("establish_time"));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    // ========== 新增单条 ==========
    public void addClazz(String code, String name, int grade, String major, String teacher, Date time) {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PWD);
            String sql = "INSERT INTO clazz(class_code,class_name,grade,major,head_teacher,establish_time) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ps.setString(2, name);
            ps.setInt(3, grade);
            ps.setString(4, major);
            ps.setString(5, teacher);
            ps.setTimestamp(6, new Timestamp(time.getTime()));
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== 修改 ==========
    public void updateClazz(int id, String code, String name, int grade, String major, String teacher, Date time) {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PWD);
            String sql = "UPDATE clazz SET class_code=?,class_name=?,grade=?,major=?,head_teacher=?,establish_time=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ps.setString(2, name);
            ps.setInt(3, grade);
            ps.setString(4, major);
            ps.setString(5, teacher);
            ps.setTimestamp(6, new Timestamp(time.getTime()));
            ps.setInt(7, id);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== 删除单条 ==========
    public void deleteClazz(int id) {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PWD);
            String sql = "DELETE FROM clazz WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== 【新增】批量删除 ==========
    public void batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PWD);
            StringBuilder sql = new StringBuilder("DELETE FROM clazz WHERE id IN (");
            for (int i = 0; i < ids.size(); i++) {
                sql.append("?");
                if (i < ids.size() - 1) sql.append(",");
            }
            sql.append(")");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== 【新增】批量修改年级 ==========
    public void batchUpdateGrade(List<Integer> ids, int newGrade) {
        if (ids == null || ids.isEmpty()) return;
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PWD);
            StringBuilder sql = new StringBuilder("UPDATE clazz SET grade=? WHERE id IN (");
            for (int i = 0; i < ids.size(); i++) {
                sql.append("?");
                if (i < ids.size() - 1) sql.append(",");
            }
            sql.append(")");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ps.setInt(1, newGrade);
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 2, ids.get(i));
            }
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== 【新增】批量修改班主任 ==========
    public void batchUpdateTeacher(List<Integer> ids, String newTeacher) {
        if (ids == null || ids.isEmpty()) return;
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PWD);
            StringBuilder sql = new StringBuilder("UPDATE clazz SET head_teacher=? WHERE id IN (");
            for (int i = 0; i < ids.size(); i++) {
                sql.append("?");
                if (i < ids.size() - 1) sql.append(",");
            }
            sql.append(")");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ps.setString(1, newTeacher);
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 2, ids.get(i));
            }
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== 【新增】批量新增 ==========
    public void batchAdd(List<Clazz> clazzList) {
        if (clazzList == null || clazzList.isEmpty()) return;
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PWD);
            String sql = "INSERT INTO clazz(class_code, class_name, grade, major, head_teacher, establish_time) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            for (Clazz c : clazzList) {
                ps.setString(1, c.getClassCode());
                ps.setString(2, c.getClassName());
                ps.setInt(3, c.getGrade());
                ps.setString(4, c.getMajor());
                ps.setString(5, c.getHeadTeacher());
                if (c.getEstablishTime() != null) {
                    ps.setTimestamp(6, new Timestamp(c.getEstablishTime().getTime()));
                } else {
                    ps.setTimestamp(6, new Timestamp(new Date().getTime()));
                }
                ps.addBatch();
            }

            ps.executeBatch();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== 【新增】统计分析 ==========
    public Map<String, Object> getStatistics() {
        Map<String, Object> result = new HashMap<>();
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PWD);

            // 1. 班级总数
            String countSql = "SELECT COUNT(*) AS total FROM clazz";
            PreparedStatement ps1 = conn.prepareStatement(countSql);
            ResultSet rs1 = ps1.executeQuery();
            int total = 0;
            if (rs1.next()) total = rs1.getInt("total");
            rs1.close();
            ps1.close();
            result.put("total", total);

            // 2. 各年级分布
            String gradeSql = "SELECT grade, COUNT(*) AS count FROM clazz GROUP BY grade ORDER BY grade";
            PreparedStatement ps2 = conn.prepareStatement(gradeSql);
            ResultSet rs2 = ps2.executeQuery();
            List<Map<String, Object>> gradeStats = new ArrayList<>();
            while (rs2.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("grade", rs2.getInt("grade"));
                item.put("count", rs2.getInt("count"));
                gradeStats.add(item);
            }
            rs2.close();
            ps2.close();
            result.put("gradeStats", gradeStats);

            // 3. 各专业分布
            String majorSql = "SELECT major, COUNT(*) AS count FROM clazz GROUP BY major ORDER BY count DESC";
            PreparedStatement ps3 = conn.prepareStatement(majorSql);
            ResultSet rs3 = ps3.executeQuery();
            List<Map<String, Object>> majorStats = new ArrayList<>();
            while (rs3.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("major", rs3.getString("major"));
                item.put("count", rs3.getInt("count"));
                majorStats.add(item);
            }
            rs3.close();
            ps3.close();
            conn.close();
            result.put("majorStats", majorStats);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}































//
//package com.example.banji;  // 声明当前类所在的包路径
//
//import org.springframework.stereotype.Service;  // 导入服务层注解，标记此类为业务逻辑处理类
//import java.sql.Connection;  // 导入数据库连接类
//import java.sql.DriverManager;  // 导入数据库驱动管理类
//import java.sql.PreparedStatement;  // 导入预编译SQL语句类
//import java.sql.ResultSet;  // 导入结果集类，用于接收查询返回的数据
//import java.sql.Timestamp;  // 导入时间戳类，适配数据库时间类型
//import java.util.ArrayList;  // 导入ArrayList集合，用于存储班级列表
//import java.util.Date;  // 导入日期类
//import java.util.List;  // 导入List接口，定义集合规范
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service  // 标记此类为Spring的服务层组件，由Spring容器管理
//public class ClazzService {  // 班级服务层，核心业务处理，负责与数据库交互
//    // 数据库连接地址，指定数据库地址、库名、时区等参数
//    private static final String URL = "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC";
//    private static final String USER = "root";  // 数据库登录用户名
//    private static final String PWD = "123456";  // 数据库登录密码
//
//    // 根据关键词查询班级列表，支持模糊搜索
//    public List<Clazz> getClazzList(String keyword) {
//        List<Clazz> list = new ArrayList<>();  // 创建集合，用于存储查询到的班级对象
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");  // 加载MySQL数据库驱动
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);  // 获取数据库连接
//            String sql = "SELECT * FROM clazz";  // 基础查询SQL，查询所有班级
//            // 判断是否传入搜索关键词，若有则拼接WHERE条件
//            if (keyword != null && !keyword.isEmpty()) {
//                sql += " WHERE class_code LIKE ? OR class_name LIKE ? OR major LIKE ?";
//            }
//            PreparedStatement ps = conn.prepareStatement(sql);  // 预编译SQL语句
//            // 若有关键词，为SQL的三个占位符赋值，实现模糊查询
//            if (keyword != null && !keyword.isEmpty()) {
//                ps.setString(1, "%" + keyword + "%");
//                ps.setString(2, "%" + keyword + "%");
//                ps.setString(3, "%" + keyword + "%");
//            }
//            ResultSet rs = ps.executeQuery();  // 执行查询，获取结果集
//            // 遍历结果集，将每一行数据封装为Clazz对象
//            while (rs.next()) {
//                Clazz c = new Clazz();
//                c.setId(rs.getInt("id"));  // 从结果集获取ID并设置
//                c.setClassCode(rs.getString("class_code"));  // 获取班级编号
//                c.setClassName(rs.getString("class_name"));  // 获取班级名称
//                c.setGrade(rs.getInt("grade"));  // 获取年级
//                c.setMajor(rs.getString("major"));  // 获取专业
//                c.setHeadTeacher(rs.getString("head_teacher"));  // 获取班主任
//                c.setEstablishTime(rs.getTimestamp("establish_time"));  // 获取成立时间
//                list.add(c);  // 将对象添加到集合
//            }
//            rs.close();  // 关闭结果集
//            ps.close();  // 关闭预编译对象
//            conn.close();  // 关闭数据库连接
//        } catch (Exception e) {  // 捕获数据库操作异常
//            e.printStackTrace();  // 打印异常信息
//        }
//        return list;  // 返回班级列表集合
//    }
//
//    // 根据ID查询单个班级详情
//    public Clazz getClazzById(int id) {
//        Clazz c = new Clazz();  // 创建班级对象，用于接收查询结果
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);  // 获取数据库连接
//            String sql = "SELECT * FROM clazz WHERE id=?";  // 根据ID查询的SQL语句
//            PreparedStatement ps = conn.prepareStatement(sql);  // 预编译SQL
//            ps.setInt(1, id);  // 为占位符设置ID值
//            ResultSet rs = ps.executeQuery();  // 执行查询
//            // 如果查询到数据，将数据封装到对象中
//            if (rs.next()) {
//                c.setId(rs.getInt("id"));
//                c.setClassCode(rs.getString("class_code"));
//                c.setClassName(rs.getString("class_name"));
//                c.setGrade(rs.getInt("grade"));
//                c.setMajor(rs.getString("major"));
//                c.setHeadTeacher(rs.getString("head_teacher"));
//                c.setEstablishTime(rs.getTimestamp("establish_time"));
//            }
//            rs.close();
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return c;  // 返回查询到的班级对象
//    }
//
//    // 新增班级数据到数据库
//    public void addClazz(String code, String name, int grade, String major, String teacher, Date time) {
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);  // 获取连接
//            // 新增班级的SQL语句
//            String sql = "INSERT INTO clazz(class_code,class_name,grade,major,head_teacher,establish_time) VALUES(?,?,?,?,?,?)";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setString(1, code);  // 设置班级编号
//            ps.setString(2, name);  // 设置班级名称
//            ps.setInt(3, grade);    // 设置年级
//            ps.setString(4, major); // 设置专业
//            ps.setString(5, teacher); // 设置班主任
//            ps.setTimestamp(6, new Timestamp(time.getTime())); // 转换时间类型并设置
//            ps.executeUpdate();  // 执行新增操作
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 修改数据库中的班级信息
//    public void updateClazz(int id, String code, String name, int grade, String major, String teacher, Date time) {
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);
//            // 修改班级的SQL语句
//            String sql = "UPDATE clazz SET class_code=?,class_name=?,grade=?,major=?,head_teacher=?,establish_time=? WHERE id=?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setString(1, code);
//            ps.setString(2, name);
//            ps.setInt(3, grade);
//            ps.setString(4, major);
//            ps.setString(5, teacher);
//            ps.setTimestamp(6, new Timestamp(time.getTime()));
//            ps.setInt(7, id);  // 根据ID进行修改
//            ps.executeUpdate();  // 执行修改
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 根据ID删除班级
//    public void deleteClazz(int id) {
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);
//            String sql = "DELETE FROM clazz WHERE id=?";  // 删除SQL
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, id);  // 设置要删除的ID
//            ps.executeUpdate();  // 执行删除
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    // ========== 新增：批量删除 ==========
//    public void batchDelete(List<Integer> ids) {
//        if (ids == null || ids.isEmpty()) return;
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);
//            // 构建 IN 语句：DELETE FROM clazz WHERE id IN (?,?,?...)
//            StringBuilder sql = new StringBuilder("DELETE FROM clazz WHERE id IN (");
//            for (int i = 0; i < ids.size(); i++) {
//                sql.append("?");
//                if (i < ids.size() - 1) sql.append(",");
//            }
//            sql.append(")");
//            PreparedStatement ps = conn.prepareStatement(sql.toString());
//            for (int i = 0; i < ids.size(); i++) {
//                ps.setInt(i + 1, ids.get(i));
//            }
//            ps.executeUpdate();
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // ========== 新增：批量修改（统一修改年级） ==========
//    public void batchUpdateGrade(List<Integer> ids, int newGrade) {
//        if (ids == null || ids.isEmpty()) return;
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);
//            StringBuilder sql = new StringBuilder("UPDATE clazz SET grade=? WHERE id IN (");
//            for (int i = 0; i < ids.size(); i++) {
//                sql.append("?");
//                if (i < ids.size() - 1) sql.append(",");
//            }
//            sql.append(")");
//            PreparedStatement ps = conn.prepareStatement(sql.toString());
//            ps.setInt(1, newGrade);
//            for (int i = 0; i < ids.size(); i++) {
//                ps.setInt(i + 2, ids.get(i));
//            }
//            ps.executeUpdate();
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // ========== 新增：批量修改（统一修改班主任） ==========
//    public void batchUpdateTeacher(List<Integer> ids, String newTeacher) {
//        if (ids == null || ids.isEmpty()) return;
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);
//            StringBuilder sql = new StringBuilder("UPDATE clazz SET head_teacher=? WHERE id IN (");
//            for (int i = 0; i < ids.size(); i++) {
//                sql.append("?");
//                if (i < ids.size() - 1) sql.append(",");
//            }
//            sql.append(")");
//            PreparedStatement ps = conn.prepareStatement(sql.toString());
//            ps.setString(1, newTeacher);
//            for (int i = 0; i < ids.size(); i++) {
//                ps.setInt(i + 2, ids.get(i));
//            }
//            ps.executeUpdate();
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // ========== 新增：统计分析 ==========
//    public Map<String, Object> getStatistics() {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);
//
//            // 1. 班级总数
//            String countSql = "SELECT COUNT(*) AS total FROM clazz";
//            PreparedStatement ps1 = conn.prepareStatement(countSql);
//            ResultSet rs1 = ps1.executeQuery();
//            int total = 0;
//            if (rs1.next()) total = rs1.getInt("total");
//            rs1.close();
//            ps1.close();
//            result.put("total", total);
//
//            // 2. 各年级分布
//            String gradeSql = "SELECT grade, COUNT(*) AS count FROM clazz GROUP BY grade ORDER BY grade";
//            PreparedStatement ps2 = conn.prepareStatement(gradeSql);
//            ResultSet rs2 = ps2.executeQuery();
//            List<Map<String, Object>> gradeStats = new ArrayList<>();
//            while (rs2.next()) {
//                Map<String, Object> item = new HashMap<>();
//                item.put("grade", rs2.getInt("grade"));
//                item.put("count", rs2.getInt("count"));
//                gradeStats.add(item);
//            }
//            rs2.close();
//            ps2.close();
//            result.put("gradeStats", gradeStats);
//
//            // 3. 各专业分布
//            String majorSql = "SELECT major, COUNT(*) AS count FROM clazz GROUP BY major ORDER BY count DESC";
//            PreparedStatement ps3 = conn.prepareStatement(majorSql);
//            ResultSet rs3 = ps3.executeQuery();
//            List<Map<String, Object>> majorStats = new ArrayList<>();
//            while (rs3.next()) {
//                Map<String, Object> item = new HashMap<>();
//                item.put("major", rs3.getString("major"));
//                item.put("count", rs3.getInt("count"));
//                majorStats.add(item);
//            }
//            rs3.close();
//            ps3.close();
//            conn.close();
//            result.put("majorStats", majorStats);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//}




















//package com.example.banji;
//
//import org.springframework.stereotype.Service;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ClazzService {
//
//    // 数据库配置（只写一次！）
//    private static final String URL = "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC";
//    private static final String USER = "root";
//    private static final String PWD = "123456";
//
//    // 1. 查询班级列表
//    public List<Clazz> getClazzList(String keyword) {
//        List<Clazz> list = new ArrayList<>();
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);
//
//            String sql = "SELECT * FROM clazz";
//            if (keyword != null && !keyword.trim().isEmpty()) {
//                sql += " WHERE class_code LIKE ? OR class_name LIKE ?";
//            }
//            PreparedStatement ps = conn.prepareStatement(sql);
//            if (keyword != null && !keyword.trim().isEmpty()) {
//                ps.setString(1, "%" + keyword + "%");
//                ps.setString(2, "%" + keyword + "%");
//            }
//
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Clazz c = new Clazz();
//                c.setId(rs.getInt("id"));
//                c.setClassCode(rs.getString("class_code"));
//                c.setClassName(rs.getString("class_name"));
//                list.add(c);
//            }
//            rs.close();
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    // 2. 根据ID查单个班级
//    public Clazz getClazzById(int id) {
//        Clazz clazz = new Clazz();
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);
//            String sql = "SELECT * FROM clazz WHERE id=?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                clazz.setId(rs.getInt("id"));
//                clazz.setClassCode(rs.getString("class_code"));
//                clazz.setClassName(rs.getString("class_name"));
//            }
//            rs.close();
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return clazz;
//    }
//
//    // 3. 添加班级
//    public void addClazz(String classCode, String className) {
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);
//            String sql = "insert into clazz(class_code, class_name) values (?,?)";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setString(1, classCode);
//            ps.setString(2, className);
//            ps.executeUpdate();
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 4. 删除
//    public void deleteClazz(int id) {
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);
//            String sql = "delete from clazz where id=?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, id);
//            ps.executeUpdate();
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 5. 修改
//    public void updateClazz(int id, String classCode, String className) {
//        try {
//            Connection conn = DriverManager.getConnection(URL, USER, PWD);
//            String sql = "UPDATE clazz SET class_code=?, class_name=? WHERE id=?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setString(1, classCode);
//            ps.setString(2, className);
//            ps.setInt(3, id);
//            ps.executeUpdate();
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}