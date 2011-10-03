<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
    <head><title>Update Employees</title></head>
    <body>
        <h3>Update Employees</h3>
        <p>Add an employee:</p>
        <form method="POST" action="addEmployee.htm">
            Employee name: <input type="text" name="employee_name" />
            <input type="SUBMIT" value="Add Employee" />
        </form>

        <c:out value="${message}"/><br />

        <h3>Current employees:</h3>
        <form method="POST" action="deleteEmployee.htm">
        <table border="1">
        <tr><th>Employee</th><th>Delete</th></tr>
        <c:forEach items="${employees}" var="emp">
        <tr><td>
        <c:out value="${emp.name}"/></td>
        <td><input type="CHECKBOX" name="Delete" value="${emp.name}"></td>
        </tr>
        </c:forEach>
        </table>
        <br />
        <input type="SUBMIT" value="Delete employee"/>
        </form>
    </body>
</html>