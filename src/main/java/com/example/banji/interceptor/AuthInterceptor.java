package com.example.banji.interceptor;

import com.example.banji.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 权限拦截器：统一处理登录和权限校验
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        String uri = request.getRequestURI();

        // ===== 1. 放行：不需要登录的请求 =====
        if (uri.startsWith("/api/user/login") ||
                uri.startsWith("/api/user/register") ||
                uri.equals("/login2.html") ||
                uri.equals("/register.html") ||
                uri.equals("/style.css") ||
                uri.equals("/error")) {
            return true;
        }

        // ===== 2. 需要登录：检查是否已登录 =====
        if (currentUser == null) {
            if (uri.startsWith("/api/")) {
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print("{\"code\":401,\"message\":\"请先登录\"}");
                out.flush();
                return false;
            }
            response.sendRedirect("/login2.html");
            return false;
        }

        // ===== 3. 需要管理员权限 =====
        boolean isAdmin = "admin".equals(currentUser.getRole());

        String[] adminApis = {
                // ===== 班级管理 =====
                "/api/add", "/api/delete", "/api/update",
                "/api/batchDelete", "/api/batchUpdateGrade",
                "/api/batchUpdateTeacher", "/api/batchAdd",

                // ===== 学生管理（新增） =====
                "/api/student/add",
                "/api/student/delete",
                "/api/student/update",
                "/api/student/batchDelete",
                // ===== 成绩管理 =====
                "/api/score/add", "/api/score/delete",
                "/api/score/update", "/api/score/batchDelete",

                // ===== 教师管理 =====
                "/api/teacher/add", "/api/teacher/delete",
                "/api/teacher/update", "/api/teacher/batchDelete"
        };

        for (String api : adminApis) {
            if (uri.startsWith(api)) {
                if (!isAdmin) {
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.print("{\"code\":403,\"message\":\"权限不足，仅管理员可操作\"}");
                    out.flush();
                    return false;
                }
                break;
            }
        }

        return true;
    }
}