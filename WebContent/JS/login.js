(function () {

    // Add eventListener on login's show password checkbox
    document.getElementById('show-login-password').addEventListener('click', ()=>{

        let loginPassword = document.getElementById('password');

        if (loginPassword.type === 'password') {
            loginPassword.type = 'text';
        } else {
            loginPassword.type = 'password';
        }

    });

})();