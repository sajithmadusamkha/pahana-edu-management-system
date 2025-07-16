$(document).ready(function () {
    // Toggle password visibility
    $("#togglePassword").click(function () {
        const passwordField = $("#floatingPassword");
        const type = passwordField.attr("type") === "password" ? "text" : "password";
        passwordField.attr("type", type);
        $(this).toggleClass("bi-eye bi-eye-slash");
    });

    // Form submission
    $("#loginForm").submit(function (e) {
        e.preventDefault();
        const loginData = {
            username: $("#userName").val(),
            password: $("#floatingPassword").val()
        };
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/pahana_edu_back_end_war_exploded/login", // Placeholder URL
            contentType: "application/json",
            data: JSON.stringify(loginData),
            success: function (response) {
                localStorage.setItem("userRole", response.role);
                $("#message").html(`
                        <div class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
                            <div class="d-flex">
                                <div class="toast-body">
                                    ${response.message}
                                </div>
                                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                            </div>
                        </div>
                    `);
                // Initialize and show the toast
                const toastElement = $("#message .toast");
                const toast = new bootstrap.Toast(toastElement[0], { delay: 1000 });
                toast.show();
                $("#loginForm")[0].reset();
                setTimeout(() => {
                    window.location.href = "./Dashboard.html"; // Redirect to dashboard
                }, 1000);
            },
            error: function () {
                $("#message").html(`<div class="alert alert-danger">Error logging in.</div>`);
            }
        });
    });
});