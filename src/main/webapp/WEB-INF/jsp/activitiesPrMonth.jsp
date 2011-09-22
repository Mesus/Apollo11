<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
  <head><title>Activities</title></head>
  <body>
    <h3>Activities for <c:out value="${activities[0].month}"/> <c:out value="${activities[0].year}"/></h3>
    <table border="1">
  <tr>
    <th>Employee Name</th>
    <th>Activity Name</th>
  </tr>
  <c:forEach items="${activities}" var="act">
  <tr>
    <td><c:out value="${act.employee.name}" /></td>
    <td><c:out value="${act.activityType.activityType}"/><br /></td>
  </tr>
  </c:forEach>

</table>

  </body>
</html>