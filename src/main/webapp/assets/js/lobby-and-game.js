// acknowledge boolean to check the server state
let acknowledge = false;

// the template for every message sent to the server
let messageTemplate = {
    "content": "",
    "ID": websocketID,
    "lobbyID": parseLobbyID(),
    "value": ""
};

/**
 * handler on successful connect to server
 * sending a validation object and await an answer
 * if the answer is not received, the server is not working properly
 */
webSocket.onopen = () => {
    sendToSocket("ACK", "alive");

    // ask for player information
    sendToSocket("getPlayerNames", null);
};

const sendToSocket = (content, value) => {
    let message = {...messageTemplate};
    message.content = content;
    message.value = value;

    console.log("sending to socket:");
    console.log(message);

    webSocket.send(JSON.stringify(message));
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

/**
 * receive a message from the server
 * the message types are predefined and statically valuated
 *
 * @param message the message string - must be JSON
 */
webSocket.onmessage = message => {
    const msgObj = JSON.parse(message.data);

    console.log("Object received from server:");
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
            location.href += msgObj.value;
            break;
    }
};

/**
 * page should be left if the connection is closed
 */
webSocket.onclose = event => {
    alert(`Connection closed! Reason: ${event.reason}`);
    // on abnormal close reason, alert and redirect to lobby overview
    if (event.code !== 1000 && event.code !== 1001) {
        location.href = `${location.protocol}//${location.host}/lobby`;
    }
};

/**
 * websocket error handler (e.g. unsuccessful connect)
 */
webSocket.onerror = () => {

};

$(() => {
    $('#btn-send').click(() => {
        makeMove($('#input').val())
    });
});

const updateUserList = (playerData) => {
    const players = $("#playerList");
    players.find("li").each((index, player) => {
        player = $(player);
        if (!playerData.includes(player.text())) {
            player.remove();
        } else {
            playerData.splice(playerData.indexOf(player.text()), 1);
        }
    });
    playerData.forEach(player => {
        players.append($(`<li class="list-group-item chess-white"><i class="fas fa-chess-queen"></i>${player}</li>`))
    })
};