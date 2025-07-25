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
        $('.content .manage-items').hide();

        // Show content based on clicked item
        const navItem = $(this).parent().data('nav');
        console.log('Clicked nav item:', navItem, 'Manage Customers exists:', $('.content .manage-customers').length); // Debug
        if (navItem === 'customers') {
            $('.content .manage-customers').show();
            localStorage.setItem('activeNav', 'customers'); // Persist view
            loadAllCustomers(); // Load all customers when navigating to customers page
        } else if (navItem === 'items') {
            $('.content .manage-items').show();
            localStorage.setItem('activeNav', 'items'); // Persist view
            loadAllItems(); // Load all items when navigating to items page
        } else if (navItem === 'home') {
            $('.content .row.g-4.mb-4').show();
            $('.content .card.mb-4').show();
            $('.content .card').last().show();
            localStorage.setItem('activeNav', 'home'); // Persist view
        }
    });

    // Function to load all customers
    function loadAllCustomers() {
        $.ajax({
            url: 'http://localhost:8080/pahana/customers',
            type: 'GET',
            success: function (customers) {
                $('#customerTableBody').empty();
                if (customers.length === 0) {
                    $('#customerTableBody').append('<tr><td colspan="6" class="text-center text-muted">No customers found.</td></tr>');
                } else {
                    customers.forEach(function (customer) {
                        $('#customerTableBody').append(`
                            <tr>
                                <td>${customer.accountNumber}</td>
                                <td>${customer.fullName}</td>
                                <td>${customer.address}</td>
                                <td>${customer.telephone}</td>
                                <td>${customer.unitsConsumed}</td>
                                <td>
                                    <button class="btn btn-sm btn-outline-primary me-1 update-btn" data-id="${customer.id}" data-bs-toggle="tooltip" title="Update Customer"><i class="bi bi-pencil"></i></button>
                                    <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${customer.id}" data-bs-toggle="tooltip" title="Delete Customer"><i class="bi bi-trash"></i></button>
                                </td>
                            </tr>
                        `);
                    });
                }
                $('[data-bs-toggle="tooltip"]').tooltip(); // Re-initialize tooltips
            },
            error: function (xhr, status, error) {
                console.error('Error loading customers:', error);
                $('#customerTableBody').empty().append('<tr><td colspan="6" class="text-center text-danger">Error loading customers.</td></tr>');
            }
        });
    }

    // Function to load all items
    function loadAllItems() {
        $.ajax({
            url: 'http://localhost:8080/pahana/items',
            type: 'GET',
            success: function (items) {
                $('#itemTableBody').empty();
                if (items.length === 0) {
                    $('#itemTableBody').append('<tr><td colspan="5" class="text-center text-muted">No items found.</td></tr>');
                } else {
                    items.forEach(function (item) {
                        $('#itemTableBody').append(`
                            <tr>
                                <td>${item.id}</td>
                                <td>${item.name}</td>
                                <td>$${item.price.toFixed(2)}</td>
                                <td>${item.quantity}</td>
                                <td>
                                    <button class="btn btn-sm btn-outline-primary me-1 update-item-btn" data-id="${item.id}" data-bs-toggle="tooltip" title="Update Item"><i class="bi bi-pencil"></i></button>
                                    <button class="btn btn-sm btn-outline-danger delete-item-btn" data-id="${item.id}" data-bs-toggle="tooltip" title="Delete Item"><i class="bi bi-trash"></i></button>
                                </td>
                            </tr>
                        `);
                    });
                }
                $('[data-bs-toggle="tooltip"]').tooltip(); // Re-initialize tooltips
            },
            error: function (xhr, status, error) {
                console.error('Error loading items:', error);
                $('#itemTableBody').empty().append('<tr><td colspan="5" class="text-center text-danger">Error loading items.</td></tr>');
            }
        });
    }

    // Check if this is a fresh login (clear navigation state on fresh login)
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('fresh') === 'true') {
        localStorage.removeItem('activeNav');
    }

    // Restore active view on page load - always default to home for fresh sessions
    const activeNav = localStorage.getItem('activeNav') || 'home';
    $('.sidebar .nav-link').removeClass('active');
    $('.sidebar [data-nav="' + activeNav + '"] .nav-link').addClass('active');
    if (activeNav === 'customers') {
        $('.content .row.g-4.mb-4').hide();
        $('.content .card.mb-4').hide();
        $('.content .card').last().hide();
        $('.content .manage-customers').show();
        $('.content .manage-items').hide();
        loadAllCustomers(); // Load customers when showing customers page
    } else if (activeNav === 'items') {
        $('.content .row.g-4.mb-4').hide();
        $('.content .card.mb-4').hide();
        $('.content .card').last().hide();
        $('.content .manage-customers').hide();
        $('.content .manage-items').show();
        loadAllItems(); // Load items when showing items page
    } else {
        $('.content .row.g-4.mb-4').show();
        $('.content .card.mb-4').show();
        $('.content .card').last().show();
        $('.content .manage-customers').hide();
        $('.content .manage-items').hide();
    }

    // Form submission with AJAX
    $('#btnAddCustomer').click(function (e) {
        e.preventDefault(); // Prevent page refresh
        const $button = $(this);
        const $spinner = $button.find('.spinner-border');
        $spinner.show();
        $button.prop('disabled', true);

        const customerData = {
            accountNumber: $('#accountNumber').val(),
            fullName: $('#name').val(),
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
                $('#customerForm')[0].reset();

                // Load all customers to refresh the table
                loadAllCustomers();

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

    // Add Item functionality
    $('#btnAddItem').click(function (e) {
        e.preventDefault(); // Prevent page refresh
        const $button = $(this);
        const $spinner = $button.find('.spinner-border');
        $spinner.show();
        $button.prop('disabled', true);

        const itemData = {
            name: $('#itemName').val().trim(),
            price: parseFloat($('#itemPrice').val()),
            quantity: parseInt($('#itemQuantity').val())
        };

        // Validate form fields
        if (!itemData.name || !itemData.price || !itemData.quantity) {
            alert('Please fill in all required fields.');
            $spinner.hide();
            $button.prop('disabled', false);
            return;
        }

        if (itemData.price <= 0) {
            alert('Price must be greater than 0.');
            $spinner.hide();
            $button.prop('disabled', false);
            return;
        }

        if (itemData.quantity < 0) {
            alert('Quantity cannot be negative.');
            $spinner.hide();
            $button.prop('disabled', false);
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/pahana/create-item',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(itemData),
            success: function (response) {
                $spinner.hide();
                $button.prop('disabled', false);
                $('#itemForm')[0].reset();

                // Load all items to refresh the table
                loadAllItems();

                // Show success toast
                $('.content').prepend(`
                    <div class="toast align-items-center text-bg-success border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body">
                                Item "${itemData.name}" added successfully!
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                `);
                const toastElement = $('.content .toast');
                const toast = new bootstrap.Toast(toastElement[0], { delay: 2000 });
                toast.show();
            },
            error: function (xhr, status, error) {
                $spinner.hide();
                $button.prop('disabled', false);
                $('.content').prepend(`
                    <div class="toast align-items-center text-bg-danger border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body">
                                Failed to add item: ${xhr.responseJSON?.message || 'Server error'}
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

    // Function to clear update customer modal
    function clearUpdateModal() {
        $('#updateAccountNumber').val('');
        $('#updateName').val('');
        $('#updateAddress').val('');
        $('#updateTelephone').val('');
        $('#updateUnitsConsumed').val('');
        $('#saveUpdateCustomer').removeData('customer-id');
        $('#saveUpdateCustomer').removeData('original-account-number');
        // Remove highlight from any previously selected row
        $('#customerTableBody tr').removeClass('table-warning');
    }

    // Function to clear update item modal
    function clearUpdateItemModal() {
        $('#updateItemId').val('');
        $('#updateItemName').val('');
        $('#updateItemPrice').val('');
        $('#updateItemQuantity').val('');
        $('#saveUpdateItem').removeData('item-id');
        // Remove highlight from any previously selected row
        $('#itemTableBody tr').removeClass('table-warning');
    }

    // Clear modals when they're hidden
    $('#updateCustomerModal').on('hidden.bs.modal', function () {
        clearUpdateModal();
    });

    $('#updateItemModal').on('hidden.bs.modal', function () {
        clearUpdateItemModal();
    });

    // Update button click - Patch data from table row to modal
    $(document).on('click', '.update-btn', function () {
        const $row = $(this).closest('tr');
        const customerId = $(this).data('id');

        // Get customer data from the specific table row
        const accountNumber = $row.find('td:nth-child(1)').text().trim();
        const fullName = $row.find('td:nth-child(2)').text().trim();
        const address = $row.find('td:nth-child(3)').text().trim();
        const telephone = $row.find('td:nth-child(4)').text().trim();
        const unitsConsumed = $row.find('td:nth-child(5)').text().trim();

        console.log('Patching customer data to modal:', {
            customerId,
            accountNumber,
            fullName,
            address,
            telephone,
            unitsConsumed
        });

        // Clear modal first to ensure clean state
        clearUpdateModal();

        // Highlight the selected row to show which customer is being updated
        $('#customerTableBody tr').removeClass('table-warning');
        $row.addClass('table-warning');

        // Populate the modal with current customer data from the selected row
        $('#updateAccountNumber').val(accountNumber);
        $('#updateName').val(fullName);
        $('#updateAddress').val(address);
        $('#updateTelephone').val(telephone);
        $('#updateUnitsConsumed').val(unitsConsumed);

        // Store the customer ID and account number for the update operation
        $('#saveUpdateCustomer').data('customer-id', customerId);
        $('#saveUpdateCustomer').data('original-account-number', accountNumber);

        // Show the modal with patched data
        $('#updateCustomerModal').modal('show');
    });

    // Update Item button click - Patch data from table row to modal
    $(document).on('click', '.update-item-btn', function () {
        const $row = $(this).closest('tr');
        const itemId = $(this).data('id');

        // Get item data from the specific table row
        const id = $row.find('td:nth-child(1)').text().trim();
        const name = $row.find('td:nth-child(2)').text().trim();
        const price = $row.find('td:nth-child(3)').text().trim().replace('$', '');
        const quantity = $row.find('td:nth-child(4)').text().trim();

        console.log('Patching item data to modal:', {
            itemId, id, name, price, quantity
        });

        // Clear modal first to ensure clean state
        clearUpdateItemModal();

        // Highlight the selected row to show which item is being updated
        $('#itemTableBody tr').removeClass('table-warning');
        $row.addClass('table-warning');

        // Populate the modal with current item data from the selected row
        $('#updateItemId').val(id);
        $('#updateItemName').val(name);
        $('#updateItemPrice').val(price);
        $('#updateItemQuantity').val(quantity);

        // Store the item ID for the update operation
        $('#saveUpdateItem').data('item-id', itemId);

        // Show the modal with patched data
        $('#updateItemModal').modal('show');
    });

    // Save update - Update customer with patched data
    $('#saveUpdateCustomer').click(function () {
        const $button = $(this);
        const customerId = $button.data('customer-id');
        const originalAccountNumber = $button.data('original-account-number');

        // Check if we have the required data from the patched row
        if (!customerId || !originalAccountNumber) {
            alert('Error: Customer data not properly loaded. Please try clicking the update button again.');
            return;
        }

        // Get updated data from the modal form (patched and potentially modified by user)
        const accountNumber = $('#updateAccountNumber').val().trim();
        const fullName = $('#updateName').val().trim();
        const address = $('#updateAddress').val().trim();
        const telephone = $('#updateTelephone').val().trim();
        const unitsConsumed = $('#updateUnitsConsumed').val();

        console.log('Saving updated customer data:', {
            customerId,
            accountNumber,
            fullName,
            address,
            telephone,
            unitsConsumed
        });

        // Validate form fields
        if (!fullName || !address || !telephone || !unitsConsumed) {
            alert('Please fill in all required fields.');
            return;
        }

        if (!/^[0-9]{10}$/.test(telephone)) {
            alert('Please enter a valid 10-digit telephone number.');
            return;
        }

        if (parseInt(unitsConsumed) < 0) {
            alert('Units consumed cannot be negative.');
            return;
        }

        // Get updated data from the modal form
        const updatedCustomer = {
            id: customerId,
            accountNumber: $('#updateAccountNumber').val(),
            fullName: fullName,
            address: address,
            telephone: telephone,
            unitsConsumed: parseInt(unitsConsumed)
        };

        // Disable button and show loading state
        $button.prop('disabled', true);
        const originalText = $button.text();
        $button.text('Updating...');

        $.ajax({
            url: 'http://localhost:8080/pahana/update-customer',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(updatedCustomer),
            success: function (response) {
                $button.prop('disabled', false);
                $button.text(originalText);
                $('#updateCustomerModal').modal('hide');

                // Show success toast with customer details
                $('.content').prepend(`
                    <div class="toast align-items-center text-bg-success border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body">
                                Customer "${fullName}" (${accountNumber}) updated successfully!
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                `);
                const toastElement = $('.content .toast');
                const toast = new bootstrap.Toast(toastElement[0], { delay: 2000 });
                toast.show();

                // Reload all customers to refresh the table with updated data
                loadAllCustomers();
            },
            error: function (xhr, status, error) {
                $button.prop('disabled', false);
                $button.text(originalText);

                // Show error toast
                $('.content').prepend(`
                    <div class="toast align-items-center text-bg-danger border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body">
                                Failed to update customer: ${xhr.responseJSON?.message || 'Server error'}
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                `);
                const toastElement = $('.content .toast');
                const toast = new bootstrap.Toast(toastElement[0], { delay: 2000 });
                toast.show();
                console.error('Update error:', status, error);
            }
        });
    });

    // Save update item - Update item with patched data
    $('#saveUpdateItem').click(function () {
        const $button = $(this);
        const itemId = $button.data('item-id');

        // Check if we have the required data from the patched row
        if (!itemId) {
            alert('Error: Item data not properly loaded. Please try clicking the update button again.');
            return;
        }

        // Get updated data from the modal form (patched and potentially modified by user)
        const id = parseInt($('#updateItemId').val());
        const name = $('#updateItemName').val().trim();
        const price = parseFloat($('#updateItemPrice').val());
        const quantity = parseInt($('#updateItemQuantity').val());

        console.log('Saving updated item data:', {
            itemId, id, name, price, quantity
        });

        // Validate form fields
        if (!name || !price || !quantity) {
            alert('Please fill in all required fields.');
            return;
        }

        if (price <= 0) {
            alert('Price must be greater than 0.');
            return;
        }

        if (quantity < 0) {
            alert('Quantity cannot be negative.');
            return;
        }

        // Get updated data from the modal form
        const updatedItem = {
            id: id,
            name: name,
            price: price,
            quantity: quantity
        };

        // Disable button and show loading state
        $button.prop('disabled', true);
        const originalText = $button.text();
        $button.text('Updating...');

        $.ajax({
            url: 'http://localhost:8080/pahana/items-update',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(updatedItem),
            success: function (response) {
                $button.prop('disabled', false);
                $button.text(originalText);
                $('#updateItemModal').modal('hide');

                // Show success toast with item details
                $('.content').prepend(`
                    <div class="toast align-items-center text-bg-success border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body">
                                Item "${name}" updated successfully!
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                `);
                const toastElement = $('.content .toast');
                const toast = new bootstrap.Toast(toastElement[0], { delay: 2000 });
                toast.show();

                // Reload all items to refresh the table with updated data
                loadAllItems();
            },
            error: function (xhr, status, error) {
                $button.prop('disabled', false);
                $button.text(originalText);

                // Show error toast
                $('.content').prepend(`
                    <div class="toast align-items-center text-bg-danger border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body">
                                Failed to update item: ${xhr.responseJSON?.message || 'Server error'}
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                `);
                const toastElement = $('.content .toast');
                const toast = new bootstrap.Toast(toastElement[0], { delay: 2000 });
                toast.show();
                console.error('Update error:', status, error);
            }
        });
    });

    // Delete button click
    $(document).on('click', '.delete-btn', function () {
        if (confirm('Are you sure you want to delete this customer?')) {
            const $row = $(this).closest('tr');
            const accountNumber = $row.find('td:first').text(); // Get account number from first column

            $.ajax({
                url: 'http://localhost:8080/pahana/delete-customer',
                type: 'DELETE',
                contentType: 'application/json',
                data: JSON.stringify({ accountNumber: accountNumber }),
                success: function (response) {
                    // Show success toast
                    $('.content').prepend(`
                        <div class="toast align-items-center text-bg-success border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                            <div class="d-flex">
                                <div class="toast-body">
                                    Customer deleted successfully!
                                </div>
                                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                            </div>
                        </div>
                    `);
                    const toastElement = $('.content .toast');
                    const toast = new bootstrap.Toast(toastElement[0], { delay: 2000 });
                    toast.show();

                    // Reload all customers to refresh the table
                    loadAllCustomers();
                },
                error: function (xhr, status, error) {
                    // Show error toast
                    $('.content').prepend(`
                        <div class="toast align-items-center text-bg-danger border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                            <div class="d-flex">
                                <div class="toast-body">
                                    Failed to delete customer: ${xhr.responseJSON?.message || 'Server error'}
                                </div>
                                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                            </div>
                        </div>
                    `);
                    const toastElement = $('.content .toast');
                    const toast = new bootstrap.Toast(toastElement[0], { delay: 2000 });
                    toast.show();
                    console.error('Delete error:', status, error);
                }
            });
        }
    });

    // Delete Item button click
    $(document).on('click', '.delete-item-btn', function () {
        const $row = $(this).closest('tr');
        const itemId = $(this).data('id');
        const itemName = $row.find('td:nth-child(2)').text(); // Get item name from second column

        if (confirm(`Are you sure you want to delete "${itemName}"?\n\nNote: Items that are used in bills cannot be deleted.`)) {
            $.ajax({
                url: 'http://localhost:8080/pahana/item-delete?id=' + itemId,
                type: 'DELETE',
                success: function (response) {
                    // Show success toast
                    $('.content').prepend(`
                        <div class="toast align-items-center text-bg-success border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                            <div class="d-flex">
                                <div class="toast-body">
                                    Item "${itemName}" deleted successfully!
                                </div>
                                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                            </div>
                        </div>
                    `);
                    const toastElement = $('.content .toast');
                    const toast = new bootstrap.Toast(toastElement[0], { delay: 2000 });
                    toast.show();

                    // Reload all items to refresh the table
                    loadAllItems();
                },
                error: function (xhr, status, error) {
                    let errorMessage = 'Server error';
                    let toastClass = 'text-bg-danger';

                    // Handle specific error cases
                    if (xhr.status === 409) { // Conflict - item used in bills
                        errorMessage = xhr.responseJSON?.message || 'Cannot delete item: This item is used in existing bills.';
                        toastClass = 'text-bg-warning';
                    } else if (xhr.responseJSON?.message) {
                        errorMessage = xhr.responseJSON.message;
                    }

                    // Show error toast with appropriate styling
                    $('.content').prepend(`
                        <div class="toast align-items-center ${toastClass} border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                            <div class="d-flex">
                                <div class="toast-body">
                                    ${errorMessage}
                                </div>
                                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                            </div>
                        </div>
                    `);
                    const toastElement = $('.content .toast');
                    const toast = new bootstrap.Toast(toastElement[0], { delay: 4000 }); // Longer delay for error messages
                    toast.show();
                    console.error('Delete error:', status, error);
                }
            });
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