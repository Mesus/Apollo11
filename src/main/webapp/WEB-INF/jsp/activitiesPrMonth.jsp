<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
  <head><title>Activities</title></head>
  <body>
    <h3>Activities for <c:out value="${activityList[0].month}"/> <c:out value="${activityList[0].year}"/></h3>
    <table border="1">
      <tr>
        <th>Employee Name</th>
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
            <c:when test="${emp.name == act.employee.name && acttype.category == act.activityType.category}" >
                <c:out value="${act.activityType.activityName}" />

            </c:when>

            </c:choose>
            </c:forEach>
            </td>
            </c:forEach>
          </tr>
        </c:forEach>
    </table>

  </body>
</html>