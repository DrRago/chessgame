let myColor;
let board;
let isTurn;
let possibilities;
const whiteSquareGrey = '#a9a9a9';
const blackSquareGrey = '#696969';

const addLog = message => {
    $("#logs > table").append(`<tr><td>${message}</td></tr>`);
    const objDiv = document.getElementById("logs");
    objDiv.scrollTop = objDiv.scrollHeight;

};

const onDragStart = (source, piece, position, orientation) => {
    // only pick up pieces for the side to move
    if ((myColor.startsWith('w') && piece.search(/^b/) !== -1) ||
        (myColor.startsWith('b') && piece.search(/^w/) !== -1) || !isTurn) {
        return false
    }
};

const onDrop = (source, target) => {
    removeGreySquares();
    sendToSocket('move', `${source}-${target}`);
};


const initGame = color => {
    myColor = color;

    const config = {
        position: 'start',
        pieceTheme: '/assets/img/chesspieces/wikipedia/{piece}.png',
        draggable: true,
        orientation: myColor,
        onMouseoutSquare: onMouseoutSquare,
        onMouseoverSquare: onMouseoverSquare,
        onDragStart: onDragStart,
        onDrop: onDrop,
    };

    board = Chessboard('board1', config);
};

const handleMove = message => {
    const fen = message.fen;
    isTurn = message.turn === myColor;
    board.position(fen);

    possibilities = message.possibilities;
};

function removeGreySquares () {
    $('#board1 .square-55d63').css('background', '')
}

function greySquare (square) {
    var $square = $('#board1 .square-' + square)

    var background = whiteSquareGrey
    if ($square.hasClass('black-3c85d')) {
        background = blackSquareGrey
    }

    $square.css('background', background)
}

function onMouseoverSquare (square, piece) {
    // get list of possible moves for this square
    possibilities.forEach(value => {
        if (value.piece === square && value.color === myColor && isTurn) {
            let moves = value.possibilities;

            // exit if there are no moves available for this square
            if (moves.length === 0) return;

            // highlight the square they moused over
            greySquare(square);

            // highlight the possible squares for this piece
            for (var i = 0; i < moves.length; i++) {
                greySquare(moves[i])
            }
        }
    });


}

function onMouseoutSquare (square, piece) {
    removeGreySquares()
}