<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
    <head><title>Activities</title></head>
    <body>

        <h3>Aktiviteter for ${Year}</h3>

        <form method="POST" action="/activityList.htm" >
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
                            <c:forEach items="${resultList}" var="res">
                                <c:choose>
                                    <c:when test="${emp.name == res.employee.name}" >
                                        <c:choose>
                                            <c:when test="${acttype.category == res.activityType.category}" >
                                                <c:out value="${res.count}" />
                                            </c:when>
                                        </c:choose>
                                    </c:when>
                                </c:choose>
                            </c:forEach>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>
        <input type="hidden" name="Year" value="${Year}" />
        <input type="SUBMIT"  align="center" value="Go" />
        </form>
    </body>
</html>
