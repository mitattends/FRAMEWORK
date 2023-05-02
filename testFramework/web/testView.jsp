<%-- 
    Document   : testView
    Created on : 18 avr. 2023, 15:24:05
    Author     : mitantsoa
--%>
<%@page import="dataObject.Emp"%>
<%
    Object obj = request.getAttribute("empList");
    Emp[]emps = (Emp[])obj;
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            for (Emp emp : emps) {
                    out.println(emp.getNom());
            }
        %>
    </body>
</html>
