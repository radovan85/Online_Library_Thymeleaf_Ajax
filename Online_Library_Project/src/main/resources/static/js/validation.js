function validateBook() {

	var name = $("#name").val();
	var publisher = $("#publisher").val();
	var author = $("#author").val();
	var description = $("#description").val();
	var language = $("#language").val();
	var publishedYear = $("#publishedYear").val();
	var pageNumber = $("#pageNumber").val();
	var price = $("#price").val();
	var cover = $("#cover").val();
	var letter = $("#letter").val();
	var genreId = $("#genreId").val();

	var returnValue = true;
	var publishedYearNum = Number(publishedYear);
	var pageNumberNum = Number(pageNumber);
	var priceNum = Number(price);

	if (name === "") {
		$("#nameError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#nameError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (publisher === "") {
		$("#publisherError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#publisherError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (author === "") {
		$("#authorError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#authorError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (description === "") {
		$("#descriptionError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#descriptionError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (language === "") {
		$("#languageError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#languageError").css({
			"visibility" : "hidden"
		});
	}
	;

	var d = new Date();
	var currentYear = d.getFullYear();
	if (publishedYear === "" || publishedYearNum > currentYear) {
		$("#publishedYearError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#publishedYearError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (pageNumber === "" || pageNumberNum < 10) {
		$("#pageNumberError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#pageNumberError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (price === "" || priceNum < 5) {
		$("#priceError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#priceError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (cover === "") {
		$("#coverError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#coverError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (letter === "") {
		$("#letterError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#letterError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (genreId === "") {
		$("#genreError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#genreError").css({
			"visibility" : "hidden"
		});
	}
	;

	return returnValue;

};

function validateDeliveryAddress() {

	var address = $("#address").val();
	var city = $("#city").val();
	var state = $("#state").val();
	var zipcode = $("#zipcode").val();
	var country = $("#country").val();

	var returnValue = true;

	if (address === "") {
		$("#addressError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#addressError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (city === "") {
		$("#cityError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#cityError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (state === "") {
		$("#stateError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#stateError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (zipcode === "") {
		$("#zipcodeError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#zipcodeError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (country === "") {
		$("#countryError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#countryError").css({
			"visibility" : "hidden"
		});
	}
	;

	return returnValue;
};

function validateGenre() {

	var name = $("#name").val();
	var description = $("#description").val();

	var returnValue = true;

	if (name === "") {
		$("#nameError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#nameError").css({
			"visibility" : "hidden"
		});
	}

	if (description === "") {
		$("#descriptionError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#descriptionError").css({
			"visibility" : "hidden"
		});
	}

	return returnValue;
};

function validateRegForm() {

	var firstName = $("#firstName").val();
	var lastName = $("#lastName").val();
	var email = $("#email").val();
	var password = $("#password").val();
	var dateOfBirthStr = $("#dateOfBirth").val();
	var address = $("#address").val();
	var city = $("#city").val();
	var state = $("#state").val();
	var zipcode = $("#zipcode").val();
	var country = $("#country").val();

	var dateOfBirth = new Date(dateOfBirthStr);

	var regEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/g;
	var date_regex = /^(0[1-9]|1[0-2])\/(0[1-9]|1\d|2\d|3[01])\/(19|20)\d{2}$/g;

	var returnValue = true;

	if (firstName === "") {
		$("#firstNameError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#firstNameError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (lastName === "") {
		$("#lastNameError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#lastNameError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (email === "" || !regEmail.test(email)) {
		$("#emailError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#emailError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (password === "") {
		$("#passwordError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#passwordError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (address === "") {
		$("#addressError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#addressError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (city === "") {
		$("#cityError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#cityError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (state === "") {
		$("#stateError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#stateError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (zipcode === "") {
		$("#zipcodeError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#zipcodeError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (country === "") {
		$("#countryError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#countryError").css({
			"visibility" : "hidden"
		});
	}
	;

	var today = new Date();
	var validMinDate = new Date(today.getFullYear() - 18, today.getMonth(),
			today.getDate(), today.getHours(), today.getMinutes());

	if (dateOfBirth > validMinDate) {
		$("#birthdayError").css({
			"visibility" : "visible"
		});
		returnValue = false
	} else {
		$("#birthdayError").css({
			"visibility" : "hidden"
		});
	}

	return returnValue;
};

function validateReview() {

	var text = $("#text").val();
	var rating = $("#rating").val();

	text = text.trim();
	var returnValue = true;

	if (text === "") {
		$("#textError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#textError").css({
			"visibility" : "hidden"
		});
	}

	if (rating === "") {
		$("#ratingError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#ratingError").css({
			"visibility" : "hidden"
		});
	}

	return returnValue;
};