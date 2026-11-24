package com.example.banji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import java.net.URI;

@SpringBootApplication// SpringBoot项目的核心注解，自动完成项目配置、组件扫描、自动装配等工作
public class BanjiApplication {
    public static void main(String[] args) {// Java程序主入口
        System.setProperty("java.awt.headless", "false");// 关闭Headless模式，使桌面功能（打开浏览器）可以正常运行
        SpringApplication.run(BanjiApplication.class, args);// 启动SpringBoot应用，初始化容器并启动Web服务
    }

    // 自动打开浏览器
    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().browse(URI.create("http://localhost:8080/pages/user/login.html"));
            }
        } catch (Exception e) {
            System.out.println("自动打开浏览器失败，请手动访问：http://localhost:8080/pages/user/login.html");
        }
    }
}











//package com.example.banji;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.net.URI;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@SpringBootApplication
//public class BanjiApplication {
//
//    public static void main(String[] args) {
//        // 关键：关闭 headless，让桌面API可用（解决HeadlessException）
//        System.setProperty("java.awt.headless", "false");
//        SpringApplication.run(BanjiApplication.class, args);
//    }
//
//    // 项目启动后自动打开浏览器
//    @EventListener(ApplicationReadyEvent.class)
//    public void openBrowser() {
//        try {
//            // 先尝试用Desktop（Windows/Mac常用）
//            if (java.awt.Desktop.isDesktopSupported()) {
//                java.awt.Desktop.getDesktop().browse(URI.create("http://localhost:8080"));
//            } else {
//                // 备用：用系统命令打开，兼容Linux等
//                String url = "http://localhost:8080";
//                String os = System.getProperty("os.name").toLowerCase();
//                if (os.contains("win")) {
//                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
//                } else if (os.contains("mac")) {
//                    Runtime.getRuntime().exec("open " + url);
//                } else {
//                    Runtime.getRuntime().exec("xdg-open " + url);
//                }
//            }
//        } catch (Exception e) {
//            // 不影响项目启动，仅打印提示
//            System.out.println("自动打开浏览器失败，请手动访问：http://localhost:8080");
//        }
//    }
//
//    // 查询 + 列表
//    @GetMapping("/")
//    public String list(String keyword, Model model) {
//        List<Clazz> list = new ArrayList<>();
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC",
//                    "root",
//                    "123456"
//            );
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
//        model.addAttribute("clazzList", list);
//        return "index";
//    }
//
//    // 添加
//    @PostMapping("/add")
//    public String add(String classCode, String className) {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC",
//                    "root",
//                    "123456"
//            );
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
//        return "redirect:/";
//    }
//
//    // 删除
//    @GetMapping("/delete")
//    public String delete(int id) {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC",
//                    "root",
//                    "123456"
//            );
//            String sql = "delete from clazz where id=?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, id);
//            ps.executeUpdate();
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "redirect:/";
//    }
//
//    // 进入修改
//    @GetMapping("/edit")
//    public String edit(int id, Model model) {
//        Clazz clazz = new Clazz();
//        List<Clazz> list = new ArrayList<>();
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC",
//                    "root",
//                    "123456"
//            );
//
//            String sqlEdit = "SELECT * FROM clazz WHERE id=?";
//            PreparedStatement psEdit = conn.prepareStatement(sqlEdit);
//            psEdit.setInt(1, id);
//            ResultSet rsEdit = psEdit.executeQuery();
//            if (rsEdit.next()) {
//                clazz.setId(rsEdit.getInt("id"));
//                clazz.setClassCode(rsEdit.getString("class_code"));
//                clazz.setClassName(rsEdit.getString("class_name"));
//            }
//            rsEdit.close();
//            psEdit.close();
//
//            String sqlList = "SELECT * FROM clazz";
//            PreparedStatement psList = conn.prepareStatement(sqlList);
//            ResultSet rsList = psList.executeQuery();
//            while (rsList.next()) {
//                Clazz c = new Clazz();
//                c.setId(rsList.getInt("id"));
//                c.setClassCode(rsList.getString("class_code"));
//                c.setClassName(rsList.getString("class_name"));
//                list.add(c);
//            }
//            rsList.close();
//            psList.close();
//
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        model.addAttribute("editClazz", clazz);
//        model.addAttribute("clazzList", list);
//        return "index";
//    }
//
//    // 保存修改
//    @PostMapping("/update")
//    public String update(int id, String classCode, String className) {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC",
//                    "root",
//                    "123456"
//            );
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
//        return "redirect:/";
//    }
//}
//

















//
//@Controller
//@SpringBootApplication
//public class BanjiApplication {
//    public static void main(String[] args) {
//        SpringApplication.run(BanjiApplication.class, args);
//    }
//
//    // 查询 + 列表
//    @GetMapping("/")
//    public String list(String keyword, Model model) {
//        List<Clazz> list = new ArrayList<>();
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC",
//                    "root",
//                    "123456"
//            );
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
//        model.addAttribute("clazzList", list);
//        return "index";
//    }
//
//    // 添加
//    @PostMapping("/add")
//    public String add(String classCode, String className) {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC",
//                    "root",
//                    "123456"
//            );
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
//        return "redirect:/";
//    }
//
//    // 删除
//    @GetMapping("/delete")
//    public String delete(int id) {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC",
//                    "root",
//                    "123456"
//            );
//            String sql = "delete from clazz where id=?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, id);
//            ps.executeUpdate();
//            ps.close();
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "redirect:/";
//    }
//
//    // 进入修改（把数据带回当前页面）
//    @GetMapping("/edit")
//    public String edit(int id, Model model) {
//        Clazz clazz = new Clazz();
//        List<Clazz> list = new ArrayList<>();
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC",
//                    "root",
//                    "123456"
//            );
//
//            // 1. 先查要修改的班级信息
//            String sqlEdit = "SELECT * FROM clazz WHERE id=?";
//            PreparedStatement psEdit = conn.prepareStatement(sqlEdit);
//            psEdit.setInt(1, id);
//            ResultSet rsEdit = psEdit.executeQuery();
//            if (rsEdit.next()) {
//                clazz.setId(rsEdit.getInt("id"));
//                clazz.setClassCode(rsEdit.getString("class_code"));
//                clazz.setClassName(rsEdit.getString("class_name"));
//            }
//            rsEdit.close();
//            psEdit.close();
//
//            // 2. 再查所有班级列表，保证页面显示正常
//            String sqlList = "SELECT * FROM clazz";
//            PreparedStatement psList = conn.prepareStatement(sqlList);
//            ResultSet rsList = psList.executeQuery();
//            while (rsList.next()) {
//                Clazz c = new Clazz();
//                c.setId(rsList.getInt("id"));
//                c.setClassCode(rsList.getString("class_code"));
//                c.setClassName(rsList.getString("class_name"));
//                list.add(c);
//            }
//            rsList.close();
//            psList.close();
//
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        model.addAttribute("editClazz", clazz);
//        model.addAttribute("clazzList", list);
//        return "index";
//    }
//
//    // 保存修改
//    @PostMapping("/update")
//    public String update(int id, String classCode, String className) {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/clazz_db?useSSL=false&serverTimezone=UTC",
//                    "root",
//                    "123456"
//            );
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
//        return "redirect:/";
//    }
//}