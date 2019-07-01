<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- catch if user is already logged in --%>
<c:if test="${sessionScope.loginUser != null}">
    <c:redirect url="index.jsp"/>
</c:if>


<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chess - Login</title>

    <link rel="stylesheet" href="assets/css/login.css">
    <%@include file='parts/styles.jsp' %>
    <%@include file='parts/javascript.jsp' %>

    <script src="assets/js/login.js"></script>
</head>
<body>

<div class="container mt-5 pt-5">
    <%@include file="parts/errors.jsp" %>
    <div class="card mx-auto border-0">
        <div class="card-header border-bottom-0 bg-transparent">
            <ul class="nav nav-tabs justify-content-center pt-4" id="pills-tab" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" id="pills-login-tab" data-toggle="pill" href="#pills-login"
                       role="tab" aria-controls="pills-login"
                       aria-selected="true">Login</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link" id="pills-register-tab" data-toggle="pill" href="#pills-register"
                       role="tab" aria-controls="pills-register"
                       aria-selected="false">Register</a>
                </li>
            </ul>
        </div>

        <div class="card-body pb-4">
            <div class="tab-content" id="pills-tabContent">
                <div class="tab-pane fade show active" id="pills-login" role="tabpanel"
                     aria-labelledby="pills-login-tab">
                    <form action="${pageContext.request.contextPath}/DoLoginOrRegister" method="post">
                        <div class="form-group">
                            <input type="text" name="username" class="form-control" id="login_username"
                                   placeholder="Username"
                                   required autofocus>
                        </div>

                        <div class="form-group">
                            <input type="password" name="password" class="form-control" id="login_password"
                                   placeholder="Password" required>
                        </div>

                        <div class="text-center pt-4">
                            <button type="submit" class="btn btn-primary" name="function" value="login">Login</button>
                        </div>
                        <div class="text-center pt-4">
                            <button type="submit" id="guest-login" class="btn btn-primary" name="function" value="guest">
                                Login as Guest
                            </button>
                        </div>
                    </form>
                </div>

                <div class="tab-pane fade" id="pills-register" role="tabpanel" aria-labelledby="pills-register-tab">
                    <form action="${pageContext.request.contextPath}/DoLoginOrRegister" method="post">
                        <div class="form-group">
                            <input type="text" name="username" id="name" class="form-control" placeholder="Username"
                                   required autofocus>
                        </div>

                        <div class="form-group">
                            <input type="password" name="password" id="password" class="form-control"
                                   placeholder="Set a password" required>
                        </div>

                        <div class="form-group">
                            <input type="password" name="password_confirmation" id="password-confirm"
                                   class="form-control" placeholder="Confirm password" required>
                        </div>

                        <div class="text-center pt-2 pb-1">
                            <button type="submit" class="btn btn-primary" name="function" value="register">Register
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
