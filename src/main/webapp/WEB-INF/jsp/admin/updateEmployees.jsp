<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
    <head>
    <link rel="stylesheet" type="text/css" href="../minstil.css" />
    <title>Oppdater liste over konsulenter</title></head>
    <body>
        <h2>Oppdater konsulentliste</h2>
          <p><a href="/admin/home.htm">Tilbake</a></p> <br />
        <p><b>Legg til konsulent:</b></p>
        <form method="POST" action="addEmployee.htm">
            Navn på konsulent: <input type="text" name="employee_name" />
            <input type="SUBMIT" value="Legg til" />
        </form>

        <c:out value="${message}"/><br />

        <h2>Registrerte konsulenter:</h2>
        <form method="POST" action="deleteEmployee.htm">
        <table border="1">
        <tr><th>Konsulent</th><th>Delete</th></tr>
        <c:forEach items="${employees}" var="emp">
        <tr><td>
        <c:out value="${emp.name}"/></td>
        <td class="center"><input type="CHECKBOX" name="Delete" value="${emp.name}"></td>
        </tr>
        </c:forEach>
        </table>
        <br />
        <input type="SUBMIT" value="Slett konsulent"/>
        </form>
    </body>
</html>