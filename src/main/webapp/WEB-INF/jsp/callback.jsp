<%@ include file="/WEB-INF/jsp/include.jsp"%>



<html>
<head> <script type='text/javascript' src='https://ajax.googleapis.com/jsapi'></script>
<script type='text/javascript'>
  google.load("identitytoolkit", "1.0", {packages: ["notify"]});
</script>
<script type='text/javascript'>
  window.google.identitytoolkit.notifyFederatedSuccess({ "email": "${email}", "registered": "${registered}" });
  // use window.google.identitytoolkit.notifyFederatedError(); in case of error
</script>

</head>
<body>
${email}
</body>
</html>