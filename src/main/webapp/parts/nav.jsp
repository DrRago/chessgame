<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">ChessGame</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/imprint.jsp">Imprint</a>
                </li>
            </ul>
            <div class="row ml-auto">
                <div class="my-2 my-lg-0">

                    <a class="btn btn-outline-danger my-sm-0 my-2" title="logout"
                       href="${pageContext.request.contextPath}/logout.jsp">
                        <i class="fas fa-sign-out-alt"></i>
                    </a>

                </div>
            </div>
        </div>
    </div>
</nav>