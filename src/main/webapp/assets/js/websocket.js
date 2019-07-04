// the websocket connection
const webSocket = new WebSocket(`ws://${location.host}/websocketendpoint`);

/**
 * parser for the lobbyID from the url
 *
 * @returns {string} the lobby ID
 */
const parseLobbyID = () => {
    // this regex matches the lobbyID as finder-group 1
    const regex = /lobby\/(.*?)\//gm;
    return regex.exec(location.href)[1];
};

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
    let message = {...messageTemplate};
    message.content = "ACK";
    message.value = "alive";

    webSocket.send(JSON.stringify(message));

    // check for server response
    setTimeout(() => {
        if (!acknowledge) {
            alert("Server did not respond! ERROR 500\nredirecting...");
            location.href = `${location.protocol}//${location.host}/lobby`;
        }
    }, 5000)
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
    }
};

/**
 * page should be left if the connection is closed
 */
webSocket.onclose = () => {
    alert("Connection closed, redirecting...");
    location.href = `${location.protocol}//${location.host}/lobby`;
};

/**
 * websocket error handler (e.g. unsuccessful connect)
 */
webSocket.onerror = () => {

};

$(() => {
    $('#btn-send').click(() => {
        makeMove($('#input').val())
    })
});