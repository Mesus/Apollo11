<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head>
    <link rel="stylesheet" type="text/css" href="../minstil.css" />
  <title>Activities</title></head>
  <body>

  <h2>Aktiviteter ${Month} ${Year}</h2>
  <p><c:out value="${message}" /> </p>

  <form method ="POST" action="activitiesPrMonth.htm" >
    <p>Finn aktiviteter for <select name="Month" size="1">
            <c:forEach items="${Months}" var="m">
                <c:choose>
                <c:when test="${m eq Month}">
                    <option value="${m}" SELECTED><c:out value="${m}"/></option>
                </c:when>
                <c:otherwise>
                    <option value="${m}" ><c:out value="${m}"/></option>
                </c:otherwise>
                </c:choose>
            </c:forEach>
        </select> <select name="Year" size="1">
            <c:forEach items="${Years}" var="y">
                <c:choose>
                <c:when test="${y eq Year}">
                    <option value="${y}" SELECTED><c:out value="${y}"/></option>
                </c:when>
                <c:otherwise>
                    <option value="${y}" ><c:out value="${y}"/></option>
                </c:otherwise>
                </c:choose>
            </c:forEach>
        </select><input type="SUBMIT" value="Go"></p>
    </form>

      <p><a href="/admin/home.htm">Tilbake</a></p>

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

    <h2>Alle aktivitetstyper:</h2>
    <form method="POST" action="updateActivityTypes.htm">
    <table border="1">
    <tr><th>Kategori</th><th>Slett</th></tr>
    <c:forEach items="${activityTypeList}" var="acttype">
    <tr>
     <td>
      <c:out value="${acttype.category}"/>
    </td>
    <td class="center">
   		<input type="CHECKBOX" name="Delete" value="${acttype.category}">
    	</td>
    	</tr>
    	</c:forEach>
    	</table> <br />
    	<input type="hidden" name="Year" value="${Year}" >
    	<input type="hidden" name="Month" value="${Month}" >
    	<input type="SUBMIT" value="Slett" />
   </form>
    <br /><br />

    <h2>Legg til kategori:</h2>
    <form method="POST" action="addActivityType.htm">
    <p>Kategori: <input type="text" name="CategoryName" /> Kun tallverdier? <input type="checkbox" name="number" value="1" />

    Synlig i årsoversikt? <input type="checkbox" name="visible" value="1" checked="checked"/>

    <input type="hidden" name="Year" value="${Year}" >
    <input type="hidden" name="Month" value="${Month}" >
    <input type="SUBMIT" value="Lagre" />  </p>
    </form>
    <br /><br />

    <h2>Endre navn på kategori:</h2>
    <form method="POST" action="changeCategoryName.htm">
    <p>Nåværende kategorinavn: <select name="OldCategoryName" size="1">
        <c:forEach items="${activityTypeList}" var="acttype">
            <option value="${acttype.category}"><c:out value="${acttype.category}"/></option>
        </c:forEach>
        </select>
     <br />
     Nytt kategorinavn: <input type="text" name="NewCategoryName"/>
     <br />
     <input type="hidden" name="Year" value="${Year}" >
     <input type="hidden" name="Month" value="${Month}" >
     <input type="SUBMIT" value="Endre navn"/>
     </form> </p>
  </body>
</html>