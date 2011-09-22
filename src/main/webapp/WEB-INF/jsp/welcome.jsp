<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
    <head><title>INMETA CHAMPIONS</title></head>
    <body>
        <h1>Inmeta Java Champion Webpage</h1>
        <p>What do you want to do?</p>
        <form method="POST"
            action="champ.htm">
        <p>Find Champion of the Month: </p>
        <input type="SUBMIT" align="center" value="Find Champion">
        </form>
        <br />
        <form method="POST"
            action="employee.htm">
        <p>Print a list of the Inmeta Java Employees:</p>
        <input type="SUBMIT" align="center" value="Print Employees">
        </form>
        <br />
        <form method="POST"
            action="update.htm">
        <p>Update the Employee List</p>
        <input type="SUBMIT" align="center" value="Update Employees">
        </form>
    </body>
</html>
