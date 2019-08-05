<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">ChessGame</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/imprint">Imprint</a>
                </li>
            </ul>
            <h3 class="mx-auto"><c:out value="${pageTitle}"/></h3>
            <div class="col-sm-2">
                <button id="edit_name" type="button" onclick=""
                        class="btn btn-outline-info">${sessionScope.user.displayName}
                    <i class="fas fa-edit ml-1"></i>
                </button>
            </div>
            <script>
                $('#edit_name').on('click', function () {
                    $.confirm({
                        escapeKey: 'cancel',
                        theme: 'bootstrap',
                        title: 'Change Name',
                        content: '' +
                            '<form action="" class="formName">' +
                            '<div class="form-group">' +
                            '<label>Please enter your new name (ESC to cancel)</label>' +
                            '<input type="text" autofocus placeholder="Your name" class="name form-control" required />' +
                            '</div>' +
                            '</form>',
                        buttons: {
                            formSubmit: {
                                text: 'Submit',
                                btnClass: 'btn-blue',
                                action: function () {
                                    const name = this.$content.find('.name').val();
                                    if (!name) {
                                        $.alert({
                                            theme: "bootstrap",
                                            content: 'provide a valid name'
                                        });
                                        return false;
                                    }
                                    $.ajax(`/user/changeName?name=${"${encodeURIComponent(name)}"}`);
                                    $('#edit_name').contents().first()[0].textContent = name
                                }
                            },
                            cancel: function () {
                                //close
                            },
                        },
                        onContentReady: function () {
                            // bind to events
                            var jc = this;
                            this.$content.find('form').on('submit', function (e) {
                                // if the user submits the form by pressing enter in the field.
                                e.preventDefault();
                                jc.$$formSubmit.trigger('click'); // reference the button and click it
                            });
                        }
                    });
                });
            </script>
        </div>
    </div>
</nav>