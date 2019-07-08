/**
 * parser for the lobbyID from the url
 *
 * @returns {string} the lobby ID
 */
const parseLobbyID = () => {
    // this regex matches the lobbyID as finder-group 1
    const regexGame = /lobby\/(.*?)\//gm;
    const regexLobby = /lobby\/(.*)/gm;
    let result;
    if (!(result = regexGame.exec(location.href))) {
        return regexLobby.exec(location.href)[1];
    }
    return result[1];
};

// the websocket connection
const webSocket = new WebSocket(`ws://${location.host}/websocketendpoint`);