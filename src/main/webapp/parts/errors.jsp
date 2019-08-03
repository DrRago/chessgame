<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
    <c:when test="${param.error == 'lobby_no_found'}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>Holy guacamole!</strong> The lobby you tried to join is not persistent anymore
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:when>
    <c:when test="${param.error == 'login_failed'}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>Holy guacamole!</strong> You entered wrong credentials! Please check them
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:when>
    <c:when test="${param.error == 'lobby_full'}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>Holy guacamole!</strong> You tried to join a lobby that is already full. Try another one from the
            list below
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:when>
    <c:when test="${param.error == 'already_in_lobby'}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>Holy guacamole!</strong> You tried to join a lobby you're already a participant in. Chess is more
            fun with a second player!
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:when>
    <c:otherwise>
    </c:otherwise>
</c:choose>

<script>
    /**
     * Remove a parameter from a url string
     * @param key the key to remove
     * @param sourceURL the source url
     * @returns {string} the new url string
     */
    function removeParam(key, sourceURL) {
        let rtn = sourceURL.split("?")[0],
            param,
            params_arr = [],
            queryString = (sourceURL.indexOf("?") !== -1) ? sourceURL.split("?")[1] : "";
        if (queryString !== "") {
            params_arr = queryString.split("&");
            for (let i = params_arr.length - 1; i >= 0; i -= 1) {
                param = params_arr[i].split("=")[0];
                if (param === key) {
                    params_arr.splice(i, 1);
                }
            }
            rtn = rtn + "?" + params_arr.join("&");
        }
        return rtn;
    }

    const alteredURL = removeParam("error", location.href);
    console.log(alteredURL);
    window.history.pushState("", "", alteredURL);
</script>