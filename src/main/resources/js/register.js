let failedElement;

function register(){
    failedElement = document.getElementById("failed");

    let firstName = document.querySelector('#firstName');
    let lastName = document.querySelector('#lastName');
    let username = document.querySelector('#username');
    let password = document.querySelector('#password');
    let passwordConfirm = document.querySelector('#passwordConfirm');
    let age = document.querySelector('#age');
    let location = document.querySelector('#location');
    let isMale = document.querySelector('#male').checked;
    let isFemale = document.querySelector('#female').checked;

    let gender;
    if(isMale){
        gender = 0;
    } else if(isFemale){
        gender = 1;
    } else {
        gender = 2;
    }

    let xhr = new XMLHttpRequest();
    let url = "/register";

    xhr.open("POST", url, true);

    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (this.readyState === 4 && xhr.status === 200) {
            window.location.replace("http://localhost:8080/login");
        }
        if (this.readyState === 4 && xhr.status !== 200) {
            writeFailure("Unable to register");
        }
    };

    let data = JSON.stringify({ "firstName": firstName.value, "username": username.value, "lastName": lastName.value,
        "password": password.value, "passwordConfirm": passwordConfirm.value, "age": age.value,
        "location": location.value, "gender": gender});

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
