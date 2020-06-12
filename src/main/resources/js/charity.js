var buttonsDiv;
var volunteerElement;
var withholdElement;
var volunteersCountElement;
var isVolunteered;

function goToCreator() {
    let arguments = document.getElementById("creator").innerText.split(" ");

    let creatorName = arguments.slice(1).join(" ");

    window.location.replace('http://localhost:8080/profile/' + creatorName);
}

function volunteer(id) {
    volunteersCountElement = document.getElementById("volunteers");
    let xhr = new XMLHttpRequest();
    let url = "/charity/volunteer/" + id;

    xhr.open("POST", url, true);

    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            writeSuccess("Volunteered to charity Successfully!");

            volunteerElement.parentNode.removeChild(volunteerElement);
            buttonsDiv.insertAdjacentElement("afterbegin", withholdElement);
            isVolunteered = true;

            let text = volunteersCountElement.innerText;
            let volunteersCountText = text.split(" ")[1].split("/")[0];

            let volunteersCount = Number(volunteersCountText);
            volunteersCount++;

            volunteersCountElement.innerText = volunteersCountElement.innerText.replace(volunteersCountText,
                String(volunteersCount));
            }
        if (xhr.readyState === 4 && xhr.status !== 200) {
            writeFailure("Couldn't volunteer to this charity!");
        }
    };

    xhr.send();
}

function withhold(id) {
    volunteersCountElement = document.getElementById("volunteers");

    let xhr = new XMLHttpRequest();
    let url = "/charity/withhold/" + id;

    xhr.open("POST", url, true);

    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            writeSuccess("Withheld from charity Successfully!");

            withholdElement.parentNode.removeChild(withholdElement);
            buttonsDiv.insertAdjacentElement("afterbegin", volunteerElement);
            isVolunteered = false;

            let text = volunteersCountElement.innerText;
            let volunteersCountText = text.split(" ")[1].split("/")[0];

            let volunteersCount = Number(volunteersCountText);
            volunteersCount--;

            volunteersCountElement.innerText = volunteersCountElement.innerText.replace(volunteersCountText,
                String(volunteersCount));
        }
        if (xhr.readyState === 4 && xhr.status !== 200) {
            writeFailure("Couldn't volunteer to this charity!");
        }
    };

    xhr.send();
}

function donate(id) {
    let budgetElement = document.getElementById("budget");
    let amountElement = document.getElementById("amount");
    let amount = Number(amountElement.value);

    let xhr = new XMLHttpRequest();
    let url = "/charity/donate/" + id;

    xhr.open("POST", url, true);

    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        console.log(status);
        if (xhr.readyState === 4 && xhr.status === 200) {
            writeSuccess("Donated to charity Successfully!");

            let text = budgetElement.innerText;
            let budgetText = text.split(" ")[1].split("/")[0];

            let budget = Number(budgetText);
            budget += amount;
            budgetElement.innerText = budgetElement.innerText.replace(budgetText,
                String(budget));
        }
        if (xhr.readyState === 4 && xhr.status !== 200) {
            writeFailure("Couldn't donate to this charity!");
        }
    };

    let data = JSON.stringify({ "amount": amount });

    xhr.send(data);
}

function deleteCharity(id){
    let xhr = new XMLHttpRequest();
    let url = "/charity/delete/" + id;

    xhr.open("DELETE", url, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location.replace("/");
        }
        if (xhr.readyState === 4 && xhr.status !== 200) {
            writeFailure("Couldn't delete this charity!");
        }
    };

    xhr.send();
}

window.onload = function() {
    buttonsDiv = document.getElementById("buttons");
    volunteerElement = document.getElementById("volunteerButton");
    withholdElement = document.getElementById("withholdButton");
    let deleteButtonElement = document.getElementById("deleteButton");
    let creatorElement = document.getElementById("creator");
    let idElement = document.getElementById("charityId");
    let id = idElement.innerHTML;

    let username = getUsernameOfCurrentUser();

    let isVolunteeredRequest = new XMLHttpRequest();
    let isVolunteeredUrl = "/isVolunteered/" + id;

    isVolunteeredRequest.open("GET", isVolunteeredUrl, true);

    isVolunteeredRequest.onreadystatechange = function () {
        if (isVolunteeredRequest.readyState === 4 && isVolunteeredRequest.status === 200) {
            idElement.parentNode.removeChild(idElement);

            if(isVolunteeredRequest.responseText === "true"){
                isVolunteered = true;
                volunteerElement.parentNode.removeChild(volunteerElement);
            } else {
                isVolunteered = false;
                withholdElement.parentNode.removeChild(withholdElement);
            }

            let creatorName = creatorElement.innerText.split(" ")[1];

            if(!(creatorName === username)){
                deleteButtonElement.parentNode.removeChild(deleteButtonElement);
            } else {
                deleteButtonElement.style.visibility = 'visible';
            }
        }
        if (isVolunteeredRequest.readyState === 4 && isVolunteeredRequest.status !== 200) {
        }
    };
    isVolunteeredRequest.send();
};

function getUsernameOfCurrentUser(){
    let username = "";

    let currentUsernameRequest = new XMLHttpRequest();
    let currentUserUrl = "/user";

    currentUsernameRequest.open("GET", currentUserUrl, false);

    currentUsernameRequest.onreadystatechange = function(){
        if (currentUsernameRequest.readyState === 4 && currentUsernameRequest.status === 200){
            username = currentUsernameRequest.responseText;
        } else {
            alert("Error has occurred!")
        }
    };

    currentUsernameRequest.send();

    return username;
}

async function writeSuccess(text) {
    let success = document.createElement("div");
    success.innerHTML = "<div style='color: #28a745'>" + text + "</div>";

    buttonsDiv.appendChild(success);

    await resolveAfter3Seconds(success);
}

async function writeFailure(text) {
    let failure = document.createElement("div");
    failure.innerHTML = "<div style='color: #dc3545'>" + text + "</div>";

    buttonsDiv.appendChild(failure);

    await resolveAfter3Seconds(failure);
}

function resolveAfter3Seconds(element) {
    return new Promise(resolve => {
        setTimeout(() => {
            buttonsDiv.removeChild(element);
        }, 3000);
    });
}
