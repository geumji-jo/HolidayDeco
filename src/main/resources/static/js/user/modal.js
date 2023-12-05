//const loremIpsum = document.getElementById("lorem-ipsum")
//  fetch("https://baconipsum.com/api/?type=all-meat&paras=200&format=html")
//      .then(response => response.text())
//      .then(result => loremIpsum.innerHTML = result)



// 모달의 초기 상태를 display: none;으로 했다가 
// 특정 버튼을 클릭하면 display: flex; 으로 변하게 하면 됩니다.          
const modal = document.getElementById("modal")
const btnModal = document.getElementById("btn-modal")
btnModal.addEventListener("click", e => {
    modal.style.display = "flex"
})

// X(클로즈) 버튼에 위 예제와 반대되는 이벤트를 부여합니다.
const closeBtn = modal.querySelector(".close-area")
closeBtn.addEventListener("click", e => {
    modal.style.display = "none"
})

// 모달 영역 외의 오버레이를 클릭하면 꺼지는 이벤트를 만들면 됩니다.
modal.addEventListener("click", e => {
    const evTarget = e.target
    if(evTarget.classList.contains("modal-overlay")) {
        modal.style.display = "none"
    }
})

// 모달창이 켜진 상태에서 ESC 버튼을 누르면 모달창이 꺼지게 하기
window.addEventListener("keyup", e => {
    if(modal.style.display === "flex" && e.key === "Escape") {
        modal.style.display = "none"
    }
})