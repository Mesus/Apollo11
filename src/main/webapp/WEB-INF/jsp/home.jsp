<%@ include file="/WEB-INF/jsp/include.jsp"%>



<html>
<head>
<script type="text/javascript">
  var userData = {
    email: ${user.email} // required
  };
  window.google.identitytoolkit.updateSavedAccount(userData);
</script>
<link rel="stylesheet" type="text/css" href="minstil.css" />
<title>INMETA CHAMPIONS</title>
</head>
<body>
	<h2>Inmeta Java Champion Webpage</h2>
	    <c:if test="${user.userRole eq 'Member'}"><c:redirect url="/activityList.htm"/></c:if>
		<c:if test="${user.userRole eq 'Admin'}">
	        <p> Velkommen til Inmeta Java sin Champion-side! Du er nå innlogget. <br />
	            Velg hva du vil gjøre:
	        </p>
            <form method="POST" action="activityList.htm">
	            <h3>Se aktivitetsoversikt:</h3>
                År: <select name="Year" size="1">
                    <c:forEach items="${Years}" var="y">
                    <c:choose>
                    <c:when test="${y eq Current_Year}">
                        <option value="${y}" SELECTED><c:out value="${y}"/></option>
                    </c:when>
                    <c:otherwise>
                        <option value="${y}" ><c:out value="${y}"/></option>
                    </c:otherwise>
                    </c:choose>
                    </c:forEach>
                </select> <input type="SUBMIT" value="Hent aktivitetsoversikt">
            </form>
            <br />
		    <p><a href="admin/home.htm">Administrer</a></p>
		</c:if>
	<jsp:include page="signoutinclude.jsp"></jsp:include>

</body>
</html>
