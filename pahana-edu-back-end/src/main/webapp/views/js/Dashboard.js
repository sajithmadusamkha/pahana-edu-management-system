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
        $('#homeStatistics').hide(); // Statistics cards
        $('#homeChart').hide(); // Chart placeholder
        $('#homeActivity').hide(); // Recent activity
        $('.content .manage-customers').hide();
        $('.content .manage-items').hide();
        $('.content .place-order').hide();
        $('.content .help-section').hide();

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
        } else if (navItem === 'place-order') {
            $('.content .place-order').show();
            localStorage.setItem('activeNav', 'place-order'); // Persist view
            console.log('Place order section shown, loading data...'); // Debug
            loadCustomersForOrder(); // Load customers for order
            loadItemsForOrder(); // Load items for order
        } else if (navItem === 'help') {
            $('.content .help-section').show();
            localStorage.setItem('activeNav', 'help'); // Persist view
        } else if (navItem === 'home') {
            $('#homeStatistics').show(); // Statistics cards
            $('#homeChart').show(); // Chart placeholder
            $('#homeActivity').show(); // Recent activity
            localStorage.setItem('activeNav', 'home'); // Persist view
            loadDashboardStatistics(); // Load real statistics data
        }
    });

    // Function to load all customers
    function loadAllCustomers() {
        $.ajax({
            url: 'http://localhost:8080/pahana/customers',
            type: 'GET',
            success: function (customers) {
                updateCustomerCounter(customers); // Update counter
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
                                <td class="text-center">
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
                updateItemCounter(items); // Update counter
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
                                <td class="text-center">
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
        $('#homeStatistics').hide();
        $('#homeChart').hide();
        $('#homeActivity').hide();
        $('.content .manage-customers').show();
        $('.content .manage-items').hide();
        $('.content .place-order').hide();
        $('.content .help-section').hide();
        loadAllCustomers(); // Load customers when showing customers page
    } else if (activeNav === 'items') {
        $('#homeStatistics').hide();
        $('#homeChart').hide();
        $('#homeActivity').hide();
        $('.content .manage-customers').hide();
        $('.content .manage-items').show();
        $('.content .place-order').hide();
        $('.content .help-section').hide();
        loadAllItems(); // Load items when showing items page
    } else if (activeNav === 'place-order') {
        $('#homeStatistics').hide();
        $('#homeChart').hide();
        $('#homeActivity').hide();
        $('.content .manage-customers').hide();
        $('.content .manage-items').hide();
        $('.content .place-order').show();
        $('.content .help-section').hide();
        loadCustomersForOrder(); // Load customers for order
        loadItemsForOrder(); // Load items for order
    } else if (activeNav === 'help') {
        $('#homeStatistics').hide();
        $('#homeChart').hide();
        $('#homeActivity').hide();
        $('.content .manage-customers').hide();
        $('.content .manage-items').hide();
        $('.content .place-order').hide();
        $('.content .help-section').show();
    } else {
        $('#homeStatistics').show();
        $('#homeChart').show();
        $('#homeActivity').show();
        $('.content .manage-customers').hide();
        $('.content .manage-items').hide();
        $('.content .place-order').hide();
        $('.content .help-section').hide();
        loadDashboardStatistics(); // Load real statistics data
    }

    // Form submission with AJAX
    $('#btnAddCustomer').click(function (e) {
        e.preventDefault(); // Prevent page refresh
        const $button = $(this);
        const $spinner = $button.find('.spinner-border');

        // Simple validation
        const accountNumber = $('#accountNumber').val().trim();
        const fullName = $('#name').val().trim();
        const address = $('#address').val().trim();
        const telephone = $('#telephone').val().trim();
        const unitsConsumed = $('#unitsConsumed').val().trim();

        // Clear previous error messages
        $('.form-control').removeClass('is-invalid');
        $('.invalid-feedback').remove();

        let hasErrors = false;

        // Validate account number
        if (!accountNumber) {
            showFieldError('#accountNumber', 'Account number is required');
            hasErrors = true;
        } else if (accountNumber.length < 6 || accountNumber.length > 12) {
            showFieldError('#accountNumber', 'Account number must be between 6 and 12 characters');
            hasErrors = true;
        } else if (!/^[A-Za-z0-9]+$/.test(accountNumber)) {
            showFieldError('#accountNumber', 'Account number must contain only letters and numbers');
            hasErrors = true;
        }

        // Validate full name
        if (!fullName) {
            showFieldError('#name', 'Full name is required');
            hasErrors = true;
        } else if (fullName.length < 2 || fullName.length > 100) {
            showFieldError('#name', 'Full name must be between 2 and 100 characters');
            hasErrors = true;
        }

        // Validate telephone
        if (!telephone) {
            showFieldError('#telephone', 'Telephone number is required');
            hasErrors = true;
        } else if (!/^[0-9]{10}$/.test(telephone)) {
            showFieldError('#telephone', 'Telephone number must be exactly 10 digits');
            hasErrors = true;
        }

        // Validate address
        if (!address) {
            showFieldError('#address', 'Address is required');
            hasErrors = true;
        } else if (address.length < 10 || address.length > 255) {
            showFieldError('#address', 'Address must be between 10 and 255 characters');
            hasErrors = true;
        }

        // Validate units consumed
        if (!unitsConsumed) {
            showFieldError('#unitsConsumed', 'Units consumed is required');
            hasErrors = true;
        } else if (isNaN(unitsConsumed) || parseInt(unitsConsumed) < 0) {
            showFieldError('#unitsConsumed', 'Units consumed must be a non-negative number');
            hasErrors = true;
        } else if (parseInt(unitsConsumed) > 10000) {
            showFieldError('#unitsConsumed', 'Units consumed cannot exceed 10,000');
            hasErrors = true;
        }

        if (hasErrors) {
            return;
        }

        $spinner.show();
        $button.prop('disabled', true);

        const customerData = {
            accountNumber: accountNumber.toUpperCase(),
            fullName: fullName,
            address: address,
            telephone: telephone,
            unitsConsumed: parseInt(unitsConsumed)
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

        // Clear previous error messages
        $('.form-control').removeClass('is-invalid');
        $('.invalid-feedback').remove();

        let hasErrors = false;

        // Validate item name
        if (!itemData.name) {
            showFieldError('#itemName', 'Item name is required');
            hasErrors = true;
        } else if (itemData.name.length < 2 || itemData.name.length > 100) {
            showFieldError('#itemName', 'Item name must be between 2 and 100 characters');
            hasErrors = true;
        }

        // Validate price
        if (!itemData.price) {
            showFieldError('#itemPrice', 'Price is required');
            hasErrors = true;
        } else if (isNaN(itemData.price) || itemData.price <= 0) {
            showFieldError('#itemPrice', 'Price must be a number greater than 0');
            hasErrors = true;
        } else if (itemData.price > 999999.99) {
            showFieldError('#itemPrice', 'Price cannot exceed 999,999.99');
            hasErrors = true;
        }

        // Validate quantity
        if (!itemData.quantity) {
            showFieldError('#itemQuantity', 'Quantity is required');
            hasErrors = true;
        } else if (isNaN(itemData.quantity) || itemData.quantity < 0) {
            showFieldError('#itemQuantity', 'Quantity must be a non-negative number');
            hasErrors = true;
        } else if (itemData.quantity > 10000) {
            showFieldError('#itemQuantity', 'Quantity cannot exceed 10,000');
            hasErrors = true;
        }

        if (hasErrors) {
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

        // Clear previous error messages
        $('.form-control').removeClass('is-invalid');
        $('.invalid-feedback').remove();

        let hasErrors = false;

        // Validate account number
        if (!accountNumber) {
            showFieldError('#updateAccountNumber', 'Account number is required');
            hasErrors = true;
        } else if (accountNumber.length < 6 || accountNumber.length > 12) {
            showFieldError('#updateAccountNumber', 'Account number must be between 6 and 12 characters');
            hasErrors = true;
        }

        // Validate full name
        if (!fullName) {
            showFieldError('#updateName', 'Full name is required');
            hasErrors = true;
        } else if (fullName.length < 2 || fullName.length > 100) {
            showFieldError('#updateName', 'Full name must be between 2 and 100 characters');
            hasErrors = true;
        }

        // Validate telephone
        if (!telephone) {
            showFieldError('#updateTelephone', 'Telephone number is required');
            hasErrors = true;
        } else if (!/^[0-9]{10}$/.test(telephone)) {
            showFieldError('#updateTelephone', 'Telephone number must be exactly 10 digits');
            hasErrors = true;
        }

        // Validate address
        if (!address) {
            showFieldError('#updateAddress', 'Address is required');
            hasErrors = true;
        } else if (address.length < 10 || address.length > 255) {
            showFieldError('#updateAddress', 'Address must be between 10 and 255 characters');
            hasErrors = true;
        }

        // Validate units consumed
        if (!unitsConsumed) {
            showFieldError('#updateUnitsConsumed', 'Units consumed is required');
            hasErrors = true;
        } else if (isNaN(unitsConsumed) || parseInt(unitsConsumed) < 0) {
            showFieldError('#updateUnitsConsumed', 'Units consumed must be a non-negative number');
            hasErrors = true;
        } else if (parseInt(unitsConsumed) > 10000) {
            showFieldError('#updateUnitsConsumed', 'Units consumed cannot exceed 10,000');
            hasErrors = true;
        }

        if (hasErrors) {
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

        // Clear previous error messages
        $('.form-control').removeClass('is-invalid');
        $('.invalid-feedback').remove();

        let hasErrors = false;

        // Validate item name
        if (!name) {
            showFieldError('#updateItemName', 'Item name is required');
            hasErrors = true;
        } else if (name.length < 2 || name.length > 100) {
            showFieldError('#updateItemName', 'Item name must be between 2 and 100 characters');
            hasErrors = true;
        }

        // Validate price
        if (!price) {
            showFieldError('#updateItemPrice', 'Price is required');
            hasErrors = true;
        } else if (isNaN(price) || price <= 0) {
            showFieldError('#updateItemPrice', 'Price must be a number greater than 0');
            hasErrors = true;
        } else if (price > 999999.99) {
            showFieldError('#updateItemPrice', 'Price cannot exceed 999,999.99');
            hasErrors = true;
        }

        // Validate quantity
        if (!quantity) {
            showFieldError('#updateItemQuantity', 'Quantity is required');
            hasErrors = true;
        } else if (isNaN(quantity) || quantity < 0) {
            showFieldError('#updateItemQuantity', 'Quantity must be a non-negative number');
            hasErrors = true;
        } else if (quantity > 10000) {
            showFieldError('#updateItemQuantity', 'Quantity cannot exceed 10,000');
            hasErrors = true;
        }

        if (hasErrors) {
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

    // Load dashboard statistics on page load
    loadDashboardStatistics();

    // ===== DASHBOARD STATISTICS FUNCTIONALITY =====
    function loadDashboardStatistics() {
        // Load total customers
        $.ajax({
            url: 'http://localhost:8080/pahana/customers',
            type: 'GET',
            success: function (customers) {
                $('#totalCustomersCard').text(customers.length);
                $('#totalCustomers').text(customers.length);
            },
            error: function () {
                $('#totalCustomersCard').text('0');
                $('#totalCustomers').text('0');
            }
        });

        // Load total items and calculate revenue
        $.ajax({
            url: 'http://localhost:8080/pahana/items',
            type: 'GET',
            success: function (items) {
                $('#totalItems').text(items.length);

                // Calculate total inventory value as revenue placeholder
                let totalValue = 0;
                items.forEach(function(item) {
                    totalValue += (item.price * item.quantity);
                });
                $('#totalRevenueCard').text('$' + totalValue.toFixed(2));
            },
            error: function () {
                $('#totalItems').text('0');
                $('#totalRevenueCard').text('$0.00');
            }
        });

        // For now, set orders to 0 (can be implemented later with actual order tracking)
        $('#totalOrdersCard').text('0');
    }

    // Update counters when customers/items are loaded
    function updateCustomerCounter(customers) {
        $('#totalCustomers').text(customers.length);
        $('#totalCustomersCard').text(customers.length);
    }

    function updateItemCounter(items) {
        $('#totalItems').text(items.length);
    }

    // ===== PLACE ORDER FUNCTIONALITY =====
    let orderItems = [];
    let availableItems = [];
    let availableCustomers = [];

    // Load customers for order
    function loadCustomersForOrder() {
        console.log('Loading customers for order...'); // Debug
        $.ajax({
            url: 'http://localhost:8080/pahana/customers',
            type: 'GET',
            success: function (customers) {
                console.log('Customers loaded:', customers.length); // Debug
                availableCustomers = customers;
                $('#customerSelect').empty().append('<option value="">Select a customer...</option>');
                customers.forEach(function (customer) {
                    $('#customerSelect').append(`
                        <option value="${customer.accountNumber}"
                                data-name="${customer.fullName}"
                                data-address="${customer.address}"
                                data-phone="${customer.telephone}">
                            ${customer.fullName} (${customer.accountNumber})
                        </option>
                    `);
                });
            },
            error: function (xhr, status, error) {
                console.error('Error loading customers for order:', error);
            }
        });
    }

    // Load items for order
    function loadItemsForOrder() {
        console.log('Loading items for order...'); // Debug
        $.ajax({
            url: 'http://localhost:8080/pahana/items',
            type: 'GET',
            success: function (items) {
                console.log('Items loaded:', items.length); // Debug
                availableItems = items;
                $('#orderItemSelect').empty().append('<option value="">Select an item...</option>');
                items.forEach(function (item) {
                    if (item.quantity > 0) { // Only show items with stock
                        $('#orderItemSelect').append(`
                            <option value="${item.id}"
                                    data-price="${item.price}"
                                    data-stock="${item.quantity}">
                                ${item.name} (Stock: ${item.quantity})
                            </option>
                        `);
                    }
                });
            },
            error: function (xhr, status, error) {
                console.error('Error loading items for order:', error);
            }
        });
    }

    // Customer selection change
    $('#customerSelect').change(function () {
        const selectedOption = $(this).find('option:selected');
        if (selectedOption.val()) {
            $('#selectedCustomerAccount').text(selectedOption.val());
            $('#selectedCustomerAddress').text(selectedOption.data('address'));
            $('#selectedCustomerPhone').text(selectedOption.data('phone'));
            $('#customerInfo').show();
        } else {
            $('#customerInfo').hide();
        }
        updatePlaceOrderButton();
    });

    // Item selection change
    $('#orderItemSelect').change(function () {
        const selectedOption = $(this).find('option:selected');
        if (selectedOption.val()) {
            $('#orderItemPrice').val('$' + parseFloat(selectedOption.data('price')).toFixed(2));
            $('#availableStock').text(selectedOption.data('stock'));
            $('#orderItemQuantity').attr('max', selectedOption.data('stock'));
        } else {
            $('#orderItemPrice').val('');
            $('#availableStock').text('0');
            $('#orderItemQuantity').attr('max', '');
        }
    });

    // Add item to order
    $('#addItemBtn').click(function () {
        const itemId = $('#orderItemSelect').val();
        const quantityInput = $('#orderItemQuantity').val();
        const quantity = parseInt(quantityInput);

        console.log('Add item clicked - ItemId:', itemId, 'QuantityInput:', quantityInput, 'ParsedQuantity:', quantity); // Debug

        if (!itemId) {
            alert('Please select an item.');
            return;
        }

        if (isNaN(quantity) || quantity <= 0) {
            alert('Please enter a valid quantity.');
            console.log('Quantity validation failed - isNaN:', isNaN(quantity), 'quantity <= 0:', quantity <= 0); // Debug
            return;
        }

        const selectedOption = $('#orderItemSelect').find('option:selected');
        const availableStock = parseInt(selectedOption.data('stock'));
        const itemName = selectedOption.text().split(' (Stock:')[0];
        const unitPrice = parseFloat(selectedOption.data('price'));

        // Check if quantity exceeds available stock
        if (quantity > availableStock) {
            alert(`Insufficient stock. Available: ${availableStock}`);
            return;
        }

        // Check if item already exists in order
        const existingItemIndex = orderItems.findIndex(item => item.itemId === parseInt(itemId));
        if (existingItemIndex !== -1) {
            // Update existing item
            const newQuantity = orderItems[existingItemIndex].quantity + quantity;
            if (newQuantity > availableStock) {
                alert(`Total quantity would exceed available stock. Available: ${availableStock}, Current in order: ${orderItems[existingItemIndex].quantity}`);
                return;
            }
            orderItems[existingItemIndex].quantity = newQuantity;
        } else {
            // Add new item
            orderItems.push({
                itemId: parseInt(itemId),
                itemName: itemName,
                quantity: quantity,
                unitPrice: unitPrice
            });
        }

        // Reset form
        $('#orderItemSelect').val('');
        $('#orderItemQuantity').val('1');
        $('#orderItemPrice').val('');
        $('#availableStock').text('0');

        updateOrderSummary();
    });

    // Update order summary table
    function updateOrderSummary() {
        const tbody = $('#orderItemsTable');
        tbody.empty();

        if (orderItems.length === 0) {
            tbody.append('<tr><td colspan="5" class="text-center text-muted">No items added to order yet.</td></tr>');
            $('#orderTotal').text('$0.00'); // Reset total to $0.00 when no items
        } else {
            let total = 0;
            orderItems.forEach(function (item, index) {
                const itemTotal = item.quantity * item.unitPrice;
                total += itemTotal;

                tbody.append(`
                    <tr>
                        <td>${item.itemName}</td>
                        <td>${item.quantity}</td>
                        <td>$${item.unitPrice.toFixed(2)}</td>
                        <td>$${itemTotal.toFixed(2)}</td>
                        <td>
                            <button class="btn btn-sm btn-outline-danger remove-item-btn" data-index="${index}">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    </tr>
                `);
            });

            $('#orderTotal').text('$' + total.toFixed(2));
        }

        updatePlaceOrderButton();
    }

    // Remove item from order
    $(document).on('click', '.remove-item-btn', function () {
        const index = parseInt($(this).data('index'));
        orderItems.splice(index, 1);
        updateOrderSummary();
    });

    // Clear all items from order
    $('#clearOrderBtn').click(function () {
        if (orderItems.length > 0 && confirm('Are you sure you want to clear all items from the order?')) {
            orderItems = [];
            updateOrderSummary();
        }
    });

    // Update place order button state
    function updatePlaceOrderButton() {
        const hasCustomer = $('#customerSelect').val() !== '';
        const hasItems = orderItems.length > 0;
        $('#placeOrderBtn').prop('disabled', !(hasCustomer && hasItems));
    }

    // Place order
    $('#placeOrderBtn').click(function () {
        const customerAccountNumber = $('#customerSelect').val();

        if (!customerAccountNumber || orderItems.length === 0) {
            alert('Please select a customer and add items to the order.');
            return;
        }

        // Show loading state
        $(this).prop('disabled', true).html('<span class="spinner-border spinner-border-sm me-2"></span>Processing...');

        const orderData = {
            customerAccountNumber: customerAccountNumber,
            items: orderItems.map(item => ({
                itemId: item.itemId,
                quantity: item.quantity
            }))
        };

        console.log('Placing order with data:', orderData); // Debug

        $.ajax({
            url: 'http://localhost:8080/pahana/bills-create',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(orderData),
            success: function (response) {
                console.log('Order response:', response); // Debug
                if (response.success && response.billId) {
                    console.log('Order successful, loading bill details for ID:', response.billId); // Debug
                    // Load and display the bill
                    loadBillDetails(response.billId);

                    // Clear the order
                    orderItems = [];
                    updateOrderSummary();
                    $('#customerSelect').val('');
                    $('#customerInfo').hide();

                    // Show success message
                    showToast('success', 'Order placed successfully!');
                } else {
                    console.error('Order failed:', response); // Debug
                    showToast('error', response.message || 'Failed to place order');
                }
            },
            error: function (xhr, status, error) {
                console.error('Error placing order:', error, xhr.responseText); // Debug
                const errorMessage = xhr.responseJSON ? xhr.responseJSON.message : 'Failed to place order';
                showToast('error', errorMessage);
            },
            complete: function () {
                // Reset button state
                $('#placeOrderBtn').prop('disabled', false).html('<i class="bi bi-check-circle"></i> Place Order');
            }
        });
    });

    // Load bill details and display
    function loadBillDetails(billId) {
        console.log('Loading bill details for ID:', billId); // Debug
        $.ajax({
            url: `http://localhost:8080/pahana/bill?id=${billId}`,
            type: 'GET',
            success: function (billData) {
                console.log('Bill data received:', billData); // Debug
                displayBill(billData);
                $('#billDisplaySection').show();
                console.log('Bill display section shown'); // Debug

                // Scroll to bill section
                $('#billDisplaySection')[0].scrollIntoView({ behavior: 'smooth' });
            },
            error: function (xhr, status, error) {
                console.error('Error loading bill details:', error, xhr.responseText); // Debug
                showToast('error', 'Failed to load bill details');
            }
        });
    }

    // Display bill in printable format
    function displayBill(billData) {
        const billDate = new Date(billData.billDate).toLocaleDateString();
        let itemsHtml = '';
        let totalAmount = 0;

        billData.items.forEach(function (item) {
            const itemTotal = item.quantity * item.unitPrice;
            totalAmount += itemTotal;
            itemsHtml += `
                <tr>
                    <td>${item.itemName}</td>
                    <td class="text-center">${item.quantity}</td>
                    <td class="text-end">$${item.unitPrice.toFixed(2)}</td>
                    <td class="text-end">$${itemTotal.toFixed(2)}</td>
                </tr>
            `;
        });

        const billHtml = `
            <div class="bill-header text-center mb-4">
                <h3>PAHANA EDU MANAGEMENT</h3>
                <p class="text-muted">Educational Institution Bill</p>
                <hr>
            </div>

            <div class="row mb-4">
                <div class="col-md-6">
                    <h6>Bill To:</h6>
                    <strong>${billData.customerName}</strong><br>
                    Account: ${billData.customerAccountNumber}<br>
                    ${billData.customerAddress}<br>
                    Phone: ${billData.customerTelephone}
                </div>
                <div class="col-md-6 text-end">
                    <h6>Bill Details:</h6>
                    Bill ID: <strong>#${billData.id}</strong><br>
                    Date: <strong>${billDate}</strong><br>
                    Status: <strong class="text-success">Paid</strong>
                </div>
            </div>

            <div class="table-responsive mb-4">
                <table class="table table-bordered">
                    <thead class="table-dark">
                        <tr>
                            <th>Item</th>
                            <th class="text-center">Quantity</th>
                            <th class="text-end">Unit Price</th>
                            <th class="text-end">Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${itemsHtml}
                    </tbody>
                    <tfoot>
                        <tr class="table-secondary">
                            <th colspan="3" class="text-end">Grand Total:</th>
                            <th class="text-end">$${totalAmount.toFixed(2)}</th>
                        </tr>
                    </tfoot>
                </table>
            </div>

            <div class="text-center">
                <p class="text-muted">Thank you for your business!</p>
                <small>This is a computer-generated bill.</small>
            </div>
        `;

        $('#printableBill').html(billHtml);
    }

    // Print bill functionality
    $('#printBillBtn').click(function () {
        const printContent = $('#printableBill').html();
        const printWindow = window.open('', '_blank');
        printWindow.document.write(`
            <html>
                <head>
                    <title>Bill - Pahana Edu Management</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
                    <style>
                        @media print {
                            .no-print { display: none !important; }
                            body { margin: 0; }
                        }
                        .bill-header h3 { color: #0d6efd; }
                    </style>
                </head>
                <body class="p-4">
                    ${printContent}
                </body>
            </html>
        `);
        printWindow.document.close();
        printWindow.print();
    });

    // New order functionality
    $('#newOrderBtn').click(function () {
        $('#billDisplaySection').hide();
        orderItems = [];
        updateOrderSummary();
        $('#customerSelect').val('');
        $('#customerInfo').hide();
        loadItemsForOrder(); // Refresh items to update stock
    });

    // Helper function to show toast messages
    function showToast(type, message) {
        const bgClass = type === 'success' ? 'text-bg-success' : 'text-bg-danger';
        $('.content').prepend(`
            <div class="toast align-items-center ${bgClass} border-0 position-fixed top-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex">
                    <div class="toast-body">
                        ${message}
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        `);
        const toastElement = $('.content .toast').first();
        const toast = new bootstrap.Toast(toastElement[0], { delay: 3000 });
        toast.show();
    }

    // Logout functionality
    $('[data-nav="logout"]').click(function (e) {
        e.preventDefault();
        localStorage.removeItem('userRole');
        localStorage.removeItem('activeNav'); // Clear active view
        window.location.href = 'index.html'; // Fixed path
    });

    // Helper function to show field validation errors
    function showFieldError(fieldSelector, message) {
        const $field = $(fieldSelector);
        $field.addClass('is-invalid');
        $field.after('<div class="invalid-feedback">' + message + '</div>');
    }
});