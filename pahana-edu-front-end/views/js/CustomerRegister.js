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
});