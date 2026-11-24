<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.banji.entity.Clazz" %>
<%
    Clazz editClazz = (Clazz) request.getAttribute("editClazz");
    if(editClazz != null) {
%>
<div class="edit-form">
    <h3>修改班级</h3>
    <form action="/update" method="post">
        <input type="hidden" name="id" value="<%= editClazz.getId() %>">
        班级编号：<input name="classCode" value="<%= editClazz.getClassCode() %>" required><br><br>
        班级名称：<input name="className" value="<%= editClazz.getClassName() %>" required><br><br>
        <button type="submit">保存修改</button>
        <a href="/">取消</a>
    </form>
</div>
<% } %>