function moveToFollowed(elem){
    const profileCard = elem.parentNode.parentNode.parentNode;
    elem.classList.add("unfollow-button");
    elem.classList.remove("follow-button");
    elem.textContent = "Unfollow";
    elem.onclick = function() {
        deleteFollowing(elem.getAttribute('data-name')); // Call the function directly
    };
    profileCard.parentNode.removeChild(profileCard);
    const followList = document.querySelector(".follow-list");
    followList.append(profileCard);
}

function moveToRecommended(elem){
    const profileCard = elem.parentNode.parentNode.parentNode;
    elem.classList.add("follow-button");
    elem.classList.remove("unfollow-button");
    elem.textContent = "Follow";
    elem.onclick = function() {
        addFollowing(elem.getAttribute('data-name')); // Call the function directly
    };
    profileCard.parentNode.removeChild(profileCard);
    const recommendedList = document.querySelector(".know-them-list");
    recommendedList.append(profileCard);
}

function deleteFollowing(id) {
    let elem = document.querySelector('[data-name="' + id + '"]');
    moveToRecommended(elem)
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/deleteFollowing/" + id, true);
    xhttp.send();
}

function addFollowing(id) {
    let elem = document.querySelector('[data-name="' + id + '"]');
    moveToFollowed(elem)
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/followerAccept/" + id, true);
    xhttp.send();
}