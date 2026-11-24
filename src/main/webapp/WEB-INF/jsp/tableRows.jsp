<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.banji.entity.Clazz" %>
<%
    List<Clazz> list = (List<Clazz>) request.getAttribute("clazzList");
    if (list != null) {
        for (Clazz c : list) {
%>
<tr>
    <td><%= c.getId() %></td>
    <td><%= c.getClassCode() %></td>
    <td><%= c.getClassName() %></td>
    <td>
        <a href="/edit?id=<%= c.getId() %>">修改</a> &nbsp;
        <a href="/delete?id=<%= c.getId() %>" onclick="return confirm('确定删除？')">删除</a>
    </td>
</tr>
<%
        }
    }
%>