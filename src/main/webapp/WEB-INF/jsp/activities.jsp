<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
  <head><title>Activities</title></head>
  <body>
    <h3>Choose action:</h3>
    <form method="POST" action="activitiesPrMonth.htm">
    <p>View activities for month:
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
        </select>
   <input type="SUBMIT" align="center" value="Find Activities">
    </p>
    </form>
  </body>
</html>