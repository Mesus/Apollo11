<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
    <head><link rel="stylesheet" type="text/css" href="minstil.css" />
        <title>Apollo 11 - Login</title>
    </head>
    <body>
    <h1>Inmeta Champion - Login</h1>
    <p>Feil ved innlogging! Vennligst pr�v p� nytt.</p>
    <form method="POST" action="j_security_check">
        Brukernavn: <input name="j_username" type="text">
        Passord: <input name="j_password" type="password">
        <input type="SUBMIT" value="Logg inn">
    </form>

</body></html>