$(document).ready(function () {
    $("#registerForm").submit(function (e) {
        e.preventDefault(); // prevent form submit

        // Simple validation
        const name = $("#floatingName").val().trim();
        const accountNumber = $("#floatingAccountNo").val().trim();
        const phone = $("#floatingTel").val().trim();
        const address = $("#floatingAddress").val().trim();
        const password = $("#floatingPassword").val();
        const confirmPassword = $("#floatingConfirmPassword").val();

        // Clear previous error messages
        $('.form-control').removeClass('is-invalid');
        $('.invalid-feedback').remove();
        $("#message").empty();

        let hasErrors = false;

        // Validate name
        if (!name) {
            showFieldError('#floatingName', 'Full name is required');
            hasErrors = true;
        } else if (name.length < 2 || name.length > 100) {
            showFieldError('#floatingName', 'Full name must be between 2 and 100 characters');
            hasErrors = true;
        }

        // Validate account number
        if (!accountNumber) {
            showFieldError('#floatingAccountNo', 'Account number is required');
            hasErrors = true;
        } else if (accountNumber.length < 6 || accountNumber.length > 12) {
            showFieldError('#floatingAccountNo', 'Account number must be between 6 and 12 characters');
            hasErrors = true;
        } else if (!/^[A-Za-z0-9]+$/.test(accountNumber)) {
            showFieldError('#floatingAccountNo', 'Account number must contain only letters and numbers');
            hasErrors = true;
        }

        // Validate phone
        if (!phone) {
            showFieldError('#floatingTel', 'Telephone number is required');
            hasErrors = true;
        } else if (!/^[0-9]{10}$/.test(phone)) {
            showFieldError('#floatingTel', 'Telephone number must be exactly 10 digits');
            hasErrors = true;
        }

        // Validate address
        if (!address) {
            showFieldError('#floatingAddress', 'Address is required');
            hasErrors = true;
        } else if (address.length < 10 || address.length > 255) {
            showFieldError('#floatingAddress', 'Address must be between 10 and 255 characters');
            hasErrors = true;
        }

        // Validate password
        if (!password) {
            showFieldError('#floatingPassword', 'Password is required');
            hasErrors = true;
        } else if (password.length < 8) {
            showFieldError('#floatingPassword', 'Password must be at least 8 characters long');
            hasErrors = true;
        } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(password)) {
            showFieldError('#floatingPassword', 'Password must contain at least one uppercase letter, one lowercase letter, and one number');
            hasErrors = true;
        }

        // Validate confirm password
        if (!confirmPassword) {
            showFieldError('#floatingConfirmPassword', 'Please confirm your password');
            hasErrors = true;
        } else if (password !== confirmPassword) {
            showFieldError('#floatingConfirmPassword', 'Passwords do not match');
            hasErrors = true;
        }

        if (hasErrors) {
            return;
        }

        const customerData = {
            name: name,
            accountNumber: accountNumber.toUpperCase(),
            phone: phone,
            address: address,
            password: password
        };

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/pahana_edu_back_end_war_exploded/register-customer",
            contentType: "application/json",
            data: JSON.stringify(customerData),
            success: function (response) {
                $("#message").html(`<div class="alert alert-success">${response}</div>`);
                $("#registerForm")[0].reset();
            },
            error: function () {
                $("#message").html(`<div class="alert alert-danger">Error registering customer.</div>`);
            }
        });
    });

    $("#togglePassword").click(function () {
        const passwordField = $("#floatingPassword");
        const type = passwordField.attr("type") === "password" ? "text" : "password";
        passwordField.attr("type", type);
        $(this).toggleClass("bi-eye bi-eye-slash");
    });
    $("#toggleConfirmPassword").click(function () {
        const confirmPasswordField = $("#floatingConfirmPassword");
        const type = confirmPasswordField.attr("type") === "password" ? "text" : "password";
        confirmPasswordField.attr("type", type);
        $(this).toggleClass("bi-eye bi-eye-slash");
    });

    // Helper function to show field validation errors
    function showFieldError(fieldSelector, message) {
        const $field = $(fieldSelector);
        $field.addClass('is-invalid');
        $field.after('<div class="invalid-feedback">' + message + '</div>');
    }
});