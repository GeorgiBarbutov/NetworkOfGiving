function createCharity(){
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
            alert("Couldn't create charity")
        }
    };

    let data = JSON.stringify({ "name": name.value, "description": description.value,
        "desiredParticipants": desiredParticipants.value, "budgetRequired": budgetRequired.value });

    xhr.send(data);
}
