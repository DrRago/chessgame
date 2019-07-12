const startGame = () => {
    sendToSocket("lobbyAction", "startGame");
    // TODO make websocket request, ask for game start and redirect / show alert depending on the answer
};

$(() => {
    $("#settings input").change(() => {
        // TODO make websocket request and change that setting
    });
});

