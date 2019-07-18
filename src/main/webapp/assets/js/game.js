let myColor;
let board;
let isTurn;
let possibilities;
let autoPlay = false;
const whiteSquareGrey = '#a9a9a9';
const blackSquareGrey = '#696969';
const redSquareColor = '#ff7979';
let finished = false;

$(() => {
    $('#autochess').click(() => {
        autoPlay = !autoPlay;
        if (isTurn && !finished) {
            makeRandomMove(possibilities);
        }
    });
});

const makeRandomMove = possibilities => {
    function getRandomInt(max) {
        return Math.floor(Math.random() * Math.floor(max));
    }

    let moves = [];
    let captures = [];
    possibilities.forEach(piece => {
        if (piece.color === myColor) {
            piece.possibilities.forEach(target => {
                moves = [...moves, `${piece.piece}-${target}`]
            });
            piece.capturePossibilities.forEach(target => {
                captures = [...captures, `${piece.piece}-${target}`]
            });
        }
    });
    if (moves.length !== 0 || captures.length !== 0) {
        setTimeout(() => {
            if (moves.length === 0 || (captures.length !== 0 && getRandomInt(4) > 0)) {
                sendToSocket('move', captures[getRandomInt(captures.length)])
            } else {
                sendToSocket('move', moves[getRandomInt(moves.length)])
            }

        }, 150);
    }
};

const addLog = message => {
    if (!Array.isArray(message)) message = [message];

    for (let logEntry in message) {
        $("#logs table").prepend(`<tr class="${message[logEntry].player}"><td>${message[logEntry].entry}</td></tr>`);
    }

    $('#lastLog').text(`(${message.slice(-1).pop().entry})`);

    $('#logs .card-body').animate({scrollTop: 0}, "fast");

};

const onDragStart = (source, piece, position, orientation) => {
    // only pick up pieces for the side to move
    if ((myColor.startsWith('w') && piece.search(/^b/) !== -1) ||
        (myColor.startsWith('b') && piece.search(/^w/) !== -1) || !isTurn || finished) {
        return false
    }
};

const onDrop = (source, target) => {
    removeGreySquares();
    if (target === "offboard" || source === target || finished) return false;
    sendToSocket('move', `${source}-${target}`);
};


const initGame = color => {
    myColor = color;

    const config = {
        position: '',
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

    if (autoPlay && isTurn && !finished) {
        makeRandomMove(possibilities);
    }

};

function removeGreySquares() {
    $('#board1 .square-55d63').css('background', '')
}

function greySquare(square) {
    var $square = $('#board1 .square-' + square);

    var background = whiteSquareGrey;
    if ($square.hasClass('black-3c85d')) {
        background = blackSquareGrey
    }

    $square.css('background', background)
}

function redSquare(square) {
    const $square = $('#board1 .square-' + square);

    $square.css('background', redSquareColor)
}

function onMouseoverSquare(square, piece) {
    // get list of possible moves for this square
    possibilities.forEach(value => {
        if (value.piece === square && value.color === myColor && isTurn) {
            let i;
            let moves = value.possibilities;
            const captureMoves = value.capturePossibilities;

            // exit if there are no moves available for this square
            if (moves.length === 0 && captureMoves.length === 0) return;

            // highlight the square they moused over
            greySquare(square);

            // highlight the possible squares for this piece
            for (i = 0; i < moves.length; i++) {
                greySquare(moves[i])
            }

            for (i = 0; i < captureMoves.length; i++) {
                redSquare(captureMoves[i])
            }
        }
    });


}

function onMouseoutSquare(square, piece) {
    removeGreySquares()
}