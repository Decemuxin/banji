package com.example.banji.entity;  // 声明当前类所在的包路径

import java.util.Date;  // 导入日期类，用于存储班级成立时间

public class Clazz {  // 班级实体类，对应数据库中的clazz表，用于封装班级数据
    private int id;  // 班级唯一标识ID，主键
    private String classCode;  // 班级编号，用于唯一标识一个班级
    private String className;  // 班级名称，例如：计算机一班
    private int grade;  // 年级，例如：2023级
    private String major;  // 专业名称，例如：计算机科学与技术
    private String headTeacher;  // 班主任姓名
    private Date establishTime;  // 班级成立时间

    public int getId() { return id; }  // 获取班级ID
    public void setId(int id) { this.id = id; }  // 设置班级ID
    public String getClassCode() { return classCode; }  // 获取班级编号
    public void setClassCode(String classCode) { this.classCode = classCode; }  // 设置班级编号
    public String getClassName() { return className; }  // 获取班级名称
    public void setClassName(String className) { this.className = className; }  // 设置班级名称
    public int getGrade() { return grade; }  // 获取年级
    public void setGrade(int grade) { this.grade = grade; }  // 设置年级
    public String getMajor() { return major; }  // 获取专业名称
    public void setMajor(String major) { this.major = major; }  // 设置专业名称
    public String getHeadTeacher() { return headTeacher; }  // 获取班主任姓名
    public void setHeadTeacher(String headTeacher) { this.headTeacher = headTeacher; }  // 设置班主任姓名
    public Date getEstablishTime() { return establishTime; }  // 获取班级成立时间
    public void setEstablishTime(Date establishTime) { this.establishTime = establishTime; }  // 设置班级成立时间
}















//package com.example.banji;
//
//public class Clazz {
//    private int id;
//    private String classCode;
//    private String className;
//
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }
//    public String getClassCode() { return classCode; }
//    public void setClassCode(String classCode) { this.classCode = classCode; }
//    public String getClassName() { return className; }
//    public void setClassName(String className) { this.className = className; }
//}