"use strict";
const likeBtn = document.querySelectorAll(".like-btn");
likeBtn.forEach((elem) =>
    elem.addEventListener("click", function () {
        let tempNumb = elem.textContent.replace(/\D/g, "");
        let wasClicked = elem.classList.contains("pressed");
        if (!wasClicked) {
            elem.classList.add("pressed");
            tempNumb++;
            elem.textContent = tempNumb + " ❤";
        } else {
            elem.classList.remove("pressed");
            tempNumb--;
            elem.textContent = tempNumb + " ❤";
        }
    })
);
const newPostBtn = document.querySelector(".posts-text");
const modal = document.querySelector(".modal");
const overlay = document.querySelector(".overlay");
const btnCloseModal = document.querySelector(".close-modal");

const closeModal = function () {
    modal.classList.add("hidden");
    overlay.classList.add("hidden");
};

btnCloseModal.addEventListener("click", closeModal);
overlay.addEventListener("click", closeModal);

newPostBtn.addEventListener("click", function () {
    modal.classList.remove("hidden");
    overlay.classList.remove("hidden");
});

document.addEventListener("keydown", function (event) {
    if (event.key === "Escape") {
        if (!modal.classList.contains("hidden")) {
            closeModal();
        }
    }
});

function addLike(id){
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/addLike/"+id, true);
    xhttp.send();
}

function deletePost(id){
    let post = document.querySelector('[data-id="' + id + '"]').parentNode;
    post.parentNode.removeChild(post);
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/deletePost/"+id, true);
    xhttp.send();
}