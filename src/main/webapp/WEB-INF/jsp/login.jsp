<%@ include file="/WEB-INF/jsp/include.jsp"%>

<html>
<head>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/jquery-ui.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/googleapis/0.0.4/googleapis.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/jsapi"></script>
<script type="text/javascript">
  google.load("identitytoolkit", "1.0", {packages: ["ac"]});
</script>
<script type="text/javascript">
  $(function() {
    window.google.identitytoolkit.setConfig({
        developerKey: "AIzaSyAFkyiRu8zivbjQzNtu1Gmf9-pr5AEaMJE",
        companyName: "Inmeta",
        callbackUrl: "http://champs.cloudfoundry.com/callback.htm",
        realm: "",
        userStatusUrl: "/userStatus.htm",
        loginUrl: "/login.htm",
        signupUrl: "/signup.htm",
        homeUrl: "/home.htm",
        logoutUrl: "/signout.htm",
        language: "en",
        idps: ["Gmail"],
        tryFederatedFirst: true,
        useCachedUserStatus: false
    });
    $("#navbar").accountChooser();
  });
</script>
<link rel="stylesheet" type="text/css" href="minstil.css" />
<title>Apollo 11 - Login</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
</head>
<body>
	<h1>Inmeta Champion - Login</h1>
	<p>Vennligst logg inn!</p>
	 <div id="navbar"></div>
</body>
</html>
