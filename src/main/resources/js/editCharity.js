let failedElement;

function editCharity(){
    failedElement = document.getElementById("failed");

    let name = document.querySelector('#name');
    let description = document.querySelector('#description');
    let desiredParticipants = document.querySelector('#desiredParticipants');
    let budgetRequired = document.querySelector('#budgetRequired');

    let charityId = window.location.pathname.split("/")[3];

    let xhr = new XMLHttpRequest();
    let url = "/charity/edit/" + charityId;

    xhr.open("POST", url, true);

    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location.replace("http://localhost:8080/charity/" + charityId);
        }
        if (xhr.readyState === 4 && xhr.status !== 200) {
            writeFailure("Unable to edit");
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
