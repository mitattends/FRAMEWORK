<%-- 
    Document   : formEmp
    Created on : 25 avr. 2023, 09:21:36
    Author     : mitantsoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form method="post" action = "huhu" enctype="multipart/form-data">
            <input type="text" name="nom" placeholder="nom">
            <input type="file" name="fu">
            <input type="submit" value="valider"> 
        </form>
    </body>
</html>
