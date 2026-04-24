const SAVE_KEY = "farawayland-save";
const LEADERBOARD_KEY = "farawayland-leaderboard";

const DIRS = [
    [-1, 0, 2],
    [0, 1, 3],
    [1, 0, 0],
    [0, -1, 1]
]; //neighbours of a pipe

const LEVELS = {
    easy: {
        label: "Easy",
        grid: [
            [
                { type: "consumer", connections: [0, 1, 0, 0] },
                { type: "corner", connections: [0, 0, 1, 1] },
                { type: "consumer", connections: [0, 0, 1, 0] },
                { type: "consumer", connections: [0, 0, 1, 0] }
            ],
            [
                { type: "consumer", connections: [0, 1, 0, 0] },
                { type: "tee", connections: [1, 1, 0, 1] },
                { type: "tee", connections: [1, 0, 1, 1] },
                { type: "straight", connections: [1, 0, 1, 0] }
            ],
            [
                { type: "consumer", connections: [0, 0, 1, 0] },
                { type: "consumer", connections: [0, 1, 0, 0] },
                { type: "source", connections: [1, 1, 0, 1] },
                { type: "tee", connections: [1, 0, 1, 1] }
            ],
            [
                { type: "corner", connections: [1, 1, 0, 0] },
                { type: "straight", connections: [0, 1, 0, 1] },
                { type: "straight", connections: [0, 1, 0, 1] },
                { type: "corner", connections: [1, 0, 0, 1] }
            ]
        ]
    },
    medium: {
        label: "Intermediate",
        grid: [
            [
                { type: "consumer", connections: [0, 0, 1, 0] },
                { type: "corner", connections: [0, 1, 1, 0] },
                { type: "consumer", connections: [0, 0, 0, 1] },
                { type: "consumer", connections: [0, 0, 1, 0] },
                { type: "consumer", connections: [0, 0, 1, 0] }
            ],
            [
                { type: "corner", connections: [1, 1, 0, 0] },
                { type: "tee", connections: [1, 1, 0, 1] },
                { type: "tee", connections: [1, 0, 1, 1] },
                { type: "corner", connections: [1, 0, 0, 1] },
                { type: "straight", connections: [1, 0, 1, 0] }
            ],
            [
                { type: "consumer", connections: [0, 1, 0, 0] },
                { type: "straight", connections: [0, 1, 0, 1] },
                { type: "source", connections: [1, 0, 1, 1] },
                { type: "consumer", connections: [0, 0, 1, 0] },
                { type: "straight", connections: [1, 0, 1, 0] }
            ],
            [
                { type: "corner", connections: [0, 1, 1, 0] },
                { type: "corner", connections: [0, 0, 1, 1] },
                { type: "tee", connections: [1, 1, 1, 0] },
                { type: "tee", connections: [1, 1, 0, 1] },
                { type: "tee", connections: [1, 0, 1, 1] }
            ],
            [
                { type: "consumer", connections: [1, 0, 0, 0] },
                { type: "corner", connections: [1, 1, 0, 0] },
                { type: "corner", connections: [1, 0, 0, 1] },
                { type: "consumer", connections: [0, 1, 0, 0] },
                { type: "corner", connections: [1, 0, 0, 1] }
            ]
        ]
    },
    hard: {
        label: "Hard",
        grid: [
            [
                { type: "consumer", connections: [0, 1, 0, 0] },
                { type: "corner", connections: [0, 0, 1, 1] },
                { type: "consumer", connections: [0, 0, 1, 0] },
                { type: "consumer", connections: [0, 1, 0, 0] },
                { type: "straight", connections: [0, 1, 0, 1] },
                { type: "tee", connections: [0, 1, 1, 1] },
                { type: "consumer", connections: [0, 0, 0, 1] }
            ],
            [
                { type: "consumer", connections: [0, 0, 1, 0] },
                { type: "corner", connections: [1, 1, 0, 0] },
                { type: "tee", connections: [1, 1, 0, 1] },
                { type: "tee", connections: [0, 1, 1, 1] },
                { type: "consumer", connections: [0, 0, 0, 1] },
                { type: "straight", connections: [1, 0, 1, 0] },
                { type: "consumer", connections: [0, 0, 1, 0] }
            ],
            [
                { type: "corner", connections: [1, 1, 0, 0] },
                { type: "tee", connections: [0, 1, 1, 1] },
                { type: "consumer", connections: [0, 0, 0, 1] },
                { type: "tee", connections: [1, 1, 1, 0] },
                { type: "straight", connections: [0, 1, 0, 1] },
                { type: "tee", connections: [1, 1, 0, 1] },
                { type: "corner", connections: [1, 0, 0, 1] }
            ],
            [
                { type: "consumer", connections: [0, 0, 1, 0] },
                { type: "tee", connections: [1, 1, 1, 0] },
                { type: "tee", connections: [0, 1, 1, 1] },
                { type: "source", connections: [1, 0, 1, 1] },
                { type: "corner", connections: [0, 1, 1, 0] },
                { type: "tee", connections: [0, 1, 1, 1] },
                { type: "corner", connections: [0, 0, 1, 1] }
            ],
            [
                { type: "tee", connections: [1, 1, 1, 0] },
                { type: "tee", connections: [1, 0, 1, 1] },
                { type: "consumer", connections: [1, 0, 0, 0] },
                { type: "tee", connections: [1, 1, 1, 0] },
                { type: "tee", connections: [1, 0, 1, 1] },
                { type: "straight", connections: [1, 0, 1, 0] },
                { type: "straight", connections: [1, 0, 1, 0] }
            ],
            [
                { type: "consumer", connections: [1, 0, 0, 0] },
                { type: "tee", connections: [1, 1, 1, 0] },
                { type: "consumer", connections: [0, 0, 0, 1] },
                { type: "consumer", connections: [1, 0, 0, 0] },
                { type: "straight", connections: [1, 0, 1, 0] },
                { type: "straight", connections: [1, 0, 1, 0] },
                { type: "straight", connections: [1, 0, 1, 0] }
            ],
            [
                { type: "consumer", connections: [0, 1, 0, 0] },
                { type: "corner", connections: [1, 0, 0, 1] },
                { type: "consumer", connections: [0, 1, 0, 0] },
                { type: "straight", connections: [0, 1, 0, 1] },
                { type: "corner", connections: [1, 0, 0, 1] },
                { type: "consumer", connections: [1, 0, 0, 0] },
                { type: "consumer", connections: [1, 0, 0, 0] }
            ]
        ]
    }
};

const state = {
    player: "",
    difficulty: "easy",
    board: [],
    seconds: 0,
    timer: null,
    won: false,
    tileSize: 80
};

const imageCache = {};
const imageNames = {
    straight: "straight.png",
    straightFlow: "straightflow.png",
    corner: "corner.png",
    cornerFlow: "cornerflow.png",
    tee: "tee.png",
    teeFlow: "teeflow.png",
    consumer: "consumer.png",
    consumerFlow: "consumerflow.png",
    sourceFlow: "sourceflow.png"
};

const hero = document.querySelector("[data-hero]");
const menuScreen = document.querySelector('[data-screen="menu"]');
const gameScreen = document.querySelector('[data-screen="game"]');
const form = document.querySelector(".start-form");
const formMessage = document.querySelector(".form-message");
const leaderboardList = document.querySelector(".leaderboard-list");
const leaderboardEmpty = document.querySelector(".leaderboard-empty");
const playerLabel = document.querySelector("[data-player-label]");
const difficultyLabel = document.querySelector("[data-difficulty-label]");
const timeLabel = document.querySelector("[data-time-label]");
const overlay = document.querySelector("[data-overlay]");
const overlayMessage = document.querySelector("[data-overlay-message]");
const canvas = document.querySelector("[data-board]");
const ctx = canvas.getContext("2d");

function copyBoard(board) {
    return board.map((row) => row.map((cell) => ({
        type: cell.type,
        connections: [...cell.connections],
        watered: false
    })));
}

function rotateCell(cell) {
    return {
        type: cell.type,
        connections: [cell.connections[3], cell.connections[0], cell.connections[1], cell.connections[2]],
        watered: false
    };
}

function randomBoard(levelName) {
    const board = copyBoard(LEVELS[levelName].grid);

    board.forEach((row, rowIndex) => row.forEach((cell, colIndex) => {
        let turns = Math.floor(Math.random() * 4);

        while (turns > 0) {
            board[rowIndex][colIndex] = rotateCell(board[rowIndex][colIndex]);
            turns -= 1;
        }
    }));

    return board;
}

function boardEquals(a, b) {
    return a.every((row, rowIndex) => row.every((cell, colIndex) => (
        cell.connections.every((value, dirIndex) => value === b[rowIndex][colIndex].connections[dirIndex])
    )));
}

function findSource(board) {
    for (let r = 0; r < board.length; r += 1) {
        for (let c = 0; c < board.length; c += 1) {
            if (board[r][c].type === "source") {
                return [r, c];
            }
        }
    }

    return null;
}

function checkBoard(board) {
    board.forEach((row) => row.forEach((cell) => {
        cell.watered = false;
    }));

    const source = findSource(board);

    if (!source) {
        return { win: false };
    }

    const queue = [source];
    const seen = new Set([`${source[0]}-${source[1]}`]);
    board[source[0]][source[1]].watered = true;
    let edges = 0;
    let leaks = 0;

    while (queue.length > 0) {
        const [row, col] = queue.shift();
        const cell = board[row][col];

        for (let dir = 0; dir < 4; dir += 1) {
            if (cell.connections[dir] !== 1) {
                continue;
            }

            const nextRow = row + DIRS[dir][0];
            const nextCol = col + DIRS[dir][1];
            const opposite = DIRS[dir][2];
            const next = board[nextRow]?.[nextCol];

            if (!next || next.connections[opposite] !== 1) {
                leaks += 1;
                continue;
            }

            if (dir === 1 || dir === 2) {
                edges += 1;
            }

            const key = `${nextRow}-${nextCol}`;

            if (!seen.has(key)) {
                seen.add(key);
                next.watered = true;
                queue.push([nextRow, nextCol]);
            }
        }
    }

    const total = board.length * board.length;
    const allConnected = seen.size === total;
    const allConsumersWatered = board.every((row) => row.every((cell) => (
        cell.type !== "consumer" || cell.watered
    )));

    board.forEach((row, rowIndex) => row.forEach((cell, colIndex) => {
        for (let dir = 0; dir < 4; dir += 1) {
            if (cell.connections[dir] !== 1) {
                continue;
            }

            const nextRow = rowIndex + DIRS[dir][0];
            const nextCol = colIndex + DIRS[dir][1];
            const opposite = DIRS[dir][2];
            const next = board[nextRow]?.[nextCol];

            if (!next || next.connections[opposite] !== 1) {
                leaks += 1;
            }
        }
    }));

    const noLeaks = leaks === 0;
    const noCycles = allConnected && edges === total - 1;
    const win = allConnected && allConsumersWatered && noLeaks && noCycles;
    return { win };
}

function makeStartBoard(levelName) {
    const solved = copyBoard(LEVELS[levelName].grid);
    let board = randomBoard(levelName);
    let tries = 0;

    while ((checkBoard(board).win || boardEquals(board, solved)) && tries < 40) {
        board = randomBoard(levelName);
        tries += 1;
    }

    if (checkBoard(board).win) {
        board[0][0] = rotateCell(board[0][0]);
    }

    return board;
}

function formatTime(seconds) {
    const minutes = String(Math.floor(seconds / 60)).padStart(2, "0");
    const secs = String(seconds % 60).padStart(2, "0");
    return `${minutes}:${secs}`;
}

function showMenu() {
    hero.hidden = false;
    menuScreen.classList.add("screen-active");
    gameScreen.classList.remove("screen-active");
}

function showGame() {
    hero.hidden = true;
    menuScreen.classList.remove("screen-active");
    gameScreen.classList.add("screen-active");
}

function stopTimer() {
    if (state.timer !== null) {
        clearInterval(state.timer);
        state.timer = null;
    }
}

function startTimer() {
    stopTimer();
    state.timer = setInterval(() => {
        state.seconds += 1;
        timeLabel.textContent = formatTime(state.seconds);
    }, 1000);
}

function tileRotation(type, connections) {
    const base = {
        straight: [1, 0, 1, 0],
        corner: [1, 1, 0, 0],
        tee: [1, 1, 1, 0],
        source: [1, 1, 0, 1],
        consumer: [0, 1, 0, 0]
    }[type];

    let current = [...base];

    for (let i = 0; i < 4; i += 1) {
        if (current.every((value, index) => value === connections[index])) {
            return i;
        }

        current = [current[3], current[0], current[1], current[2]];
    }

    return 0;
}

function drawPipe(type, rotation, watered, x, y, size) {
    const imageKey = type === "source" || watered ? `${type}Flow` : type;
    const fileName = imageNames[imageKey];

    if (!imageCache[fileName]) {
        const image = new Image();
        image.onload = () => drawBoard();
        image.src = `images/${fileName}`;
        imageCache[fileName] = image;
    }

    const image = imageCache[fileName];

    if (image.complete) {
        ctx.save();
        ctx.translate(x + size / 2, y + size / 2);
        ctx.rotate(rotation * Math.PI / 2);
        ctx.drawImage(image, -size / 2, -size / 2, size, size);
        ctx.restore();
    }
}

function drawBoard() {
    const size = state.board.length;
    state.tileSize = Math.floor(560 / size);
    canvas.width = state.tileSize * size;
    canvas.height = state.tileSize * size;

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = "#ffffff";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    state.board.forEach((row, rowIndex) => row.forEach((cell, colIndex) => {
        drawPipe(
            cell.type,
            tileRotation(cell.type, cell.connections),
            cell.watered,
            colIndex * state.tileSize,
            rowIndex * state.tileSize,
            state.tileSize
        );
    }));

    ctx.strokeStyle = "#cfcfcf";
    ctx.lineWidth = 1;

    for (let i = 0; i <= size; i += 1) {
        const p = i * state.tileSize;
        ctx.beginPath();
        ctx.moveTo(p, 0);
        ctx.lineTo(p, canvas.height);
        ctx.stroke();
        ctx.beginPath();
        ctx.moveTo(0, p);
        ctx.lineTo(canvas.width, p);
        ctx.stroke();
    }
}

function readLeaderboard() {
    return JSON.parse(localStorage.getItem(LEADERBOARD_KEY) ?? "[]");
}

function writeLeaderboard(items) {
    localStorage.setItem(LEADERBOARD_KEY, JSON.stringify(items));
}

function renderLeaderboard() {
    const items = readLeaderboard();
    leaderboardList.innerHTML = items.map((item) => `
        <li>${item.player} - ${item.difficulty} - ${formatTime(item.time)}</li>
    `).join("");
    leaderboardEmpty.hidden = items.length > 0;
}

function saveWin() {
    const items = readLeaderboard();
    items.push({
        player: state.player,
        difficulty: LEVELS[state.difficulty].label,
        time: state.seconds
    });
    items.sort((a, b) => a.time - b.time || a.player.localeCompare(b.player));
    writeLeaderboard(items.slice(0, 10));
}

function updateGame() {
    const result = checkBoard(state.board);
    playerLabel.textContent = state.player;
    difficultyLabel.textContent = LEVELS[state.difficulty].label;
    timeLabel.textContent = formatTime(state.seconds);
    drawBoard();

    if (result.win && !state.won) {
        state.won = true;
        stopTimer();
        localStorage.removeItem(SAVE_KEY);
        saveWin();
        renderLeaderboard();
        overlayMessage.textContent = `${state.player} finished in ${formatTime(state.seconds)}.`;
        overlay.hidden = false;
    }
}

function startGame(player, difficulty, board, seconds) {
    state.player = player;
    state.difficulty = difficulty;
    state.board = board;
    state.seconds = seconds;
    state.won = false;
    overlay.hidden = true;
    overlayMessage.textContent = "";
    showGame();
    startTimer();
    updateGame();
}

function saveGame() {
    localStorage.setItem(SAVE_KEY, JSON.stringify({
        player: state.player,
        difficulty: state.difficulty,
        board: state.board,
        seconds: state.seconds
    }));
}

function setContinueButton() {
    document.querySelector('[data-action="continue"]').disabled = !localStorage.getItem(SAVE_KEY);
}

function backToMenu() {
    stopTimer();
    showMenu();
    renderLeaderboard();
    setContinueButton();
}

form.addEventListener("submit", (event) => {
    event.preventDefault();

    const data = new FormData(form);
    const player = String(data.get("playerName") ?? "").trim();
    const difficulty = String(data.get("difficulty") ?? "easy");

    if (!player) {
        formMessage.textContent = "Enter your name first.";
        return;
    }

    formMessage.textContent = "";
    startGame(player, difficulty, makeStartBoard(difficulty), 0);
});

document.body.addEventListener("click", (event) => {
    const button = event.target.closest("[data-action]");

    if (!button) {
        return;
    }

    const action = button.dataset.action;

    if (action === "toggle-rules") {
        document.querySelector('[data-panel="rules"]').classList.toggle("is-open");
    }

    if (action === "toggle-leaderboard") {
        document.querySelector('[data-panel="leaderboard"]').classList.toggle("is-open");
    }

    if (action === "continue") {
        const saved = localStorage.getItem(SAVE_KEY);

        if (!saved) {
            formMessage.textContent = "No saved game found.";
            setContinueButton();
            return;
        }

        const game = JSON.parse(saved);
        startGame(game.player, game.difficulty, game.board, game.seconds);
    }

    if (action === "save-quit") {
        saveGame();
        formMessage.textContent = "Game saved.";
        backToMenu();
    }

    if (action === "quit-menu" || action === "back-after-win") {
        backToMenu();
    }
});

canvas.addEventListener("click", (event) => {
    if (!gameScreen.classList.contains("screen-active") || state.won) {
        return;
    }

    const rect = canvas.getBoundingClientRect();
    const scaleX = canvas.width / rect.width;
    const scaleY = canvas.height / rect.height;
    const x = (event.clientX - rect.left) * scaleX;
    const y = (event.clientY - rect.top) * scaleY;
    const row = Math.floor(y / state.tileSize);
    const col = Math.floor(x / state.tileSize);

    if (!state.board[row] || !state.board[row][col]) {
        return;
    }

    state.board[row][col] = rotateCell(state.board[row][col]);
    updateGame();
});

renderLeaderboard();
setContinueButton();
