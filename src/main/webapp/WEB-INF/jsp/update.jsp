<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
    <head><title>Update Employees</title></head>
    <body>
        <h3>Update Employees</h3>
        <p>Add an employee:</p>
        <form method="POST" action="updateEmployee.htm">
            Employee name: <input type="text" name="employee_name" />
            <input type="SUBMIT" value="Add Employee" />
        </form>
        <p>Delete an employee:</p>
        <form method="POST" action="deleteEmployee.htm">
            Employee name: <input type="text" name="employee_name" />
            <input type="SUBMIT" value="Delete Employee" />
         </form>
        <c:out value="${message}"/><br />

        <p><br />Current employees:</p>
        <form method ="POST" action ="showEmployees.htm">
        <input type="SUBMIT" value="Show Employees" />
        </form>
        <c:forEach items="${employees}" var="emp">
        <c:out value="${emp.name}"/><br />
        </c:forEach>
    </body>
</html>