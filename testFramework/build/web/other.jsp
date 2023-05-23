<%-- 
    Document   : other
    Created on : 23 mai 2023, 11:06:05
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
        <form action="#" method="post">
            <p>
                <input type="checkbox" id="choix1" name="choix[]" value="riche">
                <label for="choix1">riche</label>
                <input type="checkbox" id="choix2" name="choix[]" value="pauvre">
                <label for="choix2">pauvre</label>
                <input type="checkbox" id="choix3" name="choix[]" value="moyen">
                <label for="choix3">moyen</label>
            </p>
            <p>
                <label for="upload">choisissez votre photo</label>
                <input type="file" id="upload" accept="image/png,image/jpeg">
            </p>
            <p>
                <input type="submit" value="valider">
            </p>
        </form>
    </body>
</html>
