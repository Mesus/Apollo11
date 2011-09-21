 <jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
   xmlns:form="http://www.springframework.org/tags/form" xmlns:spring="http://www.springframework.org/tags"
   xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:fmt="http://java.sun.com/jsp/jstl/fmt" version="2.0">
   <jsp:directive.page contentType="text/html" />
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
</jsp:root>