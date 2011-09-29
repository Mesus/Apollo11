<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
    <head><title>INMETA CHAMPIONS</title></head>
    <body>
        <h1>Inmeta Java Champion Webpage</h1>
        <p>What do you want to do?</p>
        <form method="POST"
            action="activityList.htm">
        <p>View activities for year:
        <select name="Year" size="1">
            <option value="2010"> 2010 </option>
            <option value="2011"> 2011 </option>
            <option value="2012"> 2012 </option>
        </select> </p>
        <input type="SUBMIT" align="center" value="View activities">
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
        <br />
        <form method ="POST"
            action="activitiesPrMonth.htm">
        <p>View and update activities</p>
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
     year:
        <select name="Year" size="1">
            <option value="2010"> 2010 </option>
            <option value="2011"> 2011 </option>
            <option value="2012"> 2012 </option>
        </select>
   <input type="SUBMIT" align="center" value="Find Activities">
    </p>
    </form>
    </body>
</html>
