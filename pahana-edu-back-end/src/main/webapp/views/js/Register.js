$(document).ready(function () {
    $("#registerForm").submit(function (e) {
        e.preventDefault(); // prevent form submit

        const customerData = {
            name: $("#floatingName").val(),
            accountNumber: $("#floatingAccountNo").val(),
            phone: $("#floatingTel").val(),
            address: $("#floatingAddress").val(),
            password: $("#floatingPassword").val()
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

    // Client-side password validation
    $("#registerForm").submit(function (e) {
        const password = $("#floatingPassword").val();
        const confirmPassword = $("#floatingConfirmPassword").val();
        if (password !== confirmPassword) {
            e.preventDefault();
            $("#message").html(`<div class="alert alert-danger">Passwords do not match.</div>`);
            return false;
        }
    });
});