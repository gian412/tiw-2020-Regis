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

  // Add eventListener on login's show password checkbox
  document.getElementById('show-login-password').addEventListener('click', ()=>{

    let loginPassword = document.getElementById('login-password');

    if (loginPassword.type === 'password') {
      loginPassword.type = 'text';
    } else {
      loginPassword.type = 'password';
    }

  });

  // Add eventListener on sign up's first show password checkbox
  document.getElementById('show-sign-up-password1').addEventListener('click', ()=>{

    let signUpPassword1 = document.getElementById('sign-up-password1');

    if (signUpPassword1.type === 'password') {
      signUpPassword1.type = 'text';
    } else {
      signUpPassword1.type = 'password';
    }

  });

  // Add eventListener on sign up's second show password checkbox
  document.getElementById('show-sign-up-password2').addEventListener('click', ()=>{

    let signUpPassword2 = document.getElementById('sign-up-password2');

    if (signUpPassword2.type === 'password') {
      signUpPassword2.type = 'text';
    } else {
      signUpPassword2.type = 'password';
    }

  });

  // Check that the two passwords are the same
  document.getElementById('sign-up-password2').addEventListener('focusout', ()=>{

    let pwd1 = document.getElementById('sign-up-password1');
    let pwd2 = document.getElementById('sign-up-password2');

    if (pwd1.value !== pwd2.value) {
      pwd1.style.borderColor = '#ff0000';
      pwd2.style.borderColor = '#ff0000';
      document.getElementById('false-sign-up-button').style.display = 'block';
      document.getElementById('sign-up-button').style.display = 'none';
    } else {
      pwd1.style.borderColor = '#cccccc';
      pwd2.style.borderColor = '#cccccc';
      document.getElementById('false-sign-up-button').style.display = 'none';
      document.getElementById('sign-up-button').style.display = 'block';
    }

  });

  // Check on the experience level
  document.getElementById('role-radio').addEventListener('focusout', ()=> {

    let roleValue = document.querySelector('input[name="role"]:checked').value;

    if (roleValue == 0) {
      document.getElementById('experience-radio').style.display = 'none';
    } else if (roleValue == 1) {
      document.getElementById('experience-radio').style.display = 'block';
    }

  });

})();
