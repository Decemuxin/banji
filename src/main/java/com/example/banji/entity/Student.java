package com.example.banji.entity;

import java.util.Date;

/**
 * 学生实体类，对应数据库 student 表
 */
public class Student {
    private int id;
    private String studentNo;      // 学号
    private String name;           // 姓名
    private String gender;         // 性别
    private String phone;          // 手机号
    private String email;          // 邮箱
    private Integer clazzId;       // 所属班级ID
    private String status;         // 状态
    private Date createTime;       // 创建时间
    private Date updateTime;       // 更新时间

    // 关联字段（不存数据库，用于展示班级名称）
    private String clazzName;      // 班级名称

    // ===== Getter / Setter =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getClazzId() { return clazzId; }
    public void setClazzId(Integer clazzId) { this.clazzId = clazzId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getClazzName() { return clazzName; }
    public void setClazzName(String clazzName) { this.clazzName = clazzName; }
}