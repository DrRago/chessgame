<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${sessionScope.user == null}">
    <c:redirect url="${'login.jsp?redirect='}${pageContext.request.requestURI}"/>
</c:if>
