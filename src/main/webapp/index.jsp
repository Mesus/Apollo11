<%@ include file="/WEB-INF/jsp/include.jsp" %>
<% request.setCharacterEncoding("UTF8"); %>

<%-- Redirected because we can't set the welcome page to a virtual URL. --%>
<c:redirect url="/welcome.htm"/>