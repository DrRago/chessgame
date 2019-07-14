const startGame = () => {
    sendToSocket("lobbyAction", "startGame");
};

$(() => {
    $("#settings input").change(function () {
        sendToSocket("lobbyPrivacy", $(this).is(":checked"))
    });
});

