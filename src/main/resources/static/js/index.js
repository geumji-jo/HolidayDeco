document.addEventListener("DOMContentLoaded", () => {
  const body = document.querySelector("body");
  const MIN_DURATION = 30;

  function makeSnowflake() {
    const snowflake = document.createElement("div");
    const delay = Math.random() * 20;
    const initialOpacity = Math.random();
    const duration = Math.random() * 40 + MIN_DURATION;

    snowflake.classList.add("snowflake");
    snowflake.style.left = `${Math.random() * window.screen.width}px`;
    snowflake.style.animationDelay = `${delay}s`;
    snowflake.style.opacity = initialOpacity;
    snowflake.style.animation = `fall ${duration}s linear`;

    body.appendChild(snowflake);

    setTimeout(() => {
      body.removeChild(snowflake);
      makeSnowflake();
    }, (duration + delay) * 1000);
  }

  for (let index = 0; index < 100; index++) {
    setTimeout(makeSnowflake, 600 * index);
  }
});
