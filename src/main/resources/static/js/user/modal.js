//const loremIpsum = document.getElementById("lorem-ipsum")
//  fetch("https://baconipsum.com/api/?type=all-meat&paras=200&format=html")
//      .then(response => response.text())
//      .then(result => loremIpsum.innerHTML = result)



// 모달의 초기 상태를 display: none;으로 했다가 
// 특정 버튼을 클릭하면 display: flex; 으로 변하게 하면 됩니다.          
// 모달 1 열기 버튼에 클릭 이벤트를 부여합니다.
const btnModal1 = document.getElementById("btn-modal1")
btnModal1.addEventListener("click", () => {
  const modal1 = document.getElementById("modal1")
  modal1.style.display = "flex"
})

// 모달 2 열기 버튼에 클릭 이벤트를 부여합니다.
const btnModal2 = document.getElementById("btn-modal2")
btnModal2.addEventListener("click", () => {
  const modal2 = document.getElementById("modal2")
  modal2.style.display = "flex"
})

// 모달 1의 닫기 버튼에 클릭 이벤트를 부여합니다.
const closeBtn1 = document.querySelector("#modal1 .modal-close-area")
closeBtn1.addEventListener("click", () => {
  const modal1 = document.getElementById("modal1")
  modal1.style.display = "none"
})

// 모달 2의 닫기 버튼에 클릭 이벤트를 부여합니다.
const closeBtn2 = document.querySelector("#modal2 .modal-close-area")
closeBtn2.addEventListener("click", () => {
  const modal2 = document.getElementById("modal2")
  modal2.style.display = "none"
})

// 모달 1과 모달 2 영역 외의 오버레이를 클릭하면 꺼지는 이벤트를 만듭니다.
document.addEventListener("click", e => {
  const evTarget = e.target
  if (evTarget.classList.contains("modal-overlay")) {
    const modal1 = document.getElementById("modal1")
    const modal2 = document.getElementById("modal2")
    modal1.style.display = "none"
    modal2.style.display = "none"
  }
})