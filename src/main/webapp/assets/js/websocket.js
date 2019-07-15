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
let webSocket;

// acknowledge boolean to check the server state
let acknowledge = false;

// the template for every message sent to the server
let messageTemplate = {
    "content": "",
    "value": ""
};

const playerDisconnect = playerName => {
    $("#playerList li").each((index, player) => {
        player = $(player);
        if (player.text() === playerName) {
            player.addClass("disconnect")
        }
    });
};

const playerConnect = playerName => {
    $("#playerList li").each((index, player) => {
        player = $(player);
        if (player.text() === playerName) {
            player.remove("disconnect")
        }
    });
};

const sendToSocket = (content, value) => {
    let message = {...messageTemplate};
    message.content = content;
    message.value = value;

    console.log("sending to socket:");
    console.log(message);

    webSocket.send(JSON.stringify(message));
};

const alert = message => {
    $.alert({
        title: 'Alert!',
        type: 'red',
        content: message,
        theme: 'material'
    });
};

/**
 * send the move object to the server
 * @param move
 */
const makeMove = move => {
    let message = {...messageTemplate};
    message.content = "move";
    message.value = move;

    webSocket.send(JSON.stringify(message))
};


$(() => {
    webSocket = new WebSocket(`ws://${location.host}/websocketendpoint/${parseLobbyID()}/${websocketID}`);

    /**
     * handler on successful connect to server
     */
    webSocket.onopen = () => {

    };

    /**
     * receive a message from the server
     * the message types are predefined and statically valuated
     *
     * @param message the message string - must be JSON
     */
    webSocket.onmessage = message => {
        console.log("Object received from server:");
        console.log(message);
        const msgObj = JSON.parse(message.data);
        console.log(msgObj);

        switch (msgObj.content) {
            case "fatal":
                alert(`Error from server: ${msgObj.value}`);
                break;
            case "ACK":
                acknowledge = msgObj.value === "OK";
                break;
            case "error":
                alert(`Server responded with with error: ${msgObj.value}`);
                break;
            case "updatePlayerNames":
                updateUserList(msgObj.value);
                break;
            case "redirect":
                location.href = msgObj.value;
                break;
            case "initGame":
                initGame(msgObj.value);
                break;
            case "move":
                handleMove(msgObj.value);
                break;
            case "logs":
                addLog(msgObj.value);
                break;
            case "disconnect":
                playerDisconnect(msgObj.value);
                break;
            case "connect":
                playerConnect(msgObj.value);
                break;
            case "lobbyPrivacy":
                $("#settingsTable #privacy > input").prop("checked", msgObj.value === "true")
        }
    };

    /**
     * page should be left if the connection is closed
     */
    webSocket.onclose = event => {
        console.log(event);
        // on abnormal close reason, alert and redirect to lobby overview
        if (event.code === 1000 || event.code === 1001) {
        } else {
            alert(`Connection closed! Reason: ${event.reason}`);
            location.href = `${location.protocol}//${location.host}/lobby`;
        }
    };

    /**
     * websocket error handler (e.g. unsuccessful connect)
     */
    webSocket.onerror = () => {

    };

    $('#btn-send').click(() => {
        makeMove($('#input').val())
    });
});

const updateUserList = (playerData) => {
    const players = $("#playerList");
    players.find("li").each((index, player) => {
        player = $(player);
        player.remove();
    });
    playerData.forEach(player => {
        players.append($(`<li class="list-group-item chess-${player.color}"><i class="fas fa-chess-queen"></i>${player.name}</li>`))
    })
};

const leaveLobby = () => {
    if (location.href.indexOf("game") >= 0) {
        $.confirm({
            title: 'Caution!',
            content: 'If you leave the lobby the game will end. This can not be reverted!',
            theme: 'material',
            type: 'orange',
            buttons: {
                confirm: () => {
                    sendToSocket("lobbyAction", "leave");
                },
                cancel: () => {
                }
            }
        });
    } else {
        sendToSocket("lobbyAction", "leave");
    }
};
