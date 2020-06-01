(function () {

  // Add eventListener to the login button
  document.getElementById('login').addEventListener('click', ()=>{

  let loginDiv = document.getElementById('login-modal-box');
  loginDiv.style.display='block';

  });

  // Add eventListener to the sign up button
  document.getElementById('sign-up').addEventListener('click', ()=>{

    let signUpDiv= document.getElementById('sign-up-modal-box');
    signUpDiv.style.display='block';

  });

  // Add eventListener on window to close the modal boxes when there is a click outside them
  window.addEventListener('click', (e)=>{

    let loginDiv = document.getElementById('login-modal-box');
    let signUpDiv = document.getElementById('sign-up-modal-box');

    if (e.target===loginDiv) {
      loginDiv.style.display = 'none';
    }

    if (e.target === signUpDiv) {
      signUpDiv.style.display = 'none';
    }

  });

  // Add eventListener on close login form button
  document.getElementById('login-close').addEventListener('click', ()=>{

      let loginDiv= document.getElementById('login-modal-box');
      loginDiv.style.display='none'

  });

  // Add eventListener on close sign up form button
  document.getElementById('sign-up-close').addEventListener('click', ()=>{

    let signUpDiv= document.getElementById('sign-up-modal-box');
    signUpDiv.style.display='none'

  });

})();
