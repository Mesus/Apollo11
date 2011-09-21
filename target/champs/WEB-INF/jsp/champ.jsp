<%@ include file="/WEB-INF/jsp/include.jsp" %>
<% request.setCharacterEncoding("UTF8"); %>

<html>
    <head><title>Inmeta Java-Champ of the Month</title></head>
        <body>
            <h1 align="center">Champ of the Month</h1>
            <form method="POST"
                action="findChamp.htm">
            <p>Choose month and year to display the champ:</p>
            Month:
            <select name="Month" size="1">
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
            <br><br>
            Year:
            <select name="Year" size="1">
                <option value="2010"> 2010 </option>
                <option value="2011"> 2011 </option>
            </select>
            <br><br>
            <input type="SUBMIT" align="center" value="Execute">

        </form>
        </body>
</html>