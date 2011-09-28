<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
  <head><title>Activities</title></head>
  <body>

  <h3>Activities for ${Month} ${Year}</h3>

  <form method ="POST" action="activitiesPrMonth.htm" >

    <h3>Find activities for <select name="Month" size="1">
            <option value="">Month:</option>
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
        </select> <select name="Year" size="1">
            <option value="">Year:</option>
            <option value="2010"> 2010 </option>
            <option value="2011"> 2011 </option>
        </select><input type="SUBMIT" align="center" value="Go"></h3>
    </form>
    <form method ="POST" action="updateActivities.htm">
    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp">
          <tr>
            <td><c:out value="${emp.name}" /></td>
            </td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td>
            <c:forEach items="${activityList}" var="act">
            <c:choose>
            <c:when test="${emp.name == act.employee.name}" >
                <c:choose>
                <c:when test="${acttype.category == act.activityType.category && act.activityType.activityName != '' }" >
                    <input type="text" name="${act.activityType.activityName},${acttype.category},${emp.name},${Month},${Year}" value="${act.activityType.activityName}" />  <br />
                </c:when>

                </c:choose>
            </c:when>
            </c:choose>
            </c:forEach>
            <input type="text" name="${acttype.category},${emp.name},${Month},${Year}" />
            </td>
            </c:forEach>
          </tr>
        </c:forEach>
    </table>
    <br />
      <input type="hidden" name="Year" value="${Year}" />
    <input type="hidden" name="Month" value="${Month}" />
     <input type="SUBMIT" value="Lagre" />
    </form>
    <form method="POST" action="activitiesCancel.htm">
    <input type="hidden" name="Year" value="${Year}" />
    <input type="hidden" name="Month" value="${Month}" />
    <input type="SUBMIT" value="Avbryt" />
    </form>
    <br /><br />

    <h3>Alle aktivitetstyper:</h3>
    <table border="1">
    <tr><th>Kategori</th><th>Slett</th></tr>
    <c:forEach items="${activityTypeList}" var="acttype">
    <tr>
     <td>
      <c:out value="${acttype.category}"/>
    </td>
    <td>
    <form method="POST" action="updateActivityTypes.htm">
    <input type="CHECKBOX" name="Delete" value="${acttype.category}">
    </td>
    </tr>
    </c:forEach>
    </table>
    <input type="hidden" name="Year" value="${Year}" >
    <input type="hidden" name="Month" value="${Month}" >
    <input type="SUBMIT" value="Lagre" />
    </form>

   <p><c:out value="${message}" /> </p>
   <br />

   <h3>Legg til kategori:</h3>
   <form method="POST" action="addActivityType.htm">
   <p>Kategori: <input type="text" name="CategoryName" /> Aktivitetsnavn: <input type="text" name="ActivityName" />
   <input type="hidden" name="Year" value="${Year}" >
   <input type="hidden" name="Month" value="${Month}" >
   <input type="SUBMIT" value="Lagre" />  </p>
   </form>


  </body>
</html>