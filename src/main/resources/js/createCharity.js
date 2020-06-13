let failedElement;

function createCharity(){
    failedElement = document.getElementById("failed");

    let name = document.querySelector('#name');
    let description = document.querySelector('#description');
    let desiredParticipants = document.querySelector('#desiredParticipants');
    let budgetRequired = document.querySelector('#budgetRequired');

    let xhr = new XMLHttpRequest();
    let url = "/charity/create";

    xhr.open("POST", url, true);

    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location.replace("http://localhost:8080/charity/" + xhr.responseText);
        }
        if (xhr.readyState === 4 && xhr.status !== 200) {
            writeFailure("Couldn't create charity");
        }
    };

    let data = JSON.stringify({ "name": name.value, "description": description.value,
        "desiredParticipants": desiredParticipants.value, "budgetRequired": budgetRequired.value });

    xhr.send(data);
}

async function writeFailure(text) {
    let failure = document.createElement("div");
    failure.innerHTML = "<div style='color: #dc3545'>" + text + "</div>";

    failedElement.appendChild(failure);

    await resolveAfter3Seconds(failure);
}

function resolveAfter3Seconds(element) {
    return new Promise(() => {
        setTimeout(() => {
            failedElement.removeChild(element);
        }, 3000);
    });
}
