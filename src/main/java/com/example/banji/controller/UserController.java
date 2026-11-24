package com.example.banji.controller;

import com.example.banji.entity.User;
import com.example.banji.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器：处理登录、注册、登出、获取当前用户信息
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        String username = params.get("username");
        String password = params.get("password");

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "用户名和密码不能为空");
            return result;
        }

        User user = userService.login(username.trim(), password.trim());
        if (user != null) {
            session.setAttribute("currentUser", user);
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("role", user.getRole());
            result.put("username", user.getUsername());
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        String username = params.get("username");
        String password = params.get("password");
        String email = params.get("email");

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "用户名和密码不能为空");
            return result;
        }

        if (username.length() < 3 || username.length() > 20) {
            result.put("success", false);
            result.put("message", "用户名长度必须在3-20个字符之间");
            return result;
        }

        if (password.length() < 6) {
            result.put("success", false);
            result.put("message", "密码长度至少6位");
            return result;
        }

        boolean success = userService.register(username.trim(), password.trim(), email, "user");
        if (success) {
            result.put("success", true);
            result.put("message", "注册成功，请登录");
        } else {
            result.put("success", false);
            result.put("message", "注册失败，用户名可能已被占用");
        }
        return result;
    }

    /**
     * 用户登出
     */
    @GetMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        session.removeAttribute("currentUser");
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "已登出");
        return result;
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/current")
    public Map<String, Object> getCurrentUser(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");
        if (user != null) {
            result.put("loggedIn", true);
            result.put("username", user.getUsername());
            result.put("role", user.getRole());
            result.put("id", user.getId());
        } else {
            result.put("loggedIn", false);
        }
        return result;
    }
}