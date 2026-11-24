package com.example.banji.entity;

import java.util.Date;

/**
 * 教师实体类，对应数据库 teacher 表
 */
public class Teacher {
    private int id;
    private String teacherNo;      // 工号
    private String name;           // 姓名
    private String gender;         // 性别
    private String phone;          // 手机号
    private String email;          // 邮箱
    private String title;          // 职称
    private String department;     // 所属院系
    private Date hireDate;         // 入职日期
    private Date createTime;       // 创建时间
    private Date updateTime;       // 更新时间

    // ===== Getter / Setter =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTeacherNo() { return teacherNo; }
    public void setTeacherNo(String teacherNo) { this.teacherNo = teacherNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}