(function () {

	// Add event listener on edit profile's show old password
	document.getElementById('show-old-password').addEventListener('click', ()=>{

		let oldPassword = document.getElementById('old-password');

		if(oldPassword.type === 'password') {
			oldPassword.type = 'text';
		} else {
			oldPassword.type = 'password';
		}

	});
	
	 
	document.getElementById('show-new-password-1').addEventListener('click', ()=>{

		let newPassword = document.getElementById('new-password-1');

		if(newPassword.type === 'password') {
			newPassword.type = 'text';
		} else {
			newPassword.type = 'password';
		}

	});

	// Add event listener on edit profile's show old password
	document.getElementById('show-new-password-2').addEventListener('click', ()=>{

		let secondNewPassword = document.getElementById('new-password-2');

		if(secondNewPassword.type === 'password') {
			secondNewPassword.type = 'text';
		} else {
			secondNewPassword.type = 'password';
		}

	});

})();
