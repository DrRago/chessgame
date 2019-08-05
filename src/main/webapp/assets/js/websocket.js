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
    let shouldWarn = location.href.indexOf("game") < 0;

    window.addEventListener("beforeunload", function (e) {
        if (!shouldWarn) {
            return undefined;
        }

        const confirmationMessage = 'It looks like you have been editing something. '
            + 'If you leave before saving, your changes will be lost.';

        (e || window.event).returnValue = confirmationMessage;
        return confirmationMessage;
    });

    webSocket = new WebSocket(`${'https:' === location.protocol ? 'wss' : 'ws'}://${location.host}/websocketendpoint/${parseLobbyID()}/${websocketID}`);

    webSocket.onopen = () => {
        // send ping every 30 seconds to prevent websocket session timeout
        const ping = () => {
            if (webSocket.readyState === WebSocket.OPEN) {
                sendToSocket("ping", "ping");
                setTimeout(ping, 30000)
            }
        }
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
            case "error":
                alert(`Server responded with with error: ${msgObj.value}`);
                break;
            case "updatePlayerNames":
                updateUserList(msgObj.value);
                break;
            case "redirect":
                shouldWarn = false;
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
            case "lobbyPrivacy":
                $("#settingsTable #privacy > input").prop("checked", msgObj.value === "true");
                break;
            case "gameState":
                switch (msgObj.value) {
                    case "KING_VS_KING":
                    case "KING_VS_BISHOP":
                    case "KING_BISHOP_VS_KING":
                    case "KING_KNIGHT_VS_KING":
                    case "KING_BISHOP_VS_KING_BISHOP":
                        alert("DRAW");
                        break;
                    default:
                        alert(msgObj.value);
                }
                finished = true;
                break;
        }
    };

    /**
     * page should be left if the connection is closed
     */
    webSocket.onclose = event => {
        // on abnormal close reason, alert and redirect to lobby overview
        if (event.code === 1000 || event.code === 1001) {
        } else {
            alert(`Connection closed! Reason: ${event.reason}`);
            shouldWarn = false;
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
        players.append($(`<li class="list-group-item chess-${player.color} ${player.isActive ? "" : "disconnect"}" id="${player.id}"><i class="fas fa-chess-queen"></i>${player.name}</li>`))
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

const backToLobby = () => {
    $.confirm({
        title: 'Caution!',
        content: 'The game will end. This can not be reverted!',
        theme: 'material',
        type: 'orange',
        buttons: {
            confirm: () => {
                sendToSocket("lobbyAction", "backToLobby");
            },
            cancel: () => {
            }
        }
    });
};
