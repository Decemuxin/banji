



<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="template.html" %>





<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%@ page import="java.util.List" %>--%>
<%--<%@ page import="com.example.banji.entity.Clazz" %>--%>


<%--<html>--%>
<%--<head>--%>
<%--    <title>班级管理系统</title>--%>
<%--    <style>--%>
<%--        table { border-collapse: collapse; margin: 20px 0; width: 100%; }--%>
<%--        td,th { border:1px solid #333; padding:8px; text-align:center; }--%>
<%--        .edit-form { margin: 20px 0; padding:15px; border:1px solid #ccc; }--%>
<%--    </style>--%>
<%--</head>--%>
<%--<body>--%>
<%--<h1>班级管理系统（增删改查）</h1>--%>

<%--<!-- 搜索框 -->--%>
<%--<form action="/" method="get">--%>
<%--    搜索：<input name="keyword" placeholder="输入班级编号/名称">--%>
<%--    <button type="submit">查询</button>--%>
<%--    <a href="/">显示全部</a>--%>
<%--</form>--%>

<%--<!-- 添加班级 -->--%>
<%--<h3>添加班级</h3>--%>
<%--<form action="/add" method="post">--%>
<%--    班级编号：<input name="classCode" required><br><br>--%>
<%--    班级名称：<input name="className" required><br><br>--%>
<%--    <button type="submit">添加</button>--%>
<%--</form>--%>

<%--&lt;%&ndash; 修改区域：只有点击修改时才显示 &ndash;%&gt;--%>
<%--<%--%>
<%--    Clazz editClazz = (Clazz) request.getAttribute("editClazz");--%>
<%--    if(editClazz != null) {--%>
<%--%>--%>
<%--<div class="edit-form">--%>
<%--    <h3>修改班级</h3>--%>
<%--    <form action="/update" method="post">--%>
<%--        <input type="hidden" name="id" value="<%= editClazz.getId() %>">--%>
<%--        班级编号：<input name="classCode" value="<%= editClazz.getClassCode() %>" required><br><br>--%>
<%--        班级名称：<input name="className" value="<%= editClazz.getClassName() %>" required><br><br>--%>
<%--        <button type="submit">保存修改</button>--%>
<%--        <a href="/">取消</a>--%>
<%--    </form>--%>
<%--</div>--%>
<%--<% } %>--%>

<%--<hr>--%>

<%--<!-- 列表 -->--%>
<%--<table>--%>
<%--    <tr>--%>
<%--        <th>ID</th>--%>
<%--        <th>班级编号</th>--%>
<%--        <th>班级名称</th>--%>
<%--        <th>操作</th>--%>
<%--    </tr>--%>
<%--    <%--%>
<%--        List<Clazz> list = (List<Clazz>) request.getAttribute("clazzList");--%>
<%--        if (list != null) {--%>
<%--            for (Clazz c : list) {--%>
<%--    %>--%>
<%--    <tr>--%>
<%--        <td><%= c.getId() %></td>--%>
<%--        <td><%= c.getClassCode() %></td>--%>
<%--        <td><%= c.getClassName() %></td>--%>
<%--        <td>--%>
<%--            <a href="/edit?id=<%= c.getId() %>">修改</a> &nbsp;--%>
<%--            <a href="/delete?id=<%= c.getId() %>" onclick="return confirm('确定删除？')">删除</a>--%>
<%--        </td>--%>
<%--    </tr>--%>
<%--    <%--%>
<%--            }--%>
<%--        }--%>
<%--    %>--%>
<%--</table>--%>
<%--</body>--%>
<%--</html>--%>