<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="../minstil.css"/>
    <title>Oppdater liste over brukere</title></head>
<body>
<h2>Oppdater brukerliste</h2>

<p><a href="/admin/home.htm">Tilbake</a></p> <br/>

<p><b>Legg til bruker:</b></p>

<form method="POST" action="addUser.htm">
    Epost: <input type="text" name="email"/>
    Tilgang: <select name="userRole" size="1">
                <option value="Member">Medlem</option>
                <option value="Admin">Admin</option>
            </select> <br/>
    <input type="SUBMIT" value="Legg til"/>
</form>

<c:out value="${message}"/>
<br/>

<h2>Registrerte brukere:</h2>

<form method="POST" action="deleteUser.htm">
    <table border="1">
        <tr>
            <th>Epost</th>
            <th>Tilgang</th>
            <th>Delete</th>
        </tr>
        <c:forEach items="${users}" var="usr">
            <tr>
                <td>
                    <c:out value="${usr.email}"/>
                </td>
                <td>
                    <c:out value="${usr.userRole}" />
                </td>
                <td class="center"><input type="CHECKBOX" name="DeleteUser" value="${usr.email}"></td>
            </tr>
        </c:forEach>
    </table>
    <br/>
    <input type="SUBMIT" value="Slett bruker"/>
</form>

<h2>Registrerte forespørsler om brukertilgang:</h2>
<form method="POST" action="deleteRequest.htm">
<table border="1">
    <tr>
        <th>Epost</th>
        <th>Navn</th>
        <th>Delete</th>
    </tr>
    <c:forEach items="${userRequests}" var="ureq">
        <tr>
            <td>
                <c:out value="${ureq.email}" />
            </td>
            <td>
                <c:out value="${ureq.username}" />
            </td>
            <td class="center"><input type="CHECKBOX" name="Delete" value="${ureq.email}"/>
            </td>
        </tr>
    </c:forEach>
</table>
<br />
<input type="SUBMIT" value="Slett forespørsel" />
</form>
</body>
</html>