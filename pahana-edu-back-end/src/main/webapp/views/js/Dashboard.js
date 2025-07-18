$(document).ready(function () {
    // Toggle sidebar on mobile
    $('.btn-outline-primary').click(function () {
        $('.sidebar').toggleClass('active');
    });

    // Handle sidebar item active state and content visibility
    $('.sidebar .nav-link').click(function (e) {
        e.preventDefault(); // Prevent default link behavior
        // Remove active class from all sidebar links
        $('.sidebar .nav-link').removeClass('active');
        // Add active class to clicked link
        $(this).addClass('active');

        // Hide all content sections
        $('.content .row.g-4.mb-4').hide();
        $('.content .card.mb-4').hide();
        $('.content .card').last().hide();
        $('.content .manage-customers').hide();

        // Show content based on clicked item
        const navItem = $(this).parent().data('nav');
        console.log('Clicked nav item:', navItem, 'Manage Customers exists:', $('.content .manage-customers').length); // Debug
        if (navItem === 'customers') {
            $('.content .manage-customers').show();
        } else if (navItem === 'home') {
            $('.content .row.g-4.mb-4').show();
            $('.content .card.mb-4').show();
            $('.content .card').last().show();
        }
    });

    // Role-based sidebar and navbar control
    const userRole = localStorage.getItem('userRole');
    if (userRole === 'C') {
        $('.sidebar .nav-item').hide();
        $('.sidebar [data-nav="view-account"]').show();
        // Navbar: Set label to Customer and show only Profile, Logout, and divider
        $('#userLabel').text('Customer');
        $('.dropdown-menu li').hide();
        $('.dropdown-menu [data-nav="profile"]').show();
        $('.dropdown-menu [data-nav="logout"]').show();
        $('.dropdown-menu [data-nav="divider"]').show();
    } else {
        // Sidebar: Show all nav items
        $('.nav-item').show();
        // Navbar: Set label to Admin and show all dropdown items
        $('#userLabel').text('Admin');
        $('.dropdown-menu li').show();
    }

    // Logout functionality
    $('[data-nav="logout"]').click(function (e) {
        e.preventDefault();
        localStorage.removeItem('userRole');
        window.location.href = '../index.html';
    });
});