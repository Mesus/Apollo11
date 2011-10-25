<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
    <head>
    <link rel="stylesheet" type="text/css" href="../minstil.css" />
    <title>Aktiviteter</title></head>
    <body>
        <h1>Aktiviteter for ${Year}</h1>
            <p><a href="home.htm">Tilbake</a></p>
        <form method="POST" action="/activityList.htm" >
            <input type="hidden" name="Year" value="${Year}" />
            <table border="1">
                <tr>
                    <th><input type="SUBMIT" name="Category" value="Konsulent" /></th>
                    <c:forEach items="${activityTypeList}" var="acttype">
                        <th><input type="SUBMIT" name="Category" value="${acttype.category}" /></th>
                    </c:forEach>
                </tr>
                <c:forEach items="${employeeList}" var="emp">
                    <tr>
                        <td><c:out value="${emp.name}" /></td>
                        <c:forEach items="${activityTypeList}" var="acttype">
                            <td class="center">
                                <c:forEach items="${resultList}" var="res">
                                        <c:if test="${emp.name == res.employee.name}" >
                                                <c:if test="${acttype.category == res.activityType.category}" >
                                                    <c:out value="${res.count}" />
                                                </c:if>
                                        </c:if>
                                </c:forEach>
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </table>
        </form>

        <form method ="POST" action="/activityList.htm" > <br /> <br />
        <p> Aktiviteter for <select name="Year" size="1">
            <option value="2010"> 2010 </option>
            <option value="2011" SELECTED> 2011 </option>
            <option value="2012"> 2012 </option>
        </select>
        <input type="SUBMIT"  value="Hent aktiviteter" /> </p>
        </form> <br />
        <jsp:include page="signoutinclude.jsp"></jsp:include>
    </body>
</html>
