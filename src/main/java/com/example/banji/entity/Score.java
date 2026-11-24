package com.example.banji.entity;

import java.util.Date;

/**
 * 成绩实体类，对应数据库 score 表
 */
public class Score {
    private int id;
    private int studentId;        // 学生ID
    private String courseName;    // 课程名称
    private Double score;         // 成绩
    private String examType;      // 考试类型
    private Date examDate;        // 考试日期
    private String semester;      // 学期
    private Date createTime;      // 创建时间
    private Date updateTime;      // 更新时间

    // 关联字段（不存数据库，用于展示）
    private String studentName;   // 学生姓名
    private String studentNo;     // 学号
    private String clazzName;     // 班级名称

    // ===== Getter / Setter =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }

    public Date getExamDate() { return examDate; }
    public void setExamDate(Date examDate) { this.examDate = examDate; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }

    public String getClazzName() { return clazzName; }
    public void setClazzName(String clazzName) { this.clazzName = clazzName; }
}