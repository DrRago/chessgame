<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:useBean id="user" scope="session" class="de.dhbw.tinf18b4.chess.frontend.user.User"/>

<c:choose>
    <c:when test="${!user.isLoggedIn}">
        <c:out value="${user.isLoggedIn}"/>
        <c:redirect url="/login.jsp"/>
    </c:when>
    <c:otherwise>
        <c:out value="${user.isLoggedIn}"/>
    </c:otherwise>
</c:choose>