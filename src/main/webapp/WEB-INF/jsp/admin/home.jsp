<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
    <head>
    <link rel="stylesheet" type="text/css" href="../minstil.css" />
    <title>INMETA CHAMPIONS</title></head>
    <body>
        <h2>Inmeta Java Champion Webpage</h2>
        <p>Velkommen til Inmeta Java sin Champion administrerings-side. <br /> Velg hva du vil gjøre:</p>
        <form method="POST" action="activityList.htm">
        <h3>Se aktivitetsoversikt:</h3>
        År: <select name="Year" size="1">
            <c:forEach items="${Years}" var="y">
                <c:choose>
                <c:when test="${y eq Current_Year}">
                    <option value="${y}" SELECTED><c:out value="${y}"/></option>
                </c:when>
                <c:otherwise>
                    <option value="${y}" ><c:out value="${y}"/></option>
                </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
        <input type="SUBMIT"  value="Hent aktivitetsoversikt">
        </form>
        <br />

        <form method="POST" action="updateEmployees.htm">
        <h3>Oppdater listen over ansatte:</h3>
        <input type="SUBMIT"  value="Oppdater ansattliste">
        </form>
        <br />
        <form method ="POST" action="activitiesPrMonth.htm">
        <h3>Se og oppdater liste over aktiviteter:</h3>
        Måned: <select name="Month" size="1">
            <c:forEach items="${Months}" var="m">
                <c:choose>
                <c:when test="${m eq Current_Month}">
                    <option value="${m}" SELECTED><c:out value="${m}"/></option>
                </c:when>
                <c:otherwise>
                    <option value="${m}" ><c:out value="${m}"/></option>
                </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
   År:
        <select name="Year" size="1">
            <c:forEach items="${Years}" var="y">
                <c:choose>
                <c:when test="${y eq Current_Year}">
                    <option value="${y}" SELECTED><c:out value="${y}"/></option>
                </c:when>
                <c:otherwise>
                    <option value="${y}" ><c:out value="${y}"/></option>
                </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
   <input type="SUBMIT" value="Finn aktiviteter">
 
    </form>

    <br />
    <form method="POST" action="updateUsers.htm">
    <h3>Oppdater brukertilgang:</h3>
    <input type="SUBMIT" value="Oppdater brukere" />
    </form>   <br />
    <p><a href="/home.htm">Til hovedsiden</a></p> <br />
    <jsp:include page="../signoutinclude.jsp"></jsp:include>
    </body>
</html>
