package com.example.banji.controller;

import com.example.banji.entity.Score;
import com.example.banji.service.ScoreService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    /**
     * 分页查询成绩列表
     */
    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer studentId,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) String semester) {

        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = scoreService.pageQuery(page, pageSize, studentId, courseName, semester);
            result.put("code", 200);
            result.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据ID查询成绩
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable int id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Score score = scoreService.getById(id);
            if (score != null) {
                result.put("code", 200);
                result.put("data", score);
            } else {
                result.put("code", 404);
                result.put("message", "成绩不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "查询失败");
        }
        return result;
    }

    /**
     * 获取所有学生（下拉选择）
     */
    @GetMapping("/students")
    public Map<String, Object> getStudents() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("code", 200);
            result.put("data", scoreService.getAllStudents());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "获取学生列表失败");
        }
        return result;
    }

    /**
     * 获取所有学期
     */
    @GetMapping("/semesters")
    public Map<String, Object> getSemesters() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("code", 200);
            result.put("data", scoreService.getAllSemesters());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "获取学期列表失败");
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("code", 200);
            result.put("data", scoreService.getStatistics());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "获取统计信息失败");
        }
        return result;
    }

    /**
     * 新增成绩（仅管理员）
     */
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Score score) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (score.getStudentId() <= 0) {
                result.put("code", 400);
                result.put("message", "请选择学生");
                return result;
            }
            if (score.getCourseName() == null || score.getCourseName().trim().isEmpty()) {
                result.put("code", 400);
                result.put("message", "课程名称不能为空");
                return result;
            }
            if (score.getScore() == null || score.getScore() < 0 || score.getScore() > 100) {
                result.put("code", 400);
                result.put("message", "成绩必须在0-100之间");
                return result;
            }

            boolean success = scoreService.add(score);
            if (success) {
                result.put("code", 200);
                result.put("message", "添加成功");
            } else {
                result.put("code", 400);
                result.put("message", "添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "添加失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新成绩（仅管理员）
     */
    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Score score) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (score.getId() <= 0) {
                result.put("code", 400);
                result.put("message", "ID无效");
                return result;
            }
            if (score.getScore() == null || score.getScore() < 0 || score.getScore() > 100) {
                result.put("code", 400);
                result.put("message", "成绩必须在0-100之间");
                return result;
            }

            boolean success = scoreService.update(score);
            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除成绩（仅管理员）
     */
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable int id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = scoreService.delete(id);
            if (success) {
                result.put("code", 200);
                result.put("message", "删除成功");
            } else {
                result.put("code", 400);
                result.put("message", "删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量删除成绩（仅管理员）
     */
    @DeleteMapping("/batchDelete")
    public Map<String, Object> batchDelete(@RequestBody Map<String, List<Integer>> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Integer> ids = params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.put("code", 400);
                result.put("message", "请选择要删除的成绩");
                return result;
            }

            boolean success = scoreService.batchDelete(ids);
            if (success) {
                result.put("code", 200);
                result.put("message", "批量删除成功");
            } else {
                result.put("code", 400);
                result.put("message", "批量删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "批量删除失败：" + e.getMessage());
        }
        return result;
    }
}