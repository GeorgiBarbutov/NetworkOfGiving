function searchForUser(){
    let userSearchElement = document.getElementById("userSearch");
    let username = userSearchElement.value;
    window.location.replace("http://localhost:8080/profile/" + username);
}
function searchForCharity(){
    let charitySearchElement = document.getElementById("charitySearch");
    let charityName = charitySearchElement.value;
    window.location.replace("http://localhost:8080/charity/name/" + charityName);
}
