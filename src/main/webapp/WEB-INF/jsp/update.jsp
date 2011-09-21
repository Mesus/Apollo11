<%@ include file="/WEB-INF/jsp/include.jsp" %>
<% request.setCharacterEncoding("UTF8"); %>

<html>
    <head><title>Update Employees</title></head>
    <body>
        <h3>Update Employees</h3>
        <p>Add an employee:</p>
        <form method="POST" action="updateEmployee.htm">
            Employee name: <input type="text" name="employee_name" /><br />
            <input type="SUBMIT" value="Add Employee" />
        </form>
    </body>
</html>