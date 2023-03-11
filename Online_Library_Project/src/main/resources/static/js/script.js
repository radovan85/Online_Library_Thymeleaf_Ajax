window.onload = redirectHome;

function redirectLogin() {
	$("#ajaxLoadedContent").load("/login");
}

function redirectRegister() {
	$("#ajaxLoadedContent").load("/userRegistration");
}

function redirectAbout() {
	$("#ajaxLoadedContent").load("/aboutUs");
}

function redirectHome() {
	$("#ajaxLoadedContent").load("/home");
}

function redirectAllBooks() {
	$("#ajaxLoadedContent").load("/books/allBooks");
}

function redirectAllBooksById() {
	$("#ajaxLoadedContent").load("/books/allBooksById");
}

function redirectAllBooksByRating() {
	$("#ajaxLoadedContent").load("/books/allBooksByRating");
}

function redirectAllBooksByPrice() {
	$("#ajaxLoadedContent").load("/books/allBooksByPrice");
}

function redirectBookForm() {
	$("#ajaxLoadedContent").load("/admin/createBook");
}

function redirectAllGenres() {
	$("#ajaxLoadedContent").load("/admin/allGenres");
}

function redirectGenreForm() {
	$("#ajaxLoadedContent").load("/admin/createGenre");
}

function redirectUpdateGenre(genreId) {
	$("#ajaxLoadedContent").load("/admin/updateGenre/" + genreId);
}

function redirectUpdateBook(bookId) {
	$("#ajaxLoadedContent").load("/admin/updateBook/" + bookId);
}

function viewBookDetails(bookId) {
	$("#ajaxLoadedContent").load("/books/bookDetails/" + bookId);
}

function addReview(bookId) {
	$("#ajaxLoadedContent").load("/reviews/createReview/" + bookId);
}

function redirectAllReviews() {
	$("#ajaxLoadedContent").load("/admin/allReviews");
}

function redirectAllRequestedReviews() {
	$("#ajaxLoadedContent").load("/admin/allRequestedReviews");
}

function redirectPendingReview(reviewId) {
	$("#ajaxLoadedContent").load("/admin/pendingReviewDetails/" + reviewId);
}

function redirectReviewDetails(reviewId) {
	$("#ajaxLoadedContent").load("/admin/reviewDetails/" + reviewId);
}

function redirectWishlist() {
	$("#ajaxLoadedContent").load("/books/getWishList");
}

function redirectLoyaltyCardInfo() {
	$("#ajaxLoadedContent").load("/loyaltyCards/cardInfo");
}

function redirectCardRequestSent() {
	$("#ajaxLoadedContent").load("/loyaltyCards/cardRequestSent");
}

function redirectAllCards() {
	$("#ajaxLoadedContent").load("/admin/getAllCards");
}

function redirectAllCardRequests() {
	$("#ajaxLoadedContent").load("/admin/checkCardRequests");
}

function addToCart(bookId) {
	$("#ajaxLoadedContent").load("/books/addToCart/" + bookId);
}

function redirectItemAdded() {
	$("#ajaxLoadedContent").load("/cart/addItemCompleted");
}

function redirectCart() {
	$("#ajaxLoadedContent").load("/cart/getCart");
}

function redirectProcessOrder() {
	$("#ajaxLoadedContent").load("/orders/processOrder");
}

function redirectAllOrders() {
	$("#ajaxLoadedContent").load("/admin/allOrders");
}

function redirectAllCustomerOrders(customerId) {
	$("#ajaxLoadedContent").load("/admin/allOrders/" + customerId);
}

function redirectOrderDetails(orderId) {
	$("#ajaxLoadedContent").load("/admin/getOrder/" + orderId);
}

function redirectAllCustomers() {
	$("#ajaxLoadedContent").load("/admin/allCustomers");
}

function redirectCustomerDetails(customerId) {
	$("#ajaxLoadedContent").load("/admin/getCustomer/" + customerId);
}

function redirectAccountDetails() {
	$("#ajaxLoadedContent").load("/accountInfo");
}

function redirectUpdateAddress(addressId) {
	$("#ajaxLoadedContent").load("/addresses/updateAddress/" + addressId);
}

function redirectInvalidPath() {
	$("#ajaxLoadedContent").load("/admin/invalidPath");
}

function redirectGenreError() {
	$("#ajaxLoadedContent").load("/admin/genreError");
}

function redirectLogout() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/loggedout",
		beforeSend : function(xhr) {
			xhr.overrideMimeType("text/plain; charset=x-user-defined");
		},
		success : function(data) {
			window.location.href = "/";
		},
		error : function(error) {
			alert("Logout error");

		}
	});
}

function loginInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();

		$.ajax({
			url : "http://localhost:8080/login",
			type : 'post',
			data : $form.serialize(),
			success : function(response) {
				confirmLoginPass();
			},
			error : function(error) {
				alert("Failed");

			}
		});

	});
};

function confirmLoginPass() {
	$.ajax({
		url : "http://localhost:8080/loginPassConfirm",
		type : "POST",
		success : function(response) {
			checkForSuspension();
		},
		error : function(error) {
			$("#ajaxLoadedContent").load("/loginErrorPage");
		}
	});
}

function checkForSuspension() {
	$.ajax({
		url : "http://localhost:8080/suspensionChecker",
		type : "POST",
		success : function(response) {
			window.location.href = "/";
		},
		error : function(error) {
			$("#ajaxLoadedContent").load("/suspensionPage");
		}
	});
}

function formInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateRegForm()) {
			$.ajax({
				url : "http://localhost:8080/userRegistration",
				type : 'post',
				data : $form.serialize(),
				success : function(response) {
					$("#ajaxLoadedContent").load("/registerComplete");
				},
				error : function(error) {
					$("#ajaxLoadedContent").load("/registerFail");

				}
			});
		}
		;
	});
};

function deliveryAddressInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateDeliveryAddress()) {
			$.ajax({
				url : "http://localhost:8080/addresses/createAddress",
				type : 'post',
				data : $form.serialize(),
				success : function(response) {
					$("#ajaxLoadedContent").load("/accountInfo");
				},
				error : function(error) {
					alert("Failed");
				}
			});
		}
		;
	});
};

function itemInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateItem()) {
			$.ajax({
				url : "http://localhost:8080/cart/addToCart",
				type : 'post',
				data : $form.serialize(),
				success : function(response) {
					redirectItemAdded();
				},
				error : function(error) {
					alert("Maximum 50 books allowed in the cart!");
				}
			});
		}
		;
	});
};

function deleteGenre(genreId) {
	if (confirm('Are you sure you want to delete this genre?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteGenre/" + genreId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllGenres();
			},
			error : function(error) {
				redirectGenreError();
			}
		});
	}
	;
};

function deleteReview(reviewId) {
	if (confirm('Are you sure you want to reject this review?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/rejectReview/" + reviewId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllRequestedReviews();
			},
			error : function(error) {
				alert("Failed");

			}
		});
	}
	;
};

function executeBookForm() {

	var formData = new FormData();
	var files = $('input[type=file]');
	for (var i = 0; i < files.length; i++) {
		if (files[i].value == "" || files[i].value == null) {
			alert("Please provide image");
			return false;
		} else {
			formData.append(files[i].name, files[i].files[0]);
		}
	}
	var formSerializeArray = $("#bookForm").serializeArray();
	for (var i = 0; i < formSerializeArray.length; i++) {
		formData
				.append(formSerializeArray[i].name, formSerializeArray[i].value)
	}
	if (validateBook()) {
		$.ajax({
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			url : "http://localhost:8080/admin/createBook",
			success : function(response) {
				$("#ajaxLoadedContent").load("books/allBooks");
			},
			error : function(error) {
				redirectInvalidPath();
			}
		});
	}
	;
};

function deleteBook(bookId) {
	if (confirm('Are you sure you want to delete this book?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteBook/" + bookId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllBooks();
			},
			error : function(error) {
				redirectInvalidPath();

			}
		});
	}
	;
};

function authorizeReview(reviewId) {
	if (confirm('Are you sure you want to authorize this review?')) {
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/admin/authorizeReview/" + reviewId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllReviews();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
};

function addToWishlist(bookId) {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/books/addToWishList/" + bookId,
		beforeSend : function(xhr) {
			xhr.overrideMimeType("text/plain; charset=x-user-defined");
		},
		success : function(data) {
			alert("Book added to wish list");
		},
		error : function(error) {
			alert("Failed");

		}
	});
}

function removeFromWishlist(bookId) {
	if (confirm('Remove this book from wish list?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/books/deleteFromWishList/" + bookId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectWishlist();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
}

function getLoyaltyCard() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/loyaltyCards/createCardRequest",
		beforeSend : function(xhr) {
			xhr.overrideMimeType("text/plain; charset=x-user-defined");
		},
		success : function(data) {
			redirectCardRequestSent();
		},
		error : function(error) {
			alert("Failed");
		}
	});

}

function authorizeCard(requestId) {
	if (confirm('Authorize loyalty card for this customer?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/authorizeCard/" + requestId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllCardRequests();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
}

function denyCard(requestId) {
	if (confirm('Deny loyalty card for this customer?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/rejectCard/" + requestId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllCardRequests();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
}

function eraseItem(cartId, itemId) {
	if (confirm('Remove this item from cart?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/cart/deleteItem/" + cartId + "/"
					+ itemId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectCart();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
}

function eraseAllItems(cartId) {
	if (confirm('Are you sure you want to clear your cart?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/cart/deleteAllItems/" + cartId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectCart();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
}

function deleteOrder(orderId) {
	if (confirm('Are you sure you want to clear this order?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteOrder/" + orderId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllOrders();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
}

function suspendUser(userId) {
	if (confirm('Are you sure you want to suspend this user?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/suspendUser/" + userId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllCustomers();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
}

function reactivateUser(userId) {
	if (confirm('Are you sure you want to reactivate this user?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/reactivateUser/" + userId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllCustomers();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
}

function redirectOrderConfirmation(cartId) {
	$.ajax({
		url : "http://localhost:8080/orders/confirmOrder/" + cartId,
		beforeSend : function(xhr) {
			xhr.overrideMimeType("text/plain; charset=x-user-defined");
		},
		type : "GET",
		success : function(response) {
			$("#ajaxLoadedContent").load("orders/confirmOrder/" + cartId);
		},
		error : function(error) {
			$("#ajaxLoadedContent").load("/cart/invalidCart");
		}
	});
};

function genreInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateGenre()) {
			$.ajax({
				url : "http://localhost:8080/admin/createGenre",
				type : 'post',
				data : $form.serialize(),
				success : function(response) {
					$("#ajaxLoadedContent").load("/admin/allGenres");
				},
				error : function(error) {
					alert("Failed");
				}
			});
		}
		;
	});
};

function reviewInterceptor(formName) {
	var $form = $("#" + formName);

	$form.on('submit', function(e) {
		e.preventDefault();
		if (validateReview()) {
			$.ajax({
				url : "http://localhost:8080/reviews/createReview",
				type : 'post',
				data : $form.serialize(),
				success : function(response) {
					$("#ajaxLoadedContent")
							.load("/reviews/reviewSentCompleted");
				},
				error : function(error) {
					alert("You have rated this book already");
				}
			});
		}
		;
	});
};

function deleteCustomer(customerId) {
	if (confirm('Are you sure you want to remove this customer?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteCustomer/" + customerId,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=x-user-defined");
			},
			success : function(data) {
				redirectAllCustomers();
			},
			error : function(error) {
				alert("Failed");
			}
		});
	}
	;
};

function ValidatePassword() {
	var password = document.getElementById("password").value;
	var confirmpass = document.getElementById("confirmpass").value;
	if (password != confirmpass) {
		alert("Password does Not Match.");
		return false;
	}
	return true;
};

function validateNumber(e) {
	const pattern = /^\d{0,4}(\.\d{0,4})?$/g;

	return pattern.test(e.key)
};

function validateItem() {
	var quantity = $("#quantity").val();
	var quantityNum = Number(quantity);
	var returnValue = true;

	if (quantity === "" || quantityNum < 1 || quantityNum > 50) {
		$("#quantityError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#quantityError").css({
			"visibility" : "hidden"
		});
	}

	return returnValue;
}

function validateKeyword() {
	var returnValue = true;
	var keyword = $("#keyword").val();

	if (keyword === "") {
		$("#keywordError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#keywordError").css({
			"visibility" : "hidden"
		});
	}

	return returnValue;
};

