<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
  <head><title>Activities</title></head>
  <body>
  <c:set var="Year" value="${activityList[0].year}" />
  <c:set var="Month" value="${activityList[0].month}" />
    <form method ="POST" action="updateActivities.htm" >
    <h3> Updated - Activities for <c:out value="${Month}"/> <c:out value="${Year}"/></h3>
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

  </body>
</html>