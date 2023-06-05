<%-- 
    Document   : formArray
    Created on : 5 juin 2023, 21:55:11
    Author     : mita
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
        <form action="showArray" method = "post">
            <input type="number" name="noms[]">
            <input type="number" name="noms[]">
            <input type="number" name="noms[]">
            <input type="submit" value="valider">
        </form>
    </body>
</html>
