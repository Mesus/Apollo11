<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
    <head>
    <link rel="stylesheet" type="text/css" href="../minstil.css" />
    <title>Aktiviteter</title></head>
    <body>
        <h1>Aktiviteter for ${Year}</h1>
        <c:if test="${user.userRole eq 'Admin'}">
            <p><a href="home.htm">Tilbake</a></p>
        </c:if>
        <form method ="POST" action="/activityList.htm" > <br />
        <input type="hidden" name="Year2" value="${Year2}" />
        <input type="hidden" name="Month" value="${Month}" />
         Aktiviteter for <select name="Year" size="1">
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
        </select>
        <input type="SUBMIT"  value="Hent aktiviteter" />
        </form> <br />
        <form method="POST" action="/activityList.htm" >
            <input type="hidden" name="Year" value="${Year}" />
            <input type="hidden" name="Year2" value="${Year2}" />
            <input type="hidden" name="Month" value="${Month}" />
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
        <br /> <br />

        <h1>Aktiviteter for ${Month} ${Year2}</h1>
        <form method ="POST" action="activitiesPrMonthList.htm" >
        <input type="hidden" name="Year" value="${Year}" />
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
        </select> <select name="Year2" size="1">
            <c:forEach items="${Years}" var="y">
                <c:choose>
                <c:when test="${y eq Year2}">
                    <option value="${y}" SELECTED><c:out value="${y}"/></option>
                </c:when>
                <c:otherwise>
                    <option value="${y}" ><c:out value="${y}"/></option>
                </c:otherwise>
                </c:choose>
            </c:forEach>
        </select><input type="SUBMIT" value="Go"></p>
    </form>


    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employees}" var="emp">
          <tr>
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${activities}" var="act">

            <c:if test="${emp.name == act.employee.name}" >

                <c:if test="${acttype.category == act.activityType.category && act.activityType.activityName != '' }" >
                    <c:out value="${act.activityType.activityName}" />  <br />
                </c:if>

            </c:if>
            </c:forEach>
            </td>
            </c:forEach>
          </tr>
        </c:forEach>
    </table>
    <br />



        <jsp:include page="signoutinclude.jsp"></jsp:include>
    </body>
</html>
