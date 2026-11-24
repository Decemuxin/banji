package com.example.banji.entity;

import java.util.Date;

/**
 * 用户实体类，对应数据库 user 表
 */
public class User {
    private int id;
    private String username;
    private String password;        // 数据库中存的是BCrypt加密后的密文
    private String role;            // admin 或 user
    private String email;
    private Date createTime;
    private Date lastLoginTime;

    // ===== Getter / Setter =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(Date lastLoginTime) { this.lastLoginTime = lastLoginTime; }
}