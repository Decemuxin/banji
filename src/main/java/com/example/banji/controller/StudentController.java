package com.example.banji.controller;

import com.example.banji.entity.Student;
import com.example.banji.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer clazzId) {

        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> data = studentService.pageQuery(page, pageSize, keyword, clazzId);
            result.put("code", 200);
            result.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/clazz/list")
    public Map<String, Object> getClazzList() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("code", 200);
            result.put("data", studentService.getAllClazz());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "获取班级列表失败");
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable int id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Student student = studentService.getById(id);
            if (student != null) {
                result.put("code", 200);
                result.put("data", student);
            } else {
                result.put("code", 404);
                result.put("message", "学生不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "查询失败");
        }
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Student student) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (student.getStudentNo() == null || student.getStudentNo().trim().isEmpty()) {
                result.put("code", 400);
                result.put("message", "学号不能为空");
                return result;
            }
            if (student.getName() == null || student.getName().trim().isEmpty()) {
                result.put("code", 400);
                result.put("message", "姓名不能为空");
                return result;
            }

            boolean success = studentService.add(student);
            if (success) {
                result.put("code", 200);
                result.put("message", "添加成功");
            } else {
                result.put("code", 400);
                result.put("message", "添加失败，学号可能已被占用");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "添加失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Student student) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (student.getId() <= 0) {
                result.put("code", 400);
                result.put("message", "ID无效");
                return result;
            }

            boolean success = studentService.update(student);
            if (success) {
                result.put("code", 200);
                result.put("message", "更新成功");
            } else {
                result.put("code", 400);
                result.put("message", "更新失败，学号可能已被占用");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable int id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = studentService.delete(id);
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

    @DeleteMapping("/batchDelete")
    public Map<String, Object> batchDelete(@RequestBody Map<String, List<Integer>> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Integer> ids = params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.put("code", 400);
                result.put("message", "请选择要删除的学生");
                return result;
            }

            boolean success = studentService.batchDelete(ids);
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