<%@ include file="/WEB-INF/jsp/include.jsp"%>



<html>
<head>
<script type="text/javascript">
  var userData = {
    email: ${email} // required
  };
  window.google.identitytoolkit.updateSavedAccount(userData);
</script>
<link rel="stylesheet" type="text/css" href="minstil.css" />
<title>INMETA CHAMPIONS</title>
</head>
<body>
	<h2>Inmeta Java Champion Webpage</h2>
		<jsp:include page="homeinclude.jsp"></jsp:include>
		<p><a href="admin/home.htm">Administrer</a></p>

		Test: ${email}
</body>
</html>
