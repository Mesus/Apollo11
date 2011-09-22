<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
    <head><title>Result</title></head>
        <body>
        <h1>Result:</h1>
        <p>Month: <c:out value="${champion.month}"/> <br />Year: <c:out value="${champion.year}"/></p>
        <p>The champion of <c:out value="${champion.month}"/>, <c:out value="${champion.year}"/> is: <c:out value="${champion.employee.name}"/></p>
        </body>
</html>
