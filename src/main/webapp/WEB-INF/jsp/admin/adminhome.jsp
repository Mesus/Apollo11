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
            <option value="2010"> 2010 </option>
            <option value="2011"> 2011 </option>
            <option value="2012"> 2012 </option>
        </select>
        <input type="SUBMIT" align="center" value="Hent aktivitetsoversikt">
        </form>
        <br />

        <form method="POST" action="updateEmployees.htm">
        <h3>Oppdater listen over ansatte:</h3>
        <input type="SUBMIT" align="center" value="Oppdater ansattliste">
        </form>
        <br />
        <form method ="POST" action="activitiesPrMonth.htm">
        <h3>Se og oppdater liste over aktiviteter:</h3>
        Måned: <select name="Month" size="1">
            <option value="Januar"> Januar </option>
            <option value="Februar"> Februar </option>
            <option value="Mars"> Mars </option>
            <option value="April"> April </option>
            <option value="Mai"> Mai </option>
            <option value="Juni"> Juni </option>
            <option value="Juli"> Juli </option>
            <option value="August"> August </option>
            <option value="September"> September </option>
            <option value="Oktober"> Oktober </option>
            <option value="November"> November </option>
            <option value="Desember"> Desember </option>
        </select>
     År:
        <select name="Year" size="1">
            <option value="2010"> 2010 </option>
            <option value="2011"> 2011 </option>
            <option value="2012"> 2012 </option>
        </select>
   <input type="SUBMIT" align="center" value="Finn aktiviteter">
    </p>
    </form>
    </body>
</html>
