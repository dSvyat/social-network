function changeFollowButton(){
    let elem = document.getElementById("add-friend-button");
    elem.textContent="Request was sent";
    elem.classList.remove("friend-button");
    elem.classList.add("friend-request-sent-button");
    friendRequest(elem.dataset.name);
}


function friendRequest(id) {
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/user/friend-request/" + id, true);
    xhttp.send();
}

function addFollowing() {
    let elem = document.getElementById("follow-button");
    elem.textContent = "Successfully followed!";
    elem.classList.remove("friend-button");
    elem.classList.add("friend-request-sent-button");
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/followerAccept/" + elem.dataset.name, true);
    xhttp.send();
}
