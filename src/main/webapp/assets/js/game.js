let myColor = 'white';

const onDragStart = (source, piece, position, orientation) => {
    // only pick up pieces for the side to move
    if ((myColor.startsWith('w') && piece.search(/^b/) !== -1) ||
        (myColor.startsWith('b') && piece.search(/^w/) !== -1)) {
        return false
    }
};

$(() => {
    const objDiv = document.getElementById("chat");
    objDiv.scrollTop = objDiv.scrollHeight;


    const config = {
        position: 'start',
        pieceTheme: '/assets/img/chesspieces/wikipedia/{piece}.png',
        draggable: true,
        orientation: myColor,
        onDragStart: onDragStart
    };
    const board = Chessboard('board1', config);
});