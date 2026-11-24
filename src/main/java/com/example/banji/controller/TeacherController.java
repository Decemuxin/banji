package com.example.banji.controller;

import com.example.banji.entity.Teacher;
import com.example.banji.service.TeacherService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * 分页查询教师列表
     */
    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {

        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = teacherService.pageQuery(page, pageSize, keyword);
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
     * 根据ID查询教师
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable int id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Teacher teacher = teacherService.getById(id);
            if (teacher != null) {
                result.put("code", 200);
                result.put("data", teacher);
            } else {
                result.put("code", 404);
                result.put("message", "教师不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "查询失败");
        }
        return result;
    }

    /**
     * 获取所有教师（下拉选择）
     */
    @GetMapping("/all")
    public Map<String, Object> getAll() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("code", 200);
            result.put("data", teacherService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "获取教师列表失败");
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
            result.put("data", teacherService.getStatistics());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "获取统计信息失败");
        }
        return result;
    }

    /**
     * 新增教师（仅管理员）
     */
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Teacher teacher) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (teacher.getTeacherNo() == null || teacher.getTeacherNo().trim().isEmpty()) {
                result.put("code", 400);
                result.put("message", "工号不能为空");
                return result;
            }
            if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
                result.put("code", 400);
                result.put("message", "姓名不能为空");
                return result;
            }

            boolean success = teacherService.add(teacher);
            if (success) {
                result.put("code", 200);
                result.put("message", "添加成功");
            } else {
                result.put("code", 400);
                result.put("message", "添加失败，工号可能已被占用");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "添加失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新教师（仅管理员）
     */
    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Teacher teacher) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (teacher.getId() <= 0) {
                result.put("code", 400);
                result.put("message", "ID无效");
                return result;
            }

            boolean success = teacherService.update(teacher);
            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "更新失败，工号可能已被占用");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除教师（仅管理员）
     */
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable int id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = teacherService.delete(id);
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
     * 批量删除教师（仅管理员）
     */
    @DeleteMapping("/batchDelete")
    public Map<String, Object> batchDelete(@RequestBody Map<String, List<Integer>> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Integer> ids = params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.put("code", 400);
                result.put("message", "请选择要删除的教师");
                return result;
            }

            boolean success = teacherService.batchDelete(ids);
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