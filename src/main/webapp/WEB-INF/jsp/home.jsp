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
		<jsp:include page="homeinclude.jsp"></jsp:include>
		<c:if test="${user.userRole eq 'Admin'}">
		<p><a href="admin/home.htm">Administrer</a></p>
		</c:if>

	<jsp:include page="signoutinclude.jsp"></jsp:include>

</body>
</html>
