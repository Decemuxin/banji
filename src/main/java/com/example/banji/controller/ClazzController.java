package com.example.banji.controller;

import com.example.banji.service.ClazzService;
import com.example.banji.entity.Clazz;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ClazzController {

    private final ClazzService clazzService;

    public ClazzController(ClazzService clazzService) {
        this.clazzService = clazzService;
    }

    // ========== 查询列表 ==========
    @GetMapping("/list")
    public List<Clazz> list(String keyword) {
        return clazzService.getClazzList(keyword);
    }

    // ========== 新增单条 ==========
    @PostMapping("/add")
    public String add(@RequestBody Clazz clazz) {
        clazzService.addClazz(
                clazz.getClassCode(),
                clazz.getClassName(),
                clazz.getGrade(),
                clazz.getMajor(),
                clazz.getHeadTeacher(),
                clazz.getEstablishTime()
        );
        return "success";
    }

    // ========== 删除单条 ==========
    @GetMapping("/delete")
    public String delete(int id) {
        clazzService.deleteClazz(id);
        return "success";
    }

    // ========== 根据ID查询单个 ==========
    @GetMapping("/get")
    public Clazz get(int id) {
        return clazzService.getClazzById(id);
    }

    // ========== 修改 ==========
    @PostMapping("/update")
    public String update(@RequestBody Clazz clazz) {
        clazzService.updateClazz(
                clazz.getId(),
                clazz.getClassCode(),
                clazz.getClassName(),
                clazz.getGrade(),
                clazz.getMajor(),
                clazz.getHeadTeacher(),
                clazz.getEstablishTime()
        );
        return "success";
    }

    // ========== 【新增】批量删除 ==========
    @PostMapping("/batchDelete")
    public String batchDelete(@RequestBody List<Integer> ids) {
        clazzService.batchDelete(ids);
        return "success";
    }

    // ========== 【新增】批量修改年级 ==========
    @PostMapping("/batchUpdateGrade")
    public String batchUpdateGrade(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Integer> ids = (List<Integer>) params.get("ids");
        int newGrade = (int) params.get("newGrade");
        clazzService.batchUpdateGrade(ids, newGrade);
        return "success";
    }

    // ========== 【新增】批量修改班主任 ==========
    @PostMapping("/batchUpdateTeacher")
    public String batchUpdateTeacher(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Integer> ids = (List<Integer>) params.get("ids");
        String newTeacher = (String) params.get("newTeacher");
        clazzService.batchUpdateTeacher(ids, newTeacher);
        return "success";
    }

    // ========== 【新增】批量新增 ==========
    @PostMapping("/batchAdd")
    public String batchAdd(@RequestBody List<Clazz> clazzList) {
        clazzService.batchAdd(clazzList);
        return "success";
    }

    // ========== 【新增】统计分析 ==========
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        return clazzService.getStatistics();
    }
}



















//package com.example.banji;  // 声明当前类所在的包路径
//
//import org.springframework.web.bind.annotation.GetMapping;  // 导入GET请求注解，处理查询类请求
//import org.springframework.web.bind.annotation.PostMapping;  // 导入POST请求注解，处理提交类请求
//import org.springframework.web.bind.annotation.RequestBody;  // 导入请求体注解，用于接收JSON格式参数
//import org.springframework.web.bind.annotation.RequestMapping;  // 导入请求路径映射注解
//import org.springframework.web.bind.annotation.RestController;  // 导入Rest控制器注解，返回JSON数据
//import java.util.Date;  // 导入日期类
//import java.util.List;  // 导入集合类，用于返回班级列表数据
//import java.util.Map;
//
//@RestController  // 标记此类为控制器，所有方法返回JSON格式数据，前后端分离专用
//@RequestMapping("/api")  // 统一指定此类所有接口的访问前缀为 /api
//public class ClazzController {  // 班级控制器，接收前端请求、调用服务层、返回结果
//
//    private final ClazzService clazzService;  // 声明服务层对象，用于处理业务逻辑
//
//    public ClazzController(ClazzService clazzService) {  // 构造方法，依赖注入ClazzService
//        this.clazzService = clazzService;  // 为成员变量赋值
//    }
//
//    // 查询列表 + 搜索
//    @GetMapping("/list")  // 处理GET请求，访问路径：/api/list，用于查询班级列表
//    public List<Clazz> list(String keyword) {  // 接收前端传入的搜索关键词，返回班级集合
//        return clazzService.getClazzList(keyword);  // 调用服务层方法，获取班级列表并返回
//    }
//
//    // 新增（改为接收JSON）
//    @PostMapping("/add")  // 处理POST请求，访问路径：/api/add，用于新增班级
//    public String add(@RequestBody Clazz clazz) {  // 接收前端JSON数据，自动封装为Clazz对象
//        clazzService.addClazz(  // 调用服务层新增方法，传入班级各项信息
//                clazz.getClassCode(),  // 班级编号
//                clazz.getClassName(),  // 班级名称
//                clazz.getGrade(),  // 年级
//                clazz.getMajor(),  // 专业
//                clazz.getHeadTeacher(),  // 班主任
//                clazz.getEstablishTime()  // 成立时间
//        );
//        return "success";  // 新增成功，返回success字符串给前端
//    }
//
//    // 删除
//    @GetMapping("/delete")  // 处理GET请求，访问路径：/api/delete，用于删除班级
//    public String delete(int id) {  // 接收要删除的班级ID
//        clazzService.deleteClazz(id);  // 调用服务层删除方法
//        return "success";  // 删除成功，返回success
//    }
//
//    // 根据ID查单个
//    @GetMapping("/get")  // 处理GET请求，访问路径：/api/get，根据ID查询单个班级
//    public Clazz get(int id) {  // 接收班级ID，返回对应班级对象
//        return clazzService.getClazzById(id);  // 调用服务层方法，查询并返回班级信息
//    }
//
//    // 修改（改为接收JSON）
//    @PostMapping("/update")  // 处理POST请求，访问路径：/api/update，用于修改班级
//    public String update(@RequestBody Clazz clazz) {  // 接收前端JSON格式的修改后班级数据
//        clazzService.updateClazz(  // 调用服务层修改方法，传入所有更新字段
//                clazz.getId(),  // 班级ID
//                clazz.getClassCode(),  // 班级编号
//                clazz.getClassName(),  // 班级名称
//                clazz.getGrade(),  // 年级
//                clazz.getMajor(),  // 专业
//                clazz.getHeadTeacher(),  // 班主任
//                clazz.getEstablishTime()  // 成立时间
//        );
//        return "success";  // 修改成功，返回success
//    }
//    // ========== 新增：批量删除 ==========
//    @PostMapping("/batchDelete")
//    public String batchDelete(@RequestBody List<Integer> ids) {
//        clazzService.batchDelete(ids);
//        return "success";
//    }
//
//    // ========== 新增：批量修改年级 ==========
//    @PostMapping("/batchUpdateGrade")
//    public String batchUpdateGrade(@RequestBody Map<String, Object> params) {
//        @SuppressWarnings("unchecked")
//        List<Integer> ids = (List<Integer>) params.get("ids");
//        int newGrade = (int) params.get("newGrade");
//        clazzService.batchUpdateGrade(ids, newGrade);
//        return "success";
//    }
//
//    // ========== 新增：批量修改班主任 ==========
//    @PostMapping("/batchUpdateTeacher")
//    public String batchUpdateTeacher(@RequestBody Map<String, Object> params) {
//        @SuppressWarnings("unchecked")
//        List<Integer> ids = (List<Integer>) params.get("ids");
//        String newTeacher = (String) params.get("newTeacher");
//        clazzService.batchUpdateTeacher(ids, newTeacher);
//        return "success";
//    }
//
//    // ========== 新增：统计分析 ==========
//    @GetMapping("/statistics")
//    public Map<String, Object> getStatistics() {
//        return clazzService.getStatistics();
//    }
//}




