$(document).ready(function () {
    // Toggle sidebar on mobile
    $('.btn-outline-primary').click(function () {
        $('.sidebar').toggleClass('active');
    });

    // Role-based sidebar and navbar control
    const userRole = localStorage.getItem('userRole') || '';
    console.log('User Role:', userRole); // Debug role value

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
        $('.sidebar .nav-item').show(); // Fixed to target sidebar only
        $('#userLabel').text('Admin');
        $('.dropdown-menu li').show();
    }

    // Handle sidebar item active state and content visibility
    $('.sidebar .nav-link').click(function (e) {
        e.preventDefault(); // Prevent default link behavior
        $('.sidebar .nav-link').removeClass('active');
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
            localStorage.setItem('activeNav', 'customers'); // Persist view
        } else if (navItem === 'home') {
            $('.content .row.g-4.mb-4').show();
            $('.content .card.mb-4').show();
            $('.content .card').last().show();
            localStorage.setItem('activeNav', 'home'); // Persist view
        }
    });

    // Restore active view on page load
    const activeNav = localStorage.getItem('activeNav') || 'home';
    $('.sidebar .nav-link').removeClass('active');
    $('.sidebar [data-nav="' + activeNav + '"] .nav-link').addClass('active');
    if (activeNav === 'customers') {
        $('.content .row.g-4.mb-4').hide();
        $('.content .card.mb-4').hide();
        $('.content .card').last().hide();
        $('.content .manage-customers').show();
    } else {
        $('.content .row.g-4.mb-4').show();
        $('.content .card.mb-4').show();
        $('.content .card').last().show();
        $('.content .manage-customers').hide();
    }

    // Form submission with AJAX
    $('#customerForm').submit(function (e) {
        e.preventDefault(); // Prevent page refresh
        alert('c')
        const $button = $(this).find('button[type="submit"]');
        const $spinner = $button.find('.spinner-border');
        $spinner.show();
        $button.prop('disabled', true);

        const customerData = {
            accountNumber: $('#accountNumber').val(),
            name: $('#name').val(),
            address: $('#address').val(),
            telephone: $('#telephone').val(),
            unitsConsumed: parseInt($('#unitsConsumed').val())
        };

        $.ajax({
            url: 'http://localhost:8080/pahana/register-customer',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(customerData),
            success: function (response) {
                $spinner.hide();
                $button.prop('disabled', false);
                $('#customerTableBody').empty().append(`
                    <tr>
                        <td>${customerData.accountNumber}</td>
                        <td>${customerData.name}</td>
                        <td>${customerData.address}</td>
                        <td>${customerData.telephone}</td>
                        <td>${customerData.unitsConsumed}</td>
                        <td>
                            <button class="btn btn-sm btn-outline-primary me-1 update-btn" data-id="${response.id || 'mock-id'}" data-bs-toggle="tooltip" title="Update Customer"><i class="bi bi-pencil"></i></button>
                            <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${response.id || 'mock-id'}" data-bs-toggle="tooltip" title="Delete Customer"><i class="bi bi-trash"></i></button>
                        </td>
                    </tr>
                `);
                $('#customerForm')[0].reset();
                // Show success toast
                $('.content').prepend(`
                    <div class="toast align-items-center text-bg-success border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body">
                                Customer added successfully!
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                `);
                const toastElement = $('.content .toast');
                const toast = new bootstrap.Toast(toastElement[0], { delay: 2000 });
                toast.show();
                $('[data-bs-toggle="tooltip"]').tooltip(); // Re-initialize tooltips
            },
            error: function (xhr, status, error) {
                $spinner.hide();
                $button.prop('disabled', false);
                $('.content').prepend(`
                    <div class="toast align-items-center text-bg-danger border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body">
                                Failed to add customer: ${xhr.responseJSON?.message || 'Server error'}
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                `);
                const toastElement = $('.content .toast');
                const toast = new bootstrap.Toast(toastElement[0], { delay: 2000 });
                toast.show();
                console.error('AJAX error:', status, error);
            }
        });
    });

    // Table sorting
    $('.manage-customers th[data-sort]').click(function () {
        const key = $(this).data('sort');
        const $tbody = $('#customerTableBody');
        const rows = $tbody.find('tr').get();
        const isNumeric = key === 'unitsConsumed';
        rows.sort((a, b) => {
            const aValue = $(a).find(`td:nth-child(${$(this).index() + 1})`).text();
            const bValue = $(b).find(`td:nth-child(${$(this).index() + 1})`).text();
            return isNumeric
                ? parseFloat(aValue) - parseFloat(bValue)
                : aValue.localeCompare(bValue);
        });
        $tbody.empty().append(rows);
    });

    // Update button click
    $(document).on('click', '.update-btn', function () {
        const id = $(this).data('id');
        // Mock data for demo (replace with AJAX GET)
        $('#updateAccountNumber').val('ACC123');
        $('#updateName').val('John Doe');
        $('#updateAddress').val('123 Main St');
        $('#updateTelephone').val('1234567890');
        $('#updateUnitsConsumed').val('100');
        $('#updateCustomerModal').modal('show');
    });

    // Save update
    $('#saveUpdateCustomer').click(function () {
        $('#updateCustomerModal').modal('hide');
        // Mock update: update table row (replace with AJAX in production)
        console.log('Update customer:', $('#updateAccountNumber').val());
    });

    // Delete button click
    $(document).on('click', '.delete-btn', function () {
        if (confirm('Are you sure you want to delete this customer?')) {
            $(this).closest('tr').remove();
            if ($('#customerTableBody tr').length === 0) {
                $('#customerTableBody').append('<tr><td colspan="6" class="text-center text-muted">No customers added yet.</td></tr>');
            }
        }
    });

    // Initialize tooltips
    $('[data-bs-toggle="tooltip"]').tooltip();

    // Logout functionality
    $('[data-nav="logout"]').click(function (e) {
        e.preventDefault();
        localStorage.removeItem('userRole');
        localStorage.removeItem('activeNav'); // Clear active view
        window.location.href = './views/Login.html'; // Fixed path
    });
});