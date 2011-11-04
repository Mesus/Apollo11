<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
    <head>
    <link rel="stylesheet" type="text/css" href="../minstil.css" />
    <title>Aktiviteter</title></head>

    <style>
        body {
            font-size: 9pt;
            background: url('img/stripe.png') repeat ;

        }

        td {
            padding: 5px;
        }

        h1 {
            text-align : center;
        }

        table {

            width: 90%;
            table-layout: fixed;
            margin-left: auto ;
            margin-right: auto;

        }

        tr.odd {
            background: #EEEEFF;
        }

        tr.even {
            background: #e0e0FF;
        }

        th {
            background: #FFFFFF;
        }

        body {
            margin-left: 10% ;
            margin-right: 10%;
         }

        #bodycontainer {
            background : #E5E5e5;

        }

        #monthselector {
            margin-left: auto;
            float: right;
        }

    </style>

    </head>


    <body>
    <div id="bodycontainer">

        <c:if test="${user.userRole eq 'Admin'}">
            <p><a href="home.htm">Tilbake</a></p>
        </c:if>
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
        </form></div><br />

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

    <c:forEach items="${Months}" var="mnd">
        <c:if test="${mnd eq 'Desember' && decemberActivities[0] != null}">
        <h1>Aktiviteter for Desember </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
        <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${decemberActivities}" var="act">

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
    <c:if test="${mnd eq 'November' && novemberActivities[0] != null}">
        <h1>Aktiviteter for November </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${novemberActivities}" var="act">

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

    <c:if test="${mnd eq 'Oktober' && octoberActivities[0] != null}">
        <h1>Aktiviteter for Oktober </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${octoberActivities}" var="act">

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

    <c:if test="${mnd eq 'September' && septemberActivities[0] != null}">
        <h1>Aktiviteter for September </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${septemberActivities}" var="act">

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

    <c:if test="${mnd eq 'August' && augustActivities[0] != null}">
        <h1>Aktiviteter for August </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${augustActivities}" var="act">

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

    <c:if test="${mnd eq 'Juli' && julyActivities[0] != null}">
        <h1>Aktiviteter for Juli </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${julyActivities}" var="act">

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

    <c:if test="${mnd eq 'Juni' && juneActivities[0] != null}">
        <h1>Aktiviteter for Juni </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${juneActivities}" var="act">

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

    <c:if test="${mnd eq 'Mai' && mayActivities[0] != null}">
        <h1>Aktiviteter for Mai </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${mayActivities}" var="act">

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

    <c:if test="${mnd eq 'April' && aprilActivities[0] != null}">
        <h1>Aktiviteter for April </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${aprilActivities}" var="act">

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

    <c:if test="${mnd eq 'Mars' && marchActivities[0] != null}">
        <h1>Aktiviteter for Mars </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${marchActivities}" var="act">

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

    <c:if test="${mnd eq 'Februar' && februaryActivities[0] != null}">
        <h1>Aktiviteter for Februar </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${februaryActivities}" var="act">

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

    <c:if test="${mnd eq 'Januar' && januaryActivities[0] != null}">
        <h1>Aktiviteter for Januar </h1>

    <table border="1">
      <tr>
        <th>Konsulent</th>
        <c:forEach items="${activityTypeList}" var="acttype">
            <th><c:out value="${acttype.category}" /></th>
        </c:forEach>
      </tr>
      <c:forEach items="${employeeList}" var="emp" varStatus="loopStatus">
          <tr class="${loopStatus.index % 2 == 0 ? 'odd' : 'even'}">
            <td><c:out value="${emp.name}" /></td>
            <c:forEach items="${activityTypeList}" var="acttype">
            <td class="center">
            <c:forEach items="${januaryActivities}" var="act">

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
            <jsp:include page="signoutinclude.jsp"></jsp:include>
    </div>
    </body>
</html>
