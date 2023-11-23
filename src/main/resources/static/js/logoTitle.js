  window.onload = function() {
      const logo = document.getElementById('logoHolidayDeco');
      const text = logo.textContent;
      let coloredText = '';

      for (let i = 0; i < text.length; i++) {
          if (i % 2 === 0) {
              coloredText += '<span class="green">' + text[i] + '</span>';
          } else {
              coloredText += '<span class="red">' + text[i] + '</span>';
          }
      }

      logo.innerHTML = coloredText;
  }