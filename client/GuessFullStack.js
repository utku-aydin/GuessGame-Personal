let SERVICE_URL = "http://176.58.119.173:8090/api";
//process.env.GUESS_GAME_APP_SERVICE_URL;

let minMax;

class MinMax {
  constructor(min, max) {
    this.min = min;
    this.max = max;
  }
}

startUp();

function startUp() {
  let tableDisplay = document.getElementById("choose");
  whiteInputs();

  tableDisplay.innerHTML = `<div class="spinner-border" role="status">
    <span class="sr-only">Loading...</span>
  </div>`;

  document.getElementById("overlay").style.display = "none";

  fetch(SERVICE_URL + "/game")
    .then((response) => {
      if (response.ok) {
        tableDisplay.innerHTML = `<table>
         <tr>
           <td>ID</td>
           <td>Answer</td>
           <td>Finished</td>
         </tr>`;
        return response.json();
      } else if (response.status == 404) {
        console.log(response.status);
        tableDisplay.innerHTML = "No games.";
      } else if (response.status == 500) {
        tableDisplay.innerHTML = "Database Error.";
      }
    })
    .then((r) => {
      if (r) {
        min = r[0].id;
        max = r[r.length - 1].id;
        minMax = new MinMax(min, max);
        tableDisplay.innerHTML =
          "Game ID range: " + minMax.min + " to " + minMax.max;
      }
    })
    .catch((err) => {
      console.log("Fail: " + err);
      if (err == "TypeError: Failed to fetch") {
        tableDisplay.innerHTML = "Server offline.";
      }
    });

  tableDisplay.innerHTML += "</table>";
}

$("form#form1").submit(function (event) {
  event.preventDefault();
  let idInput = document.getElementById("getGameId");
  let id = idInput.value;
  let tableDisplay = document.getElementById("gameTable");

  whiteInputs();

  tableDisplay.innerHTML = `<div class="spinner-border" role="status">
    <span class="sr-only">Loading...</span>
  </div>`;

  if (!validateIdInput(id, tableDisplay, idInput)) {
    return;
  }

  fetch(SERVICE_URL + "/game/" + id)
    .then((response) => {
      if (response.ok) {
        tableDisplay.innerHTML = `<table>
         <tr>
           <td id="table-head">ID</td>
           <td id="table-head">Answer</td>
           <td id="table-head">Finished</td>
         </tr>`;
        return response.json();
      } else if (response.status == 404) {
        console.log(response.status);
        tableDisplay.innerHTML = "No game with given id.";
      } else if (response.status == 500) {
        tableDisplay.innerHTML = "Database Error.";
      }
    })
    .then((r) => gameConvert(r))
    .catch((err) => {
      console.log("Fail: " + err);
      if (err == "TypeError: Failed to fetch") {
        tableDisplay.innerHTML = "Server offline.";
      }
    });

  tableDisplay.innerHTML += "</table>";
});

$("form#form2").submit(function (event) {
  event.preventDefault();
  let idInput = document.getElementById("getGameRoundsId");
  let id = idInput.value;
  let tableDisplay = document.getElementById("gameTable");

  whiteInputs();

  if (!validateIdInput(id, tableDisplay, idInput)) {
    return;
  }

  tableDisplay.innerHTML = `<div class="spinner-border" role="status">
    <span class="sr-only">Loading...</span>
  </div>`;

  fetch(SERVICE_URL + "/rounds/" + id)
    .then((response) => {
      if (response.ok) {
        tableDisplay.innerHTML = `<table>
         <tr>
           <td id="table-head">ID</td>
           <td id="table-head">Time</td>
           <td id="table-head">Guess</td>
           <td id="table-head">Exact Matches</td>
           <td id="table-head">Partial Matches</td>
         </tr>`;
        return response.json();
      } else if (response.status == 404) {
        console.log(response.status);
        tableDisplay.innerHTML = "No such game.";
      } else if (response.status == 500) {
        tableDisplay.innerHTML = "Database Error.";
      }
    })
    .then((r) => {
      if (r.length != 0) {
        r.forEach((round) => roundConvert(round));
      } else {
        tableDisplay.innerHTML = "No rounds associated with given game.";
      }
    })
    .catch((err) => {
      console.log("Fail: " + err);
      if (err == "TypeError: Failed to fetch") {
        tableDisplay.innerHTML = "Server offline.";
      }
    });

  tableDisplay.innerHTML += "</table>";
});

$("form#form3").submit(function (event) {
  event.preventDefault();
  let guess = document.getElementById("guess").value;
  let guessString = guess.toString();
  let id = document.getElementById("guessGameId").value;
  let tableDisplay = document.getElementById("gameTable");
  let guessInput = document.getElementById("guess");
  let idInput = document.getElementById("guessGameId");
  let willReturn = false;

  whiteInputs();

  tableDisplay.innerHTML = `<div class="spinner-border" role="status">
    <span class="sr-only">Loading...</span>
  </div>`;

  const data = `{
    "guess": "${guess}",
    "gameId": "${id}"
  }`;

  if (!validateIdInput(id, tableDisplay, idInput)) {
    willReturn = true;
  }

  if (!/^\d+$/.test(guess)) {
    if (!willReturn) {
      tableDisplay.innerHTML = "";
    } else {
      tableDisplay.innerHTML += "<br>";
    }

    tableDisplay.innerHTML += "Guess must be an Integer.";
    guessInput.style.background = "#DE4118";
    console.log("Input not integer: " + typeof id + " " + id);
    willReturn = true;
  }

  if (guessString.length != 4) {
    if (!willReturn) {
      tableDisplay.innerHTML = "";
    } else {
      tableDisplay.innerHTML += "<br>";
    }

    tableDisplay.innerHTML += "Length of guess input incorrect.";
    guessInput.style.background = "#DE4118";
    willReturn = true;
  }

  if (new Set(guessString).size != guessString.length) {
    if (!willReturn) {
      tableDisplay.innerHTML = "";
    } else {
      tableDisplay.innerHTML += "<br>";
    }

    tableDisplay.innerHTML += "Digits of input not unique.";
    guessInput.style.background = "#DE4118";
    willReturn = true;
  }

  if (willReturn) {
    return;
  }

  let header = ["ID", "Timer", "Guess", "Exact", "Partial"];

  fetch(SERVICE_URL + "/guess", {
    method: "POST", // or 'PUT'
    headers: {
      "Content-Type": "application/json",
    },
    body: data,
  })
    .then((response) => {
      if (response.ok) {
        tableDisplay.innerHTML = translateHeader(header);
        return response.json();
      } else if (response.status == 404) {
        console.log(response.status);
        tableDisplay.innerHTML = "No game with given id.";
      } else if (response.status == 500) {
        tableDisplay.innerHTML = "Database Error.";
      }
    })
    .then((data) => {
      console.log("Success:", data);
      if (data.result == "Finished") {
        tableDisplay.innerHTML = "Game already finished.";
        idInput.style.background = "#DE4118";
      } else if (data.result == "BadLength") {
        tableDisplay.innerHTML = "Length of input incorrect.";
      } else if (data.result == "NotUnique") {
        tableDisplay.innerHTML = "Digits of input not unique.";
      } else {
        roundConvert(data);
      }
    })
    .catch((error) => {
      console.log("Fail: " + error);
      if (error == "TypeError: Failed to fetch") {
        tableDisplay.innerHTML = "Server offline.";
      }
    });
});

function getGames() {
  let tableDisplay = document.getElementById("gameTable");

  tableDisplay.innerHTML = `<div class="spinner-border" role="status">
    <span class="sr-only">Loading...</span>
  </div>`;

  whiteInputs();

  fetch(SERVICE_URL + "/game")
    .then((response) => {
      if (response.ok) {
        tableDisplay.innerHTML = `<table>
         <tr>
           <td id="table-head">ID</td>
           <td id="table-head">Answer</td>
           <td id="table-head">Finished</td>
         </tr>`;
        return response.json();
      } else if (response.status == 404) {
        console.log(response.status);
        tableDisplay.innerHTML = "No games.";
      } else if (response.status == 500) {
        tableDisplay.innerHTML = "Database Error.";
      }
    })
    .then((r) => r.forEach((game) => gameConvert(game)))
    .catch((err) => {
      console.log("Fail: " + err);
      if (err == "TypeError: Failed to fetch") {
        tableDisplay.innerHTML = "Server offline.";
      }
    });

  tableDisplay.innerHTML += "</table>";
}

function beginGame() {
  let tableDisplay = document.getElementById("gameTable");

  tableDisplay.innerHTML = `<div class="spinner-border" role="status">
    <span class="sr-only">Loading...</span>
  </div>`;

  whiteInputs();

  fetch(SERVICE_URL + "/begin", {
    method: "POST", // or 'PUT'
    headers: {
      "Content-Type": "application/json",
    },
    body: "",
  })
    .then((response) => {
      if (response.ok) {
        tableDisplay.innerHTML = `<table>
        <tr>
          <td id="table-head">Created ID</td>
        </tr>`;
        return response.json();
      } else if (response.status == 500) {
        tableDisplay.innerHTML = "Database Error.";
        tableDisplay.innerHTML += "</table>";
        return;
      }
    })
    .then((data) => {
      if (data) {
        console.log("Success:", data);
        tableDisplay.innerHTML += data;
        startUp();
      }
    })
    .catch((error) => {
      console.log("Fail: " + error);
      if (error == "TypeError: Failed to fetch") {
        tableDisplay.innerHTML = "Server offline.";
      }
    });

  tableDisplay.innerHTML += "</table>";
}

function showInstructions() {
  let showInstructionsButton = document.getElementById("showInstructions");
  let overlayDisplay = document.getElementById("overlay");

  if (showInstructionsButton.name == "show") {
    showInstructionsButton.name = "hide";
    //showInstructionsButton.innerHTML = "Hide Instructions";
    overlayDisplay.style.display = "block";
  } else if (showInstructionsButton.name == "hide") {
    showInstructionsButton.name = "show";
    //showInstructionsButton.innerHTML = "Show Instructions";
    overlayDisplay.style.display = "none";
  }
}

function gameConvert(game) {
  let tableDisplay = document.getElementById("gameTable");
  tableDisplay.innerHTML += translateGame(game);
}

function roundConvert(round) {
  let tableDisplay = document.getElementById("gameTable");
  tableDisplay.innerHTML += translateRound(round);
}

function translateGame(game) {
  if (game == "Game already finished") {
    return `<tr>
              <td>${game}</td>
            </tr>`;
  }

  if (game.id == "undefined") {
    return "No such game";
  }

  let answer = game.answer;

  if (answer == 0) {
    answer = "Not yet finished";
  }

  return `<tr>
            <td>${game.id}</td>
            <td>${answer}</td>
            <td>${game.finished}</td>
          </tr>`;
}

function translateRound(round) {
  let resultArr = round.result.split(":");
  return `<tr>
            <td>${round.id}</td>
            <td>${round.time}</td>
            <td>${round.guess}</td>
            <td>${resultArr[1]}</td>
            <td>${resultArr[3]}</td>
          </tr>`;
}

function translateHeader(header) {
  let retString = `<table>
      <tr>`;

  for (head of header) {
    retString += `<td id="table-head">${head}</td>`;
  }

  retString += `</tr></table>`;
  return retString;
}

function validateIdInput(id, tableDisplay, idInput) {
  if (!/^-?\d+$/.test(id)) {
    tableDisplay.innerHTML = "ID must be an Integer.";
    idInput.style.background = "#DE4118";
    console.log("Input not integer: " + typeof id + " " + id);
    return false;
  }

  if (id < minMax.min || id > minMax.max) {
    tableDisplay.innerHTML = `Input a game ID between ${minMax.min} and ${minMax.max}.`;
    idInput.style.background = "#DE4118";
    return false;
  }

  return true;
}

function whiteInputs() {
  let idInput1 = document.getElementById("getGameId");
  let idInput2 = document.getElementById("getGameRoundsId");
  let idInput3 = document.getElementById("guessGameId");
  let guessInput = document.getElementById("guess");

  idInput1.style.background = "white";
  idInput2.style.background = "white";
  idInput3.style.background = "white";
  guessInput.style.background = "white";
}
