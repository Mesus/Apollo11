<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="../nyStil.css" />
        <title>Aktiviteter</title>
    </head>

    <body>
        <div id="bodycontainer">

            <c:if test="${user.userRole eq 'Admin'}">
                <p><a href="home.htm">Tilbake</a></p>
            </c:if>
            <br /> <br />

            <h1>TOP 3 CHAMPIONS ${Year}</h1> <br />

            <img src="img/trophy.png" align="right"/>
            <img src="img/trophy.png" align="left"/>
            <br /> <br /> <br /> <br /> <br />
            <c:forEach items="${topThreeTotal}" var="top" varStatus="topStatus">
                <p class="champion"><b> ${top.score}. ${top.name} (${top.points} poeng) </b></p>
            </c:forEach>

            <BR CLEAR="all">

            <h2>TOP 3 - KATEGORIER</h2> <br/>
            <table width="100%" border="0" cellspacing="0" cellpadding="5">
            <tr>
            <c:forEach items="${activityTypeList}" var="actType" varStatus="outerStatus">
                <td>
                    <table border="1">
                        <tr>
                            <th><c:out value="${actType.category}" /></th>
                        </tr>
                        <c:forEach items="${topResults[actType.category]}" var="top3" varStatus="loopStatus">
                                <tr class="odd">
                                    <td>${top3.score}. ${top3.name} (${top3.count})</td>
                                </tr>
                        </c:forEach>
                    </table>
                 </td>
             </c:forEach>
            </table>
            <br /> <br /> <br /> <br />


            <h1>ÅRSOVERSIKT</h1>
            <div id="monthselector" >
                <form id="method ="POST" action="/activityList.htm" > <br />
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
                </form>
            </div>

            <form method="POST" id="activityListForm" action="/activityList.htm" >
                <input type="hidden" name="Year" value="${Year}" />
                <table border="1">
                    <tr>
                        <th> <input type="SUBMIT" name="Category" value="Konsulent" /></th>
                        <c:forEach items="${activityTypeList}" var="acttype">
                              <th><input type="SUBMIT" name="Category" value="${acttype.category}" /></th>
                        </c:forEach>
                    </tr>
                    <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
                        <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
                            <td><c:out value="${emp.name}" /></td>
                            <c:forEach items="${activityTypeList}" var="acttype">
                                <td>
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

            <c:forEach items="${Months}" var="mnd">
                <c:forEach items="${monthActivities}" var="monthActivity">
                    <c:if test="${monthActivity[0] != null && monthActivity[0].month eq mnd}">

                    <h1>Aktiviteter for ${mnd} </h1>
                    <table border="1">
                        <tr>
                            <th>Konsulent</th>
                            <c:forEach items="${activityTypeList}" var="acttype">
                                <th><c:out value="${acttype.category}" /></th>
                            </c:forEach>
                        </tr>
                        <c:forEach items="${employees}" var="emp" varStatus="loopStatus">
                            <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
                                <td><c:out value="${emp.name}" /></td>
                                <c:forEach items="${activityTypeList}" var="acttype">
                                    <td>
                                        <c:forEach items="${monthActivity}" var="act">
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

                    </c:if>
                </c:forEach>
            </c:forEach>

            <jsp:include page="signoutinclude.jsp"></jsp:include>

        </div>

    </body>

</html>
