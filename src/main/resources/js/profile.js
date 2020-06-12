function goToCharity(id){
    window.location.replace("http://localhost:8080/charity/" + id);
}

window.onload = function () {
  let genderElement = document.getElementById("gender");
  let gender = genderElement.innerText.split(" ")[1];
  let firstChar = gender[0];
  gender = firstChar + gender.substring(1).toLowerCase();

  genderElement.innerText = gender;
};
