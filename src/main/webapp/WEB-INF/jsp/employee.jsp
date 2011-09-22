<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
  <head><title>Employees in the Java-Team</title></head>
  <body>
    <h1>Employees in the Java-team:</h1>
    <h3>Employees</h3>
    <c:forEach items="${employees}" var="emp">
      <c:out value="${emp.name}"/><br />
    </c:forEach>
  </body>
</html>
