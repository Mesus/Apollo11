<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
    <head>
    <link rel="stylesheet" type="text/css" href="minstil.css" />
    <title>INMETA CHAMPIONS</title></head>
    <body>
        <h2>Inmeta Java Champion Webpage</h2>
        <p>Velkommen til Inmeta Java sin Champion-side! Du er nå innlogget. <br /> Velg hva du vil gjøre:</p>
        <form method="POST" action="activityList.htm">
        <h3>Se aktivitetsoversikt:</h3>
        År: <select name="Year" size="1">
            <option value="2010"> 2010 </option>
            <option value="2011"> 2011 </option>
            <option value="2012"> 2012 </option>
        </select>
        <input type="SUBMIT" align="center" value="Hent aktivitetsoversikt">
        </form>
        <br />

        <form method="POST" action="admin/adminhome.htm">
        <h2>Gå til admin-view:</h2>
        <input type="SUBMIT" value="Administrer">
    </form>
    <c:out value="${message}" />
    </body>
</html>
