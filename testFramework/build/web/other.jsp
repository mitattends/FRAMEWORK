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
                <input type ="text" name="childsName[]" placeholder="premierNom">
                <input type ="text" name="childsName[]" placeholder="deuxiemeNom">
                <input type ="text" name="childsName[]" placeholder="troisiemeNom">
            </p>
            <p>
                <label for="upload">choisissez votre photo</label>
                <input type="file" id="upload" accept="image/png,image/jpeg" name="familyPicture">
            </p>
            <p>
                <input type="submit" value="valider">
            </p>
        </form>
    </body>
</html>
