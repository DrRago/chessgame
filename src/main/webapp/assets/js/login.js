$(function () {
    $('#guest-login').click(function () {
        $(this).closest("form").find("input").removeAttr("required");
    })
});