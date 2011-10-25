<%@ include file="/WEB-INF/jsp/include.jsp"%>

<html>
<head><link rel="stylesheet" type="text/css" href="minstil.css" />
<title>InmetaChamp - signup</title>
</head>
<body>
<h1>Inmeta Champion - Signup</h1>
<form method="POST" action="register.htm">
	<p>Gmail-adresse:
	<input type="text" name="email" /> <br />
	Navn:
	<input type="text" name="username" /> <br />
	<input type="SUBMIT" value="Send forespørsel" />
	</p>
</form>
<p>
    <c:out value="${message}" />
</p>
<a href="/login.htm">Tilbake til login-siden</a>

    </body>
    </html>
